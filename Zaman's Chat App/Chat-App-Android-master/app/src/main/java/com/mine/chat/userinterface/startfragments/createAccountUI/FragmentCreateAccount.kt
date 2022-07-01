package com.mine.chat.userinterface.startfragments.createAccountUI

import android.os.Bundle
import android.view.LayoutInflater
import android.view.MenuItem
import android.view.View
import android.view.ViewGroup
import androidx.fragment.app.Fragment
import androidx.fragment.app.viewModels
import androidx.navigation.fragment.findNavController
import com.mine.chat.dataandmodels.EventObserver
import com.mine.chat.R
import com.mine.chat.databinding.CreateAccountFragmentsBinding

import com.mine.chat.userinterface.mainfragments.MainActivity
import com.mine.chat.utilandexts.UtilSharedPreferences
import com.mine.chat.utilandexts.forceHideKeyboard
import com.mine.chat.utilandexts.showSnackBar

class FragmentCreateAccount : Fragment() {

    private val viewModel by viewModels<ViiewModelDefaultCreateAccount>()
    private lateinit var viewDataBinding: CreateAccountFragmentsBinding

    override fun onCreateView(
        inflater: LayoutInflater, container: ViewGroup?, savedInstanceState: Bundle?
    ): View? {
        viewDataBinding = CreateAccountFragmentsBinding.inflate(inflater, container, false)
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

        viewModel.isCreatedEvent.observe(viewLifecycleOwner, EventObserver {
            UtilSharedPreferences.saveUserID(requireContext(), it.uid)
            navigateToChats()
        })
    }

    private fun navigateToChats() {
        findNavController().navigate(R.id.action_createAccountFragment_to_navigation_chats)
    }
}