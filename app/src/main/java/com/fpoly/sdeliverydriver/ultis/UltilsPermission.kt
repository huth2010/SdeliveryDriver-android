package com.fpoly.sdeliverydriver.ultis

import android.Manifest
import android.app.Activity
import android.app.AlertDialog
import android.app.Dialog
import android.content.DialogInterface
import android.content.Intent
import android.content.pm.PackageManager
import android.net.Uri
import android.os.Build
import android.os.Bundle
import android.provider.Settings
import android.widget.Toast
import androidx.core.app.ActivityCompat
import androidx.fragment.app.DialogFragment
import androidx.fragment.app.Fragment
import com.fpoly.sdeliverydriver.R
import com.permissionx.guolindev.PermissionX

fun Activity.startToDetailPermission() {
    val intent = Intent()
    intent.action = Settings.ACTION_APPLICATION_DETAILS_SETTINGS
    val uri = Uri.fromParts("package", packageName, null)
    intent.data = uri
    startActivity(intent)
}

fun Fragment.checkPermissionGallery(isAllow: (Boolean) -> Unit) {
    if (Build.VERSION.SDK_INT < Build.VERSION_CODES.TIRAMISU) {
        PermissionX.init(this)
            .permissions(android.Manifest.permission.READ_EXTERNAL_STORAGE)
            .request { allGranded, _, _ ->
                isAllow(allGranded)
            }
    } else {
        PermissionX.init(this)
            .permissions(
                android.Manifest.permission.READ_MEDIA_IMAGES,
                android.Manifest.permission.READ_MEDIA_VIDEO
            )
            .request { allGranded, _, _ ->
                isAllow(allGranded)
            }
    }
}

fun Fragment.checkPermisionCallVideo(isAllow: (Boolean) -> Unit) {
    PermissionX.init(this)
        .permissions(
            android.Manifest.permission.RECORD_AUDIO,
            android.Manifest.permission.CAMERA
        )
        .request { allGranded, _, _ ->
            isAllow(allGranded)
        }
}

fun isPermissionGranted(
    grantPermissions: Array<String>, grantResults: IntArray,
    permission: String
): Boolean {
    for (i in grantPermissions.indices) {
        if (permission == grantPermissions[i]) {
            return grantResults[i] == PackageManager.PERMISSION_GRANTED
        }
    }
    return false
}

class RationaleDialog : DialogFragment() {
    private var finishActivity = false
    private val permission_rationale_location = "permission rationale location"
    private val permission_required_toast = "permission required toast"
    override fun onCreateDialog(savedInstanceState: Bundle?): Dialog {
        val requestCode =
            arguments?.getInt(ARGUMENT_PERMISSION_REQUEST_CODE) ?: 0
        finishActivity =
            arguments?.getBoolean(ARGUMENT_FINISH_ACTIVITY) ?: false
        return AlertDialog.Builder(activity)
            .setMessage(permission_rationale_location)
            .setPositiveButton(android.R.string.ok) { dialog, which -> // After click on Ok, request the permission.
                ActivityCompat.requestPermissions(
                    requireActivity(),
                    arrayOf(
                        Manifest.permission.ACCESS_FINE_LOCATION,
                        Manifest.permission.ACCESS_COARSE_LOCATION
                    ),
                    requestCode
                )
                finishActivity = false
            }
            .setNegativeButton(android.R.string.cancel, null)
            .create()
    }

    override fun onDismiss(dialog: DialogInterface) {
        super.onDismiss(dialog)
        if (finishActivity) {
            Toast.makeText(
                activity,
                permission_required_toast,
                Toast.LENGTH_SHORT
            ).show()
            activity?.finish()
        }
    }

    companion object {
        private const val ARGUMENT_PERMISSION_REQUEST_CODE = "requestCode"
        private const val ARGUMENT_FINISH_ACTIVITY = "finish"
        fun newInstance(
            requestCode: Int,
            finishActivity: Boolean
        ): RationaleDialog {
            val arguments = Bundle().apply {
                putInt(ARGUMENT_PERMISSION_REQUEST_CODE, requestCode)
                putBoolean(ARGUMENT_FINISH_ACTIVITY, finishActivity)
            }
            return RationaleDialog().apply {
                this.arguments = arguments
            }
        }
    }
}