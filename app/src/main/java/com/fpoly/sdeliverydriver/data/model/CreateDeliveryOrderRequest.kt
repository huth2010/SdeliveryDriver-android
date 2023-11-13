package com.fpoly.sdeliverydriver.data.model

import okhttp3.MultipartBody
import okhttp3.RequestBody

data class CreateDeliveryOrderRequest(
    var orderCode: RequestBody,
    var status: RequestBody,
    var images: MultipartBody.Part,
    var comment: RequestBody,
    var cancelReason:RequestBody
)