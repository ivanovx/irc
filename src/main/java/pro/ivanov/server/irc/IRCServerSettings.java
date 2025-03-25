package pro.ivanov.server.irc;

import java.util.Map;

public class IRCServerSettings {
    private final Map<String, String> config;

    public IRCServerSettings() {
        this.config = Map.ofEntries(
                Map.entry("name", "server_name"),
                Map.entry("description", "<server_description>"),
                Map.entry("motd", "<server_motd>"),
                Map.entry("port", "5422")
        );
    }

    public String getName() {
        return config.get("name");
    }

    public String getDescription() {
        return config.get("description");
    }

    public String getMOTD() {
        return config.get("motd");
    }

    public void setMOTD(String motd) {
        config.remove("motd");
        config.put("motd", motd);
    }

    public int getPort() {
        return Integer.parseInt(config.get("port"));
    }
}
