package com.github.grishberg.telegram

import java.io.File
import java.io.FileInputStream
import java.util.Properties

class Config(configFileName: String) {
    val outputDir: String
    val botToken: String
    val botUserName: String

    init {
        val file = File(configFileName)

        val prop = Properties()

        FileInputStream(file).use {
            prop.load(it)
            outputDir = prop.getProperty("outputDir")
            botToken = prop.getProperty("botToken")
            botUserName = prop.getProperty("botUserName")
        }
    }
}
