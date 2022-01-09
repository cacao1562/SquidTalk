package com.acacia.randomchat.ui

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
    }

    fun visibleLoading(visible: Boolean) {
        binding.ivDolphine.isVisible = visible
    }

    override fun onResume() {
        super.onResume()

    }

}