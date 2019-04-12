package pokemon.entities;

import pokemon.Attack;
import pokemon.DrawingHelper;
import pokemon.SpriteHelper;
import pokemon.controllers.BattleController;
import pokemon.ui.Styles;

import javax.swing.*;
import java.awt.*;

public class Trainer {

  public static final int INFO_BOX_WIDTH = 135;
  public static final int INFO_BOX_HEIGHT = 55;

  private Pokemon mActivePokemon;
  private ImageIcon mIcon;

  private int mDrawnX;
  private int mDrawnY;

  private final int INFO_BOX_X;
  private final int INFO_BOX_Y;

  private boolean mIsPlayer;

  private int mAttackAnimationIndex;
  private Attack mAttackInAnimation;
  private BattleController.AttackAnimationListener mAttackAnimationListener;

  public Trainer(Pokemon pokemon, boolean isPlayer, int infoBoxX, int infoBoxY) {
    mActivePokemon = pokemon;
    mIsPlayer = isPlayer;

    mIcon = mIsPlayer
            ? SpriteHelper.getPokemonBack(pokemon.getName())
            : SpriteHelper.getPokemonFront(pokemon.getName());

    INFO_BOX_X = infoBoxX;
    INFO_BOX_Y = infoBoxY;
  }

  public void drawSprites(Graphics buffer) {
    maybeUpdateForAttackAnimation();
    buffer.drawImage(mIcon.getImage(), mDrawnX, mDrawnY, null);
  }

  public void drawInfoBox(Graphics buffer) {
    buffer.setColor(Color.BLACK);
    buffer.setFont(Styles.largeFont);

    buffer.drawString(
        "" + mActivePokemon.getName().toUpperCase(),
        INFO_BOX_X + 10,
        INFO_BOX_Y + 20);

    buffer.drawRect(INFO_BOX_X, INFO_BOX_Y, INFO_BOX_WIDTH, INFO_BOX_HEIGHT);
    drawHealthAndExpBars(buffer);
  }

  private void drawHealthAndExpBars(Graphics buffer) {
    buffer.setFont(Styles.smallFont);
    buffer.drawString("HP:", INFO_BOX_X + 10, INFO_BOX_Y + 30);
    // todo: make this right aligned
    buffer.drawString("LV: " + mActivePokemon.getLevel(), INFO_BOX_X + 90, INFO_BOX_Y + 20);
    DrawingHelper.drawFullSizeHPBar(buffer, mActivePokemon, INFO_BOX_X + 23, INFO_BOX_Y + 23);

    if (mIsPlayer) {
      DrawingHelper.drawExpBar(buffer, mActivePokemon, INFO_BOX_X + 23, INFO_BOX_Y + 40);

      buffer.setFont(Styles.mediumFont);
      buffer.drawString(
          mActivePokemon.getCurrentHP() + "/" + mActivePokemon.getMaxHP(),
          INFO_BOX_X + 25,
          INFO_BOX_Y + 40);
      buffer.drawRect(INFO_BOX_X + 23 - 1, INFO_BOX_Y + 23 - 1, 101, 7 + 1);
    }
  }

  public void startAttack(Attack attack, BattleController.AttackAnimationListener listener) {
    mAttackAnimationIndex = 0;
    mAttackInAnimation = attack;
    mAttackAnimationListener = listener;
  }

  private void maybeUpdateForAttackAnimation() {
    if (isAttackInProgress()) {
      if (mAttackAnimationIndex <= 5) {
        mDrawnX += 3;
      } else if (mAttackAnimationIndex < 11) {
        mDrawnX -= 3;
      } else {
        mAttackAnimationListener.onAttackFinished();
        mAttackAnimationIndex = 0;
        mAttackInAnimation = null;
        mAttackAnimationListener = null;
      }
    }
  }

  public String getTextString() {
    if (isAttackInProgress()) {
      return mActivePokemon.getName() + " used " + mAttackInAnimation.getName();
    }
    return "";
  }

  public boolean isAttackInProgress() {
    return mAttackInAnimation != null;
  }

  public Pokemon getActivePokemon() {
    return mActivePokemon;
  }

}
