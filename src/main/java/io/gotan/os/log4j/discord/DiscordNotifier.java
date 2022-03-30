package io.gotan.os.log4j.discord;

import com.fasterxml.jackson.core.JsonFactory;
import com.fasterxml.jackson.core.JsonGenerator;
import org.apache.logging.log4j.core.LoggerContext;

import java.io.IOException;
import java.io.UncheckedIOException;
import java.net.HttpURLConnection;
import java.net.MalformedURLException;
import java.net.URL;

public class DiscordNotifier {
    private URL webhook;

    private final JsonFactory factory = new JsonFactory();

    public DiscordNotifier() {
        super();
        this.initializeConfiguration();
    }

    public DiscordNotifier(final URL webhook) {
        if (webhook != null) {
            this.setWebhook(webhook);
        } else {
            this.initializeConfiguration();
        }
    }

    private void initializeConfiguration() {
        try {
            // Load webhook URL from external property
            String wh = DiscordConfiguration.getWebhookUrl();
            if (wh == null) {
                throw new DiscordConfiguration.InvalidDiscordConfiguration("No webhook configuration set");
            }
           this.setWebhook(new URL(wh));
        } catch (MalformedURLException e) {
            throw new DiscordConfiguration.InvalidDiscordConfiguration("Bad URL format");
        }
    }

    public void setWebhook(URL webhook) {
        if (!webhook.toString().startsWith("https://discord.com/api/webhooks/")) {
            throw new DiscordConfiguration.InvalidDiscordConfiguration("Bad discord webhook URL: " + webhook);
        }
        this.webhook = webhook;
    }

    public void sendMessage(String message) {
        try {
            HttpURLConnection connection = (HttpURLConnection) webhook.openConnection();
            connection.setDoOutput(true);
            connection.setRequestMethod("POST");
            connection.setRequestProperty("Content-Type", "application/json");
            connection.setRequestProperty("User-Agent", "Mozilla/5.0 (Macintosh; U; Intel Mac OS X; ja-JP-mac; rv:1.8.1.6) Gecko/20070725 Firefox/2.0.0.6");
            connection.connect();
            message = message.substring(0, Math.min(message.length(), 2000));
            try (JsonGenerator generator = factory.createGenerator(connection.getOutputStream())) {
                generator.writeStartObject();
                generator.writeStringField("content", message);
                generator.writeEndObject();
            }
            connection.getResponseCode();
        } catch (IOException e) {
            throw new UncheckedIOException(e);
        }
    }


}
