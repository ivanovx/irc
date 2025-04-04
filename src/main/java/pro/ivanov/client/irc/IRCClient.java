package pro.ivanov.client.irc;

import pro.ivanov.client.gui.GUIManager;
import pro.ivanov.util.packet.request.Request;
import pro.ivanov.util.packet.request.RequestType;

import java.io.IOException;
import java.net.Socket;

public class IRCClient {

    private GUIManager guiManager;

    private String username;
    private ClientConnection connection;

    public IRCClient() {
        guiManager = new GUIManager(this);
        guiManager.openLogin();
    }

    public boolean connect(String username, String host, int port) {
        this.username = username;
        try {
            Socket socket = new Socket(host, port);
            connection = new ClientConnection(socket, this);
            connection.request(new Request(RequestType.FETCH_SERVER_DATA));
            connection.request(new Request(RequestType.FETCH_CHANNEL_LIST));
            return true;
        } catch (IOException ignored) {}
        return false;
    }

    public void disconnect() {
        connection.destroy();
    }

    public GUIManager getGuiManager() {
        return guiManager;
    }

    public ClientConnection getConnection() {
        return connection;
    }

    public String getUsername() {
        return username;
    }
}
