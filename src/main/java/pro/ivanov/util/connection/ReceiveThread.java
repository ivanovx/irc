package pro.ivanov.util.connection;

import pro.ivanov.util.packet.PacketManager;
import pro.ivanov.util.packet.request.Request;
import pro.ivanov.util.packet.request.RequestType;
import pro.ivanov.util.packet.response.Response;
import pro.ivanov.util.packet.response.ResponseType;

import java.io.IOException;

public class ReceiveThread implements Runnable {

    private Connection connection;
    private boolean running = true;

    public ReceiveThread(Connection connection) {
        this.connection = connection;
    }

    @Override
    public void run() {
        while (running) {
            try {
                String data = connection.getDataInputStream().readUTF();
                String[] splitted = data.split(PacketManager.DELIMITER, 3);

                if (splitted[0].equalsIgnoreCase(PacketManager.RESPONSE_IDENTIFIER)) {
                    ResponseType type = ResponseType.byId(splitted[1]);
                    // If that response type doesn't exist, we can safely ignore it.
                    if (type != null) {
                        connection.onResponse(new Response(type, splitted[2]));
                    }
                } else {
                    RequestType type = RequestType.byId(splitted[1]);
                    // If that request type doesn't exist, we can safely ignore it.
                    if (type != null) {
                        connection.onRequest(new Request(type, splitted[2]));
                    }
                }
            } catch (IOException e) {
                if (connection instanceof Droppable) {
                    ((Droppable) connection).onDrop(e);
                } else {
                    e.printStackTrace();
                }
            }
        }
    }

    public void cancel() {
        running = false;
    }
}
