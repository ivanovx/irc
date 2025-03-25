package pro.ivanov.server.irc;
import org.apache.logging.log4j.Logger;
import org.apache.logging.log4j.LogManager;
//import pro.ivanov.server.Main;
import pro.ivanov.server.irc.channel.Channel;
import pro.ivanov.server.irc.channel.ChannelManager;
//import pro.ivanov.util.file.FileUtil;
import pro.ivanov.util.packet.response.Response;

import java.io.IOException;
import java.net.ServerSocket;
import java.util.ArrayList;

public class IRCServer implements Runnable {

    private IRCServerSettings settings;
    private Logger logger = LogManager.getLogger(IRCServer.class);

    private ServerSocket serverSocket;
    private AcceptorThread acceptorThread;
    private ChannelManager channelManager;

    public IRCServer(IRCServerSettings settings) {
        this.settings = settings;
    }

    @Override
    public void run() {
        //logger.info(FileUtil.getResourceContent(Main.class.getClassLoader(), "welcome.txt"));
        try {
            serverSocket = new ServerSocket(settings.getPort());
            logger.info("Listening on port: " + settings.getPort());
        } catch (IOException e) {
            e.printStackTrace();
        }

        acceptorThread = new AcceptorThread(this);
        //acceptorThread.start();

        Thread.ofVirtual().start(acceptorThread);

        channelManager = new ChannelManager(this);
    }

    public void broadcast(Response response) {
        for (Channel channel : channelManager.getChannels()) {
            channel.broadcast(response);
        }
    }

    public void close() {
        for (ServerConnection client : getClients()) {
            client.destroy();
        }

        try {
            serverSocket.close();
        } catch (IOException e) {
            e.printStackTrace();
        }

        acceptorThread.cancel();
    }

    public ArrayList<ServerConnection> getClients() {
        ArrayList<ServerConnection> clients = new ArrayList<>();

        for (Channel channel : channelManager.getChannels()) {
            clients.addAll(channel.getClients().values());
        }

        return clients;
    }

    public ServerConnection getClient(String username) {
        for (ServerConnection serverConnection : getClients()) {
            if (serverConnection.getUsername() != null && serverConnection.getUsername().equalsIgnoreCase(username)) {
                return serverConnection;
            }
        }

        return null;
    }

    public boolean containsClient(String username) {
        for (ServerConnection serverConnection : getClients()) {
            if (serverConnection.getUsername() != null && serverConnection.getUsername().equalsIgnoreCase(username)) {
                return true;
            }
        }

        return false;
    }

    public IRCServerSettings getSettings() {
        return settings;
    }

    public ServerSocket getServerSocket() {
        return serverSocket;
    }

    public ChannelManager getChannelManager() {
        return channelManager;
    }
}
