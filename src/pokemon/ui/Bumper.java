package pokemon.ui;

import java.awt.*;

public class Bumper {

  private int myX;
  private int myY;
  private int myWidth;
  private int myHeight;
  private Color myColor;

  public Bumper(int x, int y, int xWidth, int yWidth, Color c) {
    myX = x;
    myY = y;
    myWidth = xWidth;
    myHeight = yWidth;
    myColor = c;
  }

  // accessor methods
  public int getX() {
    return myX;
  }

  public int getY() {
    return myY;
  }

  public int getWidth() {
    return myWidth;
  }

  public int getHeight() {
    return myHeight;
  }

  public Color getColor() {
    return myColor;
  }

  // modifier methods
  public void setX(int x) {
    myX = x;
  }

  public void setY(int y) {
    myY = y;
  }

  public void setWidth(int x) {
    myWidth = x;
  }

  public void setHeight(int y) {
    myHeight = y;
  }

  public void setColor(Color c) {
    myColor = c;
  }

  // draws a rectangular bumper on the buffer
  public void draw(Graphics buffer) {
    buffer.setColor(getColor());
    buffer.fillRect(getX(), getY(), getWidth(), getHeight());
  }

}
