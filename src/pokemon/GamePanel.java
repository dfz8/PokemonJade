package pokemon;

import javax.swing.*;
import java.awt.*;
import java.awt.event.ActionListener;
import java.awt.image.BufferedImage;

public abstract class GamePanel extends JPanel {
  private Graphics mBuffer;
  private BufferedImage mImage;
  private Timer mTimer;
  private GameState mGameState;

  public GamePanel() {
    mImage = new BufferedImage(
        GameDriver.SCREEN_WIDTH,
        GameDriver.SCREEN_HEIGHT,
        BufferedImage.TYPE_INT_RGB);
    mBuffer = mImage.getGraphics();
  }

  public void setAndStartActionListener(int refreshDelayMS, ActionListener actionListener) {
    mTimer = new Timer(refreshDelayMS, actionListener);
    mTimer.start();
  }

  public void paintComponent(Graphics g) {
    g.drawImage(mImage, 0, 0, GameDriver.SCREEN_WIDTH, GameDriver.SCREEN_HEIGHT, null);
  }

  public Graphics getImageBuffer() {
    return mBuffer;
  }

  public void toState(GameState state) {
    if (state != mGameState) {
      mGameState = state;
      handleStateChange(state);
    } else {
      AlertHelper.debug("Going to same state: " + state);
    }
  }

  public GameState getCurrentState() {
    return mGameState;
  }

  public abstract void handleStateChange(GameState newState);
}
