package com.fpoly.sdeliverydriver

import android.app.Application
import com.fpoly.sdeliverydriver.di.DaggerPolyComponent
import com.fpoly.sdeliverydriver.di.PolyComponent

class PolyApplication : Application() {

    val polyComponent: PolyComponent by lazy {
        DaggerPolyComponent.factory().create(applicationContext)
    }

    override fun onCreate() {
        super.onCreate()
        polyComponent.inject(this)
    }

}