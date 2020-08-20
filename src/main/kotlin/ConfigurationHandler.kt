import ch.qos.logback.classic.Level
import com.uchuhimo.konf.Config
import com.uchuhimo.konf.ConfigSpec
import java.io.File
import javax.naming.ConfigurationException

object BotConfiguration : ConfigSpec("") {
    val token by required<String>("token", "Account's Discord token")
    val logLevel by required<String>("logLevel", "Logger's logging level")
    val supportedExtensions by required<Array<String>>("supportedExtensions", "Extensions which bot should put on paste service. Be carefu")
    val pastebinToken by required<String>("pastebinToken", "Pastebin's API key")
}

fun loadConfig(main: DiscordBot): Config {
    val configFile = File("config", "config.json")
    if (!configFile.exists()) {
        logger.info("Config file doesn't exist, creating one.")
        configFile.parentFile.mkdirs()
        val stream = main.javaClass.classLoader.getResourceAsStream("config.json")
            ?: throw IllegalStateException("Corrupter jar - no config.json found")
        val buffer = ByteArray(stream.available())
        stream.read(buffer)
        stream.close()
        configFile.writeBytes(buffer)
        configFile.createNewFile()
    }
    val config = Config { addSpec(BotConfiguration) }
        .from.json.file(configFile)
    validateConfig(config)
    logger.level = Level.INFO
    return config
}

private fun validateConfig(config: Config) {
    if (config[BotConfiguration.token].isEmpty()) {
        throw ConfigurationException("Bot's token can't be empty!")
    }
    Level.toLevel(config[BotConfiguration.logLevel]) ?: throw ConfigurationException("Unrecognised log level!")
}
