package com.acacia.randomchat.ui.chat

import android.view.ViewGroup
import androidx.annotation.LayoutRes
import com.acacia.randomchat.databinding.ItemChatYouBinding
import com.acacia.randomchat.model.UserMessage
import com.acacia.randomchat.ui.base.BaseBindingViewHolder

class ChatYouViewHolder constructor(
    parent: ViewGroup,
    @LayoutRes layoutRes: Int
): BaseBindingViewHolder<UserMessage, ItemChatYouBinding>(parent, layoutRes) {

    override fun bind(obj: UserMessage) {
        binding.userMessage = obj
    }

}