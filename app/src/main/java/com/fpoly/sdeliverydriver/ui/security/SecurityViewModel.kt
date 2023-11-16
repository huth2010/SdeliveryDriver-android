package com.fpoly.sdeliverydriver.ui.security

import com.airbnb.mvrx.ActivityViewModelContext
import com.airbnb.mvrx.FragmentViewModelContext
import com.airbnb.mvrx.Loading
import com.airbnb.mvrx.MvRxViewModelFactory
import com.airbnb.mvrx.Uninitialized
import com.airbnb.mvrx.ViewModelContext
import com.fpoly.sdeliverydriver.core.PolyBaseViewModel
import com.fpoly.sdeliverydriver.data.model.Data
import com.fpoly.sdeliverydriver.data.model.ResetPasswordRequest
import com.fpoly.sdeliverydriver.data.model.UserRequest
import com.fpoly.sdeliverydriver.data.model.VerifyOTPRequest
import com.fpoly.sdeliverydriver.data.repository.AuthRepository
import dagger.assisted.Assisted
import dagger.assisted.AssistedFactory
import dagger.assisted.AssistedInject

class SecurityViewModel @AssistedInject constructor(
    @Assisted state: SecurityViewState,
    val repository: AuthRepository,
) :
    PolyBaseViewModel<SecurityViewState,SecurityViewAction,SecurityViewEvent>(state) {

    init {
        handleCurrentUser()
    }

    override fun handle(action: SecurityViewAction) {
        when(action){
            is SecurityViewAction.LoginAction->handleLogin(action.userName,action.password)
            is SecurityViewAction.VerifyOTPResetPassAction->handleVerifyOTPCodeRessetPassword(action.verifyOTPRequest)
            is SecurityViewAction.ResendResetPassOTPCode->handleResendResetPasswordOTPCode(action.resendOTPCode)
            is SecurityViewAction.ForgotPassword->handleForgotPassword(action.email)
            is SecurityViewAction.ResetPassword->handleResetPassword(action.resetPasswordRequest)
            else -> {}
        }
    }

    private fun handleResetPassword(resetPasswordRequest: ResetPasswordRequest) {
        setState {copy(asyncUserCurrent= Loading())}
        repository.resetPassword(resetPasswordRequest).execute {
            copy(asyncUserCurrent= it)
        }
    }

    private fun handleForgotPassword(email: String) {
        setState {copy(asyncForgotPassword= Loading())}
        repository.forgotPassword(email).execute {
            copy(asyncForgotPassword= it)
        }
    }

    private fun handleResendResetPasswordOTPCode(resendOTPCode: Data) {
        setState {copy(asyncForgotPassword= Loading())}
        repository.resendOTPCode(resendOTPCode).execute {
            copy(asyncForgotPassword= it)
        }
    }

    private fun handleVerifyOTPCodeRessetPassword(verifyOTPRequest: VerifyOTPRequest) {
        setState { copy(asyncResetPassword= Loading()) }
        repository.verifyOTPChangePassword(verifyOTPRequest).execute {
            copy(asyncResetPassword= it)
        }
    }

    private fun handleCurrentUser() {
        setState { copy(asyncUserCurrent= Loading()) }
        repository.getCurrentUser().execute {
            copy(asyncUserCurrent=it)
        }
    }

    private fun handleLogin(userName:String, password: String){
        withState {
            setState {
                copy(asyncLogin= Loading())
            }
            repository.login(userName,password).execute {
                copy(asyncLogin=it)
            }
        }
    }

    fun handleReturnLogin() {
        _viewEvents.post(SecurityViewEvent.ReturnLoginEvent)
    }

    fun handleReturnResetPass() {
        _viewEvents.post(SecurityViewEvent.ReturnResetPassEvent)
    }

    fun handleReturnForgotPass() {
        _viewEvents.post(SecurityViewEvent.ReturnForgotPassEvent)
    }

    fun handleReturnVerifyOTPEvent() {
        _viewEvents.post(SecurityViewEvent.ReturnVerifyOTPEvent)
    }

    fun handleRemoveAsyncReset() {
        setState { copy(asyncUserCurrent= Uninitialized) }
    }

    @AssistedFactory
    interface Factory {
        fun create(initialState: SecurityViewState): SecurityViewModel
    }

    companion object : MvRxViewModelFactory<SecurityViewModel, SecurityViewState> {
        @JvmStatic
        override fun create(viewModelContext: ViewModelContext, state: SecurityViewState): SecurityViewModel {
            val factory = when (viewModelContext) {
                is FragmentViewModelContext -> viewModelContext.fragment as? Factory
                is ActivityViewModelContext -> viewModelContext.activity as? Factory
            }

            return factory?.create(state) ?: error("You should let your activity/fragment implements Factory interface")
        }
    }
}