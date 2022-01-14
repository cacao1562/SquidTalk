package com.acacia.randomchat.ui

import android.os.Bundle
import android.view.View
import android.view.inputmethod.InputMethodManager
import androidx.appcompat.app.AppCompatActivity
import com.acacia.randomchat.Common
import com.acacia.randomchat.R
import com.acacia.randomchat.databinding.FragmentInputNicknameBinding
import com.acacia.randomchat.model.UserData
import com.acacia.randomchat.showToast
import com.acacia.randomchat.ui.base.BindingFragment
import com.squareup.moshi.Moshi

class InputNickNameFragment: BindingFragment<FragmentInputNicknameBinding>(R.layout.fragment_input_nickname) {

    override fun onViewCreated(view: View, savedInstanceState: Bundle?) {
        super.onViewCreated(view, savedInstanceState)

        binding.btnNickName.setOnClickListener {
            val nick = binding.etNickName.text.toString()
            if (nick.isNullOrEmpty() || nick.length > 20) {
                showToast("닉네임을 입력해주세요 (20자)")
                return@setOnClickListener
            }
            val data = UserData(Common.uuId, binding.etNickName.text.toString())
            val moshi = Moshi.Builder().build()
            val adapter = moshi.adapter(UserData::class.java)
            val jsonData = adapter.toJson(data)
            mSocket?.emit("addUser", jsonData)
            val inputMethodManager = requireActivity().getSystemService(AppCompatActivity.INPUT_METHOD_SERVICE) as InputMethodManager
            inputMethodManager.hideSoftInputFromWindow(requireActivity().currentFocus?.windowToken, InputMethodManager.HIDE_NOT_ALWAYS)
        }

    }

}