package com.acacia.randomchat.ui

import android.graphics.drawable.Animatable2
import android.graphics.drawable.AnimatedVectorDrawable
import android.graphics.drawable.Drawable
import android.os.Bundle
import android.view.View
import androidx.core.view.isVisible
import com.acacia.randomchat.R
import com.acacia.randomchat.databinding.FragmentHomeBinding
import com.acacia.randomchat.ui.base.BindingFragment

class HomeFragment: BindingFragment<FragmentHomeBinding>(R.layout.fragment_home) {

    companion object {
        fun newInstance() = HomeFragment()
    }

    override fun onViewCreated(view: View, savedInstanceState: Bundle?) {
        super.onViewCreated(view, savedInstanceState)

        binding.btnSearch.setOnClickListener {
            mSocket?.emit("searchUser")
            visibleLoading(true)
        }

        val shape = binding.ivShape.drawable as AnimatedVectorDrawable
        shape.registerAnimationCallback(object : Animatable2.AnimationCallback() {
            override fun onAnimationEnd(drawable: Drawable?) {
                shape.start()
            }
        })
        shape.start()
    }

    fun visibleLoading(visible: Boolean) {
        binding.ivDolphine.isVisible = visible
    }

}