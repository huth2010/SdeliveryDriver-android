package com.fpoly.sdeliverydriver.data.network


import com.fpoly.sdeliverydriver.data.model.CreateDeliveryOrderRequest
import com.fpoly.sdeliverydriver.data.model.DeliveryOrder
import io.reactivex.Observable
import okhttp3.MultipartBody
import okhttp3.RequestBody
import retrofit2.http.*

interface DeliveryApi {
    @GET("/api/deliveryOrder")
    fun getAllDeliveryOrders(@Query("statusId") statusId: String): Observable<List<DeliveryOrder>>

    @GET("/api/deliveryOrder/{id}")
    fun getOneById(@Path("id") id: String): Observable<DeliveryOrder>

    @Multipart
    @POST("/api/deliveryOrder")
    fun createDeliveryOrder(
        @Part("orderCode") orderCode: RequestBody,
        @Part("status") status: RequestBody,
        @Part image: MultipartBody.Part,
        @Part("comment") comment: RequestBody
    ): Observable<DeliveryOrder>

}
