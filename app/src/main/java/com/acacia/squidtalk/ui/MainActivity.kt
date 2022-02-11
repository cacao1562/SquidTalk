package com.acacia.squidtalk.ui

import android.os.Bundle
import android.util.Log
import androidx.appcompat.app.AppCompatActivity
import androidx.databinding.DataBindingUtil
import androidx.fragment.app.DialogFragment
import androidx.lifecycle.lifecycleScope
import androidx.navigation.findNavController
import com.acacia.squidtalk.*
import com.acacia.squidtalk.databinding.ActivityMainBinding
import com.acacia.squidtalk.model.RoomData
import com.squareup.moshi.Moshi
import io.socket.client.Socket
import io.socket.emitter.Emitter
import kotlinx.coroutines.delay
import org.json.JSONObject
import java.io.IOException
import java.util.*

class MainActivity : AppCompatActivity() {

    private var mSocket: Socket? = null

    private var isConnected = false

    private var mUUID = ""

    private lateinit var binding: ActivityMainBinding

    fun getSocket() = mSocket

    override fun onCreate(savedInstanceState: Bundle?) {
        super.onCreate(savedInstanceState)
        binding = DataBindingUtil.setContentView(this, R.layout.activity_main)

        mUUID = UUID.randomUUID().toString()

        mSocket = SquidTalkApplication.instance.getSocket()
        mSocket?.on(Socket.EVENT_CONNECT, onConnect)
        mSocket?.on(Socket.EVENT_DISCONNECT, onDisconnect)
        mSocket?.on(Socket.EVENT_CONNECT_ERROR, onConnectError)
        mSocket?.on(Constants.ON_EVENT_JOINED, onUserJoined)
        mSocket?.on(Constants.ON_EVENT_USER_FOUND, onUserSearched)
        mSocket?.on(Constants.ON_EVENT_USER_NOT_FOUND, onUserNotSearched)
        mSocket?.on(Constants.ON_EVENT_USER_COUNT, onUserCount)

        mSocket?.connect()
    }

    override fun onDestroy() {
        super.onDestroy()
        mSocket?.off(Socket.EVENT_CONNECT, onConnect)
        mSocket?.off(Socket.EVENT_DISCONNECT, onDisconnect)
        mSocket?.off(Socket.EVENT_CONNECT_ERROR, onConnectError)
        mSocket?.off(Constants.ON_EVENT_JOINED, onUserJoined)
        mSocket?.off(Constants.ON_EVENT_USER_FOUND, onUserSearched)
        mSocket?.off(Constants.ON_EVENT_USER_NOT_FOUND, onUserNotSearched)
        mSocket?.off(Constants.ON_EVENT_USER_COUNT, onUserCount)
        mSocket?.disconnect()
    }

    private var doubleBackToExitPressedOnce = false

    private fun pressedBack() {
        if (doubleBackToExitPressedOnce) {
            finish()
            return
        }
        doubleBackToExitPressedOnce = true
        lifecycleScope.launchWhenResumed {
            showToast("뒤로가기 버튼을 한 번 더 누르면 종료됩니다.")
            delay(2000).run {
                doubleBackToExitPressedOnce = false
            }
        }
    }
    override fun onBackPressed() {
        supportFragmentManager.findFragmentById(R.id.nav_host_container)?.let {
            if (it.childFragmentManager.backStackEntryCount == 0) {
                pressedBack()
            } else {
                super.onBackPressed()
            }
        }
    }

    private val onConnect = Emitter.Listener {
        lifecycleScope.launchWhenResumed {
            showToast(getString(R.string.connect))
            isConnected = true
        }
    }

    private val onDisconnect = Emitter.Listener {
        lifecycleScope.launchWhenResumed {
            isConnected = false
            showToast(getString(R.string.disconnect))
        }
    }

    private val onConnectError = Emitter.Listener {
        lifecycleScope.launchWhenResumed {
            showToast(getString(R.string.error_connect))
        }
    }

    private val onUserJoined = Emitter.Listener { args ->
        lifecycleScope.launchWhenResumed {
            val action = InputNickNameFragmentDirections.actionInputNickNameFragmentToCharacterFragment()
            findNavController(R.id.nav_host_container).safeNavigate(action)
        }
    }

    private val onUserSearched = Emitter.Listener { args ->
        lifecycleScope.launchWhenResumed {
            val data = args[0] as JSONObject
            try {
                dismissLoading()
                val moshi = Moshi.Builder().build()
                val adapter = moshi.adapter<RoomData>(RoomData::class.java)
                val roomData = adapter.fromJson(args[0].toString())
                roomData?.let {
                    val action = HomeFragmentDirections.actionHomeFragmentToChatRoomFragment(it)
                    findNavController(R.id.nav_host_container).safeNavigate(action)
                }
            }catch (e: IOException) {
                e.printStackTrace()
            }catch (e: Exception) {
                e.printStackTrace()
            }

        }
    }

    private val onUserNotSearched = Emitter.Listener { args ->
        lifecycleScope.launchWhenResumed {
            showToast("대기중인 사용자가 없습니다.")
            dismissLoading()
        }
    }

    private val onUserCount = Emitter.Listener { args ->
        lifecycleScope.launchWhenResumed {
            val count = args[0] as Int
            Common.userCount = count
            supportFragmentManager.fragments.forEach { child ->
                child.childFragmentManager.fragments.forEach {
                    if (it is HomeFragment) {
                        it.setUserCount(count)
                    }
                }
            }
        }
    }

    private fun dismissLoading() {
        supportFragmentManager.fragments.forEach { child ->
            child.childFragmentManager.fragments.forEach {
                val loading = it.childFragmentManager.findFragmentByTag("loading")
                if (loading?.isVisible == true) {
                    (loading as DialogFragment).dismiss()
                }
            }
        }
    }
}