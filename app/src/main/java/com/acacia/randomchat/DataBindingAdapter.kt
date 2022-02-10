package com.acacia.randomchat

import android.util.Log
import android.widget.ImageView
import androidx.annotation.RawRes
import androidx.databinding.BindingAdapter
import com.bumptech.glide.Glide
import com.bumptech.glide.load.DataSource
import com.bumptech.glide.load.engine.DiskCacheStrategy
import com.bumptech.glide.load.engine.GlideException
import com.bumptech.glide.load.resource.gif.GifDrawable
import com.bumptech.glide.request.RequestListener
import com.bumptech.glide.request.RequestOptions
import com.bumptech.glide.request.target.Target

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
            imageView.setImageResource(android.R.drawable.ic_menu_gallery)
        }
    }

    @JvmStatic
    @BindingAdapter("loadImageCrop")
    fun <T: Any> loadImageCrop(imageView: ImageView, path: T?) {
        if (path != null) {
            val name = (path as String)
            val url = "${Constants.BaseURL}/image/$name"
            Log.d("yhw", "[DataBindingAdapter>loadImageCrop] Crop url=$url [37 lines]")
            Glide.with(imageView)
                .load(url)
                .centerCrop()
                .apply(
                    RequestOptions()
                        .placeholder(android.R.drawable.ic_menu_gallery)
                        .error(android.R.drawable.stat_notify_error))
                .into(imageView)
        }else {
            imageView.setImageResource(android.R.drawable.ic_menu_gallery)
        }
    }

    @JvmStatic
    @BindingAdapter("loadEmojiGif")
    fun loadEmojiGif(imageView: ImageView, @RawRes res: Int?) {
        if (res != null) {
            Glide.with(imageView)
                .asGif()
                .load(res) //Your gif resource
                .apply(RequestOptions
                    .diskCacheStrategyOf(DiskCacheStrategy.NONE)
                    .placeholder(android.R.drawable.ic_menu_gallery)
                    .error(android.R.drawable.stat_notify_error))
                .skipMemoryCache(true)
                .listener(object : RequestListener<GifDrawable> {
                    override fun onLoadFailed(
                        e: GlideException?,
                        model: Any?,
                        target: Target<GifDrawable>?,
                        isFirstResource: Boolean
                    ): Boolean {
                        return false
                    }

                    override fun onResourceReady(
                        resource: GifDrawable?,
                        model: Any?,
                        target: Target<GifDrawable>?,
                        dataSource: DataSource?,
                        isFirstResource: Boolean
                    ): Boolean {
                        resource?.setLoopCount(1)
                        return false
                    }
                })
                .into(imageView)
        }else {
            imageView.setImageResource(android.R.drawable.ic_menu_gallery)
        }
    }

    @JvmStatic
    @BindingAdapter("setProfileShape")
    fun setProfileShape(imageView: ImageView, any: Any?) {
        imageView.setImageResource(getShapeDrawable(Common.youShape))
    }

}