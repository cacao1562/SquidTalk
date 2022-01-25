package com.acacia.randomchat.model

enum class ChatViewType {
    MSG_ME,
    MSG_YOU,
    IMG_ME,
    IMG_ME_MULTI,
    IMG_YOU,
    IMG_YOU_MULTI,
    TYPING
}

//sealed class ChatMsgType(val type: ChatViewType) {
//    object MSG_ME: ChatMsgType(ChatViewType.MSG_ME)
//    object MSG_YOU: ChatMsgType(ChatViewType.MSG_YOU)
//    object IMG_ME: ChatMsgType(ChatViewType.IMG_ME)
//    object IMG_YOU: ChatMsgType(ChatViewType.IMG_YOU)
//}