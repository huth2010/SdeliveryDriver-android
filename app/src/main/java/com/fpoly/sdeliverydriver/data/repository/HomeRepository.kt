package com.fpoly.sdeliverydriver.data.repository

import com.fpoly.sdeliverydriver.data.model.Gallery
import com.fpoly.sdeliverydriver.data.network.ContentDataSource
import com.fpoly.sdeliverydriver.data.network.OrderApi
import kotlinx.coroutines.Dispatchers
import kotlinx.coroutines.withContext
import javax.inject.Inject

class HomeRepository @Inject constructor(
    private val api: OrderApi,
    private val contentDataSource: ContentDataSource
) {
    suspend fun getDataFromGallery(): ArrayList<Gallery> {
        return withContext(Dispatchers.IO){
            contentDataSource.getImage()
        }
    }
}