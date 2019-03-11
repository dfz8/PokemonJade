import java.awt.*;

public class Bumper {

  private int myX;
  private int myY;
  private int myXWidth;
  private int myYWidth;
  private Color myColor;

  public Bumper() {
    myX = 50;
    myY = 50;
    myYWidth = 100;
    myXWidth = 50;
    myColor = Color.BLACK;
  }

  public Bumper(int x, int y, int xWidth, int yWidth, Color c) {
    myX = x;
    myY = y;
    myXWidth = xWidth;
    myYWidth = yWidth;
    myColor = c;
  }

  // accessor methods
  public int getX() {
    return myX;
  }

  public int getY() {
    return myY;
  }

  public int getXWidth() {
    return myXWidth;
  }

  public int getYWidth() {
    return myYWidth;
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

  public void setXWidth(int x) {
    myXWidth = x;
  }

  public void setYWidth(int y) {
    myYWidth = y;
  }

  public void setColor(Color c) {
    myColor = c;
  }

  // draws a rectangular bumper on the buffer
  public void draw(Graphics myBuffer) {
    myBuffer.setColor(getColor());
    myBuffer.fillRect(getX(), getY(), getXWidth(), getYWidth());
  }

}
