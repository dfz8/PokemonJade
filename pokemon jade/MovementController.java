import javax.swing.*;

public class MovementController {
  enum Direction {
    DOWN,
    LEFT,
    UP,
    RIGHT
  }

  private boolean upPressed;
  private boolean downPressed;
  private boolean leftPressed;
  private boolean rightPressed;

  private int mSpriteIndex;
  private Direction mLastDirection;

  public MovementController() {
    upPressed = downPressed = leftPressed = rightPressed = false;
    mLastDirection = Direction.DOWN;
  }

  public boolean isUpPressed() {
    return upPressed;
  }

  public void setUpPressed(boolean upPressed) {
    this.upPressed = upPressed;
    if (upPressed) {
      mLastDirection = Direction.UP;
    }
  }

  public boolean isDownPressed() {
    return downPressed;
  }

  public void setDownPressed(boolean downPressed) {
    this.downPressed = downPressed;
    if (downPressed) {
      mLastDirection = Direction.DOWN;
    }
  }

  public boolean isLeftPressed() {
    return leftPressed;
  }

  public void setLeftPressed(boolean leftPressed) {
    this.leftPressed = leftPressed;
    if (leftPressed) {
      mLastDirection = Direction.LEFT;
    }
  }

  public boolean isRightPressed() {
    return rightPressed;
  }

  public void setRightPressed(boolean rightPressed) {
    this.rightPressed = rightPressed;
    if (rightPressed) {
      mLastDirection = Direction.RIGHT;
    }
  }

  public ImageIcon getStationarySprite() {
    switch (mLastDirection) {
      case LEFT:
        return ImageLibrary.faceLeft;
      case UP:
        return ImageLibrary.faceUp;
      case RIGHT:
        return ImageLibrary.faceRight;
      case DOWN:
      default:
        return ImageLibrary.faceDown;
    }
  }

  public ImageIcon getWalkingSprite() {
    mSpriteIndex++;
    ImageIcon[] sprites = ImageLibrary.movementSprites[directionToInd(mLastDirection)];

    if (mSpriteIndex == sprites.length) {
      mSpriteIndex = 0;
    }
    return sprites[mSpriteIndex];
  }

  private int directionToInd(Direction d) {
    switch (d) {
      case UP:
        return 0;
      case LEFT:
        return 1;
      case RIGHT:
        return 2;
      case DOWN:
      default:
        return 3;
    }
  }
}
