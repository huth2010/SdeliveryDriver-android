package com.fpoly.sdeliverydriver.data.network

import com.fpoly.sdeliverydriver.data.model.OrderResponse
import com.fpoly.sdeliverydriver.data.model.Size
import com.fpoly.sdeliverydriver.data.model.UpdateStatusRequest
import io.reactivex.Observable
import retrofit2.http.Body
import retrofit2.http.GET
import retrofit2.http.PATCH
import retrofit2.http.Path
import retrofit2.http.Query

interface OrderApi {
    @GET("/api/size/{id}")
    fun getOneSize(@Path("id") id: String): Observable<Size>

    @GET("/api/getAllorder")
    fun getAllOrderByStatus(
        @Query("statusId") statusId: String
    ): Observable<List<OrderResponse>>

    @GET("/api/orders/delivering")
    fun getAllOrderByShipper(
        @Query("statusId") statusId: String
    ): Observable<List<OrderResponse>>

    @GET("/api/order/{id}")
    fun getCurrentOrder(@Path("id") id: String): Observable<OrderResponse>

    @PATCH("/api/order/{id}")
    fun updateStatusOrder(
        @Path("id") id: String,
        @Query("shipperId") shipperId: String,
        @Body statusRequest: UpdateStatusRequest
    ): Observable<OrderResponse>
}