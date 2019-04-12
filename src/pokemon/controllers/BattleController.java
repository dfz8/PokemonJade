package pokemon.controllers;

import pokemon.Attack;
import pokemon.GameDriver;
import pokemon.entities.Pokemon;
import pokemon.entities.Trainer;
import pokemon.ui.Styles;

import java.awt.*;

import static pokemon.entities.Trainer.INFO_BOX_HEIGHT;
import static pokemon.entities.Trainer.INFO_BOX_WIDTH;

public class BattleController {

  private static final int HORIZONTAL_MARGIN = 5;
  private static final int VERTICAL_MARGIN = 5;
  private static final int TEXT_BOX_WIDTH = GameDriver.SCREEN_WIDTH - 2;
  private static final int TEXT_BOX_HEIGHT = 55;

  private static final int ENEMY_INFO_BOX_X = HORIZONTAL_MARGIN;
  private static final int ENEMY_INFO_BOX_Y = VERTICAL_MARGIN;
  private static final int PLAYER_INFO_BOX_X =
      GameDriver.SCREEN_WIDTH - INFO_BOX_WIDTH - HORIZONTAL_MARGIN;
  private static final int PLAYER_INFO_BOX_Y =
      GameDriver.SCREEN_HEIGHT - TEXT_BOX_HEIGHT - INFO_BOX_HEIGHT - 2 * VERTICAL_MARGIN;

  private Trainer mSelf;
  private Trainer mEnemy;

  private boolean isPlayersTurn;

  public interface AttackAnimationListener {
    void onAttackFinished();
  }

  public BattleController() {

  }

  public void initBattle(Pokemon myPokemon, Pokemon enemyPokemon) {
    mSelf = new Trainer(myPokemon, true, PLAYER_INFO_BOX_X, PLAYER_INFO_BOX_Y);
    mEnemy = new Trainer(enemyPokemon, false, ENEMY_INFO_BOX_X, ENEMY_INFO_BOX_Y);
  }

  public void drawScreen(Graphics buffer) {
    updateSpritesAndDraw(buffer);
    drawInfoBoxes(buffer);
    drawTextBox(buffer);
  }

  private void updateSpritesAndDraw(Graphics buffer) {
    mSelf.drawSprites(buffer);
    mEnemy.drawSprites(buffer);
  }

  private void drawInfoBoxes(Graphics buffer) {
    mSelf.drawInfoBox(buffer);
    mEnemy.drawInfoBox(buffer);
  }

  private void drawTextBox(Graphics myBuffer) {
    myBuffer.setColor(Color.BLACK);
    myBuffer.drawRect(1, PLAYER_INFO_BOX_Y + 60, TEXT_BOX_WIDTH, TEXT_BOX_HEIGHT);
    myBuffer.setFont(Styles.normalFont);

    if (isPlayersTurn) {
      myBuffer.drawString(mSelf.getTextString(), 5, 170);
    } else {
      myBuffer.drawString(mEnemy.getTextString(), 5, 170);
    }
  }

  public void startPlayerAttack(Attack attack) {
    if (mSelf.isAttackInProgress()) {
      return;
    }

    AttackAnimationListener listener = new AttackAnimationListener() {
      @Override
      public void onAttackFinished() {
        isPlayersTurn = !isPlayersTurn;

        // apply attack here
      }
    };
    mSelf.startAttack(attack, listener);
  }
}
