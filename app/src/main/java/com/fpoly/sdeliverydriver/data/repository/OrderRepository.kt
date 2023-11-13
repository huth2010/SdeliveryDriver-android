package com.fpoly.sdeliverydriver.data.repository

import com.fpoly.sdeliverydriver.data.model.OrderResponse
import com.fpoly.sdeliverydriver.data.model.Size
import com.fpoly.sdeliverydriver.data.model.UpdateStatusRequest
import com.fpoly.sdeliverydriver.data.network.OrderApi
import io.reactivex.Observable
import io.reactivex.schedulers.Schedulers
import retrofit2.http.Path
import javax.inject.Inject

class OrderRepository @Inject constructor(
    private val api: OrderApi
) {
    fun getAllOrderByStatus(statusId: String): Observable<List<OrderResponse>> =
        api.getAllOrderByStatus(statusId).subscribeOn(Schedulers.io())

    fun getAllOrderByShipper(statusId: String): Observable<List<OrderResponse>> =
        api.getAllOrderByShipper(statusId).subscribeOn(Schedulers.io())

    fun getCurrentOrder(id: String): Observable<OrderResponse> =
        api.getCurrentOrder(id).subscribeOn(Schedulers.io())

    fun updateOrderStatus(id: String,shipperId: String, updateStatusRequest: UpdateStatusRequest): Observable<OrderResponse> =
        api.updateStatusOrder(id,shipperId, updateStatusRequest).subscribeOn(Schedulers.io())
}