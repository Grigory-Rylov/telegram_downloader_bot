import com.github.grishberg.telegram.BotApp

fun main(args: Array<String>) {
    println("Telegram downloader started")

    val outputDirFromArgs = args.firstOrNull()
    BotApp().run(outputDirFromArgs)
}
