package com.acacia.randomchat.ui.dialog

import android.annotation.SuppressLint
import android.app.Dialog
import android.content.DialogInterface
import android.os.Bundle
import android.view.LayoutInflater
import android.view.View
import android.view.ViewGroup
import android.widget.FrameLayout
import androidx.core.os.bundleOf
import androidx.fragment.app.setFragmentResult
import com.acacia.randomchat.R
import com.acacia.randomchat.databinding.DialogEmojiBinding
import com.acacia.randomchat.dp2px
import com.bumptech.glide.Glide
import com.bumptech.glide.load.DataSource
import com.bumptech.glide.load.engine.DiskCacheStrategy
import com.bumptech.glide.load.engine.GlideException
import com.bumptech.glide.load.resource.gif.GifDrawable
import com.bumptech.glide.request.RequestListener
import com.bumptech.glide.request.RequestOptions
import com.bumptech.glide.request.target.Target
import com.google.android.material.bottomsheet.BottomSheetBehavior
import com.google.android.material.bottomsheet.BottomSheetDialog
import com.google.android.material.bottomsheet.BottomSheetDialogFragment

class EmojiDialog: BottomSheetDialogFragment() {

    companion object {
        fun newInstance() = EmojiDialog()
        const val KEY_GIF_INDEX = "KEY_GIF_INDEX"
    }

    private lateinit var binding: DialogEmojiBinding

    private val resources = arrayOf(R.raw.squid_emoji_1, R.raw.squid_emoji_2, R.raw.squid_emoji_3, R.raw.squid_emoji_4)

    private val imageViews by lazy {
        arrayOf(binding.ivEmoji01, binding.ivEmoji02, binding.ivEmoji03, binding.ivEmoji04)
    }

    override fun onCreateView(
        inflater: LayoutInflater,
        container: ViewGroup?,
        savedInstanceState: Bundle?
    ): View? {
        binding = DialogEmojiBinding.inflate(layoutInflater, container, false)
        return binding.root
    }

    override fun onViewCreated(view: View, savedInstanceState: Bundle?) {
        super.onViewCreated(view, savedInstanceState)
        resources.forEachIndexed(::setGif)
        imageViews.mapIndexed { index, imageView -> imageView.setOnClickListener {
            setFragmentResult(KEY_GIF_INDEX, bundleOf("index" to index))
            dismiss()
        } }
    }

    @SuppressLint("CheckResult")
    private fun setGif(index: Int, resourceId: Int) {
        Glide.with(this)
            .asGif()
            .load(resourceId) //Your gif resource
            .apply(RequestOptions.diskCacheStrategyOf(DiskCacheStrategy.NONE))
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
            .into(imageViews[index])
    }

    override fun onCreateDialog(savedInstanceState: Bundle?): Dialog {
        val dialog = super.onCreateDialog(savedInstanceState)
        dialog.setOnShowListener { dialogInterface: DialogInterface? ->
            val bottomSheetDialog = dialogInterface as BottomSheetDialog?
            setupRatio(bottomSheetDialog!!)
        }
        return dialog
    }

    private fun setupRatio(bottomSheetDialog: BottomSheetDialog) {
        val bottomSheet =
            bottomSheetDialog.findViewById<View>(R.id.design_bottom_sheet) as FrameLayout?
        val behavior: BottomSheetBehavior<*> = BottomSheetBehavior.from(bottomSheet!!)
        val layoutParams = bottomSheet!!.layoutParams
        layoutParams.height =  dp2px(300f).toInt()
        bottomSheet.layoutParams = layoutParams
        behavior.state = BottomSheetBehavior.STATE_EXPANDED
        behavior.skipCollapsed = true
    }

}