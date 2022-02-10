package com.acacia.randomchat.model

import com.squareup.moshi.JsonClass

@JsonClass(generateAdapter = true)
data class UserMessage(
    val uuid: String,
    val msg: String,
    override var viewType: ChatViewType?
): BaseItemModel(viewType)