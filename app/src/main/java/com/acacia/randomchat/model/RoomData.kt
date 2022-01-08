package com.acacia.randomchat.model

import android.os.Parcelable
import com.squareup.moshi.JsonClass
import kotlinx.android.parcel.Parcelize

@Parcelize
@JsonClass(generateAdapter = true)
data class RoomData(
    val userMe: UserData,
    val userYou: UserData
): Parcelable