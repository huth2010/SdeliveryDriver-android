package com.fpoly.sdeliverydriver.data.repository

import com.fpoly.sdeliverydriver.data.network.OrderApi
import javax.inject.Inject

class HomeRepository @Inject constructor(
    private val api: OrderApi
) {
}