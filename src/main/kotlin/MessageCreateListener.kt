import net.dv8tion.jda.api.events.message.MessageReceivedEvent
import net.dv8tion.jda.api.hooks.ListenerAdapter
import org.jpaste.exceptions.PasteException
import java.io.InputStreamReader

class MessageCreateListener(private val discordBot: DiscordBot) : ListenerAdapter() {
    override fun onMessageReceived(event: MessageReceivedEvent) {
        if (event.message.attachments.isEmpty()) {
            return
        }
        event.message.attachments.forEach {
            it.retrieveInputStream().thenAccept { acceptedStream ->
                acceptedStream.use { inputStream ->
                    InputStreamReader(inputStream).use{ reader ->
                        if (discordBot.supportsExtension(it.fileExtension ?: return@thenAccept)) {
                            try {
                                event.message.channel.sendMessage("${event.message.author.asMention}, ${discordBot.paste(reader, it.fileName)}").queue()
                            } catch (e: PasteException) {
                                logger.error(e.stackTraceToString())
                            }
                        }
                    }
                }
            }
        }
    }
}
