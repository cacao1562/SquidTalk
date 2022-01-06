package com.acacia.randomchat

import android.os.Bundle
import android.view.View
import com.acacia.randomchat.databinding.FragmentHomeBinding
import io.socket.client.Socket

class HomeFragment: BindingFragment<FragmentHomeBinding>(R.layout.fragment_home) {

    companion object {
        fun newInstance() = HomeFragment()
    }

    private var mSocket: Socket? = null

    override fun onCreate(savedInstanceState: Bundle?) {
        super.onCreate(savedInstanceState)
//        mSocket = RandomChatApplication.instance.getSocket()
        if (activity is MainActivity) {
            mSocket = (activity as MainActivity).getSocket()
        }
    }
    override fun onViewCreated(view: View, savedInstanceState: Bundle?) {
        super.onViewCreated(view, savedInstanceState)


        binding.btnSearch.setOnClickListener {
            mSocket?.emit("searchUser")
        }
    }
}