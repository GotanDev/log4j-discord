package io.gotan.os.log4j.discord;

import java.io.IOException;


public class DiscordConfiguration {
    /** Environment variable name.
     */
    public static final String ENV_VAR = "DISCORD_WEBHOOK";
    /** property name in application.properties resource file
     */
    public static final String PROPERTY_NAME = "discord.webhook";

    private static String webhookUrl = null;

    /** Get webhook url :
     * <ul>
     *     <li>either from <code>DISCORD_WEBHOOK</code> environment variable</li>
     *     <li>or from <code>discord.webhook</code> in <code>application.properties</code> resource file.</li>
     * </ul>
     * @return URL | null
     */
    public static String getWebhookUrl() throws InvalidDiscordConfiguration {
        if (webhookUrl == null) {
            webhookUrl = System.getenv(ENV_VAR);
            if (webhookUrl == null || webhookUrl.trim().isEmpty()) {
                try {
                    webhookUrl = new PropertiesReader("application.properties").getProperty(PROPERTY_NAME);
                } catch (IOException e) {
                    throw new InvalidDiscordConfiguration("Unable to load environment variable or application property for discord webhook URL");
                }
            }
        }
        return webhookUrl;
    }

    public static class InvalidDiscordConfiguration extends RuntimeException {
        public String message;
        public InvalidDiscordConfiguration(String s) {
            this.message = message;
        }

        @Override
        public String getMessage() {
            return message;
        }
    }
}
