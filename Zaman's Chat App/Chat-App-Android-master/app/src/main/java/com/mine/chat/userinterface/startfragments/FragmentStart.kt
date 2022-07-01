package com.mine.chat.userinterface.startfragments

import android.os.Bundle
import android.view.LayoutInflater
import android.view.View
import android.view.ViewGroup
import androidx.fragment.app.Fragment
import androidx.fragment.app.viewModels
import androidx.navigation.fragment.findNavController
import com.mine.chat.R

import com.mine.chat.dataandmodels.EventObserver
import com.mine.chat.databinding.StartFragmentBinding


import com.mine.chat.utilandexts.UtilSharedPreferences

class FragmentStart : Fragment() {

    private val viewModel by viewModels<ViewModelStart>()
    private lateinit var viewDataBinding: StartFragmentBinding

    override fun onCreateView(
        inflater: LayoutInflater, container: ViewGroup?, savedInstanceState: Bundle?
    ): View? {
        viewDataBinding =
            StartFragmentBinding.inflate(inflater, container, false).apply { viewmodel = viewModel }
        viewDataBinding.lifecycleOwner = this.viewLifecycleOwner
        setHasOptionsMenu(false)
        return viewDataBinding.root
    }

    override fun onActivityCreated(savedInstanceState: Bundle?) {
        super.onActivityCreated(savedInstanceState)
        setupObservers()

        if (userIsAlreadyLoggedIn()) {
            navigateDirectlyToChats()
        }
    }

    private fun userIsAlreadyLoggedIn(): Boolean {
        return UtilSharedPreferences.getUserID(requireContext()) != null
    }

    private fun setupObservers() {
        viewModel.loginEvent.observe(viewLifecycleOwner, EventObserver { navigateToLogin() })
        viewModel.createAccountEvent.observe(
            viewLifecycleOwner, EventObserver { navigateToCreateAccount() })
    }

    private fun navigateDirectlyToChats() {
        findNavController().navigate(R.id.action_startFragment_to_navigation_chats)
    }

    private fun navigateToLogin() {
        findNavController().navigate(R.id.action_startFragment_to_loginFragment)
    }

    private fun navigateToCreateAccount() {
        findNavController().navigate(R.id.action_startFragment_to_createAccountFragment)
    }
}