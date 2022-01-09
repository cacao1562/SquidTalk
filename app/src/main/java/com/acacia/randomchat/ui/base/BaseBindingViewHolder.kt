package com.acacia.randomchat.ui.base

import android.view.LayoutInflater
import android.view.ViewGroup
import androidx.annotation.LayoutRes
import androidx.databinding.DataBindingUtil
import androidx.databinding.ViewDataBinding
import androidx.recyclerview.widget.RecyclerView


abstract class BaseBindingViewHolder<ITEM : Any, VB : ViewDataBinding>(
    parent: ViewGroup,
    @LayoutRes layoutRes: Int
) : RecyclerView.ViewHolder(LayoutInflater.from(parent.context).inflate(layoutRes, parent, false)) {

    protected val binding: VB = DataBindingUtil.bind(itemView)!!

    abstract fun bind(obj: ITEM)
}

