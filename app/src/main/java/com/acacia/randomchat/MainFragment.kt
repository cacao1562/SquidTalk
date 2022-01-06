package com.acacia.randomchat

import android.os.Bundle
import android.view.View
import com.acacia.randomchat.databinding.FragmentMainBinding

class MainFragment: BindingFragment<FragmentMainBinding>(R.layout.fragment_main) {

    override fun onViewCreated(view: View, savedInstanceState: Bundle?) {
        super.onViewCreated(view, savedInstanceState)

        binding.vp2Main.adapter = MainViewPagerAdapter(requireActivity())

    }
}