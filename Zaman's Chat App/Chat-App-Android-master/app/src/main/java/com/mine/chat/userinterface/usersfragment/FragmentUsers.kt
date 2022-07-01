package com.mine.chat.userinterface.usersfragment

import android.os.Bundle
import android.view.LayoutInflater
import android.view.View
import android.view.ViewGroup
import androidx.core.os.bundleOf
import androidx.fragment.app.Fragment
import androidx.fragment.app.viewModels
import androidx.navigation.fragment.findNavController
import com.mine.chat.App
import com.mine.chat.R

import com.mine.chat.dataandmodels.EventObserver
import com.mine.chat.databinding.UsersFragmentBinding
import com.mine.chat.userinterface.profilefragment.FragmentProfile


class FragmentUsers : Fragment() {

    private val viewModel: UsersViewModelDefault by viewModels { UsersViewModelFactory(App.myUserID) }
    private lateinit var viewDataBinding: UsersFragmentBinding
    private lateinit var listAdapter: UsersListAdapter

    override fun onCreateView(
        inflater: LayoutInflater, container: ViewGroup?,
        savedInstanceState: Bundle?
    ): View? {
        viewDataBinding =
            UsersFragmentBinding.inflate(inflater, container, false).apply { viewmodel = viewModel }
        viewDataBinding.lifecycleOwner = this.viewLifecycleOwner
        return viewDataBinding.root
    }

    override fun onActivityCreated(savedInstanceState: Bundle?) {
        super.onActivityCreated(savedInstanceState)
        setupListAdapter()
        setupObservers()
    }

    private fun setupListAdapter() {
        val viewModel = viewDataBinding.viewmodel
        if (viewModel != null) {
            listAdapter = UsersListAdapter(viewModel)
            viewDataBinding.usersRecyclerView.adapter = listAdapter
        } else {
            throw Exception("The viewmodel is not initialized")
        }
    }

    private fun setupObservers() {
        viewModel.selectedUser.observe(viewLifecycleOwner, EventObserver { navigateToProfile(it.info.id) })
    }

    private fun navigateToProfile(userID: String) {
        val bundle = bundleOf(FragmentProfile.ARGS_KEY_USER_ID to userID)
        findNavController().navigate(R.id.action_navigation_users_to_profileFragment, bundle)
    }
}