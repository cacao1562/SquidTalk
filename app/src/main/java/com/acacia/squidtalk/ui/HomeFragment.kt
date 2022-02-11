package com.acacia.squidtalk.ui

import android.os.Bundle
import android.util.Log
import android.view.View
import androidx.transition.ChangeBounds
import androidx.transition.TransitionInflater
import com.acacia.squidtalk.Common
import com.acacia.squidtalk.Constants
import com.acacia.squidtalk.R
import com.acacia.squidtalk.databinding.FragmentHomeBinding
import com.acacia.squidtalk.getShapeDrawable
import com.acacia.squidtalk.ui.base.BindingFragment
import com.acacia.squidtalk.ui.dialog.LoadingDialog

class HomeFragment: BindingFragment<FragmentHomeBinding>(R.layout.fragment_home) {

    companion object {
        fun newInstance() = HomeFragment()
    }

    override fun onCreate(savedInstanceState: Bundle?) {
        super.onCreate(savedInstanceState)
        childFragmentManager.setFragmentResultListener(LoadingDialog.KEY_DISMISS, this) { requstKey, bundle ->
            mSocket?.emit(Constants.EMIT_EVENT_STOP_FIND)
            setUserCount(Common.userCount)
        }
    }

    override fun onCreateViewInit() {
        val transition = TransitionInflater.from(context).inflateTransition(android.R.transition.move)
        sharedElementEnterTransition = ChangeBounds().apply {
            duration = 700
            enterTransition = transition
        }
    }

    override fun onViewCreated(view: View, savedInstanceState: Bundle?) {
        super.onViewCreated(view, savedInstanceState)
        binding.tvHomeNickName.text = Common.nickName
        val drawable = getShapeDrawable(Common.selectedShape)
        binding.ivProfile.setImageResource(drawable)

        binding.btnSearch.setOnClickListener {
            mSocket?.emit(Constants.EMIT_EVENT_FIND_USERS)
            LoadingDialog.newInstance().show(childFragmentManager, "loading")
        }

    }

    fun setUserCount(count: Int) {
        binding.tvUserCount.text = "$count 명"
    }

    override fun onResume() {
        super.onResume()
        setUserCount(Common.userCount)
    }
}