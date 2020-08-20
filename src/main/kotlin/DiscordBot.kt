import discord4j.core.DiscordClient
import org.slf4j.LoggerFactory

val logger = LoggerFactory.getLogger(DiscordBot::class.java.simpleName) as ch.qos.logback.classic.Logger

fun main() {
    DiscordBot().create()
}

class DiscordBot {
    private lateinit var client: DiscordClient
    private val config = loadConfig(this)

    fun create() {
        this.client = DiscordClient.create(config[BotConfiguration.token])
    }

}