package com.fpoly.sdeliverydriver.ui.main.home

import android.os.Bundle
import androidx.fragment.app.Fragment

import com.fpoly.sdeliverydriver.core.PolyViewEvent


sealed class HomeViewEvent : PolyViewEvent {
    data class ChangeDarkMode(var isCheckedDarkMode: Boolean) : HomeViewEvent()
    data class ReturnVisibleBottomNav(val isVisibleBottomNav: Boolean): HomeViewEvent()
}