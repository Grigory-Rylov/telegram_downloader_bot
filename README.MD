#files_downloader_telegram_bot
# Description
I used this project for downloading small files (20mb limit) to my remote Oragne pi NAS server.

# Usage
1) Clone current project

2) Build with `./gradlew fatJar`

3) get executable jar in `build/libs/``

3) create `settings.properties` in `telegram_downloader_bot_<VERSION>.jar`'s folder
with:
```
outputDir=output_dir_for_downloaded_files
botToken=your_telegram_bot_token
botUserName=your_bot_user_name
```

4) launch with `java -jar telegram_downloader_bot_<VERSION>.jar`

5) Send any files to your bot in Telegram.
