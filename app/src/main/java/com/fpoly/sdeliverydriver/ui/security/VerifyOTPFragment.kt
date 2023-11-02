import android.os.Bundle
import android.os.CountDownTimer
import android.view.LayoutInflater
import android.view.View
import android.view.ViewGroup
import android.widget.EditText
import android.text.Editable
import android.text.TextWatcher
import android.view.KeyEvent
import com.airbnb.mvrx.Fail
import com.airbnb.mvrx.Success
import com.airbnb.mvrx.activityViewModel
import com.airbnb.mvrx.withState
import com.fpoly.sdeliverydriver.R
import com.fpoly.sdeliverydriver.core.PolyBaseFragment
import com.fpoly.sdeliverydriver.data.model.VerifyOTPRequest
import com.fpoly.sdeliverydriver.databinding.FragmentVerifyOTPBinding
import com.fpoly.sdeliverydriver.ui.security.SecurityViewAction
import com.fpoly.sdeliverydriver.ui.security.SecurityViewModel

class VerifyOTPFragment : PolyBaseFragment<FragmentVerifyOTPBinding>() {
    private val viewModel: SecurityViewModel by activityViewModel()
    private var resendTime: Long = 180
    private var resendEnabled = false
    private lateinit var editTexts: Array<EditText>
    private var isAllFieldsFilled = false
    private var countDownTimer: CountDownTimer? = null

    override fun onViewCreated(view: View, savedInstanceState: Bundle?) {
        super.onViewCreated(view, savedInstanceState)
        listenEvent()
    }

    private fun listenEvent() {
        views.sendButton.isEnabled = false
        editTexts = arrayOf(
            views.otpDigit1,
            views.otpDigit2,
            views.otpDigit3,
            views.otpDigit4,
            views.otpDigit5,
            views.otpDigit6
        )

        for (i in 0 until editTexts.size) {
            editTexts[i].addTextChangedListener(object : TextWatcher {
                override fun beforeTextChanged(
                    s: CharSequence?,
                    start: Int,
                    count: Int,
                    after: Int
                ) {
                }

                override fun onTextChanged(s: CharSequence?, start: Int, before: Int, count: Int) {
                }

                override fun afterTextChanged(s: Editable?) {
                    if (s?.isNotEmpty() == true) {
                        if (i < editTexts.size - 1) {
                            editTexts[i + 1].requestFocus()
                        }
                    }
                    isAllFieldsFilled = checkAllFieldsFilled()
                    views.sendButton.isEnabled = isAllFieldsFilled
                }
            })
            editTexts[i].setOnKeyListener { v, keyCode, event ->
                if (keyCode == KeyEvent.KEYCODE_DEL) {
                    if (v.isFocused && i > 0 && i < 5) {
                        editTexts[i - 1].requestFocus()
                        editTexts[i - 1].setText("")
                    } else if (v.isFocused && i == 5 && editTexts[i].text.isEmpty()) {
                        editTexts[i - 1].requestFocus()
                        editTexts[i - 1].setText("")
                    } else {
                        editTexts[i].setText("")
                    }
                    return@setOnKeyListener true
                }
                false
            }
        }

        startCountDownTimer()

        views.resendOtp.setOnClickListener {
            if (resendEnabled) {
                withState(viewModel) {
                    val resendOTPCode = it.asyncForgotPassword.invoke()?.data
                    viewModel.handle(SecurityViewAction.ResendResetPassOTPCode(resendOTPCode!!))
                }
                startCountDownTimer()
            }
        }

        views.sendButton.setOnClickListener {
            views.apply {
                val otp = otpDigit1.text.toString() +
                        otpDigit2.text.toString() +
                        otpDigit3.text.toString() +
                        otpDigit4.text.toString() +
                        otpDigit5.text.toString() +
                        otpDigit6.text.toString()

                withState(viewModel) {
                    val userId = it.asyncForgotPassword.invoke()?.data?.userId
                    val verifyOTP = VerifyOTPRequest(userId, otp)
                    viewModel.handle(SecurityViewAction.VerifyOTPResetPassAction(verifyOTP))
                }
                progressBar.visibility = View.VISIBLE
            }
        }
    }

    private fun checkAllFieldsFilled(): Boolean {
        for (editText in editTexts) {
            if (editText.text.isNullOrBlank()) {
                return false
            }
        }
        return true
    }

    private fun startCountDownTimer() {
        resendEnabled = false
        countDownTimer = object : CountDownTimer(resendTime * 1000, 1000) {
            override fun onTick(millisUntilFinished: Long) {
                views.resendOtp.text =
                    """${getString(R.string.verify_otp_resend_button_text)}(${millisUntilFinished / 1000}s)"""
            }
            override fun onFinish() {
                resendEnabled = true
                views.resendOtp.text = getString(R.string.verify_otp_resend_button_text)
            }
        }.start()
    }

    override fun onDestroyView() {
        super.onDestroyView()
        countDownTimer?.cancel()
    }

    override fun getBinding(
        inflater: LayoutInflater,
        container: ViewGroup?
    ): FragmentVerifyOTPBinding {
        return FragmentVerifyOTPBinding.inflate(inflater, container, false)
    }

    override fun invalidate(): Unit = withState(viewModel) {
        when (it.asyncResetPassword) {
            is Success -> {
                viewModel.handleReturnResetPass()
            }

            is Fail -> {
            }

            else -> {}
        }
    }
}

