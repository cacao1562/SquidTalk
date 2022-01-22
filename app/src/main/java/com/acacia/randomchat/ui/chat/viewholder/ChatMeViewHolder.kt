package com.acacia.randomchat.ui.chat.viewholder

import android.view.ViewGroup
import androidx.annotation.LayoutRes
import com.acacia.randomchat.databinding.ItemChatMsgMeBinding
import com.acacia.randomchat.model.UserMessage
import com.acacia.randomchat.ui.base.BaseBindingViewHolder

class ChatMeViewHolder constructor(
    parent: ViewGroup,
    @LayoutRes layoutRes: Int
): BaseBindingViewHolder<UserMessage, ItemChatMsgMeBinding>(parent, layoutRes) {

    override fun bind(obj: UserMessage) {
        binding.userMessage = obj
    }

}