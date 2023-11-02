package com.fpoly.sdeliverydriver.ui.security

import VerifyOTPFragment
import android.os.Bundle
import androidx.fragment.app.add
import androidx.fragment.app.commit
import com.airbnb.mvrx.viewModel
import com.fpoly.sdeliverydriver.PolyApplication
import com.fpoly.sdeliverydriver.R
import com.fpoly.sdeliverydriver.core.PolyBaseActivity
import com.fpoly.sdeliverydriver.data.network.SessionManager
import com.fpoly.sdeliverydriver.databinding.ActivityLoginBinding
import com.fpoly.sdeliverydriver.ultis.addFragmentToBackstack
import com.fpoly.sdeliverydriver.ultis.changeLanguage
import com.fpoly.sdeliverydriver.ultis.changeMode
import javax.inject.Inject

class LoginActivity : PolyBaseActivity<ActivityLoginBinding>(), SecurityViewModel.Factory {
    val viewModel: SecurityViewModel by viewModel()

    @Inject
    lateinit var securityViewModelFactory: SecurityViewModel.Factory

    @Inject
    lateinit var sessionManager: SessionManager

    override fun onCreate(savedInstanceState: Bundle?) {
        (application as PolyApplication).polyComponent.inject(this)
        super.onCreate(savedInstanceState)
        setContentView(views.root)
        setupUi()
    }

    private fun setupUi() {
        supportFragmentManager.commit {
                add<LoginFragment>(R.id.frame_layout)
        }
        viewModel.observeViewEvents {
            if (it != null) {
                handleEvent(it)
            }
        }
        changeMode(sessionManager.fetchDarkMode())
        changeLanguage(sessionManager.fetchLanguage())
    }

    private fun handleEvent(event: SecurityViewEvent) {
        when (event) {
            is SecurityViewEvent.ReturnLoginEvent -> {
                addFragmentToBackstack(R.id.frame_layout, LoginFragment::class.java)
            }

            is SecurityViewEvent.ReturnResetPassEvent -> {
                addFragmentToBackstack(R.id.frame_layout, ResetPasswordFragment::class.java)
            }

            is SecurityViewEvent.ReturnForgotPassEvent -> {
                addFragmentToBackstack(R.id.frame_layout, ForgotPasswordFragment::class.java)
            }

            is SecurityViewEvent.ReturnVerifyOTPEvent -> {
                addFragmentToBackstack(R.id.frame_layout, VerifyOTPFragment::class.java)
            }
        }

    }

    override fun getBinding(): ActivityLoginBinding =
        ActivityLoginBinding.inflate(layoutInflater)

    override fun create(initialState: SecurityViewState): SecurityViewModel {
        return securityViewModelFactory.create(initialState)
    }

}