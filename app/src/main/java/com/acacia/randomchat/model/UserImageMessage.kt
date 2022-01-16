package com.acacia.randomchat.model

import com.squareup.moshi.JsonClass

@JsonClass(generateAdapter = true)
data class UserImageMessage(
    val uuId: String,
    val imageNames: List<String> = emptyList(),
    override var viewType: ChatViewType?,
): BaseItemModel(viewType)
