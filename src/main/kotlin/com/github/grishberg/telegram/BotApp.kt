package com.github.grishberg.telegram

import org.telegram.telegrambots.meta.TelegramBotsApi
import org.telegram.telegrambots.updatesreceivers.DefaultBotSession
import java.io.File


class BotApp {
    private val telegramBotsApi = TelegramBotsApi(DefaultBotSession::class.java)
    private val config = Config("settings.properties")

    fun run(outputDir: String?) {
        telegramBotsApi.registerBot(
            TorrentsFilesHandler(
                botToken = config.botToken,
                botUserName = config.botUserName,
                torrentFileProcessor = DocumentAttachmentsDownloader(
                    outputDir = File(
                        outputDir ?: config.outputDir
                    )
                )
            )
        )
    }
}
