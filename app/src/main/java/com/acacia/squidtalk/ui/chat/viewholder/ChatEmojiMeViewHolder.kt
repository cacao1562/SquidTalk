package com.acacia.squidtalk.ui.chat.viewholder

import android.view.ViewGroup
import androidx.annotation.LayoutRes
import com.acacia.squidtalk.databinding.ItemChatEmojiMeBinding
import com.acacia.squidtalk.model.UserEmoji
import com.acacia.squidtalk.ui.base.BaseBindingViewHolder
import com.bumptech.glide.load.resource.gif.GifDrawable

class ChatEmojiMeViewHolder constructor(
    parent: ViewGroup,
    @LayoutRes layoutRes: Int
): BaseBindingViewHolder<UserEmoji, ItemChatEmojiMeBinding>(parent, layoutRes) {

    init {
        binding.ivItemChatEmoji.setOnClickListener {
            val drawable = binding.ivItemChatEmoji.drawable
            if (drawable is GifDrawable) {
                if (drawable.isRunning.not()) {
                    drawable.startFromFirstFrame()
                }
            }
        }
    }
    override fun bind(obj: UserEmoji) {
        binding.data = obj
    }

}