package com.acacia.squidtalk.ui.chat.viewholder

import android.content.Intent
import android.view.ViewGroup
import androidx.annotation.LayoutRes
import com.acacia.squidtalk.databinding.ItemChatImgMultiYouBinding
import com.acacia.squidtalk.dp2px
import com.acacia.squidtalk.model.UserImageMessage
import com.acacia.squidtalk.ui.base.BaseBindingViewHolder
import com.acacia.squidtalk.ui.chat.ChatImageItem
import com.acacia.squidtalk.ui.chat.ImageSlideActivity

class ChatMultiImageYouViewHolder constructor(
    parent: ViewGroup,
    @LayoutRes layoutRes: Int,
) : BaseBindingViewHolder<UserImageMessage, ItemChatImgMultiYouBinding>(parent, layoutRes) {

    override fun bind(obj: UserImageMessage) {
        binding.data = obj
        val size = (300 / obj.imageNames.size).toFloat()
        binding.root.post {
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

}