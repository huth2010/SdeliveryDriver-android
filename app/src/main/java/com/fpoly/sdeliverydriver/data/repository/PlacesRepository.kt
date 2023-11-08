package com.fpoly.sdeliverydriver.data.repository

import com.fpoly.sdeliverydriver.data.model.OpenStreetMapResponse
import com.fpoly.sdeliverydriver.data.network.PlacesApi
import io.reactivex.Observable
import io.reactivex.schedulers.Schedulers
import javax.inject.Inject

class PlacesRepository @Inject constructor(
    private val api: PlacesApi
) {
    fun getLocationName(
        latitude: Double,
        longitude: Double
    ): Observable<OpenStreetMapResponse> =
        api.getLocationName(latitude, longitude).subscribeOn(Schedulers.io())
}