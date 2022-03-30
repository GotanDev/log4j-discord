package io.gotan.os.log4j.discord;

import static org.junit.Assert.assertNotNull;

import org.apache.logging.log4j.Level;
import org.apache.logging.log4j.LogManager;
import org.apache.logging.log4j.Marker;
import org.apache.logging.log4j.MarkerManager;
import org.apache.logging.log4j.core.Filter;
import org.apache.logging.log4j.core.config.Configurator;
import org.apache.logging.log4j.core.config.builder.api.AppenderComponentBuilder;
import org.apache.logging.log4j.core.config.builder.api.ConfigurationBuilder;
import org.apache.logging.log4j.core.config.builder.api.ConfigurationBuilderFactory;
import org.apache.logging.log4j.core.config.builder.impl.BuiltConfiguration;
import org.junit.Before;
import org.junit.Test;

@SuppressWarnings("ALL")
public class DiscordTest {

    @Before
    public void setup() {
        ConfigurationBuilder<BuiltConfiguration> builder = ConfigurationBuilderFactory.newConfigurationBuilder();
        builder.setPackages("io.gotan.os");
        //String webhookUrl = System.getProperty(GOOGLECHAT_WEBHOOK, System.getenv(GOOGLECHAT_WEBHOOK));
        String webhookUrl = null;
        try {
            webhookUrl = DiscordConfiguration.getWebhookUrl();
        } catch (DiscordConfiguration.InvalidDiscordConfiguration e) {
            System.err.println("Missing configuration for webhook url");
        }
        assertNotNull(DiscordConfiguration.ENV_VAR + " MUST NOT be null or " + DiscordConfiguration.PROPERTY_NAME + " property in application.properties file MUST NOT BE NULL", webhookUrl);
        AppenderComponentBuilder appenderComponentBuilder = builder.newAppender("discord", "Discord");
        appenderComponentBuilder.addAttribute("webhook", webhookUrl);
        appenderComponentBuilder.add(builder.newFilter(
                "MarkerFilter",
                Filter.Result.ACCEPT,
                Filter.Result.DENY).addAttribute("marker", "DISCORD"));
        appenderComponentBuilder.add(builder.newLayout("PatternLayout").
                addAttribute("pattern", "[%d{yyyy-MM-dd HH:mm:ss.SSS}], %-5p, %t, %c, %m%n"));
        builder.add(appenderComponentBuilder);
        builder.add(builder.newRootLogger(Level.INFO).add(builder.newAppenderRef("discord")));
        System.out.println(builder.toXmlConfiguration());
        Configurator.initialize(builder.build());
    }


    @Test
    public void sendInfo() throws InterruptedException {
        Marker DISCORD = MarkerManager.getMarker("DISCORD");
        LogManager.getLogger(getClass()).warn(DISCORD, "Test warning message");
        Thread.sleep(2000);
    }
    @Test
    public void sendInfoLong() throws InterruptedException {
        Marker DISCORD = MarkerManager.getMarker("DISCORD");
        StringBuilder stringBuilder = new StringBuilder();
        for(int i=0,l=2001;i<l;i++){
            stringBuilder.append("x");
        }
        LogManager.getLogger(getClass()).warn(DISCORD, stringBuilder.toString());
        Thread.sleep(2000);
    }
}