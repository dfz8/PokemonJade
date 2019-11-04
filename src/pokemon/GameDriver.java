package pokemon;//David Zhao, 3/31/2011

import javax.swing.*;

public class GameDriver {
  public static final int SCREEN_WIDTH = 275;
  public static final int SCREEN_HEIGHT = 215;

  public static void main(String[] args) {
    JFrame playWindow = new JFrame("Pokemon Jade");
    playWindow.setSize(SCREEN_WIDTH + 8, 2 * SCREEN_HEIGHT + 34);
    playWindow.setLocation(100, 100);
    playWindow.setDefaultCloseOperation(JFrame.EXIT_ON_CLOSE);
    GameContainer panel = new GameContainer();
    playWindow.setContentPane(panel);
    playWindow.setVisible(true);
    playWindow.setResizable(false);
    panel.requestFocus();
  }
}