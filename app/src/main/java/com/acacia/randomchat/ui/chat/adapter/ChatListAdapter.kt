package com.acacia.randomchat.ui.chat.adapter

import android.annotation.SuppressLint
import android.util.Log
import android.view.ViewGroup
import androidx.recyclerview.widget.DiffUtil
import androidx.recyclerview.widget.ListAdapter
import com.acacia.randomchat.R
import com.acacia.randomchat.model.BaseItemModel
import com.acacia.randomchat.model.ChatViewType
import com.acacia.randomchat.ui.base.BaseBindingViewHolder
import com.acacia.randomchat.ui.chat.viewholder.*

class ChatListAdapter: ListAdapter<BaseItemModel, BaseBindingViewHolder<*, *>>(DIFF_CALLBACK) {

    private val dataList = ArrayList<BaseItemModel>()

    fun updateItem(data: BaseItemModel) {
        dataList.add(data)
        submitList(dataList)
    }

    fun getDataList() = dataList

    override fun getItemViewType(position: Int): Int {
        return dataList[position].viewType!!.ordinal
    }

    override fun onCreateViewHolder(parent: ViewGroup, viewType: Int): BaseBindingViewHolder<*, *> {
        Log.d("yhw", "[ChatListAdapter>onCreateViewHolder] CreateViewHolder type = ${ChatViewType.values()[viewType]} [29 lines]")
        return when(ChatViewType.values()[viewType]) {
            ChatViewType.MSG_ME -> ChatMeViewHolder(parent, R.layout.item_chat_msg_me)
            ChatViewType.MSG_YOU -> ChatYouViewHolder(parent, R.layout.item_chat_msg_you)
            ChatViewType.IMG_ME -> ChatImageMeViewHolder(parent, R.layout.item_chat_img_me)
            ChatViewType.IMG_YOU -> ChatImageYouViewHoler(parent, R.layout.item_chat_img_you)
            ChatViewType.IMG_ME_MULTI -> ChatMultiImageMeViewHolder(parent, R.layout.item_chat_img_multi_me)
            ChatViewType.IMG_YOU_MULTI -> ChatMultiImageYouViewHolder(parent, R.layout.item_chat_img_multi_you)
        }
    }

    override fun onBindViewHolder(holder: BaseBindingViewHolder<*, *>, position: Int) {
        dataList[position].let {
            Log.d("yhw", "[ChatListAdapter>onBindViewHolder] bind time=${it.time} [43 lines]")
            (holder as BaseBindingViewHolder<Any, *>).bind(it)
        }
    }

    companion object {
        val DIFF_CALLBACK: DiffUtil.ItemCallback<BaseItemModel> =
            object : DiffUtil.ItemCallback<BaseItemModel>() {
                override fun areItemsTheSame(oldItem: BaseItemModel, newItem: BaseItemModel) =
                    oldItem.time == newItem.time

                @SuppressLint("DiffUtilEquals")
                override fun areContentsTheSame(oldItem: BaseItemModel, newItem: BaseItemModel) =
                    oldItem == newItem
            }
    }
}