package com.acacia.randomchat.ui.chat.viewholder

import android.view.ViewGroup
import androidx.annotation.LayoutRes
import com.acacia.randomchat.databinding.ItemChatTypingBinding
import com.acacia.randomchat.model.UserMessage
import com.acacia.randomchat.model.UserTyping
import com.acacia.randomchat.ui.base.BaseBindingViewHolder

class ChatTypingViewHolder constructor(
    parent: ViewGroup,
    @LayoutRes layoutRes: Int
): BaseBindingViewHolder<UserTyping, ItemChatTypingBinding>(parent, layoutRes) {

    override fun bind(obj: UserTyping) {

    }

}