package pro.ivanov.server.irc.channel;

import pro.ivanov.server.irc.IRCServer;
import pro.ivanov.server.irc.ServerConnection;
import pro.ivanov.util.packet.response.Response;
import pro.ivanov.util.packet.response.ResponseType;

import java.util.*;

public class ChannelManager {

    private IRCServer ircServer;

    private Map<String, String> config;

    //private YamlConfiguration config;
    private List<String> registeredChannels = new ArrayList<>();
    private LinkedHashMap<String, Channel> channels = new LinkedHashMap<>();
    private Channel defaultChannel;

    public ChannelManager(IRCServer ircServer) {
        this.ircServer = ircServer;

        this.config = Map.ofEntries(
                Map.entry("channels", "general, other")
        );

        init();
        load();
    }

    private void init() {
        //ConfigFile file = new ConfigFile("channels.yml");
       // config = file.getConfig();
        //if (file.isNew()) {
         //   config.setStringList("channels", new ArrayList<>(Collections.singletonList("general")));
        //}
    }

    private void load() {
        registeredChannels = Arrays.stream(config.get("channels").trim().split(",")).toList();
        // In case administrator accidentally removes all the channels, we'll add a fallback
        if (registeredChannels.size() == 0) {
            registeredChannels.add("general");
        }

        for (String channelName : registeredChannels) {
            channels.put(channelName, new Channel(channelName));
        }

        defaultChannel = channels.get(registeredChannels.get(0));
    }

    public void addChannel(String channelName) {
        channels.put(channelName, new Channel(channelName));
        registeredChannels.add(channelName);
        //config.setStringList("channels", registeredChannels);
        ircServer.broadcast(new Response(ResponseType.CHANNEL_CREATE, channelName));
    }

    public void removeChannel(String channelName) {
        Channel channel = getChannel(channelName);
        // Reconnect all the clients
        for (ServerConnection client : channel.getClients().values()) {
            channel.disconnect(client.getUsername());
            defaultChannel.connect(client.getUsername(), client);
        }
        channels.remove(channelName);
        registeredChannels.remove(channelName);
        //config.setStringList("channels", registeredChannels);
        ircServer.broadcast(new Response(ResponseType.CHANNEL_DELETE, channelName));
    }

    public void moveChannel(String channelName, int index) {
        registeredChannels.remove(channelName);
        registeredChannels.add(index, channelName);
        //config.setStringList("channels", registeredChannels);
    }

    public boolean existsChannel(String channelName) {
        return channels.containsKey(channelName);
    }

    public Channel getChannel(String channelName) {
        return channels.get(channelName);
    }

    public Collection<Channel> getChannels() {
        return channels.values();
    }

    public Channel getDefaultChannel() {
        return defaultChannel;
    }
}
