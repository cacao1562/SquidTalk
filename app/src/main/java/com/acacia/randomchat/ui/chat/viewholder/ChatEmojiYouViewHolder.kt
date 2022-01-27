package com.acacia.randomchat.ui.chat.viewholder

import android.view.ViewGroup
import androidx.annotation.LayoutRes
import com.acacia.randomchat.databinding.ItemChatEmojiYouBinding
import com.acacia.randomchat.model.UserEmoji
import com.acacia.randomchat.ui.base.BaseBindingViewHolder
import com.bumptech.glide.load.resource.gif.GifDrawable

class ChatEmojiYouViewHolder constructor(
    parent: ViewGroup,
    @LayoutRes layoutRes: Int
): BaseBindingViewHolder<UserEmoji, ItemChatEmojiYouBinding>(parent, layoutRes) {

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