package com.acacia.randomchat.ui.dialog

import android.app.Dialog
import android.graphics.Color
import android.graphics.drawable.ColorDrawable
import android.os.Bundle
import android.view.*
import androidx.fragment.app.DialogFragment
import com.acacia.randomchat.R
import com.acacia.randomchat.databinding.DialogRoomLeaveBinding

class RoomLeaveDialog: DialogFragment() {

    private var callbackLeave: (() -> Unit)? = null

    override fun onCreateView(
        inflater: LayoutInflater,
        container: ViewGroup?,
        savedInstanceState: Bundle?
    ): View? {
        val binding = DialogRoomLeaveBinding.inflate(inflater, container, false)
        binding.btnDialogCancel.setOnClickListener {
            dismiss()
        }
        binding.btnDialogLeave.setOnClickListener {
            dismiss()
            callbackLeave?.invoke()
        }
        isCancelable = false
        return binding.root
    }

    override fun onStart() {
        super.onStart()
        dialog?.let {
            it.window?.let { window ->
                with(window) {
                    setBackgroundDrawable(ColorDrawable(Color.TRANSPARENT))
                    setGravity(Gravity.CENTER)
                    setLayout(
                        ViewGroup.LayoutParams.MATCH_PARENT,
                        ViewGroup.LayoutParams.WRAP_CONTENT
                    )
                }
            }
        }
    }

    override fun onCreateDialog(savedInstanceState: Bundle?): Dialog {
        val dialog = super.onCreateDialog(savedInstanceState)
        dialog.requestWindowFeature(Window.FEATURE_NO_TITLE)
//        dialog.window?.attributes?.windowAnimations = R.style.DialogAnimation
        return dialog
    }

    companion object {
        fun newInstance(callback: (()-> Unit)? = null): RoomLeaveDialog {
            return RoomLeaveDialog().apply {
                callbackLeave = callback
            }
        }
    }
}