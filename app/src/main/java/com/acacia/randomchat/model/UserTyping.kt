package com.acacia.randomchat.model

data class UserTyping(
    override var viewType: ChatViewType?
): BaseItemModel(viewType)