package com.fpoly.sdeliverydriver.data.repository

import com.fpoly.sdeliverydriver.data.model.CreateDeliveryOrderRequest
import com.fpoly.sdeliverydriver.data.model.DeliveryOrder
import com.fpoly.sdeliverydriver.data.network.DeliveryApi
import io.reactivex.Observable
import io.reactivex.schedulers.Schedulers

class DeliveryRepository(
    private val api: DeliveryApi
) {
    fun getAllDeliveryOrders(statusId : String): Observable<List<DeliveryOrder>> =
        api.getAllDeliveryOrders(statusId).subscribeOn(Schedulers.io())

    fun getOneById(id: String): Observable<DeliveryOrder> =
        api.getOneById(id).subscribeOn(Schedulers.io())

    fun createDeliveryOrder(
        createDeliveryOrderRequest: CreateDeliveryOrderRequest
    ): Observable<DeliveryOrder> = api.createDeliveryOrder(
        createDeliveryOrderRequest.orderCode,
        createDeliveryOrderRequest.status,
        createDeliveryOrderRequest.images,
        createDeliveryOrderRequest.comment
    ).subscribeOn(Schedulers.io())
}