package pro.ivanov.server.commander.command;

import com.duplxey.javasimpleirc.server.commander.command.commands.*;
import pro.ivanov.server.commander.command.commands.*;
import pro.ivanov.server.irc.IRCServer;

public class CommandRegister {

    public CommandRegister(IRCServer ircServer) {
        new CommandsCommand();
        new ExitCommand(ircServer);
        new InfoCommand();
        new ClientsCommand(ircServer);
        new KickCommand(ircServer);
        new SayCommand(ircServer);
        new MotdCommand(ircServer);
        new ChannelCommand(ircServer);
        new CsayCommand(ircServer);
    }
}
