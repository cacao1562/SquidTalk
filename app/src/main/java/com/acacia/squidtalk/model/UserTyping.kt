package com.acacia.squidtalk.model

data class UserTyping(
    override var viewType: ChatViewType?
): BaseItemModel(viewType)