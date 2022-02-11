package com.acacia.squidtalk.ui

import android.os.Bundle
import android.view.View
import android.view.inputmethod.InputMethodManager
import androidx.appcompat.app.AppCompatActivity
import com.acacia.squidtalk.Common
import com.acacia.squidtalk.Constants
import com.acacia.squidtalk.R
import com.acacia.squidtalk.databinding.FragmentInputNicknameBinding
import com.acacia.squidtalk.model.UserData
import com.acacia.squidtalk.showToast
import com.acacia.squidtalk.ui.base.BindingFragment
import com.squareup.moshi.Moshi

class InputNickNameFragment: BindingFragment<FragmentInputNicknameBinding>(R.layout.fragment_input_nickname) {

    override fun onViewCreated(view: View, savedInstanceState: Bundle?) {
        super.onViewCreated(view, savedInstanceState)

        Common.selectedShape = (1..3).random()

        binding.btnNickName.setOnClickListener {
            val nick = binding.etNickName.text.toString()
            if (nick.isNullOrEmpty() || nick.length > 20) {
                showToast("닉네임을 입력해주세요 (20자)")
                return@setOnClickListener
            }
            val nickName = binding.etNickName.text.toString()
            Common.nickName = nickName
            val data = UserData(Common.uuId, nickName)
            val moshi = Moshi.Builder().build()
            val adapter = moshi.adapter(UserData::class.java)
            val jsonData = adapter.toJson(data)
            mSocket?.emit(Constants.EMIT_EVENT_ADD_USER, jsonData)
            val inputMethodManager = requireActivity().getSystemService(AppCompatActivity.INPUT_METHOD_SERVICE) as InputMethodManager
            inputMethodManager.hideSoftInputFromWindow(requireActivity().currentFocus?.windowToken, InputMethodManager.HIDE_NOT_ALWAYS)
        }

    }

}