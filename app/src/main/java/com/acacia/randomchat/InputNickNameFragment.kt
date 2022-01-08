package com.acacia.randomchat

import android.os.Bundle
import android.view.View
import com.acacia.randomchat.databinding.FragmentInputNicknameBinding
import com.acacia.randomchat.model.UserData
import com.squareup.moshi.Moshi
import io.socket.client.Socket

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
        }

    }

}