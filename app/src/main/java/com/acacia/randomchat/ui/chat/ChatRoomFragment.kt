package com.acacia.randomchat.ui.chat

import android.os.Bundle
import android.util.Log
import android.view.View
import androidx.navigation.fragment.findNavController
import androidx.navigation.fragment.navArgs
import com.acacia.randomchat.R
import com.acacia.randomchat.databinding.FragmentChatRoomBinding
import com.acacia.randomchat.model.ChatViewType
import com.acacia.randomchat.model.UserData
import com.acacia.randomchat.model.UserMessage
import com.acacia.randomchat.showToast
import com.acacia.randomchat.ui.base.BindingFragment
import com.squareup.moshi.Moshi
import io.socket.emitter.Emitter
import kotlinx.coroutines.CoroutineScope
import kotlinx.coroutines.Dispatchers
import kotlinx.coroutines.launch
import java.io.IOException

class ChatRoomFragment: BindingFragment<FragmentChatRoomBinding>(R.layout.fragment_chat_room) {

    private val args: ChatRoomFragmentArgs by navArgs()

    private lateinit var mAdapter: ChatListAdapter

    override fun onViewCreated(view: View, savedInstanceState: Bundle?) {
        super.onViewCreated(view, savedInstanceState)

        mAdapter = ChatListAdapter()
        binding.rvChat.apply {
            setHasFixedSize(true)
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

        binding.etChatMsg.setOnFocusChangeListener { view, focus ->
            if (focus) {
                binding.rvChat.scrollTo(0, binding.rvChat.bottom)
            }
        }

    }

    private fun scrollBottomMsg() {
        binding.rvChat.scrollToPosition(mAdapter.getDataList().size - 1)
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
    }

}