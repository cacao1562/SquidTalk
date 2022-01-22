package com.acacia.randomchat.ui.chat

import android.os.Bundle
import android.util.Log
import android.widget.FrameLayout
import android.widget.ImageView
import androidx.appcompat.app.AppCompatActivity
import androidx.core.view.isVisible
import androidx.databinding.DataBindingUtil
import androidx.viewpager2.widget.ViewPager2
import com.acacia.randomchat.DataBindingAdapter
import com.acacia.randomchat.R
import com.acacia.randomchat.databinding.ActivityImageSlideBinding
import com.acacia.randomchat.ui.chat.adapter.ImageSlideAdapter
import com.bumptech.glide.Glide
import com.google.android.material.tabs.TabLayoutMediator

class ImageSlideActivity: AppCompatActivity() {

    companion object {
        const val KEY_IMAGES = "KEY_IMAGES"
        const val KEY_POSITION = "KEY_POSITION"
    }
    private lateinit var binding: ActivityImageSlideBinding

    private val viewPagerCallback = object : ViewPager2.OnPageChangeCallback() {
        override fun onPageSelected(position: Int) {
            repeat(binding.tlImageSlide.tabCount) {
                binding.tlImageSlide.getTabAt(it)?.customView?.findViewById<FrameLayout>(R.id.fl_tab_item)?.isVisible = false
            }
            binding.tlImageSlide.getTabAt(position)?.customView?.findViewById<FrameLayout>(R.id.fl_tab_item)?.isVisible = true
        }
    }

    override fun onCreate(savedInstanceState: Bundle?) {
        super.onCreate(savedInstanceState)
        binding = DataBindingUtil.setContentView(this, R.layout.activity_image_slide)
        val data = intent.getStringArrayExtra(KEY_IMAGES)
        val selectedPosition = intent.getIntExtra(KEY_POSITION, 0)
        Log.d("yhw", "[ImageSlideActivity>onCreate] selectedPosition=$selectedPosition [26 lines]")
        val adapter = ImageSlideAdapter {
            binding.flImageSlide.post {
                binding.flImageSlide.isVisible = !binding.flImageSlide.isVisible
            }
        }
        binding.vp2ImageSlide.adapter = adapter
        binding.vp2ImageSlide.registerOnPageChangeCallback(viewPagerCallback)
        data?.let {
            adapter.setItem(data.toList())
        }
        TabLayoutMediator(binding.tlImageSlide, binding.vp2ImageSlide) { tab, position ->
            binding.vp2ImageSlide.currentItem = tab.position
            tab.setCustomView(R.layout.item_tab_image)
            val imageView = tab.customView?.findViewById<ImageView>(R.id.iv_tab_item)
            imageView?.let { view ->
                DataBindingAdapter.loadImageCrop(view, data?.getOrNull(position))
            }
        }.attach()
        
        binding.vp2ImageSlide.setCurrentItem(selectedPosition, false)
        
        binding.btnBackImageSlide.setOnClickListener {
            finish()
        }
    }
}