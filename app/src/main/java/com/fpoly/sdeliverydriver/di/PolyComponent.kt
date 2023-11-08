package com.fpoly.sdeliverydriver.di

import android.content.Context
import androidx.fragment.app.FragmentFactory
import androidx.lifecycle.ViewModelProvider
import com.fpoly.sdeliverydriver.PolyApplication
import com.fpoly.sdeliverydriver.di.modules.FragmentModule
import com.fpoly.sdeliverydriver.di.modules.NetworkModule
import com.fpoly.sdeliverydriver.di.modules.ViewModelModule
import com.fpoly.sdeliverydriver.ui.chat.ChatActivity
import com.fpoly.sdeliverydriver.ui.chat.home.HomeChatFragment
import com.fpoly.sdeliverydriver.ui.delivery.DeliveryActivity
import com.fpoly.sdeliverydriver.ui.main.MainActivity
import com.fpoly.sdeliverydriver.ui.main.profile.ChangePasswordFragment
import com.fpoly.sdeliverydriver.ui.main.profile.LanguageFragment
import com.fpoly.sdeliverydriver.ui.main.profile.ProfileFragment
import com.fpoly.sdeliverydriver.ui.security.LoginActivity
import com.fpoly.sdeliverydriver.ui.security.LoginFragment
import com.fpoly.sdeliverydriver.ui.security.SplashScreenActivity
import dagger.BindsInstance
import dagger.Component

@Component(modules = [
    ViewModelModule::class,
    NetworkModule::class,
    FragmentModule::class
])
interface PolyComponent {

    fun inject(application: PolyApplication)
    fun inject(activity: SplashScreenActivity)
    fun inject(activity: MainActivity)
    fun inject(activity: LoginActivity)
    fun inject(activity: ChatActivity)
    fun inject(activity: DeliveryActivity)
    fun inject(fragment: LoginFragment)
    fun inject(fragment: ProfileFragment)
    fun inject(fragment: HomeChatFragment)
    fun inject(fragment: LanguageFragment)
    fun inject(fragment: ChangePasswordFragment)
    fun fragmentFactory(): FragmentFactory
    fun viewModelFactory(): ViewModelProvider.Factory

    @Component.Factory
    interface Factory {
        fun create(@BindsInstance context: Context): PolyComponent
    }
}