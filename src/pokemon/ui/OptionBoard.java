package pokemon.ui;//David Zhao	12/9/2010
//updated by David as of 6/11

import java.awt.*;
import javax.swing.*;

public class OptionBoard extends Bumper {
  public static final int Y_HIGH = 2;
  public static final int Y_MIDDLE = 1;
  public static final int Y_LOW = 0;
  private int myLocation;

  private ImageIcon myImage;
  private String myText;
  private boolean lookForClick = false;
  private boolean showHighlight = false;
  private boolean hasPicAsImage = false;
  private static int highlightSize = 1;

  public OptionBoard(int x, int y, int width, int height, Color c, String text) {
    super(x, y, width, height, c);
    lookForClick = true;
    myText = text;
    myLocation = 0;// Y_LOW;
  }

  public OptionBoard(int x, int y, int width, int height, Color c, String text, int myLoc) {
    super(x, y, width, height, c);
    lookForClick = true;
    myText = text;
    myLocation = myLoc;
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
      buffer.fillRect(getX() - highlightSize,
                      getY() - highlightSize,
                      getXWidth() + 2 * highlightSize,
                      getYWidth() + 2 * highlightSize);
    if (hasPicAsImage)
      buffer.drawImage(myImage.getImage(), getX(), getY(), getXWidth(), getYWidth(), null);
    else {
      buffer.setColor(getColor());
      buffer.fillRect(getX(), getY(), getXWidth(), getYWidth());
    }
    buffer.setColor(Color.BLACK);

    int decrement = myLocation * getYWidth() / 2;
    if (myLocation == Y_HIGH)
      decrement -= (getYWidth() / 4 + 3);
    buffer.drawString(myText, getX(), getY() + getYWidth() - decrement - 1);

  }

  public void setText(String t) {
    myText = t;
  }

  public void showHighlight(boolean yesno) {
    showHighlight = yesno;
  }
}