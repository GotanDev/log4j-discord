# Discord Appender for Log4j (and simple Discord message from Java)

This is a simple webhook-based Discord appender for Log4j 2.x.  
Can also be used to send manually simple message to Discord. 


## Simple message example

```java
// With global configuration
new DiscordNotifier().sendMessage("Send simple message");
// With custom oneshot URL
new DiscordNotifier("https://discord.com/api/webhooks/XXXXXXX").sendMessage("Send simple message");
```

## Get Started


### Maven
Include this project directly from Gotan Maven repository in your `pom.xml`
```XML
<dependency>
	<groupId>io.gotan.os</groupId>
    <artifactId>log4j-discord</artifactId>
    <version>1.0-SNAPSHOT</version>
</dependency>
```


#### Gotan maven repository 

```XML
<repositories>
	<repository>
		<id>gotan</id>
		<name>Gotan multi-proxies and OpenSource Contributions</name>
		<url>https://repository.dev.gotan.io/repository/maven-public/</url>
		<releases><enabled>true</enabled></releases>
		<snapshots><enabled>true</enabled></snapshots>
	</repository>
</repositories>
```

### Webhook URL Configuration 

You can set [discord webhook URL](https://discord.com/developers/docs/resources/webhook) in 3 ways

* `DISCORD_WEBHOOK` env var
* `discord.webhook` in `application.properties` file
* directly with custom URL set in constructor / factory

## Log4j Appender  
To use this appender in your code, first get a webhook URL from Discord.
Then, add this to your project's dependencies:


To use this plugin, add the following to your `log4j.xml`) configuration (or translate to your preferred format, i.e. `slf4j.xml` or `logback.xml`) :

```xml
<Configuration packages="io.gotan.os">
    <Appenders>
        <Discord name="discord" [webhook="https://discord..."]>
            <MarkerFilter onMatch="ACCEPT" onMismatch="DENY" marker="DISCORD"/>
            <PatternLayout pattern="%m"/>
        </Discord>
    </Appenders>
    <Loggers>
        <Root level="info">
            <AppenderRef ref="discord"/>
        </Root>
    </Loggers>
</Configuration>
```

Using the MarkerFilter configuration like this allows you to send a log message to Discord from any class.  
By using the `DISCORD` marker, this allows the configuration to filter all messages with that marker and make sure to send them to the DiscordAppender.
As long as you don't override the additivity property of the relevant loggers, then these log messages will also be logged to the console or file or whatever you have configured normally.
Read more about markers [here](https://logging.apache.org/log4j/2.x/manual/markers.html).



### Configuration

The following are all the configuration attributes or elements supported:

* name: name of the appender, used when referencing via `<AppenderRef ref="name"/>`.
* filter: an optional filter to use; a MarkerFilter is recommended here as described above.
* ignoreExceptions: whether or not to let exceptions from the appender to be swallowed or propagated.
  This is true by default and should usually only be set to false when using a [FailoverAppender](https://logging.apache.org/log4j/2.x/manual/appenders.html#FailoverAppender) or similar.
* layout: a string layout to use to format log events into a Discord message.
  Only `StringLayout` layouts are supported since sending arbitrary binary data to Discord doesn't make sense.
  If no layout is specified, the default pattern layout is used.
* webhook: URL to the Discord webhook to send messages to.

## Test
set `DISCORD_WEBHOOK environment` variable  
(or use default #test channel in our discord, set in `application.properties`) 

and run **`test`**.
```bash
DISCORD_WEBHOOK="https://...." mvn test
```

## Contributing

Submit pull requests.

## License
Apache License, Version 2.0


## Thanks
[log4j-slack](https://github.com/jvz/log4j-slack)  
Original [log4j-discord](https://github.com/modeverv/log4j-discord) - built with gradle