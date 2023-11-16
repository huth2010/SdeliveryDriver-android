package com.fpoly.sdeliverydriver.ultis

object MyConfigNotifi{
    const val CHANNEL_ID = "MY_CHANNEL_ID"
    const val CHANNEL_ID_CHAT = "MY_CHANNEL_ID_CHAT"
    const val CHANNEL_ID_CALL = "MY_CHANNEL_ID_CALL"

    // type để nhận biết loại thông báo muốn giử
    var TYPE_ALL = "TYPE_ALL"
    var TYPE_CHAT = "TYPE_CHAT"
    var TYPE_CALL = "TYPE_CALL"

}

class Constants {
    companion object{
        const val collection_user_locations = "User Locations"
        const val CONFIRMED_STATUS: String = "65264c102d9b3bb388078976"
        const val DELIVERING_STATUS: String = "65264c672d9b3bb388078978"
        const val SUCCESS_STATUS: String = "6526a6e6adce6a54f6f67d7d"
        const val CANCEL_STATUS: String = "653bc0a72006e5791beab35b"
        const val ERROR_DIALOG_REQUEST = 9001
        const val PERMISSIONS_REQUEST_ENABLE_GPS =9002
        const val MAPVIEW_BUNDLE_KEY = "MapViewBundleKey"
    }

}