package com.acacia.squidtalk.ui.chat.viewholder

import android.view.ViewGroup
import androidx.annotation.LayoutRes
import com.acacia.squidtalk.databinding.ItemChatNoticeBinding
import com.acacia.squidtalk.model.ChatNotice
import com.acacia.squidtalk.ui.base.BaseBindingViewHolder

class ChatNoticeViewHolder constructor(
    parent: ViewGroup,
    @LayoutRes layoutRes: Int
): BaseBindingViewHolder<ChatNotice, ItemChatNoticeBinding>(parent, layoutRes) {

    override fun bind(obj: ChatNotice) {
        binding.text = obj.text
    }

}