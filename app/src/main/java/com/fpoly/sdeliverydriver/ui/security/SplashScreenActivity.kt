package com.fpoly.sdeliverydriver.ui.security

import android.content.Intent
import android.os.Bundle
import com.airbnb.mvrx.Fail
import com.airbnb.mvrx.Success
import com.airbnb.mvrx.viewModel
import com.fpoly.sdeliverydriver.PolyApplication
import com.fpoly.sdeliverydriver.core.PolyBaseActivity
import com.fpoly.sdeliverydriver.data.network.SessionManager
import com.fpoly.sdeliverydriver.databinding.ActivitySplashScreenBinding
import com.fpoly.sdeliverydriver.ui.chat.ChatActivity
import com.fpoly.sdeliverydriver.ui.main.MainActivity
import com.fpoly.sdeliverydriver.ultis.MyConfigNotifi
import com.fpoly.sdeliverydriver.ultis.changeLanguage
import com.fpoly.sdeliverydriver.ultis.changeMode
import com.fpoly.sdeliverydriver.ultis.handleLogOut
import com.fpoly.sdeliverydriver.ultis.startActivityWithData
import javax.inject.Inject

class SplashScreenActivity : PolyBaseActivity<ActivitySplashScreenBinding>(),SecurityViewModel.Factory{

    private val viewModel: SecurityViewModel by viewModel()

    @Inject
    lateinit var securityViewModelFactory: SecurityViewModel.Factory

    @Inject
    lateinit var sessionManager: SessionManager

    override fun onCreate(savedInstanceState: Bundle?) {
        (application as PolyApplication).polyComponent.inject(this)
        super.onCreate(savedInstanceState)
        setContentView(views.root)
        viewModel.subscribe(this) {
            handleStateChange(it)
        }
        configData()
    }

    private fun handleStateChange(it: SecurityViewState) {
        when (it.asyncUserCurrent) {
            is Success -> {
                startActivityWithFCMReciveDataNotifi()
            }

            is Fail -> {
                handleLogOut()
                startActivity(Intent(this, LoginActivity::class.java))
                finish()
            }

            else -> {}
        }
    }

    private fun startActivityWithFCMReciveDataNotifi() {
        val type = intent.extras?.getString("type")
        val idUrl = intent.extras?.getString("idUrl")

        when(type){
            MyConfigNotifi.TYPE_ALL ->{

            }
            MyConfigNotifi.TYPE_CHAT ->{
                startMainActivityToBackStack()
                val intent = Intent(this, ChatActivity::class.java)
                startActivityWithData(intent, type, idUrl)
            }
            MyConfigNotifi.TYPE_CALL_ANSWER ->{
                startMainActivityToBackStack()
                val intent = Intent(this, ChatActivity::class.java)
                startActivityWithData(intent, type, idUrl)
            }
            else ->{
                startActivity(Intent(this, MainActivity::class.java))
            }
        }
        finishAffinity()
    }

    fun startMainActivityToBackStack(){
        var intent = Intent(this, MainActivity::class.java)
        intent.flags = Intent.FLAG_ACTIVITY_NO_ANIMATION
        startActivity(intent)
    }


    private fun configData() {
        sessionManager.fetchDarkMode().let { changeMode(it) }
        sessionManager.fetchLanguage().let { changeLanguage( it ) }
    }

    override fun getBinding(): ActivitySplashScreenBinding {
        return ActivitySplashScreenBinding.inflate(layoutInflater)
    }

    override fun create(initialState: SecurityViewState): SecurityViewModel {
        return securityViewModelFactory.create(initialState)
    }
}