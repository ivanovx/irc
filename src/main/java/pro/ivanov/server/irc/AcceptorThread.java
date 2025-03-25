package pro.ivanov.server.irc;

import org.apache.logging.log4j.LogManager;
import org.apache.logging.log4j.Logger;
import pro.ivanov.util.connection.Connection;
import pro.ivanov.util.packet.request.Request;
import pro.ivanov.util.packet.request.RequestType;

import java.io.IOException;
import java.net.Socket;

public class AcceptorThread implements Runnable {

    private IRCServer ircServer;
    private Logger logger = LogManager.getLogger(AcceptorThread.class);

    private boolean running = true;

    public AcceptorThread(IRCServer ircServer) {
        this.ircServer = ircServer;
    }

    @Override
    public void run() {
        logger.info("Waiting for clients...");
        
        while (running) {
            Socket socket = null;

            try {
                socket = ircServer.getServerSocket().accept();
                logger.info("Accepted a connection from " + socket.getRemoteSocketAddress() + ".");
            } catch (IOException e) {
                e.printStackTrace();
            }
            // Connection has been successfully established, but we still don't know how to identify the user
            // let's send him a FETCH_USERNAME requests and once he responds actually add him as a client
            Connection connection = new ServerConnection(socket, ircServer);
            connection.request(new Request(RequestType.FETCH_USERNAME));
        }
    }

    public void cancel() {
        running = false;
    }
}
