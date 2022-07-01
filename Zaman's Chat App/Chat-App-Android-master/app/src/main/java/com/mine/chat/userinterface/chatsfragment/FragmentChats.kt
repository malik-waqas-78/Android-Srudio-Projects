package com.mine.chat.userinterface.chatsfragment

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
import com.mine.chat.dataandmodels.modelclasses.ChatAndUserInfo
import com.mine.chat.dataandmodels.EventObserver
import com.mine.chat.databinding.ChatsFragmentsBinding
import com.mine.chat.userinterface.chatfragment.FragmentChat
import com.mine.chat.utilandexts.convertTwoUserIDs

class FragmentChats : Fragment() {

    private val viewModel: ChatsViewModelDefault by viewModels { ChatsViewModelFactory(App.myUserID) }
    private lateinit var viewDataBinding: ChatsFragmentsBinding
    private lateinit var listAdapter: ChatsListAdapter

    override fun onCreateView(
        inflater: LayoutInflater, container: ViewGroup?, savedInstanceState: Bundle?
    ): View? {
        viewDataBinding =
            ChatsFragmentsBinding.inflate(inflater, container, false).apply { viewmodel = viewModel }
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
            listAdapter = ChatsListAdapter(viewModel)
            viewDataBinding.chatsRecyclerView.adapter = listAdapter
        } else {
            throw Exception("The viewmodel is not initialized")
        }
    }

    private fun setupObservers() {
        viewModel.selectedChat.observe(viewLifecycleOwner,
            EventObserver { navigateToChat(it) })
    }

    private fun navigateToChat(chatAndUserInfo: ChatAndUserInfo) {
        val bundle = bundleOf(
            FragmentChat.ARGS_KEY_USER_ID to App.myUserID,
            FragmentChat.ARGS_KEY_OTHER_USER_ID to chatAndUserInfo.mUserInfo.id,
            FragmentChat.ARGS_KEY_CHAT_ID to convertTwoUserIDs(App.myUserID, chatAndUserInfo.mUserInfo.id)
        )
        findNavController().navigate(R.id.action_navigation_chats_to_chatFragment, bundle)
    }
}