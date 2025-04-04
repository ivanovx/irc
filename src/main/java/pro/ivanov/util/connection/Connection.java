package pro.ivanov.util.connection;

import org.apache.logging.log4j.Logger;
import org.apache.logging.log4j.LogManager;
import pro.ivanov.util.packet.request.Request;
import pro.ivanov.util.packet.response.Response;

import java.io.DataInputStream;
import java.io.DataOutputStream;
import java.io.IOException;
import java.net.Socket;

public abstract class Connection {
    private Socket socket;

    private Logger logger = LogManager.getLogger(Connection.class);

    private long establishedStamp;
    private DataInputStream dataInputStream;
    private DataOutputStream dataOutputStream;
    private ReceiveThread receiveThread;

    public Connection(Socket socket) {
        this.socket = socket;

        init();
    }

    private void init() {
        logger.debug("Initialized a connection.");
        establishedStamp = System.currentTimeMillis();

        try {
            dataInputStream = new DataInputStream(socket.getInputStream());
            dataOutputStream = new DataOutputStream(socket.getOutputStream());
        } catch (IOException e) {
            e.printStackTrace();
        }

        receiveThread = new ReceiveThread(this);
        //receiveThread.start();

        Thread.ofVirtual().start(receiveThread);
    }

    public void request(Request request) {
        logger.debug("Sending a " + request.getRequestType() + " (" + request.getContent() + ") request.");
        try {
            dataOutputStream.writeUTF(request.toString());
        } catch (IOException e) {
            e.printStackTrace();
        }
    }

    public abstract void onRequest(Request request);

    public void respond(Response response) {
        logger.debug("Responding with a " + response.getResponseType() + " (" + response.getContent() + ") response.");
        try {
            dataOutputStream.writeUTF(response.toString());
        } catch (IOException e) {
            e.printStackTrace();
        }
    }

    public abstract void onResponse(Response response);

    public void destroy() {
        logger.debug("Connection has been destroyed.");
        try {
            receiveThread.cancel();
            dataInputStream.close();
            dataOutputStream.close();
            socket.close();
        } catch (IOException e) {
            e.printStackTrace();
        }
    }

    public long timeAlive() {
        return System.currentTimeMillis() - establishedStamp;
    }

    public Socket getSocket() {
        return socket;
    }

    public DataInputStream getDataInputStream() {
        return dataInputStream;
    }

    public DataOutputStream getDataOutputStream() {
        return dataOutputStream;
    }
}
