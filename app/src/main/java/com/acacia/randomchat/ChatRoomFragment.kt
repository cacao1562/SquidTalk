package com.acacia.randomchat

import android.os.Bundle
import android.view.View
import androidx.navigation.fragment.findNavController
import androidx.navigation.fragment.navArgs
import com.acacia.randomchat.databinding.FragmentChatRoomBinding

class ChatRoomFragment: BindingFragment<FragmentChatRoomBinding>(R.layout.fragment_chat_room) {

    private val args: ChatRoomFragmentArgs by navArgs()

    override fun onViewCreated(view: View, savedInstanceState: Bundle?) {
        super.onViewCreated(view, savedInstanceState)
        binding.tvChatUserTitle.text = args.roomData.userYou.userName
        binding.btnChatBack.setOnClickListener {
            findNavController().popBackStack()
        }
    }
}