package com.acacia.randomchat.ui.chat

import android.view.ViewGroup
import androidx.annotation.LayoutRes
import com.acacia.randomchat.databinding.ItemChatImgMeBinding
import com.acacia.randomchat.model.UserImageMessage
import com.acacia.randomchat.ui.base.BaseBindingViewHolder

class ChatImageMeViewHolder constructor(
    parent: ViewGroup,
    @LayoutRes layoutRes: Int
): BaseBindingViewHolder<UserImageMessage, ItemChatImgMeBinding>(parent, layoutRes) {

    override fun bind(obj: UserImageMessage) {
        binding.data = obj
    }

}