package com.acacia.randomchat.model

import com.acacia.randomchat.getTodayDate

abstract class BaseItemModel(
    open val viewType: ChatViewType?,
    val time: Long = System.currentTimeMillis(),
    val displayDate: String = getTodayDate()
)