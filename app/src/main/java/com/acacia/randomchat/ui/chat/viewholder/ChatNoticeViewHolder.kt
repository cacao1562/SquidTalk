package com.acacia.randomchat.ui.chat.viewholder

import android.view.ViewGroup
import androidx.annotation.LayoutRes
import com.acacia.randomchat.databinding.ItemChatNoticeBinding
import com.acacia.randomchat.model.ChatNotice
import com.acacia.randomchat.ui.base.BaseBindingViewHolder

class ChatNoticeViewHolder constructor(
    parent: ViewGroup,
    @LayoutRes layoutRes: Int
): BaseBindingViewHolder<ChatNotice, ItemChatNoticeBinding>(parent, layoutRes) {

    override fun bind(obj: ChatNotice) {
        binding.text = obj.text
    }

}