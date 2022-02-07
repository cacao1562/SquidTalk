package com.acacia.randomchat.model

data class ChatNotice(
    val text: String,
    override var viewType: ChatViewType?
): BaseItemModel(viewType)