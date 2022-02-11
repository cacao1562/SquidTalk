package com.acacia.squidtalk.model

import androidx.annotation.RawRes

data class UserEmoji(
    @RawRes val emojiRes: Int,
    override val viewType: ChatViewType?
): BaseItemModel(viewType)