package com.acacia.squidtalk.model

import android.os.Parcelable
import com.acacia.squidtalk.Common
import com.squareup.moshi.JsonClass
import kotlinx.android.parcel.Parcelize

@Parcelize
@JsonClass(generateAdapter = true)
data class UserData(
    val uuId: String,
    val userName: String,
    val roomId: String = "",
    val shapeType: Int = Common.selectedShape
): Parcelable