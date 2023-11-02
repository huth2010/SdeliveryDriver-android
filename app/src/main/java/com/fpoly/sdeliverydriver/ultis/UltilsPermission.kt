package com.fpoly.sdeliverydriver.ultis

import android.app.Activity
import android.content.Intent
import android.net.Uri
import android.os.Build
import android.provider.Settings
import androidx.fragment.app.Fragment
import com.permissionx.guolindev.PermissionX

fun Activity.startToDetailPermission() {
    val intent = Intent()
    intent.action = Settings.ACTION_APPLICATION_DETAILS_SETTINGS
    val uri = Uri.fromParts("package", packageName, null)
    intent.data = uri
    startActivity(intent)
}

fun Fragment.checkPermissionGallery(isAllow: (Boolean) -> Unit) {
    if (Build.VERSION.SDK_INT < Build.VERSION_CODES.TIRAMISU){
        PermissionX.init(this)
            .permissions(android.Manifest.permission.READ_EXTERNAL_STORAGE)
            .request { allGranded, _, _ ->
                    isAllow(allGranded)
            }
    }else{
        PermissionX.init(this)
            .permissions(android.Manifest.permission.READ_MEDIA_IMAGES, android.Manifest.permission.READ_MEDIA_VIDEO)
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