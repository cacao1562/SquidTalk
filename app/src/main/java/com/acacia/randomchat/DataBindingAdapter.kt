package com.acacia.randomchat

import android.util.Log
import android.widget.ImageView
import androidx.databinding.BindingAdapter
import com.bumptech.glide.Glide
import com.bumptech.glide.request.RequestOptions

object DataBindingAdapter {

    @JvmStatic
    @BindingAdapter("loadImageOrDefault")
    fun <T: Any> loadImageOrDefault(imageView: ImageView, path: T?) {
        if (path != null) {
            val name = (path as String)
            val url = "${Constants.BaseURL}/image/$name"
            Log.d("yhw", "[DataBindingAdapter>loadImageOrDefault] url=$url [17 lines]")
            Glide.with(imageView)
                .load(url)
                .apply(
                    RequestOptions()
                        .placeholder(android.R.drawable.ic_menu_gallery)
                        .error(android.R.drawable.stat_notify_error))
                .into(imageView)
        }else {
            imageView.setImageResource(android.R.drawable.star_off)
        }
    }

    @JvmStatic
    @BindingAdapter("loadImageCrop")
    fun <T: Any> loadImageCrop(imageView: ImageView, path: T?) {
        if (path != null) {
            val name = (path as String)
            val url = "${Constants.BaseURL}/image/$name"
            Log.d("yhw", "[DataBindingAdapter>loadImageOrDefault] url=$url [17 lines]")
            Glide.with(imageView)
                .load(url)
                .centerCrop()
                .apply(
                    RequestOptions()
                        .placeholder(android.R.drawable.ic_menu_gallery)
                        .error(android.R.drawable.stat_notify_error))
                .into(imageView)
        }else {
            imageView.setImageResource(android.R.drawable.star_off)
        }
    }

}