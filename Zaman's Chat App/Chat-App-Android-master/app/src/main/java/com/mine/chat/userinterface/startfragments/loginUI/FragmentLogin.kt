package com.mine.chat.userinterface.startfragments.loginUI

import android.os.Bundle
import android.view.LayoutInflater
import android.view.MenuItem
import android.view.View
import android.view.ViewGroup
import androidx.fragment.app.Fragment
import androidx.fragment.app.viewModels
import androidx.navigation.fragment.findNavController
import com.mine.chat.R
import com.mine.chat.dataandmodels.EventObserver
import com.mine.chat.databinding.LoginFragmentsBinding

import com.mine.chat.utilandexts.showSnackBar
import com.mine.chat.userinterface.mainfragments.MainActivity
import com.mine.chat.utilandexts.UtilSharedPreferences
import com.mine.chat.utilandexts.forceHideKeyboard

class FragmentLogin : Fragment() {

    private val viewModel by viewModels<ViewModelDefaultLogin>()
    private lateinit var viewDataBinding: LoginFragmentsBinding

    override fun onCreateView(
        inflater: LayoutInflater, container: ViewGroup?, savedInstanceState: Bundle?
    ): View? {
        viewDataBinding = LoginFragmentsBinding.inflate(inflater, container, false)
            .apply { viewmodel = viewModel }
        viewDataBinding.lifecycleOwner = this.viewLifecycleOwner
        setHasOptionsMenu(true)
        return viewDataBinding.root
    }

    override fun onActivityCreated(savedInstanceState: Bundle?) {
        super.onActivityCreated(savedInstanceState)
        setupObservers()
    }

    override fun onOptionsItemSelected(item: MenuItem): Boolean {
        when (item.itemId) {
            android.R.id.home -> {
                findNavController().popBackStack()
                return true
            }
        }
        return super.onOptionsItemSelected(item)
    }

    private fun setupObservers() {
        viewModel.dataLoading.observe(viewLifecycleOwner,
            EventObserver { (activity as MainActivity).showGlobalProgressBar(it) })

        viewModel.snackBarText.observe(viewLifecycleOwner,
            EventObserver { text ->
                view?.showSnackBar(text)
                view?.forceHideKeyboard()
            })

        viewModel.isLoggedInEvent.observe(viewLifecycleOwner, EventObserver {
            UtilSharedPreferences.saveUserID(requireContext(), it.uid)
            navigateToChats()
        })
    }

    private fun navigateToChats() {
        findNavController().navigate(R.id.action_loginFragment_to_navigation_chats)
    }
}