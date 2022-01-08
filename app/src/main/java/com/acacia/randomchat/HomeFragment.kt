package com.acacia.randomchat

import android.os.Bundle
import android.view.View
import androidx.core.view.isVisible
import com.acacia.randomchat.databinding.FragmentHomeBinding

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

}