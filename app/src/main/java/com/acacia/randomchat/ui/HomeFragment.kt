package com.acacia.randomchat.ui

import android.os.Bundle
import android.util.Log
import android.view.View
import com.acacia.randomchat.Common
import com.acacia.randomchat.R
import com.acacia.randomchat.databinding.FragmentHomeBinding
import com.acacia.randomchat.ui.base.BindingFragment
import com.acacia.randomchat.ui.dialog.LoadingDialog

class HomeFragment: BindingFragment<FragmentHomeBinding>(R.layout.fragment_home) {

    companion object {
        fun newInstance() = HomeFragment()
    }

    override fun onViewCreated(view: View, savedInstanceState: Bundle?) {
        super.onViewCreated(view, savedInstanceState)

        binding.btnSearch.setOnClickListener {
            mSocket?.emit("searchUser")
            LoadingDialog.newInstance().show(childFragmentManager, "loading")
        }

    }

    fun setUserCount(count: Int) {
        Log.d("yhw", "[HomeFragment>setUserCount] count=$count [35 lines]")
        binding.tvUserCount.text = "$count 명"
    }

    override fun onResume() {
        super.onResume()
        Log.d("yhw", "[HomeFragment>onResume]  [35 lines]")
        setUserCount(Common.userCount)
    }
}