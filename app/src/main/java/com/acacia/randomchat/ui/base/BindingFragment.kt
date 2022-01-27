package com.acacia.randomchat.ui.base

import android.os.Bundle
import android.view.LayoutInflater
import android.view.View
import android.view.ViewGroup
import androidx.annotation.LayoutRes
import androidx.databinding.DataBindingUtil
import androidx.databinding.ViewDataBinding
import androidx.fragment.app.Fragment
import com.acacia.randomchat.ui.MainActivity
import io.socket.client.Socket

abstract class BindingFragment<T : ViewDataBinding>(
    @LayoutRes private val layoutId: Int
) : Fragment() {

    private var _binding: T? = null
    val binding get() = _binding!!

    private var _mSocket: Socket? = null
    val mSocket get() = _mSocket

    override fun onCreateView(
        inflater: LayoutInflater,
        container: ViewGroup?,
        savedInstanceState: Bundle?
    ): View? {
        _binding = DataBindingUtil.inflate(inflater, layoutId, container, false)
        if (activity is MainActivity) {
            _mSocket = (activity as MainActivity).getSocket()
        }
        onCreateViewInit()
        return binding.root
    }

    open fun onCreateViewInit() {}

    override fun onViewCreated(view: View, savedInstanceState: Bundle?) {
        super.onViewCreated(view, savedInstanceState)
        binding.lifecycleOwner = viewLifecycleOwner
    }

    override fun onDestroyView() {
        _binding = null
        super.onDestroyView()
    }
}