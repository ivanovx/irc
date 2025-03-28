package pro.ivanov.util.packet.request;

import pro.ivanov.util.packet.PacketManager;

public class Request {

    private RequestType requestType;
    private String content;

    public Request(RequestType requestType, String content) {
        this.requestType = requestType;
        this.content = content;
    }

    public Request(RequestType requestType) {
        this(requestType, null);
    }

    public RequestType getRequestType() {
        return requestType;
    }

    public String getContent() {
        return content;
    }

    @Override
    public String toString() {
        return PacketManager.REQUEST_IDENTIFIER +  PacketManager.DELIMITER + requestType.getId() + PacketManager.DELIMITER + content;
    }
}
