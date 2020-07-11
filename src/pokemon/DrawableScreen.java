package pokemon;

import pokemon.util.DrawingHelper;

import javax.swing.*;
import java.awt.*;
import java.awt.event.ActionListener;
import java.awt.image.BufferedImage;

/**
 * Classes extending this will have render-able content to be drawn
 */
public abstract class DrawableScreen extends JPanel {
  private Graphics mBuffer;
  private BufferedImage mImage;
  private Timer mTimer;
  private GameState mGameState;

  public DrawableScreen() {
    mImage = new BufferedImage(
        DrawingHelper.SCREEN_WIDTH,
        DrawingHelper.SCREEN_HEIGHT,
        BufferedImage.TYPE_INT_RGB);
    mBuffer = mImage.getGraphics();
  }

  public void setAndStartActionListener(int refreshDelayMS, ActionListener actionListener) {
    mTimer = new Timer(refreshDelayMS, actionListener);
    mTimer.start();
  }

  public void paintComponent(Graphics g) {
    g.drawImage(mImage, 0, 0, DrawingHelper.SCREEN_WIDTH, DrawingHelper.SCREEN_HEIGHT, null);
  }

  public Graphics getImageBuffer() {
    return mBuffer;
  }

  public void toState(GameState state) {
    if (state != mGameState) {
      handleStateChange(mGameState, state);
      mGameState = state;
    } else {
      AlertHelper.debug("Going to same state: " + state);
    }
  }

  public GameState getCurrentState() {
    return mGameState;
  }

  public abstract void handleStateChange(GameState currentState, GameState newState);
}
