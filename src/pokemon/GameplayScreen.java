package pokemon;

import pokemon.controllers.BattleController;
import pokemon.controllers.MapController;
import pokemon.controllers.MovementController;
import pokemon.entities.Pokemon;
import pokemon.ui.Styles;
import pokemon.util.DrawingHelper;
import pokemon.util.SpriteHelper;

import javax.swing.*;
import java.awt.*;
import java.awt.event.ActionEvent;
import java.awt.event.ActionListener;

/**
 * Controls what is drawn in the top half of the screen
 */
public class GameplayScreen extends DrawableScreen {
  public static final int SPRITE_WIDTH = 23;
  public static final int SPRITE_HEIGHT = 26;

  private GameContainer mGameContainer;
  private Color bgColor;

  //conditions:
  static boolean isAttacking;

  private boolean inSummary;
  private Pokemon summaryPoke;
  private boolean enemyIsAttacking;

  //player sprite info
  private ImageIcon curSprite;

  //battle info
  private static int myMove = 1;
  private static int enemyMove = 1;
  private static int switchingMove = 1;
  private static Pokemon[] normalParty = new Pokemon[6]; // to save the order of your party
  // before the battle starts (so you can switch around all you want)
  Pokemon myPoke;
  Pokemon enemy;
  static String myAttackName = "";
  static String enemyAttackName = "";

  private MovementController mMovementController;
  private BattleController mBattleController;
  private MapController mMapController;

  public GameplayScreen(GameContainer gameContainer) {
    bgColor = Color.WHITE;
    mGameContainer = gameContainer;
    mMovementController = gameContainer.getMovementController();
    mMovementController.setCanMove(true);
    mBattleController = mGameContainer.getBattleController();
    mMapController = mGameContainer.getMapController();

    curSprite = ImageLibrary.faceDown;
    mMovementController.setCoord(7, 8);

    setAndStartActionListener(150, new RefreshListener());
  }

  @Override
  public void handleStateChange(GameState currentState, GameState newState) {
    switch (newState) {
      case DEFAULT:
        inSummary = false;
        mMovementController.setCanMove(true);
        if (currentState == GameState.BATTLE_DEFAULT) {
          resetOrderOfPokemonInParty();
          mBattleController.setIsInBattle(false);
        }
        break;
      case BATTLE_DEFAULT:
        initBattle();
        break;
      case BATTLE_WAIT_ON_ENEMY:
        isAttacking = false;
        enemyIsAttacking = true;
        break;
      case VIEW_SELF:
      case VIEW_POKEDEX:
        mMovementController.setCanMove(false);
        break;
      case VIEW_POKEMON:
        inSummary = true;
        summaryPoke = mGameContainer.getPokemonForSummary();
        break;
      case VIEW_PARTY:
      case BATTLE_VIEW_POKEMON:
        mMovementController.setCanMove(false);
        break;
    }
  }

  public class RefreshListener implements ActionListener {
    public void actionPerformed(ActionEvent e) {
      Graphics myBuffer = getImageBuffer();
      myBuffer.setColor(bgColor);
      myBuffer.fillRect(0, 0, DrawingHelper.SCREEN_WIDTH, DrawingHelper.SCREEN_HEIGHT);

      if (inSummary) {
        drawSummaryScreen(myBuffer);
      } else if (mBattleController.isInBattle()) {
        if (mBattleController.isSwappingPokemon()) {
        } else if (isAttacking) {
          mBattleController.startPlayerAttack(AttackLibrary.getAttack(myAttackName));
        }
        mBattleController.drawScreen(myBuffer);
      } else {
        bgColor = Color.WHITE;
        if (mMovementController.canMove()) {
          handleMovementOfPlayer();
        }
        mMapController.drawMap(myBuffer, curSprite);
      }

      repaint();
    }
  }

  private void handleMovementOfPlayer() {
    if (!mMovementController.canMove()) {
      return;
    }

    mMapController.maybeLoadNextMap();
    int curR = mMovementController.getRow();
    int curC = mMovementController.getCol();

    boolean hasMoved = true; //for wild pokemon in grass/ocean
    if (mMovementController.isDownPressed()) {
      if (mMapController.canMoveHere(curR + 1, curC)) {
        mMovementController.setRow(curR + 1);
      }
    } else if (mMovementController.isLeftPressed()) {
      if (mMapController.canMoveHere(curR, curC - 1)) {
        mMovementController.setCol(curC - 1);
      }
    } else if (mMovementController.isUpPressed()) {
      if (mMapController.canMoveHere(curR - 1, curC)) {
        mMovementController.setRow(curR - 1);
      }
    } else if (mMovementController.isRightPressed()) {
      if (mMapController.canMoveHere(curR, curC + 1)) {
        mMovementController.setCol(curC + 1);
      }
    } else {
      hasMoved = false;
      curSprite = mMovementController.getStationarySprite();
    }

    if (hasMoved) {
      curSprite = mMovementController.getWalkingSprite();

      //code for wild pokemon encounter:
      maybeInitPokemonBattle();
    }

    //healing:
    if (mMapController.isOnHealingTile()) {
      for (int i = 0; i < 6; i++) {
        if (GameContainer.myPokemon[i] != null) {
          GameContainer.myPokemon[i].heal(
              GameContainer.myPokemon[i].getMaxHP() - GameContainer.myPokemon[i].getCurrentHP());
        }
      }
    }
  }

  private void maybeInitPokemonBattle() {
    if (mMapController.canTriggerWildPokemonEncounter()) {
      if ((int) (Math.random() * 256) <= 32) {
        mGameContainer.setState(GameState.BATTLE_DEFAULT);
      }
    }
  }

  private void drawSummaryScreen(Graphics myBuffer) {
    bgColor = Color.WHITE;
    ImageIcon pokemon = SpriteHelper.getPokemonFront(summaryPoke.getName());
    DrawingHelper.drawImage(myBuffer, pokemon, 150, 10);

    myBuffer.setColor(Color.BLACK);
    myBuffer.setFont(Styles.largerLargeFont);
    myBuffer.drawString(summaryPoke.getName().toUpperCase(), 10, 20);
    myBuffer.drawString("Level: " + summaryPoke.getLevel(), 10, 40);

    //stats:
    myBuffer.drawString("Attack:", 10, 150);
    myBuffer.drawString("" + summaryPoke.getAttackLevel(), 70, 150);
    myBuffer.drawString("Defense:", 10, 170);
    myBuffer.drawString("" + summaryPoke.getDefenseLevel(), 70, 170);
    //hp
    myBuffer.setFont(Styles.normalFont);
    myBuffer.drawString(summaryPoke.getCurrentHP() + " / " + summaryPoke.getMaxHP(), 40, 80);
    myBuffer.setFont(Styles.smallFont);
    myBuffer.drawString("HP:", 10, 65);
    DrawingHelper.drawFullSizeHPBar(myBuffer, summaryPoke, 23, 60);
    //exp
    DrawingHelper.drawExpBar(myBuffer, summaryPoke, 23, 85);
    myBuffer.setColor(Color.BLACK);
    myBuffer.drawString("XP:", 10, 85);
    myBuffer.drawString(
        summaryPoke.getMyEXP() + " out of " + summaryPoke.getMyNextLevelEXP(),
        23,
        100);
  }

  private void drawBattleScene(Graphics myBuffer) {
    int attackX = 5;
    int attackXEnemy = 300;
    int attackYEnemy = 300;
    bgColor = Color.WHITE;
    if (mGameContainer.getBattleController().isSwappingPokemon()) {
      myBuffer.drawString("Which pokemon do you want to switch out?", 5, 170);
    }
    if (mBattleController.isSwappingPokemon()) {
      mGameContainer.getBattleController().setIsSwappingPokemon(false);
      if (switchingMove < 16) {
        //drawing pokemon out
        attackX -= 5;
        switchingMove++;
        myBuffer.drawString("Come back " + myPoke.getName() + "!", 5, 170);
        mGameContainer.setState(GameState.BATTLE_SWITCH_ANIMATION);
      } else if (switchingMove == 16) {//switch the two pokemon
        //switch pokemon
        myPoke = mGameContainer.myPokemon[0];//mGameContainer.getOptionsPanel().switchPokemonInd];
        switchingMove++;
      } else if (switchingMove < 32) {//putting pokemon in
        attackX += 5;
        switchingMove++;
        myBuffer.drawString("Go get them " + myPoke.getName() + "!", 5, 170);
      } else {
        mBattleController.setIsSwappingPokemon(false);
        switchingMove = 1;

        isAttacking = false;
        enemyIsAttacking = true;
      }
    } else if (isAttacking) {
      if (myMove == 0) {
        mGameContainer.setState(GameState.BATTLE_ATTACK_ANIMATION);
      }
      if (myMove <= 5) {
        attackX = attackX + 3;
      } else if (myMove < 11) {
        attackX = attackX - 3;
      } else {//gives the attack
        double accuracy = Math.random() * 100;
        if (accuracy > 5) {
          Attack a = AttackLibrary.getAttack(myAttackName);
          a.giveDamage(myPoke, enemy);
        }

        myMove = 0;
        isAttacking = false;
        enemyIsAttacking = true;
      }
      myMove++;

      if (enemy.isFainted()) {
        myPoke.gainIsFast = true;
        myPoke.EXPgain();
        //put image somewhere outside of the screen
        attackXEnemy = 300;
        attackYEnemy = 300;
        mGameContainer.setState(GameState.DEFAULT);
      }
    } else if (enemyIsAttacking) {
      if (enemyMove == 1) {
        enemyAttackName = enemy.chooseAttackEnemy();
      }
      if (enemyMove <= 5) {
        attackXEnemy = attackXEnemy - 3;
        attackYEnemy = attackYEnemy + 3;
        enemyMove++;
        myBuffer.drawString("Wild " + enemy.getName() + " used " + enemyAttackName, 5, 170);

      } else if (enemyMove < 11) {
        attackXEnemy = attackXEnemy + 3;
        attackYEnemy = attackYEnemy - 3;
        enemyMove++;
        myBuffer.drawString("Wild " + enemy.getName() + " used " + enemyAttackName, 5, 170);

      } else {
        double accuracy = Math.random() * 100;
        if (accuracy > 5) {
          Attack b = AttackLibrary.getAttack(enemyAttackName);
          b.giveDamage(enemy, myPoke); //actually attack the enemy
        }
        //a.giveDamage(enemy,myPoke);
        enemyMove = 1;
        enemyIsAttacking = false;

        mGameContainer.setState(GameState.BATTLE_DEFAULT);
      }
      if (myPoke.isFainted()) {
        //put image somewhere outside of the screen
        myPoke.setCurrentHP(0);
        isAttacking = false;
        enemyIsAttacking = false;

        int pokeLeft = 0;
        for (int i = 0; i < 6; i++) {
          if (GameContainer.myPokemon[i] == null) {
            break;
          }
          if (!GameContainer.myPokemon[i].isFainted()) {
            pokeLeft++;
          }
        }
        if (pokeLeft == 0) {
          // lose the battle
          mGameContainer.setState(GameState.DEFAULT);
        } else {
          mGameContainer.setState(GameState.BATTLE_VIEW_POKEMON);
        }
      }
    }
  }

  private void saveOrderOfPokemonInParty() {
    for (int i = 0; i < normalParty.length; i++) {
      normalParty[i] = GameContainer.myPokemon[i];
    }
  }

  private void resetOrderOfPokemonInParty() {
    for (int i = 0; i < normalParty.length; i++) {
      GameContainer.myPokemon[i] = normalParty[i];
    }
  }

  private void initBattle() {
    mMovementController.setCanMove(false);
    mBattleController.setIsInBattle(true);

    myPoke = getFirstUsablePokemonForBattle();
    enemy = mMapController.getRandomWildPokemon();

    mGameContainer.getPlayer().markSeenPokemon(enemy.getName());
    mBattleController.initBattle(myPoke, enemy);
  }

  private Pokemon getFirstUsablePokemonForBattle() {
    saveOrderOfPokemonInParty();
    int myPokeInd = -1;
    for (int i = 0; i < 6; i++) {
      if (GameContainer.myPokemon[i] == null || GameContainer.myPokemon[i].isFainted()) {
        continue;
      }
      myPokeInd = i;
      break;
    }
    if (myPokeInd < 0) {
      AlertHelper.fatal(
          "How in the world were you allowed into the wild with no " +
          "usable pokemon???\nNow go rant the game makers.");
    }
    Pokemon temp = GameContainer.myPokemon[myPokeInd];
    GameContainer.myPokemon[myPokeInd] = GameContainer.myPokemon[0];
    GameContainer.myPokemon[0] = temp;
    return GameContainer.myPokemon[0];
  }
}