package com.acacia.randomchat.ui.chat.viewholder

import android.content.Intent
import android.view.ViewGroup
import androidx.annotation.LayoutRes
import com.acacia.randomchat.databinding.ItemChatImgMultiMeBinding
import com.acacia.randomchat.dp2px
import com.acacia.randomchat.model.UserImageMessage
import com.acacia.randomchat.ui.base.BaseBindingViewHolder
import com.acacia.randomchat.ui.chat.ChatImageItem
import com.acacia.randomchat.ui.chat.ImageSlideActivity

class ChatMultiImageMeViewHolder constructor(
    parent: ViewGroup,
    @LayoutRes layoutRes: Int,
) : BaseBindingViewHolder<UserImageMessage, ItemChatImgMultiMeBinding>(parent, layoutRes) {

    override fun bind(obj: UserImageMessage) {
        binding.data = obj
        val size = (300 / obj.imageNames.size).toFloat()
        obj.imageNames.forEachIndexed { index, str ->
            val item = ChatImageItem(binding.root.context).apply {
                setItem(str, dp2px(size).toInt())
            }
            item.setOnClickListener {
                Intent(binding.root.context, ImageSlideActivity::class.java).also {
                    it.putExtra(ImageSlideActivity.KEY_IMAGES, obj.imageNames.toTypedArray())
                    it.putExtra(ImageSlideActivity.KEY_POSITION, index)
                    binding.root.context.startActivity(it)
                }
            }
            binding.llItemChatImgMulti.addView(item)
        }
    }

}