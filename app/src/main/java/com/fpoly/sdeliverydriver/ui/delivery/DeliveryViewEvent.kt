package com.fpoly.sdeliverydriver.ui.delivery

import com.fpoly.sdeliverydriver.core.PolyViewEvent

sealed class DeliveryViewEvent : PolyViewEvent {
    object OpenCamera : DeliveryViewEvent()
    object ShowPermissionDeniedToast : DeliveryViewEvent()
}
