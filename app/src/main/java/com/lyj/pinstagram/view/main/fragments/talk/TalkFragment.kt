package com.lyj.pinstagram.view.main.fragments.talk

import android.os.Bundle
import android.util.Log
import android.view.View
import androidx.fragment.app.viewModels
import com.google.gson.Gson
import com.lyj.api.socket.TalkMessage
import com.lyj.core.base.BaseFragment
import com.lyj.core.extension.socketTag
import com.lyj.pinstagram.R
import com.lyj.pinstagram.databinding.TalkFragmentBinding
import io.socket.client.IO
import io.socket.engineio.client.transports.WebSocket
import java.net.URI

class TalkFragment private constructor() : BaseFragment<TalkFragmentViewModel, TalkFragmentBinding>(
    R.layout.talk_fragment,
    { layoutInflater, viewGroup ->
        TalkFragmentBinding.inflate(
            layoutInflater,
            viewGroup,
            false
        )
    }
) {

    companion object {
        val instance: TalkFragment by lazy { TalkFragment() }
    }

    override val viewModel: TalkFragmentViewModel by viewModels()

    override fun onViewCreated(view: View, savedInstanceState: Bundle?) {
        super.onCreate(savedInstanceState)
        Log.d(socketTag, "onViewCreated")

        try {
            val gson = Gson()
            val opts = IO.Options.builder()
                .setPath("/talk/socket")
                .setTransports(arrayOf(WebSocket.NAME))
                .build()
            var socket = IO.socket(URI.create("https://www.coguri.shop"), opts)


            socket.connect()

            binding.talkButton.setOnClickListener {
                socket.emit("say", gson.toJson(TalkMessage("김씨", "test@test.com", 1L)))
            }

        } catch (e: Exception) {
            Log.d(socketTag, e.printStackTrace().toString())
        }


    }
}