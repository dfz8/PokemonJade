package pokemon.ui;

import java.awt.*;

public class OptionBoard {

  private static final Color HIGHLIGHT_COLOR = Color.RED;
  private static final int HIGHLIGHT_SIZE = 1;

  private int mX;
  private int mY;
  private int mWidth;
  private int mHeight;
  private Color mBackgroundColor;

  private TextPlacement mTextPlacement;
  private String mText;
  private boolean mIsEnabled;
  private boolean mShouldShowHighlight;
  private OnClickListener mOnClickListener;

  public OptionBoard(int x, int y, int width, int height, Color c, String text) {
    this(x, y, width, height, c, text, TextPlacement.LOW);
  }

  public OptionBoard(
      int x,
      int y,
      int width,
      int height,
      Color c,
      String text,
      TextPlacement textPlacement) {
    mX = x;
    mY = y;
    mWidth = width;
    mHeight = height;
    mBackgroundColor = c;
    mIsEnabled = true;
    mText = text;
    mTextPlacement = textPlacement;
  }

  public boolean isHighlighted() {
    return mShouldShowHighlight;
  }

  public String getText() {
    return mText;
  }

  public void draw(Graphics buffer) {
    buffer.setColor(HIGHLIGHT_COLOR);
    if (mShouldShowHighlight && mIsEnabled) {
      buffer.fillRect(
          mX - HIGHLIGHT_SIZE,
          mY - HIGHLIGHT_SIZE,
          mWidth + 2 * HIGHLIGHT_SIZE,
          mHeight + 2 * HIGHLIGHT_SIZE);
    }

    buffer.setColor(mBackgroundColor);
    buffer.fillRect(mX, mY, mWidth, mHeight);

    buffer.setColor(Color.BLACK);
    int decrement = 0; // myTextPlacement * mHeight / 2;
    if (mTextPlacement == TextPlacement.HIGH) {
      decrement -= (mHeight / 4 + 3);
    }
    buffer.drawString(mText, mX, mY + mHeight - decrement - 1);
  }

  public void setText(String t) {
    mText = t;
  }

  public void shouldShowHighlight(boolean shouldShowHighlight) {
    mShouldShowHighlight = shouldShowHighlight;
  }

  public void setX(int x) {
    mX = x;
  }

  public void setY(int y) {
    mY = y;
  }

  public void setBackgroundColor(Color c) {
    mBackgroundColor = c;
  }

  public void setOnClickListener(OnClickListener listener) {
    mOnClickListener = listener;
  }

  public void onClick() {
    mOnClickListener.onClick();
  }

  public int getX() {
    return mX;
  }

  public int getY() {
    return mY;
  }

  public int getWidth() {
    return mWidth;
  }

  public int getHeight() {
    return mHeight;
  }

  public boolean hasOnClickListener() {
    return mOnClickListener != null;
  }
}