package com.fpoly.sdeliverydriver.di

interface HasScreenInjector {
    // inject scope nhỏ k phải toàn app
    fun injector(): PolyComponent
}