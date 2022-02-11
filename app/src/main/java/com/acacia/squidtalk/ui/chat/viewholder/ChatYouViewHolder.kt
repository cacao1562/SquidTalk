package com.acacia.squidtalk.ui.chat.viewholder

import android.view.ViewGroup
import androidx.annotation.LayoutRes
import com.acacia.squidtalk.databinding.ItemChatMsgYouBinding
import com.acacia.squidtalk.model.UserMessage
import com.acacia.squidtalk.ui.base.BaseBindingViewHolder

class ChatYouViewHolder constructor(
    parent: ViewGroup,
    @LayoutRes layoutRes: Int
): BaseBindingViewHolder<UserMessage, ItemChatMsgYouBinding>(parent, layoutRes) {

    override fun bind(obj: UserMessage) {
        binding.userMessage = obj
    }

}