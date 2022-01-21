package com.acacia.randomchat.ui.chat

import android.view.ViewGroup
import androidx.annotation.LayoutRes
import com.acacia.randomchat.databinding.ItemChatImgMultiMeBinding
import com.acacia.randomchat.dp2px
import com.acacia.randomchat.model.UserImageMessage
import com.acacia.randomchat.ui.base.BaseBindingViewHolder

class ChatMultiImageMeViewHolder constructor(
    parent: ViewGroup,
    @LayoutRes layoutRes: Int
) : BaseBindingViewHolder<UserImageMessage, ItemChatImgMultiMeBinding>(parent, layoutRes) {

    override fun bind(obj: UserImageMessage) {
        binding.data = obj
        val size = (300 / obj.imageNames.size).toFloat()
        obj.imageNames.forEach {
            val item = ChatImageItem(binding.root.context).apply {
                setItem(it, dp2px(size).toInt())
            }
            binding.llItemChatImgMulti.addView(item)
        }
    }

}