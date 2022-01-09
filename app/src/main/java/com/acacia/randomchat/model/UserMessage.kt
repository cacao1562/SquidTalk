package com.acacia.randomchat.model

import com.squareup.moshi.JsonClass

@JsonClass(generateAdapter = true)
data class UserMessage(
    val uuid: String,
    val msg: String,
    var viewType: ChatViewType
)

enum class ChatViewType {
    ME, YOU
}

//sealed class ChatMsgType(val type: ChatViewType) {
//    object ME: ChatMsgType(ChatViewType.ME)
//    object YOU: ChatMsgType(ChatViewType.YOU)
//}