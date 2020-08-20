import net.dv8tion.jda.api.JDA
import net.dv8tion.jda.api.JDABuilder
import net.dv8tion.jda.api.requests.GatewayIntent
import net.dv8tion.jda.api.utils.cache.CacheFlag
import org.jpaste.pastebin.Pastebin
import org.slf4j.LoggerFactory
import java.io.InputStreamReader
import java.net.URL

val logger = LoggerFactory.getLogger(DiscordBot::class.java.simpleName) as ch.qos.logback.classic.Logger

fun main() {
    DiscordBot().create()
}

class DiscordBot {
    private lateinit var client: JDA
    private val config = loadConfig(this)

    fun create(): DiscordBot {
        this.client = JDABuilder.create(config[BotConfiguration.token], GatewayIntent.GUILD_MESSAGES)
            .disableCache(
                CacheFlag.ACTIVITY,
                CacheFlag.CLIENT_STATUS,
                CacheFlag.EMOTE,
                CacheFlag.MEMBER_OVERRIDES,
                CacheFlag.VOICE_STATE
            )
            .build()
        this.client.addEventListener(MessageCreateListener(this))
        return this
    }

    fun supportsExtension(extension: String): Boolean {
        return config[BotConfiguration.supportedExtensions].contains(extension)
    }

    fun paste(isr: InputStreamReader, title: String): URL {
        return Pastebin.pastePaste(config[BotConfiguration.pastebinToken], isr.readText(), title)
    }
}
