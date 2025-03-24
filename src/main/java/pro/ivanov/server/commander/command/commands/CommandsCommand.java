package pro.ivanov.server.commander.command.commands;

import pro.ivanov.server.commander.Commander;
import pro.ivanov.server.commander.command.Command;
import pro.ivanov.server.commander.command.CommandManager;

public class CommandsCommand extends Command {

    public CommandsCommand() {
        super("commands", "Displays the command list.", new String[] {"help"});
    }

    @Override
    public void execute(String[] args) {
        Commander.getLogger().info("Registered commands:");
        Commander.getLogger().info("+--------------+---------------------------------------------+");
        Commander.getLogger().info("| Command      | Description                                 |");
        Commander.getLogger().info("+--------------+---------------------------------------------+");
        String rowFormat = "| %-12s | %-43s |";
        for (Command command : CommandManager.getCommands()) {
            Commander.getLogger().info(String.format(rowFormat, command.getCommand(), command.getDescription()));
        }
        Commander.getLogger().info("+--------------+---------------------------------------------+");
    }
}
