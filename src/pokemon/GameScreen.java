package pokemon;//David Zhao, 3/31/2011
//top screen of a "DS"
/*
RC C C C ...
R
.
.
.
matrix[r][c]
*/

import pokemon.controllers.MovementController;
import pokemon.entities.Pokemon;
import pokemon.entities.Terrain;
import pokemon.ui.Styles;

import javax.swing.*;
import java.awt.*;
import java.awt.event.ActionEvent;
import java.awt.event.ActionListener;
import java.io.*;
import java.util.Scanner;

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
  ImageIcon enemyIcon;
  ImageIcon myPokeIcon;

  private MovementController mMovementController;

  public GameScreen(PlayPanel playPanel) {
    bgColor = Color.WHITE;
    mPlayPanel = playPanel;
    mMovementController = playPanel.getMovementController();
    mMovementController.setCanMove(true);

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
        //!inBattle)
        bgColor = Color.WHITE;
        handleMovementOfPlayer();
        drawMap(myBuffer);
      } else if (inBattle) {
        drawBattleScene(myBuffer);
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
          // draw ground as a background for all sprites
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
    if (map[mMovementController.getRow()][mMovementController.getCol()].getType() == Terrain.GRASS) {
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

    //sprite of pokemon:
    myPokeIcon = SpriteHelper.getPokemonBack(myPoke.getName());
    myBuffer.drawImage(myPokeIcon.getImage(), attackX, attackY, null);
    myBuffer.drawImage(enemyIcon.getImage(), attackXEnemy, attackYEnemy, null);
    //draw display of hp and stuff:
    int enemyX = 15;
    int enemyY = 15;
    int myX = 100;
    int myY = 100;

    myBuffer.setColor(Color.BLACK);
    //bg box
    myBuffer.drawRect(myX, myY, 140, 50);
    myBuffer.drawRect(enemyX, enemyY, 140, 40);
    //pokemon names
    myBuffer.setFont(Styles.largeFont);
    myBuffer.drawString("" + enemy.getName().toUpperCase(), enemyX + 10, enemyY + 20);
    myBuffer.drawString("" + myPoke.getName().toUpperCase(), myX + 10, myY + 20);
    //your hp
    myBuffer.setFont(Styles.mediumFont);
    myBuffer.drawString(myPoke.getCurrentHP() + "/" + myPoke.getMaxHP(), myX + 25, myY + 40);
    //healthbar outline
    myBuffer.drawRect(myX + 23 - 1, myY + 23 - 1, 101, 7 + 1);
    myBuffer.drawRect(enemyX + 23 - 1, enemyY + 23 - 1, 101, 7 + 1);
    //hp bar text
    myBuffer.setFont(Styles.smallFont);
    myBuffer.drawString("HP:", myX + 10, myY + 30);
    myBuffer.drawString("HP:", enemyX + 10, enemyY + 30);
    //pokemon levels
    myBuffer.drawString("LV: " + enemy.getLevel(), enemyX + 90, enemyY + 20);
    myBuffer.drawString("LV: " + myPoke.getLevel(), myX + 90, myY + 20);


    DrawingHelper.drawFullSizeHPBar(myBuffer, myPoke, myX + 23, myY + 23);
    DrawingHelper.drawFullSizeHPBar(myBuffer, enemy, enemyX + 23, enemyY + 23);
    DrawingHelper.drawExpBar(myBuffer, myPoke, myX + 23, myY + 40);

    //textbox:
    myBuffer.setColor(Color.BLACK);
    myBuffer.drawRect(
        0,
        myY + 60,
        GameDriver.SCREEN_WIDTH - 1,
        GameDriver.SCREEN_HEIGHT - 50 - myY);
    myBuffer.setFont(Styles.normalFont);

    if (mPlayPanel.getOptionsPanel().switchingPokemon) {
      myBuffer.drawString("Which pokemon do you want to switch out?", 5, 170);
    }

    if (swapPokemon) {
      mPlayPanel.getOptionsPanel().switchingPokemon = false;
      if (switchingMove < 16)//drawing pokemon out
      {
        attackX -= 5;
        switchingMove++;
        myBuffer.drawString("Come back " + myPoke.getName() + "!", 5, 170);
        mPlayPanel.getOptionsNavigationHelper().toBlack();
      } else if (switchingMove == 16)//switch the two pokemon
      {
        //switch pokemon
        myPoke = mPlayPanel.myPokemon[0];//mPlayPanel.getOptionsPanel().switchPokemonInd];
        switchingMove++;
      } else if (switchingMove < 32) //putting pokemon in
      {
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
      if (myMove <= 5) {//moves towards
        mPlayPanel.getOptionsNavigationHelper().toBlack();
        attackX = attackX + 3;
        //attackY=attackY-3;
        myMove++;
        myBuffer.drawString(myPoke.getName() + " used " + myAttackName, 5, 170);

      } else if (myMove < 11) {//moves back
        attackX = attackX - 3;
        //attackY=attackY+3;
        myMove++;

        myBuffer.drawString(myPoke.getName() + " used " + myAttackName, 5, 170);

      } else {//gives the attack
        double accuracy = Math.random() * 100;
        if (accuracy > 5) {
          AttackLibrary.fill();
          Attack a = new Attack(
              myAttackName,
              AttackLibrary.getType(myAttackName),
              10,
              AttackLibrary.getAttackDamage(myAttackName));
          a.giveDamage(myPoke, enemy, a.getAttackDamage()); //actually attack the enemy
          //a.giveDamage(myPoke,enemy); //actually attack the enemy
        }

        isAttacking = false;
        myMove = 1;
        enemyIsAttacking = true;
      }

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
        //b = new pokemon.Attack(enemyAttackName,pokemon.AttackLibrary.getType(enemyAttackName),10,
        // pokemon.AttackLibrary.getAttackDamage(enemyAttackName));
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

        // pokemon.AttackLibrary.fill();
        // enemyAttackName = enemy.chooseAttackEnemy();
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

  public void saveParty() {
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

    saveParty();
    //gets first usable pokemon
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

    myPoke = PlayPanel.myPokemon[0];

    enemy = new Pokemon.Builder()
        .setName("Raikou")
        .setType("Electric")
        .setFirstAttack("Tackle")
        .setSecondAttack("Shock")
        .setThirdAttack("Thunder")
        .setFourthAttack("Headbutt")
        .setLevel(50)
        .setExp(0)
        .setAttack(55)
        .setDefense(30)
        .setHp(150)
        .setMaxHp(150)
        .build();
    setRandomEnemy();
    enemyIcon = SpriteHelper.getPokemonFront(enemy.getName());

    mPlayPanel.getPlayer().markSeenPokemon(enemy.getName());

    attackX = 5;
    attackY = 80;
    attackXEnemy = 170;
    attackYEnemy = 1;
  }

  public String[] loadEnemyArray() {
    Scanner infile = null;
    try {
      infile = new Scanner(new File("./resources/enemies.txt"));
    } catch (FileNotFoundException e) {
      AlertHelper.fatal("Error: File not found.");
    }

    int count = Integer.parseInt(infile.nextLine());
    String[] enemyArray = new String[count];

    for (int enemyIndex = 0; enemyIndex < enemyArray.length; enemyIndex++) {
      enemyArray[enemyIndex] = infile.nextLine();
    }
    return enemyArray;
  }

  public void setRandomEnemy() {
    int enemyUsed = (int) (Math.random() * wildPokemonArray.length);
    String[] enemyInfo = wildPokemonArray[enemyUsed].split("\\.");
    //    String[] enemyArray = loadEnemyArray();

    enemy.setName(enemyInfo[0]);
    enemy.setType(enemyInfo[1]);
    int level = (int) (
        Math.random() * (Integer.parseInt(enemyInfo[7]) - Integer.parseInt(enemyInfo[6]) + 1)
        + Integer.parseInt(enemyInfo[6]));
    enemy.setLevel(level);

    int atk = 5;
    int def = 5;
    int hp = 10;
    for (int i = 0; i < level; i++) {
      atk += (int) (Math.random() * 3 + 1);
      def += (int) (Math.random() * 3 + 1);
      hp += (int) (Math.random() * 2 + 2);
    }

    enemy.setAttackLevel(atk);// (int)(Math.random()*2*enemy.getLevel() + enemy.getLevel() ));
    enemy.setDefenseLevel(def);// (int)(Math.random()*2*enemy.getLevel() + enemy.getLevel() ));

    enemy.setMaxHP(hp);// (int)(Math.random()*enemy.getLevel()*2.5 + enemy.getLevel()));
    enemy.setCurrentHP(hp);//enemy.getMaxHP());

    enemy.setAttackOne(enemyInfo[2]);
    enemy.setAttackTwo(enemyInfo[3]);
    enemy.setAttackThree(enemyInfo[4]);
    enemy.setAttackFour(enemyInfo[5]);
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