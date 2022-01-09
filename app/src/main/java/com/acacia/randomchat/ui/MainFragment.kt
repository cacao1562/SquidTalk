package com.acacia.randomchat.ui

import android.os.Bundle
import android.view.View
import com.acacia.randomchat.R
import com.acacia.randomchat.databinding.FragmentMainBinding
import com.acacia.randomchat.ui.base.BindingFragment

class MainFragment: BindingFragment<FragmentMainBinding>(R.layout.fragment_main) {

    override fun onViewCreated(view: View, savedInstanceState: Bundle?) {
        super.onViewCreated(view, savedInstanceState)

        binding.vp2Main.adapter = MainViewPagerAdapter(requireActivity())
        binding.vp2Main.currentItem = 1
    }
}