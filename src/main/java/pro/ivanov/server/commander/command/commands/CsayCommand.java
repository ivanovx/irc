package pro.ivanov.server.commander.command.commands;

import pro.ivanov.server.commander.CMessage;
import pro.ivanov.server.commander.Commander;
import pro.ivanov.server.commander.command.Command;
import pro.ivanov.server.irc.IRCServer;
import pro.ivanov.server.irc.channel.Channel;
import pro.ivanov.util.packet.response.Response;
import pro.ivanov.util.packet.response.ResponseType;
import pro.ivanov.util.util.StringUtil;

public class CsayCommand extends Command {

    private IRCServer ircServer;

    public CsayCommand(IRCServer ircServer) {
        super("channelsay", "Sends a message to all channel's clients.", new String[] {"csay"}, "<command> <channel> <message>");

        this.ircServer = ircServer;
    }

    @Override
    public void execute(String[] args) {
        if (args.length < 2) {
            Commander.getLogger().info(CMessage.WRONG_SYNTAX.getText() + getSyntax());
            return;
        }
        String channelName = args[0];
        if (!ircServer.getChannelManager().existsChannel(channelName)) {
            Commander.getLogger().info(CMessage.CHANNEL_DOESNT_EXIST.getText());
            return;
        }
        Channel channel = ircServer.getChannelManager().getChannel(channelName);
        String message = StringUtil.cutLast(StringUtil.glue(args, 1));
        channel.broadcast(new Response(ResponseType.CHANNEL_MESSAGE, ircServer.getSettings().getName() + "@" + message));
        Commander.getLogger().info("Sent '" + message + "' to all the clients in '" + channelName + "'.");
    }
}