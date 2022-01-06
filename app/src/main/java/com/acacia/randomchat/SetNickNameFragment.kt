package com.acacia.randomchat

import android.os.Bundle
import android.view.View
import com.acacia.randomchat.databinding.DialogSetNicknameBinding
import io.socket.client.Socket
import org.json.JSONObject

class SetNickNameFragment: BindingDialogFragment<DialogSetNicknameBinding>(R.layout.dialog_set_nickname) {

    private var mSocket: Socket? = null

    override fun onCreate(savedInstanceState: Bundle?) {
        super.onCreate(savedInstanceState)
        setStyle(STYLE_NO_TITLE, R.style.dialogFragment)
        isCancelable = false
        if (activity is MainActivity) {
            mSocket = (activity as MainActivity).getSocket()
        }
    }

    override fun onViewCreated(view: View, savedInstanceState: Bundle?) {
        super.onViewCreated(view, savedInstanceState)

        binding.btnNickName.setOnClickListener {
            val data = JSONObject().apply {
                put("userName", binding.etNickName.text)
                put("uuid", Common.uuId)
            }
            mSocket?.emit("addUser", data)
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