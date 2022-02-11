package com.acacia.squidtalk.ui.chat.viewholder

import android.view.ViewGroup
import androidx.annotation.LayoutRes
import com.acacia.squidtalk.databinding.ItemChatTypingBinding
import com.acacia.squidtalk.model.UserTyping
import com.acacia.squidtalk.ui.base.BaseBindingViewHolder

class ChatTypingViewHolder constructor(
    parent: ViewGroup,
    @LayoutRes layoutRes: Int
): BaseBindingViewHolder<UserTyping, ItemChatTypingBinding>(parent, layoutRes) {

    override fun bind(obj: UserTyping) {

    }

}