package com.github.grishberg.telegram

import org.telegram.telegrambots.bots.DefaultAbsSender
import org.telegram.telegrambots.meta.api.methods.GetFile
import org.telegram.telegrambots.meta.api.objects.Document
import org.telegram.telegrambots.meta.exceptions.TelegramApiException
import java.io.File
import java.io.FileOutputStream
import java.io.FileReader

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
            FileReader(downloadFileWithId(fileDownloader, fileId)).use { reader ->
                println("Downloaded: ${doc.fileName}, file size: ${doc.fileSize} bytes")
                val targetFile = File(outputDir, doc.fileName)
                val outputStream = FileOutputStream(targetFile)

                readToFile(targetFile, outputStream, reader)

                messageSender.sendMessage(chatId, "Downloaded ok")
            }
        } catch (e: Exception) {
            e.printStackTrace()
        }
    }

    private fun readToFile(
        file: File,
        outputStream: FileOutputStream,
        reader: FileReader,
    ) {
        var byteRead: Int
        outputStream.use { outputStream ->
            while (reader.read().also { byteRead = it } != -1) {
                outputStream.write(byteRead)
            }
        }
        file.setReadable(true, false)
        file.setExecutable(true, false)
        file.setWritable(true, false)
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
