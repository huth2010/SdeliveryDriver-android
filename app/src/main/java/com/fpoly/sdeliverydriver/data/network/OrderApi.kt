package com.fpoly.sdeliverydriver.data.network

import com.fpoly.sdeliverydriver.data.model.OrderResponse
import com.fpoly.sdeliverydriver.data.model.Size
import io.reactivex.Observable
import retrofit2.http.GET
import retrofit2.http.Path
import retrofit2.http.Query

interface OrderApi {
    @GET("/api/size/{id}")
    fun getOneSize(@Path("id")id: String): Observable<Size>
    @GET("/api/order/{userId}/user")
    fun getAllOrderByUserId(
        @Path("userId") userId: String,
        @Query("statusId") statusId: String
    ): Observable<List<OrderResponse>>
    @GET("/api/order/{id}")
    fun getCurrentOrder(@Path("id") id: String): Observable<OrderResponse>




}