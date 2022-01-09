package com.acacia.randomchat.ui.chat

import android.view.ViewGroup
import androidx.annotation.LayoutRes
import com.acacia.randomchat.databinding.ItemChatMeBinding
import com.acacia.randomchat.model.UserMessage
import com.acacia.randomchat.ui.base.BaseBindingViewHolder

class ChatMeViewHolder constructor(
    parent: ViewGroup,
    @LayoutRes layoutRes: Int
): BaseBindingViewHolder<UserMessage, ItemChatMeBinding>(parent, layoutRes) {

    override fun bind(obj: UserMessage) {
        binding.userMessage = obj
    }

}