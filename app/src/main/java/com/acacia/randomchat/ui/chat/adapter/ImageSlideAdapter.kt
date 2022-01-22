package com.acacia.randomchat.ui.chat.adapter

import android.view.LayoutInflater
import android.view.ViewGroup
import androidx.recyclerview.widget.RecyclerView
import com.acacia.randomchat.databinding.ItemImageSlideBinding

class ImageSlideAdapter(
    private val mClickCallback: () -> Unit
): RecyclerView.Adapter<ImageSlideViewHolder>() {

    private var mData: List<String> = emptyList()

    override fun onCreateViewHolder(parent: ViewGroup, viewType: Int): ImageSlideViewHolder {
        return ImageSlideViewHolder.from(parent, mClickCallback)
    }

    override fun onBindViewHolder(holder: ImageSlideViewHolder, position: Int) {
        holder.bind(mData[position])
    }

    override fun getItemCount(): Int {
        return mData.size
    }

    fun setItem(data: List<String>) {
        mData = data
        notifyDataSetChanged()
    }
}

class ImageSlideViewHolder(
    private val binding: ItemImageSlideBinding,
    private val callback: () -> Unit
): RecyclerView.ViewHolder(binding.root) {

    init {
        binding.ivItemSlide.setOnClickListener {
            callback.invoke()
        }
    }

    fun bind(data: String) {
        binding.data = data
    }

    companion object {
        fun from(parent: ViewGroup, callback: () -> Unit): ImageSlideViewHolder {
            val layoutInflater = LayoutInflater.from(parent.context)
            val binding = ItemImageSlideBinding.inflate(layoutInflater, parent, false)
            return ImageSlideViewHolder(binding, callback)
        }
    }
}