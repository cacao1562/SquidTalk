package com.acacia.randomchat.ui.chat

import android.util.Log
import android.view.ViewGroup
import androidx.annotation.LayoutRes
import com.acacia.randomchat.databinding.ItemChatImgYouBinding
import com.acacia.randomchat.model.UserImageMessage
import com.acacia.randomchat.ui.base.BaseBindingViewHolder

class ChatImageYouViewHoler constructor(
    parent: ViewGroup,
    @LayoutRes layoutRes: Int
): BaseBindingViewHolder<UserImageMessage, ItemChatImgYouBinding>(parent, layoutRes) {

    override fun bind(obj: UserImageMessage) {
        Log.d("yhw", "[ChatImageYouViewHoler>bind] YouImag bind [16 lines]")
        binding.data = obj
    }

}