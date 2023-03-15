package com.github.grishberg.telegram

import org.telegram.telegrambots.bots.DefaultAbsSender
import org.telegram.telegrambots.meta.api.methods.GetFile
import org.telegram.telegrambots.meta.api.objects.Document
import org.telegram.telegrambots.meta.exceptions.TelegramApiException
import java.io.File
import java.nio.file.Files
import java.nio.file.StandardCopyOption

/**
 * Process document attachment.
 */
class DocumentAttachmentsDownloader(
    private val outputDir: File,
) {

    init {
        if (!outputDir.exists()) {
            outputDir.mkdirs()
        }
    }

    fun downloadDocumentFile(
        chatId: Long,
        doc: Document,
        messageSender: MessageSender,
        fileDownloader: DefaultAbsSender
    ) {

        val fileId: String = doc.fileId
        try {
            val downloadFileWithId = downloadFileWithId(fileDownloader, fileId)
            println("Downloaded: ${doc.fileName}, file size: ${doc.fileSize} bytes")

            val targetFile = File(outputDir, doc.fileName)
            Files.copy(
                downloadFileWithId.toPath(), targetFile.toPath(),
                StandardCopyOption.REPLACE_EXISTING
            )

            targetFile.setReadable(true, false)
            targetFile.setWritable(true, false)

            messageSender.sendMessage(chatId, "Downloaded ok")

        } catch (e: Exception) {
            e.printStackTrace()
        }
    }

    @Throws(TelegramApiException::class)
    private fun downloadFileWithId(sender: DefaultAbsSender, fileId: String): File {
        return sender.downloadFile(
            sender.execute(
                GetFile.builder().fileId(fileId).build()
            )
        )
    }
}
