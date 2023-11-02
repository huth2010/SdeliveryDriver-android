package com.fpoly.sdeliverydriver.data.repository

import com.fpoly.sdeliverydriver.data.model.OrderResponse
import com.fpoly.sdeliverydriver.data.model.Size
import com.fpoly.sdeliverydriver.data.network.OrderApi
import io.reactivex.Observable
import io.reactivex.schedulers.Schedulers
import javax.inject.Inject

class OrderRepository @Inject constructor(
    private val api: OrderApi
) {
    fun getOneSize(id: String): Observable<Size> = api.getOneSize(id).subscribeOn(Schedulers.io())
    fun getAllOrderByUserId(userId: String, statusId: String): Observable<List<OrderResponse>> =
        api.getAllOrderByUserId(userId, statusId).subscribeOn(Schedulers.io())
    fun getCurrentOrder(id: String): Observable<OrderResponse> =
        api.getCurrentOrder(id).subscribeOn(Schedulers.io())

}