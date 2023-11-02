package com.fpoly.sdeliverydriver.ui.main.profile

import android.content.Intent
import android.os.Bundle
import android.view.LayoutInflater
import android.view.View
import android.view.ViewGroup
import android.widget.PopupMenu
import androidx.navigation.fragment.findNavController
import com.airbnb.mvrx.activityViewModel
import com.airbnb.mvrx.withState
import com.bumptech.glide.Glide
import com.fpoly.sdeliverydriver.PolyApplication
import com.fpoly.sdeliverydriver.R
import com.fpoly.sdeliverydriver.core.PolyBaseFragment
import com.fpoly.sdeliverydriver.data.network.SessionManager
import com.fpoly.sdeliverydriver.databinding.FragmentProfileBinding
import com.fpoly.sdeliverydriver.ui.chat.ChatActivity
import com.fpoly.sdeliverydriver.ui.main.home.HomeViewModel
import com.fpoly.sdeliverydriver.ultis.handleLogOut
import javax.inject.Inject

class ProfileFragment : PolyBaseFragment<FragmentProfileBinding>() {
    private val homeViewModel: HomeViewModel by activityViewModel()
    private val userViewModel: UserViewModel by activityViewModel()

    @Inject
    lateinit var sessionManager: SessionManager

    override fun onViewCreated(view: View, savedInstanceState: Bundle?) {
        (requireActivity().application as PolyApplication).polyComponent.inject(this)
        super.onViewCreated(view, savedInstanceState)
        configData()
        setupUI()
        listenEvent()
    }

    private fun setupUI(): Unit = withState(userViewModel){
        val user = it.asyncCurrentUser.invoke()
        views.apply {
            if (user != null) {
                Glide.with(requireContext())
                    .load(user.avatar?.url)
                    .placeholder(R.drawable.baseline_person_outline_24)
                    .error(R.drawable.baseline_person_outline_24)
                    .into(imgAvatar)
                displayName.text = "${user.first_name} ${user.last_name}"
            }
        }
    }

    override fun onResume() {
        super.onResume()
        homeViewModel.returnVisibleBottomNav(true)
    }

    private fun configData() {
        sessionManager.fetchDarkMode().let { views.switchDarkMode.isChecked = it }
    }

    private fun listenEvent() {
        views.btnEditProfile.setOnClickListener {
            findNavController().navigate(R.id.action_profileFragment_to_editProfileFragment)
        }
        views.layoutChangePass.setOnClickListener {
        findNavController().navigate(R.id.action_profileFragment_to_changePasswordFragment)
        }
        views.layoutLanguage.setOnClickListener{
            findNavController().navigate(R.id.action_profileFragment_to_languageFragment)
        }
        sessionManager.fetchDarkMode().let {
            views.switchDarkMode.isChecked=it
        }
        views.switchDarkMode.setOnCheckedChangeListener { buttonView, isChecked ->
            homeViewModel.handleChangeThemeMode(isChecked)
        }

        views.layoutChat.setOnClickListener{
            activity?.startActivity(Intent(requireContext(), ChatActivity::class.java))
        }

        views.logout.setOnClickListener {
            activity?.handleLogOut()
        }
    }

    override fun getBinding(
        inflater: LayoutInflater,
        container: ViewGroup?
    ): FragmentProfileBinding = FragmentProfileBinding.inflate(layoutInflater)

    override fun invalidate() {

    }
}