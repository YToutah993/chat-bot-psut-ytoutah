package com.yaserpsut.chatbot.ui

import android.content.Intent
import android.net.Uri
import androidx.appcompat.app.AppCompatActivity
import android.os.Bundle
import android.util.Log
import android.widget.Button
import android.widget.EditText
import androidx.recyclerview.widget.LinearLayoutManager
import androidx.recyclerview.widget.RecyclerView
import com.google.android.material.internal.ContextUtils.getActivity
import com.yaserpsut.chatbot.R
import com.yaserpsut.chatbot.data.Message
import com.yaserpsut.chatbot.utils.BotResponse
import com.yaserpsut.chatbot.utils.Constants.CLOSING_THE_APP
import com.yaserpsut.chatbot.utils.Constants.GET_WEATHER
import com.yaserpsut.chatbot.utils.Constants.OPEN_GOOGLE
import com.yaserpsut.chatbot.utils.Constants.OPEN_OVER_LEAF
import com.yaserpsut.chatbot.utils.Constants.OPEN_PSUT
import com.yaserpsut.chatbot.utils.Constants.OPEN_SEARCH
import com.yaserpsut.chatbot.utils.Constants.RECEIVE_ID
import com.yaserpsut.chatbot.utils.Constants.SEND_ID
import com.yaserpsut.chatbot.utils.Time
import kotlinx.coroutines.*

class MainActivity : AppCompatActivity() {

    private val TAG = MainActivity::class.java.simpleName

    private lateinit var adapter: MessagingAdapter
    private val botList = listOf("Diala", "Yaser")

    private lateinit var btn_send: Button
    private lateinit var et_message: EditText
    private lateinit var rv_messages: RecyclerView

    override fun onCreate(savedInstanceState: Bundle?) {
        super.onCreate(savedInstanceState)
        setContentView(R.layout.activity_main)

        btn_send = findViewById(R.id.btn_send)
        et_message = findViewById(R.id.et_message)
        rv_messages = findViewById(R.id.rv_messages)

        recycleView()

        clickEvents()

        val random = (0..1).random()

        customMessage("Hello! Today you're speaking with ${botList[random]}, how can I help?")
    }

    private fun clickEvents() {
        btn_send.setOnClickListener {
            sendMessage()
        }

        et_message.setOnClickListener {
            GlobalScope.launch {
                delay(100)
                withContext(Dispatchers.Main) {
                    rv_messages.scrollToPosition(adapter.itemCount - 1)
                }
            }

        }
    }

    private fun recycleView() {
        adapter = MessagingAdapter()
        rv_messages.adapter = adapter
        rv_messages.layoutManager = LinearLayoutManager(applicationContext)
    }

    private fun sendMessage() {
        val message = et_message.text.toString()
        val timeStamp = Time.timeStamp()

        if(message.isNotEmpty()) {
            et_message.setText("")
            adapter.insertMessage(Message(message, SEND_ID, timeStamp))
            rv_messages.scrollToPosition(adapter.itemCount - 1)

            botResponse(message)
        }
    }

    private fun botResponse(message: String) {
        val timeStamp = Time.timeStamp()

        GlobalScope.launch {
            delay(1000)
            withContext(Dispatchers.Main) {
                val response = BotResponse.basicResponses(message)
                adapter.insertMessage(Message(response, RECEIVE_ID, timeStamp))
                rv_messages.scrollToPosition(adapter.itemCount - 1)

                Log.d(TAG, "in botResponse: Message: $message & Response: $response")

                if(response.equals(CLOSING_THE_APP)) {
                    finishAndRemoveTask();
                }

                when(response) {
                    OPEN_GOOGLE -> {
                        val site = Intent(Intent.ACTION_VIEW)
                        site.data = Uri.parse("https://www.google.com/")
                        startActivity(site)
                    }
                    OPEN_SEARCH -> {
                        val site = Intent(Intent.ACTION_VIEW)
                        val searchTerm: String? = message.substringAfter("search")
                        site.data = Uri.parse("https://www.google.com/search?&q=$searchTerm")
                        startActivity(site)
                    }
                    OPEN_PSUT -> {
                        val site = Intent(Intent.ACTION_VIEW)
                        site.data = Uri.parse("https://www.psut.edu.jo/")
                        startActivity(site)
                    }
                    OPEN_OVER_LEAF -> {
                        val site = Intent(Intent.ACTION_VIEW)
                        site.data = Uri.parse("https://www.overleaf.com/read/jygfdxznkftp")
                        startActivity(site)
                    }
                    GET_WEATHER -> {
                        val site = Intent(Intent.ACTION_VIEW)
                        site.data = Uri.parse("https://www.accuweather.com/en/jo/amman/221790/weather-forecast/221790")
                        startActivity(site)
                    }
                }
            }
        }
    }


    // keeping us at the bottom even when we go and then come back - always at the latest
    override fun onStart() {
        super.onStart()

        GlobalScope.launch {
            delay(100)
            withContext(Dispatchers.Main) {
                rv_messages.scrollToPosition(adapter.itemCount - 1)
            }
        }
    }

    private fun customMessage(message: String) {
        GlobalScope.launch {
            delay(1000)
            withContext(Dispatchers.Main) {
                val timeStamp = Time.timeStamp()
                adapter.insertMessage(Message(message, RECEIVE_ID, timeStamp))
                rv_messages.scrollToPosition(adapter.itemCount - 1)
            }
        }
    }
}