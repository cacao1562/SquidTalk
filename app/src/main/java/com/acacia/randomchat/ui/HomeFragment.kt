package com.acacia.randomchat.ui

import android.graphics.drawable.Animatable2
import android.graphics.drawable.AnimatedVectorDrawable
import android.graphics.drawable.Drawable
import android.os.Bundle
import android.view.View
import com.acacia.randomchat.R
import com.acacia.randomchat.databinding.FragmentHomeBinding
import com.acacia.randomchat.ui.base.BindingFragment
import com.acacia.randomchat.ui.dialog.LoadingDialog

class HomeFragment: BindingFragment<FragmentHomeBinding>(R.layout.fragment_home) {

    companion object {
        fun newInstance() = HomeFragment()
    }

    private lateinit var mDialog: LoadingDialog

    override fun onViewCreated(view: View, savedInstanceState: Bundle?) {
        super.onViewCreated(view, savedInstanceState)

        binding.btnSearch.setOnClickListener {
            mSocket?.emit("searchUser")
            visibleLoading(true)
        }

        mDialog = LoadingDialog.newInstance()
    }

    fun visibleLoading(visible: Boolean) {
        if (visible) {
            mDialog.show(childFragmentManager, "loading")
        }else {
            mDialog.dismiss()
        }
    }

}