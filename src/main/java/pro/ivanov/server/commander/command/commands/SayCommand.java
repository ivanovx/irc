package pro.ivanov.server.commander.command.commands;

import pro.ivanov.server.commander.CMessage;
import pro.ivanov.server.commander.Commander;
import pro.ivanov.server.commander.command.Command;
import pro.ivanov.server.irc.IRCServer;
import pro.ivanov.util.packet.response.Response;
import pro.ivanov.util.packet.response.ResponseType;
import pro.ivanov.util.util.StringUtil;

public class SayCommand extends Command {

    private IRCServer ircServer;

    public SayCommand(IRCServer ircServer) {
        super("say", "Sends a message to other clients.", new String[] {"broadcast", "alert"}, "<command> <message>");

        this.ircServer = ircServer;
    }

    @Override
    public void execute(String[] args) {
        if (args.length == 0) {
            Commander.getLogger().info(CMessage.WRONG_SYNTAX.getText() + getSyntax());
            return;
        }
        String message = StringUtil.cutLast(StringUtil.glue(args));
        ircServer.broadcast(new Response(ResponseType.MESSAGE, ircServer.getSettings().getName() + "@" + message));
        Commander.getLogger().info("Sent '" + message + "' to all the clients.");
    }
}