package io.gotan.os.log4j.discord;

import java.net.MalformedURLException;
import java.net.URL;

import org.apache.logging.log4j.core.LoggerContext;
import org.apache.logging.log4j.core.appender.AbstractManager;
import org.apache.logging.log4j.core.appender.ManagerFactory;

public class DiscordManager extends AbstractManager {

    private static final DiscordManagerFactory FACTORY = new DiscordManagerFactory();


    private DiscordNotifier notifier = null;



    private DiscordManager(final LoggerContext loggerContext, final String name, final URL webhook) {
        super(loggerContext, name);
        notifier = new DiscordNotifier(webhook);
    }

    public static DiscordManager getManager(final String name, final LoggerContext context, final URL webhook) {

        ChatConfiguration config = new ChatConfiguration();
        config.context = context;
        if (webhook == null) {
            try {
                // Load webhook URL from external property
                config.webhook = new URL(DiscordConfiguration.getWebhookUrl());
            } catch (MalformedURLException e) {
                throw new DiscordConfiguration.InvalidDiscordConfiguration("No webhook configuration set");
            }
        } else {
            config.webhook = webhook;
        }
        return getManager(name, FACTORY, config);
    }

    /** Get Discord Manager with default webhook URL.<br />
     * URL provided either by DISCORD_WEBHOOK env var, or discord.webhook application.properties file. */
    public static DiscordManager getManager(final String name, final LoggerContext context) throws DiscordConfiguration.InvalidDiscordConfiguration {
        return getManager(name, FACTORY, null);
    }


    public void sendMessage(String message){
        this.notifier.sendMessage(message);
    }

    private static class ChatConfiguration {
        private LoggerContext context;
        private URL webhook;
    }

    private static class DiscordManagerFactory implements ManagerFactory<DiscordManager, ChatConfiguration> {

        @Override
        public DiscordManager createManager(final String name, final ChatConfiguration data) {
            return new DiscordManager(data.context, name, data.webhook);
        }
    }
}