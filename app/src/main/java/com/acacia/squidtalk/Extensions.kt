package com.acacia.squidtalk

import android.annotation.SuppressLint
import android.content.res.Resources
import android.os.Build
import android.util.TypedValue
import android.widget.Toast
import androidx.appcompat.app.AppCompatActivity
import androidx.fragment.app.Fragment
import androidx.navigation.NavController
import androidx.navigation.NavDirections
import java.text.SimpleDateFormat
import java.time.LocalDateTime
import java.time.format.DateTimeFormatter
import java.util.*


fun dp2px(dp: Float): Float {
    val r = Resources.getSystem()
    return TypedValue.applyDimension(TypedValue.COMPLEX_UNIT_DIP, dp, r.displayMetrics)
}

fun NavController.safeNavigate(direction: NavDirections) {
    currentDestination?.getAction(direction.actionId)?.run { navigate(direction) }
}

@SuppressLint("SimpleDateFormat")
fun getTodayDate(): String {
    return if (Build.VERSION.SDK_INT >= Build.VERSION_CODES.O) {
        LocalDateTime.now().format(DateTimeFormatter.ofPattern("a HH:mm"))
    }else {
        SimpleDateFormat("a HH:mm").format(Date())
    }
}

fun Fragment.showToast(msg: String) = Toast.makeText(requireContext(), msg, Toast.LENGTH_SHORT).show()
fun AppCompatActivity.showToast(msg: String) = Toast.makeText(this, msg, Toast.LENGTH_SHORT).show()


fun Int.asEmojiRes(): Int {
    return when(this) {
        0 -> R.raw.squid_emoji_1
        1 -> R.raw.squid_emoji_2
        2 -> R.raw.squid_emoji_3
        3 -> R.raw.squid_emoji_4
        else -> R.raw.squid_emoji_1
    }
}

fun getShapeDrawable(type: Int): Int {
    return when(type) {
        1 -> R.drawable.ic_squid_circle
        2 -> R.drawable.ic_squid_triangle
        3 -> R.drawable.ic_squid_square
        else -> 0
    }
}