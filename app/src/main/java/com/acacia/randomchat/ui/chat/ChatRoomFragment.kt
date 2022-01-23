package com.acacia.randomchat.ui.chat

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
import androidx.navigation.fragment.findNavController
import androidx.navigation.fragment.navArgs
import androidx.recyclerview.widget.LinearLayoutManager
import com.acacia.randomchat.R
import com.acacia.randomchat.api.RanChatRetrofit
import com.acacia.randomchat.databinding.FragmentChatRoomBinding
import com.acacia.randomchat.dp2px
import com.acacia.randomchat.getTodayDate
import com.acacia.randomchat.model.*
import com.acacia.randomchat.showToast
import com.acacia.randomchat.ui.base.BindingFragment
import com.acacia.randomchat.ui.chat.adapter.ChatListAdapter
import com.acacia.randomchat.ui.dialog.RoomLeaveDialog
import com.acacia.randomchat.utils.ImageUtil
import com.acacia.randomchat.utils.KeyboardVisibilityUtils
import com.squareup.moshi.Moshi
import com.squareup.moshi.kotlin.reflect.KotlinJsonAdapterFactory
import io.socket.emitter.Emitter
import kotlinx.coroutines.CoroutineScope
import kotlinx.coroutines.Dispatchers
import kotlinx.coroutines.launch
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
                     * 앨범 이미지
                     */
                    intent.clipData?.let { clipData ->
                        if (clipData.itemCount > 3) {
                            showToast("이미지는 3장까지 선택할 수 있습니다.")
                            return@registerForActivityResult
                        }
                        // 여러장 선택 했을때
                        repeat(clipData.itemCount) { idx ->
                            uriList.add(clipData.getItemAt(idx).uri)
                        }
                    }.run {
                        // 한 장 선택 했을때
                        intent.data?.let { uri ->
                            uriList.add(uri)
                        }
                    }
                    callApi(uriList)
                }else {
                    /**
                     * 카메라 촬영 이미지
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
        Log.d("yhw", "[ChatRoomFragment>onAttach]  [103 lines]")
        backPressedCallback = object : OnBackPressedCallback(true) {
            override fun handleOnBackPressed() {
                showLeavePopup()
            }
        }
        requireActivity().onBackPressedDispatcher.addCallback(this, backPressedCallback)
    }

    override fun onCreate(savedInstanceState: Bundle?) {
        super.onCreate(savedInstanceState)
        Log.d("yhw", "[ChatRoomFragment>onCreate] bundle=$savedInstanceState [108 lines]")
    }

    override fun onViewCreated(view: View, savedInstanceState: Bundle?) {
        super.onViewCreated(view, savedInstanceState)
        Log.d("yhw", "[ChatRoomFragment>onViewCreated] bundle=$savedInstanceState [115 lines]")
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
            addItemDecoration(ChatItemDecoration(dp2px(5f).toInt()))
        }

        binding.tvChatUserTitle.text = navArgs.roomData.userYou.userName
        binding.btnChatBack.setOnClickListener {
            showLeavePopup()
        }
        binding.btnChatSend.setOnClickListener {
            val msg = binding.etChatMsg.text.toString()
            if (msg.isNullOrEmpty()) {
                showToast("메시지를 입력해주세요.")
                return@setOnClickListener
            }
            Log.d("yhw", "[ChatRoomFragment>onViewCreated] date=${getTodayDate()} [60 lines]")
            val userMessage = UserMessage(navArgs.roomData.userMe.uuId, msg, ChatViewType.MSG_ME)
            val moshi = Moshi.Builder().add(KotlinJsonAdapterFactory()).build()
            val adapter = moshi.adapter(UserMessage::class.java)
            val jsonData = adapter.toJson(userMessage)
            mSocket?.emit("sendMsg", jsonData)
            binding.etChatMsg.setText("")
            mAdapter.updateItem(userMessage)
            scrollBottomMsg()
        }
        mSocket?.on("chat message", onChatMessage)
        mSocket?.on("chat image", onChatImage)
        mSocket?.on("user leave", onUserLeave)


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
        CoroutineScope(Dispatchers.Main).launch {
            Log.d("yhw", "[ChatRoomFragment>onChatImage] args=${args[0]} [203 lines]")
            try {
                val moshi = Moshi.Builder().build()
                val adapter = moshi.adapter(UserImageMessage::class.java)
                val userImageMessage = adapter.fromJson(args[0].toString())
                val uuId = navArgs.roomData.userMe.uuId
                userImageMessage?.let {
//                    it.viewType = if (uuId == it.uuId) {
//                        if (it.imageNames.size > 1) {
//                            ChatViewType.IMG_ME_MULTI
//                        }else {
//                            ChatViewType.IMG_ME
//                        }
//                    } else {
//                        ChatViewType.IMG_YOU
//                    }
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
        CoroutineScope(Dispatchers.Main).launch {
            Log.d("yhw", "[ChatRoomFragment>onUserLeave] call on user leave [226 lines]")
            showToast("${navArgs.roomData.userYou.userName} 님이 나가셨습니다.")
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

            val acceptTypes = listOf("image/png", "image/jpg", "image/jpeg", "image/gif")
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
            Log.d("aaa", "body= ${body.body.contentLength()}")
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
                        Log.d("ccc", "response body = $it")
                    }
                }
                isUploadSuccess = response.code() == 200
            }
            override fun onFailure(call: Call<ResponseBody?>, t: Throwable) {
                Log.d("ccc", " onFailure = ${t.message}")
            }
        })

    }

    override fun onViewStateRestored(savedInstanceState: Bundle?) {
        super.onViewStateRestored(savedInstanceState)
        Log.d("yhw", "[ChatRoomFragment>onViewStateRestored] bundle=$savedInstanceState [338 lines]")
    }

    override fun onStart() {
        super.onStart()
        Log.d("yhw", "[ChatRoomFragment>onStart]  [338 lines]")
    }

    override fun onResume() {
        super.onResume()
        Log.d("yhw", "[ChatRoomFragment>onResume]  [343 lines]")
    }

    override fun onPause() {
        super.onPause()
        Log.d("yhw", "[ChatRoomFragment>onPause]  [348 lines]")
    }

    override fun onStop() {
        super.onStop()
        Log.d("yhw", "[ChatRoomFragment>onStop]  [358 lines]")
    }

    override fun onSaveInstanceState(outState: Bundle) {
        super.onSaveInstanceState(outState)
        Log.d("yhw", "[ChatRoomFragment>onSaveInstanceState] bundle=$outState [363 lines]")
    }
    
    override fun onDestroyView() {
        super.onDestroyView()
        Log.d("yhw", "[ChatRoomFragment>onDestroyView]  [23 lines]")
    }

    override fun onDestroy() {
        super.onDestroy()
        Log.d("yhw", "[ChatRoomFragment>onDestroy]  [381 lines]")
        val moshi = Moshi.Builder().build()
        val adapter = moshi.adapter(UserData::class.java)
        val jsonData = adapter.toJson(navArgs.roomData.userMe)
        mSocket?.emit("roomLeave", jsonData)
        mSocket?.off("chat message", onChatMessage)
        mSocket?.off("chat image", onChatImage)
        mSocket?.off("user leave", onUserLeave)
        keyboardVisibilityUtils.detachKeyboardListeners()
    }

    override fun onDetach() {
        super.onDetach()
        Log.d("yhw", "[ChatRoomFragment>onDetach]  [386 lines]")
    }
}