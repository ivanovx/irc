package pro.ivanov.client.gui;

import pro.ivanov.client.gui.controller.LoginFrameController;
import pro.ivanov.client.gui.controller.MainFrameController;
import pro.ivanov.client.irc.IRCClient;

public class GUIManager {

    private LoginFrameController loginController;
    private MainFrameController mainController;

    public GUIManager(IRCClient ircClient) {
        loginController = new LoginFrameController(ircClient);
        mainController = new MainFrameController(ircClient);
    }

    public void openLogin() {
        loginController.show();
    }

    public void openMain() {
        mainController.show();
    }

    public LoginFrameController getLoginController() {
        return loginController;
    }

    public MainFrameController getMainController() {
        return mainController;
    }
}
