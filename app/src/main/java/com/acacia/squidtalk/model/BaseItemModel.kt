package com.acacia.squidtalk.model

import com.acacia.squidtalk.getTodayDate

abstract class BaseItemModel(
    open val viewType: ChatViewType?,
    val time: Long = System.currentTimeMillis(),
    val displayDate: String = getTodayDate()
)