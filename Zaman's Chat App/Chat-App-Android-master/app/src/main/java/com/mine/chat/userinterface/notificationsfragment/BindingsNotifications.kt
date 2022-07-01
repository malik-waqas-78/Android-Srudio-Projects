package com.mine.chat.userinterface.notificationsfragment

import androidx.databinding.BindingAdapter
import androidx.recyclerview.widget.RecyclerView
import com.mine.chat.dataandmodels.firebasedb.fbrelatedentity.UserInfo

@BindingAdapter("bind_notifications_list")
fun bindNotificationsList(listView: RecyclerView, items: List<UserInfo>?) {
    items?.let { (listView.adapter as NotificationsListAdapter).submitList(items) }
}
