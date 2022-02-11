package com.acacia.squidtalk.ui.chat.viewholder

import android.content.Intent
import android.view.ViewGroup
import androidx.annotation.LayoutRes
import com.acacia.squidtalk.databinding.ItemChatImgMeBinding
import com.acacia.squidtalk.model.UserImageMessage
import com.acacia.squidtalk.ui.base.BaseBindingViewHolder
import com.acacia.squidtalk.ui.chat.ImageSlideActivity

class ChatImageMeViewHolder constructor(
    parent: ViewGroup,
    @LayoutRes layoutRes: Int,
): BaseBindingViewHolder<UserImageMessage, ItemChatImgMeBinding>(parent, layoutRes) {

    override fun bind(obj: UserImageMessage) {
        binding.data = obj
        binding.ivItemChatImg.setOnClickListener {
            Intent(binding.root.context, ImageSlideActivity::class.java).also {
                it.putExtra(ImageSlideActivity.KEY_IMAGES, obj.imageNames.toTypedArray())
                it.putExtra(ImageSlideActivity.KEY_POSITION, adapterPosition)
                binding.root.context.startActivity(it)
            }
        }
    }

}