package pro.ivanov.server;

import pro.ivanov.server.commander.Commander;
import pro.ivanov.server.irc.IRCServer;
import pro.ivanov.server.irc.IRCServerSettings;

public class Main {

    public static void main(String[] args) {
        IRCServerSettings ircServerSettings = new IRCServerSettings();
        IRCServer ircServer = new IRCServer(ircServerSettings);

        Commander commander = new Commander(ircServer);
        commander.start();
    }
}
