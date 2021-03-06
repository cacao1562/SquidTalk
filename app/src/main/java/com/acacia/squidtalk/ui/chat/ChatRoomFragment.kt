package com.acacia.squidtalk.ui.chat

import android.app.Activity
import android.content.Context
import android.content.Intent
import android.graphics.drawable.AnimatedVectorDrawable
import android.graphics.drawable.Drawable
import android.net.Uri
import android.os.Bundle
import android.provider.MediaStore
import android.text.Editable
import android.text.TextWatcher
import android.util.Log
import android.view.View
import androidx.activity.OnBackPressedCallback
import androidx.activity.result.ActivityResult
import androidx.activity.result.contract.ActivityResultContracts
import androidx.core.content.ContextCompat
import androidx.core.content.FileProvider
import androidx.lifecycle.lifecycleScope
import androidx.navigation.fragment.findNavController
import androidx.navigation.fragment.navArgs
import androidx.recyclerview.widget.LinearLayoutManager
import com.acacia.squidtalk.*
import com.acacia.squidtalk.api.RanChatRetrofit
import com.acacia.squidtalk.databinding.FragmentChatRoomBinding
import com.acacia.squidtalk.model.*
import com.acacia.squidtalk.ui.base.BindingFragment
import com.acacia.squidtalk.ui.chat.adapter.ChatListAdapter
import com.acacia.squidtalk.ui.dialog.EmojiDialog
import com.acacia.squidtalk.ui.dialog.RoomLeaveDialog
import com.acacia.squidtalk.utils.ImageUtil
import com.acacia.squidtalk.utils.KeyboardVisibilityUtils
import com.squareup.moshi.Moshi
import com.squareup.moshi.kotlin.reflect.KotlinJsonAdapterFactory
import io.socket.emitter.Emitter
import okhttp3.MediaType.Companion.toMediaTypeOrNull
import okhttp3.MultipartBody
import okhttp3.RequestBody
import okhttp3.RequestBody.Companion.asRequestBody
import okhttp3.RequestBody.Companion.toRequestBody
import okhttp3.ResponseBody
import retrofit2.Call
import retrofit2.Callback
import retrofit2.Response
import java.io.File
import java.io.IOException


class ChatRoomFragment: BindingFragment<FragmentChatRoomBinding>(R.layout.fragment_chat_room) {

    private val navArgs: ChatRoomFragmentArgs by navArgs()

    private lateinit var mAdapter: ChatListAdapter

    private lateinit var keyboardVisibilityUtils: KeyboardVisibilityUtils

    private var avdRes01: Drawable? = null
    private var avdRes02: Drawable? = null

    private var isUploadSuccess = false

    private val imgChooseLauncher =
        registerForActivityResult(ActivityResultContracts.StartActivityForResult()) { result: ActivityResult ->
            if (result.resultCode == Activity.RESULT_OK) {
                val intent = result.data
                if (intent != null) {
                    val uriList = mutableListOf<Uri>()
                    /**
                     * ?????? ?????????
                     */
                    intent.clipData?.let { clipData ->
                        if (clipData.itemCount > 3) {
                            showToast("???????????? 3????????? ????????? ??? ????????????.")
                            return@registerForActivityResult
                        }
                        // ????????? ?????? ?????????
                        repeat(clipData.itemCount) { idx ->
                            uriList.add(clipData.getItemAt(idx).uri)
                        }
                    }?:run {
                        // ??? ??? ?????? ?????????
                        intent.data?.let { uri ->
                            uriList.add(uri)
                        }
                    }
                    callApi(uriList)
                }else {
                    /**
                     * ????????? ?????? ?????????
                     */
                    cameraImageURI?.let { uri ->
                        callApi(listOf(uri))
                    }
                }
            }
        }

    private lateinit var backPressedCallback: OnBackPressedCallback

    override fun onAttach(context: Context) {
        super.onAttach(context)
        backPressedCallback = object : OnBackPressedCallback(true) {
            override fun handleOnBackPressed() {
                showLeavePopup()
            }
        }
        requireActivity().onBackPressedDispatcher.addCallback(this, backPressedCallback)
    }

    override fun onCreate(savedInstanceState: Bundle?) {
        super.onCreate(savedInstanceState)
        childFragmentManager.setFragmentResultListener(EmojiDialog.KEY_GIF_INDEX, this) { requestKey, bundle ->
            val index = bundle.getInt("index")
            mAdapter.updateItem(UserEmoji(index.asEmojiRes(), ChatViewType.EMOJI_ME))
            mSocket?.emit(Constants.EMIT_EVENT_EMOJI, index)
            scrollBottomMsg()
        }
    }

    private var isKeyboardUp = false

    override fun onViewCreated(view: View, savedInstanceState: Bundle?) {
        super.onViewCreated(view, savedInstanceState)
        avdRes01 = ContextCompat.getDrawable(requireContext(), R.drawable.avd_text_send)
        avdRes02 = ContextCompat.getDrawable(requireContext(), R.drawable.avd_text_send_reverse)
        keyboardVisibilityUtils = KeyboardVisibilityUtils(requireActivity().window, { height ->
            scrollBottomMsg()
            isKeyboardUp = true
        }, {
            isKeyboardUp = false
        })

        binding.btnEmoji.setOnClickListener {
            EmojiDialog.newInstance().show(childFragmentManager, "emoji")
        }
        
        mAdapter = ChatListAdapter()
        val lm = LinearLayoutManager(requireContext())
        lm.stackFromEnd = true
        binding.rvChat.apply {
            setHasFixedSize(true)
            layoutManager = lm
            adapter = mAdapter
            addItemDecoration(ChatItemDecoration(dp2px(5f).toInt()))
        }

        val userName = navArgs.roomData.userYou.userName
        binding.tvChatUserTitle.text = userName
        mAdapter.updateItem(ChatNotice("$userName ?????? ?????????????????????.", ChatViewType.NOTICE))
        Common.youShape = navArgs.roomData.userYou.shapeType
        binding.btnChatBack.setOnClickListener {
            showLeavePopup()
        }
        binding.btnChatSend.setOnClickListener {
            val msg = binding.etChatMsg.text.toString()
            if (msg.isNullOrEmpty()) {
                showToast("???????????? ??????????????????.")
                return@setOnClickListener
            }
            val userMessage = UserMessage(navArgs.roomData.userMe.uuId, msg, ChatViewType.MSG_ME)
            val moshi = Moshi.Builder().add(KotlinJsonAdapterFactory()).build()
            val adapter = moshi.adapter(UserMessage::class.java)
            val jsonData = adapter.toJson(userMessage)
            mSocket?.emit(Constants.EMIT_EVENT_MESSAGE, jsonData)
            binding.etChatMsg.setText("")
            mAdapter.updateItem(userMessage)
            scrollBottomMsg()
        }
        mSocket?.on(Constants.ON_EVENT_MESSAGE, onChatMessage)
        mSocket?.on(Constants.ON_EVENT_IMAGES, onChatImage)
        mSocket?.on(Constants.ON_EVENT_TYPING, onTyping)
        mSocket?.on(Constants.ON_EVENT_NOT_TYPING, onNotTyping)
        mSocket?.on(Constants.ON_EVENT_EMOJI, onChatEmoji)
        mSocket?.on(Constants.ON_EVENT_LEAVE_ROOM, onUserLeave)


        binding.etChatMsg.addTextChangedListener(inputWatcher)

        binding.btnAddImg.setOnClickListener {
            getChooseIntent()?.let { intent ->
                imgChooseLauncher.launch(intent)
            }
        }

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
                mSocket?.emit(Constants.EMIT_EVENT_NOT_TYPING)
            }else {
                if (isStart) return
                binding.btnChatSend.setImageDrawable(avdRes01)
                val icon = binding.btnChatSend.drawable as AnimatedVectorDrawable
                isStart = true
                icon.start()
                mSocket?.emit(Constants.EMIT_EVENT_TYPING)
            }

        }

        override fun afterTextChanged(p0: Editable?) {
            if (p0.toString().isNullOrEmpty()) {
                isStart = false
            }
        }
    }

    private fun scrollBottomMsg() {
        binding.rvChat.post {
            binding.rvChat.scrollToPosition(mAdapter.getDataList().size - 1)
        }
    }

    private val onChatMessage = Emitter.Listener { args ->
        lifecycleScope.launchWhenResumed {
            try {
                val moshi = Moshi.Builder().build()
                val adapter = moshi.adapter(UserMessage::class.java)
                val userMessage = adapter.fromJson(args[0].toString())
                userMessage?.let {
                    it.viewType = ChatViewType.MSG_YOU
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

    private val onChatImage = Emitter.Listener { args ->
        lifecycleScope.launchWhenResumed {
            try {
                val moshi = Moshi.Builder().build()
                val adapter = moshi.adapter(UserImageMessage::class.java)
                val userImageMessage = adapter.fromJson(args[0].toString())
                val uuId = navArgs.roomData.userMe.uuId
                userImageMessage?.let {
                    it.viewType = when {
                        (uuId == it.uuId && it.imageNames.size > 1) -> ChatViewType.IMG_ME_MULTI
                        uuId == it.uuId -> ChatViewType.IMG_ME
                        (uuId != it.uuId && it.imageNames.size > 1) -> ChatViewType.IMG_YOU_MULTI
                        uuId != it.uuId -> ChatViewType.IMG_YOU
                        else -> {
                            error("")
                        }
                    }
                    mAdapter.updateItem(it)
                    scrollBottomMsg()
                }
            } catch (e: IOException) {
                e.printStackTrace()
            } catch (e: Exception) {
                e.printStackTrace()
            }
        }
    }

    private val onUserLeave = Emitter.Listener { args ->
        lifecycleScope.launchWhenResumed {
            mAdapter.updateItem(ChatNotice("${navArgs.roomData.userYou.userName} ?????? ??????????????????.", ChatViewType.NOTICE))
            scrollBottomMsg()
        }
    }

    private val onTyping = Emitter.Listener { args ->
        lifecycleScope.launchWhenResumed {
            mAdapter.updateItem(UserTyping(ChatViewType.TYPING))
            scrollBottomMsg()
        }
    }

    private val onNotTyping = Emitter.Listener { args ->
        lifecycleScope.launchWhenResumed {
            mAdapter.removeTyping()
        }
    }

    private val onChatEmoji = Emitter.Listener { args ->
        lifecycleScope.launchWhenResumed {
            val index = args[0] as Int
            mAdapter.updateItem(UserEmoji(index.asEmojiRes(), ChatViewType.EMOJI_YOU))
            scrollBottomMsg()
        }
    }

    private var cameraImageURI: Uri? = null

    private fun getChooseIntent(): Intent? {
        return try {
            val imgFilePath = File.createTempFile("IMG", ".jpg", context?.cacheDir).apply {
                createNewFile()
                deleteOnExit()
            }
            cameraImageURI = FileProvider.getUriForFile(requireContext(), "${activity?.packageName}.provider", imgFilePath)

            val cameraIntent = Intent(MediaStore.ACTION_IMAGE_CAPTURE).apply {
                putExtra(MediaStore.EXTRA_OUTPUT, cameraImageURI)
            }

            val acceptTypes = listOf("image/png", "image/jpg", "image/jpeg")
            val albumIntent = Intent(Intent.ACTION_GET_CONTENT).apply {
                addCategory(Intent.CATEGORY_OPENABLE)
                addFlags(Intent.FLAG_GRANT_READ_URI_PERMISSION)
                type = "image/*"
                putExtra(Intent.EXTRA_MIME_TYPES, acceptTypes.toTypedArray())
                putExtra(Intent.EXTRA_ALLOW_MULTIPLE, true)
            }

            Intent.createChooser(albumIntent, "").apply {
                putExtra(Intent.EXTRA_INITIAL_INTENTS, arrayOf(cameraIntent))
            }
        }catch (e: Exception) {
            e.printStackTrace()
            null
        }
    }

    private fun showLeavePopup() {
        val dialog = RoomLeaveDialog.newInstance {
            findNavController().popBackStack()
        }
        dialog.show(childFragmentManager, "leaveRoom")
    }

    private fun callApi(listUri: List<Uri>) {

        val params = hashMapOf<String, RequestBody>()
        params["roomId"] = navArgs.roomData.userMe.roomId.toRequestBody(MultipartBody.FORM)
        params["uuId"] = navArgs.roomData.userMe.uuId.toRequestBody(MultipartBody.FORM)
        params["date"] = getTodayDate().toRequestBody(MultipartBody.FORM)
        val bodys = mutableListOf<MultipartBody.Part>()

        listUri.forEach {
            val resizedBmp = ImageUtil.getResizeBitmapFromUri(requireContext(), it) ?: return
            val file = ImageUtil.convertBitmapToFile(requireContext(), resizedBmp)
            val reqFile = file.asRequestBody("image/*".toMediaTypeOrNull())
            val body = MultipartBody.Part.createFormData("upload", file.name, reqFile)
            bodys.add(body)
        }

        val call = RanChatRetrofit.getService().uploadImg(bodys.toList(), params)
        call?.enqueue(object : Callback<ResponseBody?> {
            override fun onResponse(
                call: Call<ResponseBody?>,
                response: Response<ResponseBody?>
            ) {
                if (response.code() == 200) {
                    response.body()?.string()?.let {
                        Log.d("yhw", "[ChatRoomFragment>onResponse] body=$it [363 lines]")
                    }
                }
                isUploadSuccess = response.code() == 200
            }
            override fun onFailure(call: Call<ResponseBody?>, t: Throwable) {
                Log.d("yhw", "[ChatRoomFragment>onFailure] ${t.message} [368 lines]")
            }
        })

    }

    override fun onDestroy() {
        super.onDestroy()
        Common.youShape = 0
        val moshi = Moshi.Builder().build()
        val adapter = moshi.adapter(UserData::class.java)
        val jsonData = adapter.toJson(navArgs.roomData.userMe)
        mSocket?.emit(Constants.EMIT_EVENT_LEAVE_ROOM, jsonData)
        mSocket?.off(Constants.ON_EVENT_MESSAGE, onChatMessage)
        mSocket?.off(Constants.ON_EVENT_IMAGES, onChatImage)
        mSocket?.off(Constants.ON_EVENT_TYPING, onTyping)
        mSocket?.off(Constants.ON_EVENT_NOT_TYPING, onNotTyping)
        mSocket?.off(Constants.ON_EVENT_EMOJI, onChatEmoji)
        mSocket?.off(Constants.ON_EVENT_LEAVE_ROOM, onUserLeave)

        keyboardVisibilityUtils.detachKeyboardListeners()
    }

}