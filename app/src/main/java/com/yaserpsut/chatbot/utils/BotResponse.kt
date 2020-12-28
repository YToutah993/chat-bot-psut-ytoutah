package com.yaserpsut.chatbot.utils

import com.yaserpsut.chatbot.utils.Constants.CLOSING_THE_APP
import com.yaserpsut.chatbot.utils.Constants.GET_WEATHER
import com.yaserpsut.chatbot.utils.Constants.OPEN_GOOGLE
import com.yaserpsut.chatbot.utils.Constants.OPEN_OVER_LEAF
import com.yaserpsut.chatbot.utils.Constants.OPEN_PSUT
import com.yaserpsut.chatbot.utils.Constants.OPEN_SEARCH
import java.lang.Exception


object BotResponse {

    fun basicResponses(_message: String): String {
        val random = (0..2).random()
        val message = _message.toLowerCase()

        return when {
            //Hello
            message.contains("hello") -> {
                when (random) {
                    0 -> "Hello there!"
                    1 -> "Hello my friend"
                    2 -> "Hey buddy!"
                    else -> "error"
                }
            }
            //How are you
            message.contains("how are you") -> {
                when (random) {
                    0 -> "I'm doing fine, thanks for asking!"
                    1 -> "I'm pretty good, how are you?"
                    2 -> "I'm hungry, how may I help you?"
                    else -> "error"
                }
            }
            //Math
            message.contains("solve") -> {
                val equation: String? = message.substringAfter("solve")

                return try {
                    val answer = SolveMath.solveMath(equation ?: "0")
                    answer.toString()
                } catch (e: Exception) {
                    "Sorry, I can't solve that."
                }
            }
            //Time
            message.contains("time") && message.contains("?") -> {
                Time.timeStamp()
            }
            //Open google
            message.contains("open") && message.contains("google") -> {
                OPEN_GOOGLE
            }
            //Open search
            message.contains("search") -> {
                OPEN_SEARCH
            }
            //Open PSUT
            message.contains("open") && message.contains("psut") -> {
                OPEN_PSUT
            }
            //Open our paper (overleaf)
            message.contains("yaser") && message.contains("diala") && message.contains("open") && message.contains("paper") -> {
                OPEN_OVER_LEAF
            }
            //Getting the weather for today in Amman
            message.contains("weather") && message.contains("amman") -> {
                GET_WEATHER
            }
            //Close the app
            (message.contains("close") || message.contains("shut down")) && message.contains("app") -> {
                CLOSING_THE_APP
            }
            else -> {
                when (random) {
                    0 -> "I don't understand"
                    1 -> "IDK"
                    2 -> "Try asking me something different!"
                    else -> "error"
                }
            }
        }
    }
}