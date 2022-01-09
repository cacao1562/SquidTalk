package com.acacia.randomchat.ui.chat

import android.view.ViewGroup
import androidx.recyclerview.widget.DiffUtil
import androidx.recyclerview.widget.ListAdapter
import com.acacia.randomchat.R
import com.acacia.randomchat.model.ChatViewType
import com.acacia.randomchat.model.UserMessage
import com.acacia.randomchat.ui.base.BaseBindingViewHolder

class ChatListAdapter: ListAdapter<UserMessage, BaseBindingViewHolder<*, *>>(DIFF_CALLBACK) {

    private val dataList = ArrayList<UserMessage>()

    fun updateItem(data: UserMessage) {
        dataList.add(data)
        submitList(dataList)
    }

    fun getDataList() = dataList

    override fun getItemViewType(position: Int): Int {
        return dataList[position].viewType.ordinal
    }

    override fun onCreateViewHolder(parent: ViewGroup, viewType: Int): BaseBindingViewHolder<*, *> {
        return when(ChatViewType.values()[viewType]) {
            ChatViewType.ME -> ChatMeViewHolder(parent, R.layout.item_chat_me)
            ChatViewType.YOU -> ChatYouViewHolder(parent, R.layout.item_chat_you)
        }
    }

    override fun onBindViewHolder(holder: BaseBindingViewHolder<*, *>, position: Int) {
        dataList[position].let {
            (holder as BaseBindingViewHolder<Any, *>).bind(it)
        }
    }

    companion object {
        val DIFF_CALLBACK: DiffUtil.ItemCallback<UserMessage> =
            object : DiffUtil.ItemCallback<UserMessage>() {
                override fun areItemsTheSame(oldItem: UserMessage, newItem: UserMessage) =
                    oldItem.uuid == newItem.uuid

                override fun areContentsTheSame(oldItem: UserMessage, newItem: UserMessage) =
                    oldItem == newItem
            }
    }
}