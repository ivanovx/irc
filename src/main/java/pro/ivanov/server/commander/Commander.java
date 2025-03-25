package pro.ivanov.server.commander;

import org.apache.logging.log4j.LogManager;
import org.apache.logging.log4j.Logger;

import pro.ivanov.server.commander.command.Command;
import pro.ivanov.server.commander.command.CommandManager;
import pro.ivanov.server.commander.command.CommandRegister;
import pro.ivanov.server.irc.IRCServer;

import java.util.Arrays;
import java.util.Scanner;

public class Commander implements Runnable {

    private boolean running = true;
    private static Logger logger = LogManager.getLogger(Commander.class);

    public Commander(IRCServer ircServer) {
        // Registers all the commands.
        new CommandRegister(ircServer);
    }
    @Override
    public void run() {
        Scanner scanner = new Scanner(System.in);
        while (running) {
            String input = scanner.nextLine();

            if (input != null) {
                input = input.toLowerCase();

                String cmd;
                String[] arguments = {};
                if (input.contains(" ")) {
                    // isValid arguments
                    arguments = input.split(" ");
                    cmd = arguments[0];
                    arguments = Arrays.copyOfRange(arguments, 1, arguments.length);
                } else {
                    cmd = input;
                }
                Command command = CommandManager.getCommand(cmd);
                if (command == null) {
                    Commander.getLogger().info(CMessage.UNKNOWN_COMMAND.getText());
                } else {
                    command.execute(arguments);
                }
            }
        }
    }

    public void stop() {
        running = false;
    }

    public static Logger getLogger() {
        return logger;
    }
}
