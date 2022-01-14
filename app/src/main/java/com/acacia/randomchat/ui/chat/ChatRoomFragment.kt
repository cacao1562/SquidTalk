package com.acacia.randomchat.ui.chat

import android.graphics.drawable.AnimatedVectorDrawable
import android.graphics.drawable.Drawable
import android.os.Bundle
import android.text.Editable
import android.text.TextWatcher
import android.util.Log
import android.view.View
import androidx.core.content.ContextCompat
import androidx.navigation.fragment.findNavController
import androidx.navigation.fragment.navArgs
import androidx.recyclerview.widget.LinearLayoutManager
import com.acacia.randomchat.R
import com.acacia.randomchat.databinding.FragmentChatRoomBinding
import com.acacia.randomchat.getTodayDate
import com.acacia.randomchat.model.ChatViewType
import com.acacia.randomchat.model.UserData
import com.acacia.randomchat.model.UserMessage
import com.acacia.randomchat.showToast
import com.acacia.randomchat.ui.base.BindingFragment
import com.acacia.randomchat.utils.KeyboardVisibilityUtils
import com.squareup.moshi.Moshi
import io.socket.emitter.Emitter
import kotlinx.coroutines.CoroutineScope
import kotlinx.coroutines.Dispatchers
import kotlinx.coroutines.launch
import java.io.IOException


class ChatRoomFragment: BindingFragment<FragmentChatRoomBinding>(R.layout.fragment_chat_room) {

    private val args: ChatRoomFragmentArgs by navArgs()

    private lateinit var mAdapter: ChatListAdapter

    private lateinit var keyboardVisibilityUtils: KeyboardVisibilityUtils

    var avdRes01: Drawable? = null
    var avdRes02: Drawable? = null

    override fun onViewCreated(view: View, savedInstanceState: Bundle?) {
        super.onViewCreated(view, savedInstanceState)
        avdRes01 = ContextCompat.getDrawable(requireContext(), R.drawable.avd_text_send)
        avdRes02 = ContextCompat.getDrawable(requireContext(), R.drawable.avd_text_send_reverse)
        keyboardVisibilityUtils = KeyboardVisibilityUtils(requireActivity().window, {
            scrollBottomMsg()
        })
        mAdapter = ChatListAdapter()
        val lm = LinearLayoutManager(requireContext())
        lm.stackFromEnd = true
        binding.rvChat.apply {
            setHasFixedSize(true)
            layoutManager = lm
            adapter = mAdapter
        }

        binding.tvChatUserTitle.text = args.roomData.userYou.userName
        binding.btnChatBack.setOnClickListener {
            findNavController().popBackStack()
        }
        binding.btnChatSend.setOnClickListener {
            val msg = binding.etChatMsg.text.toString()
            if (msg.isNullOrEmpty()) {
                showToast("메시지를 입력해주세요.")
                return@setOnClickListener
            }
            Log.d("yhw", "[ChatRoomFragment>onViewCreated] date=${getTodayDate()} [60 lines]")
            val userMessage = UserMessage(args.roomData.userMe.uuId, msg, ChatViewType.ME)
            val moshi = Moshi.Builder().build()
            val adapter = moshi.adapter(UserMessage::class.java)
            val jsonData = adapter.toJson(userMessage)
            mSocket?.emit("sendMsg", jsonData)
            binding.etChatMsg.setText("")
            mAdapter.updateItem(userMessage)
            scrollBottomMsg()
        }
        mSocket?.on("chat message", onChatMessage)

        binding.etChatMsg.addTextChangedListener(inputWatcher)

    }

    private val inputWatcher = object : TextWatcher {
        var isStart = false

        override fun beforeTextChanged(p0: CharSequence?, p1: Int, p2: Int, p3: Int) = Unit

        override fun onTextChanged(p0: CharSequence?, p1: Int, p2: Int, p3: Int) {

            if (p0.toString().isNullOrEmpty()) {
                binding.btnChatSend.setImageDrawable(avdRes02)
                val icon = binding.btnChatSend.drawable as AnimatedVectorDrawable
                isStart = false
                icon.start()
            }else {
                if (isStart) return
                binding.btnChatSend.setImageDrawable(avdRes01)
                val icon = binding.btnChatSend.drawable as AnimatedVectorDrawable
                isStart = true
                icon.start()
            }

        }

        override fun afterTextChanged(p0: Editable?) {
            if (p0.toString().isNullOrEmpty()) {
                isStart = false
            }
        }
    }

    private fun scrollBottomMsg() {
        CoroutineScope(Dispatchers.Main).launch {
            binding.rvChat.scrollToPosition(mAdapter.getDataList().size - 1)
        }
    }
    private val onChatMessage = Emitter.Listener { args ->
        CoroutineScope(Dispatchers.Main).launch {
            Log.d("yhw", "[ChatRoomFragment>onChatMessage] args=${args[0]} [49 lines]")
            try {
                val moshi = Moshi.Builder().build()
                val adapter = moshi.adapter(UserMessage::class.java)
                val userMessage = adapter.fromJson(args[0].toString())
                userMessage?.viewType = ChatViewType.YOU
                showToast("userMessage = ${userMessage?.msg}")
                userMessage?.let {
                    mAdapter.updateItem(it)
                    scrollBottomMsg()
                }
            }catch (e: IOException) {
                e.printStackTrace()
            }catch (e: Exception) {
                e.printStackTrace()
            }
        }
    }

    override fun onDestroyView() {
        super.onDestroyView()
        Log.d("yhw", "[ChatRoomFragment>onDestroyView]  [23 lines]")
        val moshi = Moshi.Builder().build()
        val adapter = moshi.adapter(UserData::class.java)
        val jsonData = adapter.toJson(args.roomData.userMe)
        mSocket?.emit("roomLeave", jsonData)
        mSocket?.off("chat message", onChatMessage)
        keyboardVisibilityUtils.detachKeyboardListeners()
    }

}