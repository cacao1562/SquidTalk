package com.acacia.randomchat.ui.dialog

import android.graphics.Color
import android.graphics.drawable.Animatable2
import android.graphics.drawable.AnimatedVectorDrawable
import android.graphics.drawable.ColorDrawable
import android.graphics.drawable.Drawable
import android.os.Bundle
import android.view.Gravity
import android.view.LayoutInflater
import android.view.View
import android.view.ViewGroup
import androidx.fragment.app.DialogFragment
import com.acacia.randomchat.databinding.DialogLoadingBinding

class LoadingDialog: DialogFragment() {

    private lateinit var shape: AnimatedVectorDrawable

    private val animationCallback = object : Animatable2.AnimationCallback() {
        override fun onAnimationEnd(drawable: Drawable?) {
            binding.ivShape.post {
                shape.start()
            }
        }
    }

    private lateinit var binding: DialogLoadingBinding

    override fun onCreateView(
        inflater: LayoutInflater,
        container: ViewGroup?,
        savedInstanceState: Bundle?
    ): View? {
        binding = DialogLoadingBinding.inflate(inflater, container, false)
        isCancelable = false
        return binding.root
    }

    override fun onViewCreated(view: View, savedInstanceState: Bundle?) {
        super.onViewCreated(view, savedInstanceState)
        shape = binding.ivShape.drawable as AnimatedVectorDrawable
        shape.registerAnimationCallback(animationCallback)
        shape.start()
    }

    override fun onStart() {
        super.onStart()
        dialog?.let {
            it.window?.let { window ->
                with(window) {
                    setBackgroundDrawable(ColorDrawable(Color.TRANSPARENT))
                    setGravity(Gravity.CENTER)
                    setLayout(
                        ViewGroup.LayoutParams.WRAP_CONTENT,
                        ViewGroup.LayoutParams.WRAP_CONTENT
                    )
                }
            }
        }
    }

    override fun onDestroyView() {
        super.onDestroyView()
        shape.unregisterAnimationCallback(animationCallback)
    }

    companion object {
        fun newInstance() = LoadingDialog()
    }

}