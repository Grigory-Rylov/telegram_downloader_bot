package com.github.grishberg.telegram

import org.telegram.telegrambots.bots.TelegramLongPollingBot
import org.telegram.telegrambots.meta.api.methods.send.SendMessage
import org.telegram.telegrambots.meta.api.objects.Update
import org.telegram.telegrambots.meta.exceptions.TelegramApiException


class TorrentsFilesHandler(
    botToken: String,
    private val botUserName: String,
    private val torrentFileProcessor: DocumentAttachmentsDownloader,
) : TelegramLongPollingBot(botToken), MessageSender {

    override fun getBotUsername(): String = botUserName

    override fun onUpdateReceived(update: Update) {
        if (!update.hasMessage()) {
            println(update)
            println("Has no messages")
            return
        }

        if (update.message.hasDocument()) {
            val doc = update.message.document
            if (doc.mimeType != TORRENT_MIME_TYPE) {
                sendMessage(update.message.chatId, "Unsupported file type")
                return
            }

            torrentFileProcessor.downloadDocumentFile(update.message.chatId, doc, this, this)
            return
        }

        if (update.message.hasText()) {
            val message = SendMessage() // Create a SendMessage object with mandatory fields
            message.chatId = update.message.chatId.toString()
            message.text = update.message.text
            try {
                execute(message) // Call method to send the message
            } catch (e: TelegramApiException) {
                e.printStackTrace()
            }
        }
    }

    override fun sendMessage(chatId: Long, text: String) {
        val message = SendMessage() // Create a SendMessage object with mandatory fields
        message.chatId = chatId.toString()
        message.text = text
        try {
            execute(message) // Call method to send the message
        } catch (e: TelegramApiException) {
            e.printStackTrace()
        }
    }

    private companion object {
        private const val TORRENT_MIME_TYPE = "application/x-bittorrent"
    }
}
