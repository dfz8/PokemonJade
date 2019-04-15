package pokemon;

import pokemon.controllers.BattleController;
import pokemon.controllers.MapController;
import pokemon.controllers.MovementController;
import pokemon.entities.Pokemon;
import pokemon.ui.Styles;

import javax.swing.*;
import java.awt.*;
import java.awt.event.ActionEvent;
import java.awt.event.ActionListener;

public class GameScreen extends GamePanel {
  public static final int SPRITE_WIDTH = 23;
  public static final int SPRITE_HEIGHT = 26;

  private PlayPanel mPlayPanel;
  private Color bgColor;

  //conditions:
  static boolean inBattle;
  static boolean swapPokemon;
  static boolean inSummary;
  static Pokemon summaryPoke;
  static boolean inSave;
  static boolean isAttacking;
  static boolean enemyIsAttacking;

  //player sprite info
  private ImageIcon curSprite;

  //battle info
  private static int attackX = 5;
  private static int attackY = 50;
  private static int attackXEnemy = 170;
  private static int attackYEnemy = 1;
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

  public GameScreen(PlayPanel playPanel) {
    bgColor = Color.WHITE;
    mPlayPanel = playPanel;
    mMovementController = playPanel.getMovementController();
    mMovementController.setCanMove(true);

    mBattleController = new BattleController();
    mMapController = mPlayPanel.getMapController();

    curSprite = ImageLibrary.faceDown;
    mMovementController.setCoord(7, 8);

    setAndStartActionListener(150, new RefreshListener());
  }

  public class RefreshListener implements ActionListener {
    public void actionPerformed(ActionEvent e) {
      Graphics myBuffer = getImageBuffer();
      myBuffer.setColor(bgColor);
      myBuffer.fillRect(0, 0, GameDriver.SCREEN_WIDTH, GameDriver.SCREEN_HEIGHT);

      if (inSummary) {
        drawSummaryScreen(myBuffer);
      } else if (mMovementController.canMove()) {
        bgColor = Color.WHITE;
        handleMovementOfPlayer();
        mMapController.drawMap(myBuffer, curSprite);
      } else if (inBattle) {
        if (OptionsPanel.switchingPokemon) {
        } else if (isAttacking) {
          mBattleController.startPlayerAttack(AttackLibrary.getAttack(myAttackName));
        }
        mBattleController.drawScreen(myBuffer);
      } else {
        bgColor = Color.WHITE;
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
        if (PlayPanel.myPokemon[i] != null) {
          PlayPanel.myPokemon[i].heal(
              PlayPanel.myPokemon[i].getMaxHP() - PlayPanel.myPokemon[i].getCurrentHP());
        }
      }
    }
  }

  private void maybeInitPokemonBattle() {
    if (mMapController.canTriggerWildPokemonEncounter()) {
      if ((int) (Math.random() * 256) <= 32) {
        toBattle();
      }
    }
  }

  private void drawSummaryScreen(Graphics myBuffer) {
    bgColor = Color.WHITE;
    ImageIcon pokemon = SpriteHelper.getPokemonFront(summaryPoke.getName());
    myBuffer.drawImage(pokemon.getImage(), 150, 10, null);

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
    bgColor = Color.WHITE;
    if (mPlayPanel.getOptionsPanel().switchingPokemon) {
      myBuffer.drawString("Which pokemon do you want to switch out?", 5, 170);
    }
    if (swapPokemon) {
      mPlayPanel.getOptionsPanel().switchingPokemon = false;
      if (switchingMove < 16) {
        //drawing pokemon out
        attackX -= 5;
        switchingMove++;
        myBuffer.drawString("Come back " + myPoke.getName() + "!", 5, 170);
        mPlayPanel.getOptionsNavigationHelper().toBlack();
      } else if (switchingMove == 16) {//switch the two pokemon
        //switch pokemon
        myPoke = mPlayPanel.myPokemon[0];//mPlayPanel.getOptionsPanel().switchPokemonInd];
        switchingMove++;
      } else if (switchingMove < 32) {//putting pokemon in
        attackX += 5;
        switchingMove++;
        myBuffer.drawString("Go get them " + myPoke.getName() + "!", 5, 170);
      } else {
        swapPokemon = false;
        switchingMove = 1;

        isAttacking = false;
        enemyIsAttacking = true;
      }
    } else if (isAttacking) {
      if (myMove == 0) {
        mPlayPanel.getOptionsNavigationHelper().toBlack();
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
        enemyIsAttacking = false;
        isAttacking = false;

        resetOrderOfPokemonInParty();
        mPlayPanel.getOptionsNavigationHelper().toNormal();
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

        mPlayPanel.getOptionsNavigationHelper().toBattle();
      }
      if (myPoke.isFainted()) {
        //put image somewhere outside of the screen
        myPoke.setCurrentHP(0);

        // attackX = 300;
        // attackY = 300;
        isAttacking = false;
        enemyIsAttacking = false;

        int pokeLeft = 0;
        for (int i = 0; i < 6; i++) {
          if (PlayPanel.myPokemon[i] == null) {
            break;
          }
          if (!PlayPanel.myPokemon[i].isFainted()) {
            pokeLeft++;
          }
        }
        if (pokeLeft == 0) {
          resetOrderOfPokemonInParty();
          mPlayPanel.getOptionsNavigationHelper().toNormal();
        } else {
          mPlayPanel.getOptionsPanel().toSwitchPokemon();
        }
      }
    }
  }

  public void saveOrderOfPokemonInParty() {
    for (int i = 0; i < normalParty.length; i++) {
      normalParty[i] = PlayPanel.myPokemon[i];
    }
  }

  public void resetOrderOfPokemonInParty() {
    for (int i = 0; i < normalParty.length; i++) {
      PlayPanel.myPokemon[i] = normalParty[i];
    }
  }

  public void toBattle() {
    mMovementController.setCanMove(false);
    inBattle = true;

    myPoke = getFirstUsablePokemonForBattle();
    enemy = mMapController.getRandomWildPokemon();

    mPlayPanel.getPlayer().markSeenPokemon(enemy.getName());

    attackX = 5;
    attackY = 80;
    attackXEnemy = 170;
    attackYEnemy = 1;

    mBattleController.initBattle(myPoke, enemy);
  }

  private Pokemon getFirstUsablePokemonForBattle() {
    saveOrderOfPokemonInParty();
    int myPokeInd = -1;
    for (int i = 0; i < 6; i++) {
      if (PlayPanel.myPokemon[i] == null || PlayPanel.myPokemon[i].isFainted()) {
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
    Pokemon temp = PlayPanel.myPokemon[myPokeInd];
    PlayPanel.myPokemon[myPokeInd] = PlayPanel.myPokemon[0];
    PlayPanel.myPokemon[0] = temp;
    return PlayPanel.myPokemon[0];
  }
}