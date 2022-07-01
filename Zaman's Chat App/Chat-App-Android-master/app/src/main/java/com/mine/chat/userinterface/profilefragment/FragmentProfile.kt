package com.mine.chat.userinterface.profilefragment

import android.os.Bundle
import android.view.LayoutInflater
import android.view.MenuItem
import android.view.View
import android.view.ViewGroup
import androidx.fragment.app.Fragment
import androidx.fragment.app.viewModels
import androidx.navigation.fragment.findNavController
import com.mine.chat.App

import com.mine.chat.dataandmodels.EventObserver
import com.mine.chat.databinding.ProfileFragmentBinding
import com.mine.chat.utilandexts.showSnackBar
import com.mine.chat.userinterface.mainfragments.MainActivity
import com.mine.chat.utilandexts.forceHideKeyboard


class FragmentProfile : Fragment() {

    companion object {
        const val ARGS_KEY_USER_ID = "bundle_user_id"
    }

    private val viewModel: ProfileViewModelDefault by viewModels {
        ProfileViewModelFactory(App.myUserID, requireArguments().getString(ARGS_KEY_USER_ID)!!)
    }

    private lateinit var viewDataBinding: ProfileFragmentBinding

    override fun onCreateView(
        inflater: LayoutInflater, container: ViewGroup?, savedInstanceState: Bundle?
    ): View? {
        viewDataBinding = ProfileFragmentBinding.inflate(inflater, container, false)
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
    }
}