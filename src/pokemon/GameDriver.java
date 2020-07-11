package pokemon;
//David Zhao, 3/31/2011

import pokemon.util.DrawingHelper;

import javax.swing.*;

public class GameDriver {

  public static void main(String[] args) {
    JFrame playWindow = new JFrame("Pokemon Jade");
    playWindow.setSize(DrawingHelper.SCREEN_WIDTH + 8, 2 * DrawingHelper.SCREEN_HEIGHT + 34);
    playWindow.setLocation(100, 100);
    playWindow.setDefaultCloseOperation(JFrame.EXIT_ON_CLOSE);
    GameContainer panel = new GameContainer();
    playWindow.setContentPane(panel);
    playWindow.setVisible(true);
    playWindow.setResizable(false);
    panel.requestFocus();
  }
}