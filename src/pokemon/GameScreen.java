package pokemon;

import pokemon.controllers.BattleController;
import pokemon.controllers.MovementController;
import pokemon.entities.Pokemon;
import pokemon.entities.Terrain;
import pokemon.ui.Styles;

import javax.swing.*;
import java.awt.*;
import java.awt.event.ActionEvent;
import java.awt.event.ActionListener;
import java.io.BufferedReader;
import java.io.File;
import java.io.FileReader;
import java.io.IOException;

public class GameScreen extends GamePanel {
  public static final int SPRITE_WIDTH = 23;
  public static final int SPRITE_HEIGHT = 26;

  private PlayPanel mPlayPanel;
  private Color bgColor;

  static String mapName = "";
  private static Terrain[][] map;
  private static boolean[][] buildings;
  private static boolean[][] items;

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
  private static String[] wildPokemonArray;
  Pokemon myPoke;
  Pokemon enemy;
  static String myAttackName = "";
  static String enemyAttackName = "";

  private MovementController mMovementController;
  private BattleController mBattleController;

  public GameScreen(PlayPanel playPanel) {
    bgColor = Color.WHITE;
    mPlayPanel = playPanel;
    mMovementController = playPanel.getMovementController();
    mMovementController.setCanMove(true);

    mBattleController = new BattleController();

    curSprite = ImageLibrary.faceDown;
    mMovementController.setCoord(7, 8);

    try {
      loadMap(mapName);
    } catch (IOException e) {
      AlertHelper.fatal("map could not be loaded: " + mapName);
    }

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
        drawMap(myBuffer);
      } else if (inBattle) {
        if (OptionsPanel.switchingPokemon) {
        } else if (isAttacking) {
          mBattleController.startPlayerAttack(new Attack(
              myAttackName,
              AttackLibrary.getType(myAttackName),
              10,
              AttackLibrary.getAttackDamage(myAttackName)));
        }
        mBattleController.drawScreen(myBuffer);
      } else {
        bgColor = Color.WHITE;
      }

      repaint();
    }
  }

  private void drawMap(Graphics myBuffer) {
    int curR = mMovementController.getRow();
    int curC = mMovementController.getCol();
    for (int r = curR - 5; r < map.length && r < curR + 5; r++) {
      if (r < 0) {
        continue;
      }
      for (int c = curC - 8; c < map[r].length && c < curC + 8; c++) {
        if (c < 0) {
          continue;
        }

        if (map[r][c].getType() != Terrain.GROUND) {
          // drawSprites ground as a background for all sprites
          myBuffer.drawImage(
              ImageLibrary.ground.getImage(),
              (c - curC + 6) * SPRITE_WIDTH - SPRITE_WIDTH / 2,
              (r - curR + 4) * SPRITE_HEIGHT - SPRITE_HEIGHT / 2,
              null);
        }
        map[r][c].draw(myBuffer, c - curC + 6, r - curR + 4);
      }
    }

    myBuffer.drawImage(
        curSprite.getImage(),
        GameDriver.SCREEN_WIDTH / 2 - SPRITE_WIDTH / 2,
        GameDriver.SCREEN_HEIGHT / 2 - SPRITE_HEIGHT / 2 - 5,
        null); // -5 for visual purposes
  }

  private void handleMovementOfPlayer() {
    if (!mMovementController.canMove()) {
      return;
    }

    maybeLoadNextMap();
    int curR = mMovementController.getRow();
    int curC = mMovementController.getCol();

    boolean hasMoved = true; //for wild pokemon in grass/ocean
    if (mMovementController.isDownPressed()) {
      if (map[curR + 1][curC].canMoveHere()) {
        mMovementController.setRow(curR + 1);
      }
    } else if (mMovementController.isLeftPressed()) {
      if (map[curR][curC - 1].canMoveHere()) {
        mMovementController.setCol(curC - 1);
      }
    } else if (mMovementController.isUpPressed()) {
      if (map[curR - 1][curC].canMoveHere()) {
        mMovementController.setRow(curR - 1);
      }
    } else if (mMovementController.isRightPressed()) {
      if (map[curR][curC + 1].canMoveHere()) {
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
    if (map[curR][curC].getType() == Terrain.HEALING_TILE) {
      for (int i = 0; i < 6; i++) {
        if (PlayPanel.myPokemon[i] != null) {
          PlayPanel.myPokemon[i].heal(
              PlayPanel.myPokemon[i].getMaxHP() - PlayPanel.myPokemon[i].getCurrentHP());
        }
      }
    }
  }

  private void maybeLoadNextMap() {
    int curR = mMovementController.getRow();
    int curC = mMovementController.getCol();
    if (map[curR][curC].isGate()) {
      try {
        int newR = map[curR][curC].getMapLinkRow();
        int newC = map[curR][curC].getMapLinkCol();
        loadMap(map[curR][curC].getMapLink());
        mMovementController.setCoord(newR, newC);
      } catch (IOException a) {
        AlertHelper.fatal(
            "Error: loadMap() has failed in pokemon.GameScreen @ RefreshListener: "
            + map[curR][curC].getMapLink());
      }
    }
  }

  private void maybeInitPokemonBattle() {
    if (map[mMovementController.getRow()][mMovementController.getCol()].getType()
        == Terrain.GRASS) {
      if ((int) (Math.random() * 256) <= 32) {
        toBattle();
      }
    }
  }

  private void drawSummaryScreen(Graphics myBuffer) {
    bgColor = Color.WHITE;
    ImageIcon pokemon = SpriteHelper.getPokemonFront(summaryPoke.getName());
    AlertHelper.debug("" + (pokemon.getImage() == null));
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
          AttackLibrary.fill();
          Attack a = new Attack(
              myAttackName,
              AttackLibrary.getType(myAttackName),
              10,
              AttackLibrary.getAttackDamage(myAttackName));
          a.giveDamage(myPoke, enemy, a.getAttackDamage());
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
        AttackLibrary.fill();
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
          Attack b = new Attack(
              enemyAttackName,
              AttackLibrary.getType(enemyAttackName),
              10,
              AttackLibrary.getAttackDamage(enemyAttackName));
          b.giveDamage(enemy, myPoke, b.getAttackDamage()); //actually attack the enemy
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
    enemy = Pokemon.generateRandomEnemy(
        wildPokemonArray[(int) (Math.random() * wildPokemonArray.length)]);

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

  public static void loadMap(String name) throws IOException {
    BufferedReader in = new BufferedReader(new FileReader(new File("./resources/maps/" + name)));
    String[] mapDimens = in.readLine().split(" ");
    int width = Integer.parseInt(mapDimens[0]);
    int height = Integer.parseInt(mapDimens[1]);

    String[][] mapSymbolForm = new String[height][width];
    Terrain[][] terrainMap = new Terrain[height][width];
    int gateCount = 0;
    int buildingCount = 0;
    for (int r = 0; r < height; r++) {
      mapSymbolForm[r] = in.readLine().split(" ");
      for (int c = 0; c < width; c++) {
        terrainMap[r][c] = new Terrain(mapSymbolForm[r][c]);

        if (terrainMap[r][c].isGate()) {
          gateCount++;
        } else if (terrainMap[r][c].isBuilding()) {
          buildingCount++;
        }
      }
    }

    buildings = new boolean[height][width];
    items = new boolean[height][width];
    for (int r = 0; r < height; r++) {
      for (int c = 0; c < width; c++) {
        buildings[r][c] = false;
        items[r][c] = false;
      }
    }

    //setting up links between maps
    for (int i = 0; i < gateCount; i++) {
      String[] info = in.readLine().split(" ");
      int c = Integer.parseInt(info[0]);
      int r = Integer.parseInt(info[1]);
      terrainMap[r][c].setMapLink(info[2]);
      terrainMap[r][c].setMapLinkCoordinates(Integer.parseInt(info[4]), Integer.parseInt(info[3]));
    }

    String[] locationInfo;

    //setting up buildings
    for (int i = 0; i < buildingCount; i++) {
      locationInfo = in.readLine().split(" ");
      buildings[Integer.parseInt(locationInfo[1])][Integer.parseInt(locationInfo[0])] = true;
    }

    //setting up items (i.e pokeballs)
    int itemCount = Integer.parseInt(in.readLine());
    for (int i = 0; i < itemCount; i++) {
      locationInfo = in.readLine().split(" ");
      items[Integer.parseInt(locationInfo[1])][Integer.parseInt(locationInfo[0])] = true;
    }

    //setting up wild pokemons
    wildPokemonArray = new String[Integer.parseInt(in.readLine())];
    for (int i = 0; i < wildPokemonArray.length; i++) {
      wildPokemonArray[i] = in.readLine();
    }

    in.close();

    mapName = name;
    map = terrainMap;
  }
}