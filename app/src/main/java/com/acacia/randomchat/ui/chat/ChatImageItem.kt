package com.acacia.randomchat.ui.chat

import android.content.Context
import android.util.AttributeSet
import android.view.LayoutInflater
import android.widget.LinearLayout
import androidx.constraintlayout.widget.ConstraintLayout
import com.acacia.randomchat.databinding.ItemChatImgBinding

class ChatImageItem @JvmOverloads constructor(
    context: Context,
    attrs: AttributeSet? = null,
    defStyleAttr: Int = 0
) : ConstraintLayout(context, attrs, defStyleAttr) {

    init {
        initView(attrs)
    }

    private lateinit var binding: ItemChatImgBinding

    private fun initView(attrs: AttributeSet?) {
        val li = context.getSystemService(Context.LAYOUT_INFLATER_SERVICE) as LayoutInflater
        binding = ItemChatImgBinding.inflate(li, this, false)
        addView(binding.root)
    }

    fun setItem(data: String, heightValue: Int) {
        val lp = LinearLayout.LayoutParams(LayoutParams.MATCH_PARENT, LayoutParams.WRAP_CONTENT, 1f)
        lp.height = heightValue
        lp.width = heightValue
        lp.weight = 1f
        binding.ivChatItem.layoutParams = lp
        binding.data = data
    }

}