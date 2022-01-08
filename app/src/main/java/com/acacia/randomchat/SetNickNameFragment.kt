package com.acacia.randomchat

import android.os.Bundle
import android.view.View
import com.acacia.randomchat.databinding.DialogSetNicknameBinding
import com.acacia.randomchat.model.UserData
import com.squareup.moshi.Moshi

class SetNickNameFragment: BindingDialogFragment<DialogSetNicknameBinding>(R.layout.dialog_set_nickname) {

    override fun onCreate(savedInstanceState: Bundle?) {
        super.onCreate(savedInstanceState)
        setStyle(STYLE_NO_TITLE, R.style.dialogFragment)
        isCancelable = false
    }

    override fun onViewCreated(view: View, savedInstanceState: Bundle?) {
        super.onViewCreated(view, savedInstanceState)

        binding.btnNickName.setOnClickListener {
//            val data = JSONObject().apply {
//                put("userName", binding.etNickName.text)
//                put("uuid", Common.uuId)
//            }
            val data = UserData(Common.uuId, binding.etNickName.text.toString())
            val moshi = Moshi.Builder().build()
            val adapter = moshi.adapter(UserData::class.java)
            val jsonData = adapter.toJson(data)
//            mSocket?.emit("addUser", jsonData)
            dismiss()
        }
    }

    override fun onResume() {
        super.onResume()
        val params = dialog!!.window!!.attributes
        params.width = getScreenWidth(requireContext()) - dp2px(40f).toInt()
        params.horizontalMargin = 0.0f
        dialog!!.window!!.attributes = params

    }

}