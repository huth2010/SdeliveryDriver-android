package com.fpoly.sdeliverydriver.ultis

object MyConfigNotifi{
    const val CHANNEL_ID = "MY_CHANNEL_ID"
    const val CHANNEL_ID_CHAT = "MY_CHANNEL_ID_CHAT"
    const val CHANNEL_ID_CALL = "MY_CHANNEL_ID_CALL"
    const val RC_SIGN_IN = 1111

    var TYPE_ALL = "TYPE_ALL"
    var TYPE_ORDER = "TYPE_ORDER"
    var TYPE_COUPONS = "TYPE_COUPONS"
    var TYPE_CHAT = "TYPE_CHAT"
    var TYPE_CALL_OFFER = "TYPE_CALL_OFFER"
    var TYPE_CALL_ANSWER = "TYPE_CALL_ANSWER"
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