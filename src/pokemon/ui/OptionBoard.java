package pokemon.ui;

import pokemon.DrawingHelper;

import javax.swing.*;
import java.awt.*;

public class OptionBoard extends Bumper {

  public enum TextPlacement {
    HIGH,
    MIDDLE,
    LOW
  }

  private static final int HIGHLIGHT_SIZE = 1;

  private TextPlacement myTextPlacement;

  private ImageIcon myImage;
  private String myText;
  private boolean lookForClick;
  private boolean showHighlight;
  private boolean hasPicAsImage;

  public OptionBoard(int x, int y, int width, int height, Color c, String text) {
    super(x, y, width, height, c);
    lookForClick = true;
    myText = text;
    myTextPlacement = TextPlacement.LOW;
  }

  public OptionBoard(
      int x,
      int y,
      int width,
      int height,
      Color c,
      String text,
      TextPlacement textPlacement) {
    super(x, y, width, height, c);
    lookForClick = true;
    myText = text;
    myTextPlacement = textPlacement;
  }

  public OptionBoard(int x, int y, int width, int height, String picName, boolean click) {
    super(x, y, width, height, Color.WHITE);
    myImage = new ImageIcon(picName);
    lookForClick = click;
    hasPicAsImage = true;
  }

  public boolean isClickable() {
    return lookForClick;
  }

  public boolean isHighlighted() {
    return showHighlight;
  }

  public String getText() {
    return myText;
  }

  public void setClickable(boolean b) {
    lookForClick = b;
  }

  public void draw(Graphics buffer) {
    buffer.setColor(Color.RED);
    if (showHighlight && lookForClick) //shows the highlight
    {
      buffer.fillRect(
          getX() - HIGHLIGHT_SIZE,
          getY() - HIGHLIGHT_SIZE,
          getWidth() + 2 * HIGHLIGHT_SIZE,
          getHeight() + 2 * HIGHLIGHT_SIZE);
    }
    if (hasPicAsImage) {
      DrawingHelper.drawImage(buffer, myImage, getX(), getY(), getWidth(), getHeight());
    } else {
      super.draw(buffer);
    }
    buffer.setColor(Color.BLACK);
    int decrement = 0; // myTextPlacement * getHeight() / 2;
    if (myTextPlacement == TextPlacement.HIGH) {
      decrement -= (getHeight() / 4 + 3);
    }
    buffer.drawString(myText, getX(), getY() + getHeight() - decrement - 1);
  }

  public void setText(String t) {
    myText = t;
  }

  public void showHighlight(boolean yesno) {
    showHighlight = yesno;
  }
}