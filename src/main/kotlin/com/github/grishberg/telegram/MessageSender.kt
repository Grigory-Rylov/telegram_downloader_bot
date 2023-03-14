package com.github.grishberg.telegram

interface MessageSender {
    fun sendMessage(chatId: Long, text: String)
}
