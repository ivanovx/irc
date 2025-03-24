package pro.ivanov.client.gui;

public interface Controller {
    String getTitle();
    int getWidth();
    int getHeight();

    void initComponents();
    void initListeners();

    void show();
}
