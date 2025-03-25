package pro.ivanov.server;

import pro.ivanov.server.commander.Commander;
import pro.ivanov.server.irc.IRCServer;
import pro.ivanov.server.irc.IRCServerSettings;

import java.util.concurrent.ExecutorService;
import java.util.concurrent.Executors;

public class Main {

    public static void main(String[] args) {
        IRCServerSettings ircServerSettings = new IRCServerSettings();
        IRCServer ircServer = new IRCServer(ircServerSettings);

        Commander commander = new Commander(ircServer);
        
        try (ExecutorService service = Executors.newVirtualThreadPerTaskExecutor()) {
            service.execute(ircServer);
            service.execute(commander);
        }
    }
}
