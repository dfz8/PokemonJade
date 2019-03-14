package pokemon;//David Zhao, 3/31/2011

import javax.swing.JFrame;

public class GameDriver {
  public static void main(String[] args) {
    JFrame playWindow = new JFrame("FINAL PROJECT: pokemon.entities.Pokemon");
    playWindow.setSize(275 + 8, 430 + 34);
    playWindow.setLocation(100, 100);
    playWindow.setDefaultCloseOperation(JFrame.EXIT_ON_CLOSE);
    PlayPanel panel = new PlayPanel();
    playWindow.setContentPane(panel);
    playWindow.setVisible(true);
    playWindow.setResizable(false);
    panel.requestFocus();
  }
}