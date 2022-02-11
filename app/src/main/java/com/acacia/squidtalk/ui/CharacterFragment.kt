package com.acacia.squidtalk.ui

import android.graphics.drawable.Animatable2
import android.graphics.drawable.AnimatedVectorDrawable
import android.graphics.drawable.Drawable
import android.os.Bundle
import android.view.View
import androidx.core.os.bundleOf
import androidx.lifecycle.lifecycleScope
import androidx.navigation.fragment.FragmentNavigatorExtras
import androidx.navigation.fragment.findNavController
import com.acacia.squidtalk.Common
import com.acacia.squidtalk.R
import com.acacia.squidtalk.databinding.FragmentCharacterBinding
import com.acacia.squidtalk.ui.base.BindingFragment
import kotlinx.coroutines.delay


class CharacterFragment: BindingFragment<FragmentCharacterBinding>(R.layout.fragment_character) {

    private lateinit var shape: AnimatedVectorDrawable
    private var isShapeStop = false

    private val animationCallback = object : Animatable2.AnimationCallback() {
        override fun onAnimationEnd(drawable: Drawable?) {
            if (isShapeStop) {
                return
            }
            binding.ivShape.post {
                shape.start()
            }
        }
    }

    private fun completedShape() {

        val shape = when(Common.selectedShape) {
            1 -> "동그라미"
            2 -> "세모"
            3 -> "네모"
            else -> ""
        }
        val drawable = when(Common.selectedShape) {
            1 -> R.drawable.mask_circle
            2 -> R.drawable.mask_tri
            3 -> R.drawable.mask_rect
            else -> 0
        }
        binding.tvCharacter.text = "당신은 $shape 입니다."
        binding.ivShape.setImageResource(drawable)
        binding.tvCharacter.animate().alpha(1f).setDuration(1500)
        binding.ivShape.animate().alpha(1f).setDuration(1500)
        binding.btnConfirm.animate().alpha(1f).setDuration(1500)
    }

    override fun onViewCreated(view: View, savedInstanceState: Bundle?) {
        super.onViewCreated(view, savedInstanceState)
        shape = binding.ivShapeText.drawable as AnimatedVectorDrawable
        shape.registerAnimationCallback(animationCallback)

        binding.tvCharacter.alpha = 0f
        binding.ivShapeText.alpha = 0f
        binding.ivShape.alpha = 0f
        binding.btnConfirm.alpha = 0f
        binding.tvCharacter.animate().alpha(1f).setDuration(1500)
        binding.ivShapeText.animate().alpha(1f).setDuration(1500).withEndAction {
            shape.start()
        }
        lifecycleScope.launchWhenStarted {
            delay(5000)
            binding.tvCharacter.animate().alpha(0f).setDuration(1500)
            binding.ivShapeText.animate().alpha(0f).setDuration(1500).withEndAction {
                isShapeStop = true
                shape.stop()
                completedShape()
            }
        }

        binding.btnConfirm.setOnClickListener {
            val extra = FragmentNavigatorExtras(binding.ivShape to "home")
            findNavController()
                .navigate(R.id.action_characterFragment_to_homeFragment,
                    bundleOf("shape" to Common.selectedShape),
                    null,
                    extra)
        }
    }

    override fun onDestroyView() {
        super.onDestroyView()
        shape.unregisterAnimationCallback(animationCallback)
    }

}