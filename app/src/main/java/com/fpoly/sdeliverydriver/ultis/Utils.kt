package com.fpoly.sdeliverydriver.ultis

import android.annotation.SuppressLint
import android.app.Activity
import android.content.Context
import android.content.Intent
import android.content.res.Configuration
import android.content.res.Resources
import android.database.Cursor
import android.graphics.Bitmap
import android.graphics.Color
import android.net.Uri
import android.os.Bundle
import android.provider.MediaStore
import android.util.DisplayMetrics
import android.view.View
import android.view.ViewGroup
import android.view.inputmethod.InputMethodManager
import androidx.appcompat.app.AppCompatActivity
import androidx.appcompat.app.AppCompatDelegate
import androidx.core.content.ContextCompat
import androidx.fragment.app.Fragment
import androidx.fragment.app.FragmentTransaction
import com.fpoly.sdeliverydriver.R
import com.fpoly.sdeliverydriver.data.network.SessionManager
import com.fpoly.sdeliverydriver.ui.security.LoginActivity
import com.google.android.material.snackbar.Snackbar
import java.io.ByteArrayOutputStream
import java.util.Locale


fun changeMode(isChecked: Boolean) {
    if (isChecked) {
        AppCompatDelegate.setDefaultNightMode(AppCompatDelegate.MODE_NIGHT_YES)
    } else {
        AppCompatDelegate.setDefaultNightMode(AppCompatDelegate.MODE_NIGHT_NO)
    }
}
fun Activity.changeLanguage(lang: String) {
    val res: Resources = resources
    val dm: DisplayMetrics = res.displayMetrics
    val conf: Configuration = res.configuration
    val myLocale = Locale(lang)
    conf.setLocale(myLocale)
    res.updateConfiguration(conf, dm)
}

fun Activity.startActivityAnim(intent: Intent) {
    startActivity(intent)
    overridePendingTransition(R.anim.enter_from_right, R.anim.exit_to_left)
}

fun Activity.popActivityAnim() {
    finish()
    overridePendingTransition(R.anim.enter_from_left, R.anim.exit_to_right)
}

fun Context.hideKeyboard(view: View) {
    val inputMethodManager = getSystemService(Activity.INPUT_METHOD_SERVICE) as InputMethodManager
    inputMethodManager.hideSoftInputFromWindow(view.windowToken, 0)
    view.clearFocus()
}

fun Context.showKeyboard(view: View) {
    view.requestFocus()
    val imm = getSystemService(Context.INPUT_METHOD_SERVICE) as InputMethodManager
    imm.showSoftInput(view, InputMethodManager.SHOW_IMPLICIT)
}
@SuppressLint("ShowToast", "ResourceAsColor")
public fun showSnackbar(view: View, message: String, isSuccess: Boolean, btnStr: String?, onClick: () -> Unit){
    val snackbar = Snackbar.make(view, message, Snackbar.LENGTH_LONG)
    snackbar.view.setBackgroundColor(ContextCompat.getColor(view.context!!, if (isSuccess) R.color.green_light else R.color.red_light))
    snackbar.setActionTextColor(Color.WHITE)
    snackbar.setAction(btnStr){ onClick() }
    snackbar.show()
}

inline fun androidx.fragment.app.FragmentManager.commitTransaction(allowStateLoss: Boolean = false, func: FragmentTransaction.() -> FragmentTransaction) {
    val transaction = beginTransaction().func()
    if (allowStateLoss) {
        transaction.commitAllowingStateLoss()
    } else {
        transaction.commit()
    }
}
fun <T : Fragment> AppCompatActivity.addFragmentToBackstack(
    frameId: Int,
    fragmentClass: Class<T>,
    tag: String? = null,
    allowStateLoss: Boolean = true,
    option: ((FragmentTransaction) -> Unit)? = null,
    bundle: Bundle?=null
) {
    supportFragmentManager.
    commitTransaction(allowStateLoss) {
        option?.invoke(this)
        replace(frameId, fragmentClass,bundle, tag).addToBackStack(tag)
    }
}

fun Activity.handleLogOut() {
    SessionManager(applicationContext).also {
        it.removeTokenAccess()
    }

    val intent = Intent(this, LoginActivity::class.java)
    intent.addFlags(Intent.FLAG_ACTIVITY_CLEAR_TOP)
    startActivity(intent)
    finish()
}

fun getRealPathFromURI(context: Context, uri: Uri?): String? {
    var cursor: Cursor? = null
    try {
        val projection = arrayOf(MediaStore.Images.Media.DATA)
        cursor = context.contentResolver.query(uri!!, projection, null, null, null)
        if (cursor != null && cursor.moveToFirst()) {
            val columnIndex = cursor.getColumnIndexOrThrow(MediaStore.Images.Media.DATA)
            return cursor.getString(columnIndex)
        }
    } catch (e: Exception) {
        e.printStackTrace()
    } finally {
        cursor!!.close()
    }
    return null
}
fun getImageUri(inContext: Context, inImage: Bitmap): Uri? {
    val bytes = ByteArrayOutputStream()
    inImage.compress(Bitmap.CompressFormat.JPEG, 100, bytes)
    val path =
        MediaStore.Images.Media.insertImage(inContext.contentResolver, inImage, "Title", null)
    return Uri.parse(path)
}

fun View.setMargins(left: Int, top: Int, right: Int, bottom: Int){
    if (this.layoutParams is ViewGroup.MarginLayoutParams) {
        val marginLayoutParams = this.layoutParams as ViewGroup.MarginLayoutParams
        marginLayoutParams.setMargins(left, top, right, bottom)
        this.requestLayout()
    }
}

fun Activity.startActivityWithData(intent: Intent, type: String?, idUrl: String?){
    intent.apply {
        putExtras(Bundle().apply {
            putString("type", type)
            putString("idUrl", idUrl) }
        )
        flags = Intent.FLAG_ACTIVITY_NEW_TASK
    }
    startActivity(intent)
}


fun Context.uriToFilePath(uri: Uri?): String {
    return when (uri?.scheme) {
        "file" -> {
            uri.path ?: ""
        }
        "content" -> {
            getFilePathFromContentUri(this, uri)
        }

        else -> ""
    }
}

private fun getFilePathFromContentUri(context: Context, uri: Uri): String {
    val projection = arrayOf(MediaStore.Images.Media.DATA)
    val cursor = context.contentResolver.query(uri, projection, null, null, null)

    cursor?.use {
        if (it.moveToFirst()) {
            val columnIndex = it.getColumnIndex(MediaStore.Images.Media.DATA)
            if (columnIndex != -1) {
                return it.getString(columnIndex) ?: ""
            }
        }
    }
    return ""
}