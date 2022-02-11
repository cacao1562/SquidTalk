package com.acacia.squidtalk.model

data class ChatNotice(
    val text: String,
    override var viewType: ChatViewType?
): BaseItemModel(viewType)