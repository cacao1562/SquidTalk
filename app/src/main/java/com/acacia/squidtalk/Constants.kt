package com.acacia.squidtalk

class Constants {

    companion object {
        const val BaseURL = "http://192.168.1.2:3000"

        const val EMIT_EVENT_ADD_USER = "send add user"
        const val EMIT_EVENT_FIND_USERS = "send find users"
        const val EMIT_EVENT_STOP_FIND = "send stop find"
        const val EMIT_EVENT_MESSAGE = "send message"
        const val EMIT_EVENT_TYPING = "send typing"
        const val EMIT_EVENT_NOT_TYPING = "send not typing"
        const val EMIT_EVENT_EMOJI = "send emoji"
        const val EMIT_EVENT_LEAVE_ROOM = "send leave room"

        const val ON_EVENT_JOINED = "receive joined"
        const val ON_EVENT_USER_FOUND = "receive user found"
        const val ON_EVENT_USER_NOT_FOUND = "receive user not found"
        const val ON_EVENT_USER_COUNT = "receive user count"
        const val ON_EVENT_MESSAGE = "receive message"
        const val ON_EVENT_IMAGES = "receive images"
        const val ON_EVENT_TYPING = "receive typing"
        const val ON_EVENT_NOT_TYPING = "receive not typing"
        const val ON_EVENT_EMOJI = "receive emoji"
        const val ON_EVENT_LEAVE_ROOM = "receive leave room"
    }
}