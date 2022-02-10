package com.acacia.randomchat.ui

import android.os.Bundle
import android.util.Log
import android.view.View
import androidx.transition.ChangeBounds
import androidx.transition.TransitionInflater
import com.acacia.randomchat.Common
import com.acacia.randomchat.Constants
import com.acacia.randomchat.R
import com.acacia.randomchat.databinding.FragmentHomeBinding
import com.acacia.randomchat.getShapeDrawable
import com.acacia.randomchat.ui.base.BindingFragment
import com.acacia.randomchat.ui.dialog.LoadingDialog

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
        Log.d("yhw", "[HomeFragment>setUserCount] count=$count [35 lines]")
        binding.tvUserCount.text = "$count 명"
    }

    override fun onResume() {
        super.onResume()
        Log.d("yhw", "[HomeFragment>onResume]  [35 lines]")
        setUserCount(Common.userCount)
    }
}