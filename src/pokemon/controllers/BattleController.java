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
  private static final int BATTLE_SPRITE_DIMEN = 80;

  private static final int TEXT_BOX_MARGIN = 1;
  private static final int TEXT_BOX_WIDTH = GameDriver.SCREEN_WIDTH - 2 * TEXT_BOX_MARGIN;
  private static final int TEXT_BOX_HEIGHT = 55;
  private static final int TEXT_BOX_X = TEXT_BOX_MARGIN;
  private static final int TEXT_BOX_Y =
      GameDriver.SCREEN_HEIGHT - TEXT_BOX_HEIGHT - VERTICAL_MARGIN;

  private static final int ENEMY_INFO_BOX_X = HORIZONTAL_MARGIN;
  private static final int ENEMY_INFO_BOX_Y = VERTICAL_MARGIN;

  private static final int PLAYER_INFO_BOX_X =
      GameDriver.SCREEN_WIDTH - INFO_BOX_WIDTH - HORIZONTAL_MARGIN;
  private static final int PLAYER_INFO_BOX_Y = TEXT_BOX_Y - INFO_BOX_HEIGHT - VERTICAL_MARGIN;

  private static final int ENEMY_START_X =
      GameDriver.SCREEN_WIDTH - BATTLE_SPRITE_DIMEN - HORIZONTAL_MARGIN;
  private static final int ENEMY_START_Y = VERTICAL_MARGIN;
  private static final int PLAYER_START_X = HORIZONTAL_MARGIN;
  private static final int PLAYER_START_Y = TEXT_BOX_Y - BATTLE_SPRITE_DIMEN;


  private Trainer mSelf;
  private Trainer mEnemy;

  private boolean isPlayersTurn;

  public interface AttackAnimationListener {
    void onAttackFinished();
  }

  public BattleController() {

  }

  public void initBattle(Pokemon myPokemon, Pokemon enemyPokemon) {
    mSelf = new Trainer(
        myPokemon,
        true,
        PLAYER_INFO_BOX_X,
        PLAYER_INFO_BOX_Y,
        PLAYER_START_X,
        PLAYER_START_Y);
    mEnemy = new Trainer(
        enemyPokemon,
        false,
        ENEMY_INFO_BOX_X,
        ENEMY_INFO_BOX_Y,
        ENEMY_START_X,
        ENEMY_START_Y);
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
    myBuffer.drawRect(TEXT_BOX_X, TEXT_BOX_Y, TEXT_BOX_WIDTH, TEXT_BOX_HEIGHT);
    myBuffer.setFont(Styles.normalFont);

    int textMargin = 5;
    if (isPlayersTurn) {
      myBuffer.drawString(mSelf.getTextString(), TEXT_BOX_X + textMargin, TEXT_BOX_Y + textMargin);
    } else {
      myBuffer.drawString(mEnemy.getTextString(), TEXT_BOX_X + textMargin, TEXT_BOX_Y + textMargin);
    }
  }

  public void startPlayerAttack(Attack attack) {
    if (mSelf.isAttackInProgress()) {
      return;
    }

    AttackAnimationListener listener = new AttackAnimationListener() {
      @Override
      public void onAttackFinished() {
        if (isPlayersTurn) {
          attack.giveDamage(mSelf.getActivePokemon(), mEnemy.getActivePokemon());
        } else {
          attack.giveDamage(mEnemy.getActivePokemon(), mSelf.getActivePokemon());
        }
        isPlayersTurn = !isPlayersTurn;
      }
    };
    mSelf.startAttack(attack, listener);
  }
}
