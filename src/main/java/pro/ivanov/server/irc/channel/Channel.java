package pro.ivanov.server.irc.channel;

import org.apache.logging.log4j.LogManager;
import org.apache.logging.log4j.Logger;
import pro.ivanov.server.irc.ServerConnection;
import pro.ivanov.util.packet.Message;
import pro.ivanov.util.packet.PacketManager;
import pro.ivanov.util.packet.response.Response;
import pro.ivanov.util.packet.response.ResponseType;

import java.util.LinkedHashMap;
import java.util.LinkedList;

public class Channel {

    private String name;
    private Logger logger = LogManager.getLogger(Channel.class);

    private LinkedHashMap<String, ServerConnection> clients = new LinkedHashMap<>();
    private LinkedList<Message> messageHistory = new LinkedList<>();

    public Channel(String name) {
        this.name = name;
    }

    public void connect(String username, ServerConnection serverConnection) {
        broadcast(new Response(ResponseType.CHANNEL_OTHER_CONNECT, username));
        clients.put(username, serverConnection);
        logger.debug("Added a new client named '" + username + "' to '" + name + "'.");
    }

    public void disconnect(String username) {
        clients.remove(username);
        broadcast(new Response(ResponseType.CHANNEL_DISCONNECT, username));
        logger.debug("Disconnected a client named '" + username + "' from '" + name + "'.");
    }

    public void broadcast(Response response) {
        for (ServerConnection serverConnection : clients.values()) {
            serverConnection.respond(response);
        }
        if (response.getResponseType() == ResponseType.CHANNEL_MESSAGE || response.getResponseType() == ResponseType.MESSAGE) {
            String[] splitted = response.getContent().split(PacketManager.DELIMITER, 2);
            if (messageHistory.size() >= 10) messageHistory.removeFirst();
            messageHistory.add(new Message(splitted[0], splitted[1]));
        }
    }

    public boolean containsClient(String username) {
        return clients.containsKey(username);
    }

    public ServerConnection getClient(String username) {
        return clients.get(username);
    }

    public String getName() {
        return name;
    }

    public LinkedHashMap<String, ServerConnection> getClients() {
        return clients;
    }

    public LinkedList<Message> getMessageHistory() {
        return messageHistory;
    }
}
