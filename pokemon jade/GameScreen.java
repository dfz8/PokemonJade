//David Zhao, 3/31/2011
//top screen of a "DS"
/*
RC C C C ...
R
.
.
.
matrix[r][c]
*/

import javax.swing.*;
import java.awt.*;
import java.awt.event.*;
import java.awt.image.*;
import java.io.*;
import java.util.Scanner;

public class GameScreen extends JPanel {
  //mandatory stuff
  BufferedImage myImage;
  Graphics myBuffer;
  private static final int WIDTH = 275;
  private static final int HEIGHT = 215;
  static final int SPRITE_WIDTH = 23;
  static final int SPRITE_HEIGHT = 26;
  Color bgColor;
  Color myColor;
  private static PlayPanel controlPanel;
  Timer t;
  static String mapName = ""; //for saving purposes
  private static Terrain[][] map;
  private static boolean[][] buildings;
  private static boolean[][] items;
  //conditions:
  static boolean canMove = false;
  static boolean inBattle = false;
  static boolean swapPokemon = false;

  static boolean inParty = false; // havent used

  static boolean inSummary = false;
  static Pokemon summaryPoke; //static ImageIcon summaryPoke;
  static boolean inSave = false;
  static boolean upPressed = false;
  static boolean downPressed = false;
  static boolean leftPressed = false;
  static boolean rightPressed = false;
  static boolean isAttacking = false;
  static boolean enemyIsAttacking = false;
  //player sprite info
  private ImageIcon curSprite = ImageLibrary.faceDown;
  private static int moveSpriteIndex = 0;
  private static int curDirection = 0; //0 = down, 1 = left, 2 = up, 3 = right
  private static int increment = 2;//2 = walking, 5 = running, 10 = bike
  static int curC = 8; // current position
  static int curR = 5; // within the map
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
  ImageIcon enemyIcon = null;//ImageLibrary.raikou;
  ImageIcon myPokeIcon = null;// ImageLibrary.bulbosaurBack;
  //extras:
  static Font normalFont = new Font("Ariel", Font.PLAIN, 10);
  static Font extraLargeFont = new Font("Agency FB", Font.BOLD, 30);
  static Font largerLargeFont = new Font("Agency FB", Font.BOLD, 20);
  static Font largeFont = new Font("Agency FB", Font.BOLD, 15);
  static Font mediumFont = new Font("Agency FB", Font.BOLD, 12);
  static Font smallFont = new Font("Agency FB", Font.BOLD, 10);

  public GameScreen(PlayPanel panel) {
    bgColor = Color.WHITE;
    controlPanel = panel;

    myImage = new BufferedImage(WIDTH, HEIGHT, BufferedImage.TYPE_INT_RGB);
    myBuffer = myImage.getGraphics();
    myBuffer.setColor(bgColor);
    myBuffer.fillRect(0, 0, WIDTH, HEIGHT);

    try {
      loadMap(mapName);
    } catch (IOException e) {
      System.out.println("map could not be loaded: " + mapName);
      System.exit(0);
    }

    t = new Timer(150, new RefreshListener());
    t.start();
  }

  public void paintComponent(Graphics g) {
    g.drawImage(myImage, 0, 0, WIDTH, HEIGHT, null);
  }

  public class RefreshListener implements ActionListener {
    public void actionPerformed(ActionEvent e) {
      myBuffer.setColor(bgColor);
      myBuffer.fillRect(0, 0, WIDTH, HEIGHT);

      if (inSummary) {
        drawSummaryScreen();
      } else if (canMove) {
        //!inBattle)
        bgColor = Color.WHITE;
        handleMovementOfPlayer();
        drawMap();
      } else if (inBattle) {
        drawBattleScene();
      } else {
        bgColor = Color.WHITE;
      }

      repaint();
    }

  }

  private void drawMap() {
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
          myBuffer.drawImage(ImageLibrary.ground.getImage(),
                             (c - curC + 6) * SPRITE_WIDTH - SPRITE_WIDTH / 2,
                             (r - curR + 4) * SPRITE_HEIGHT - SPRITE_HEIGHT / 2,
                             null);
        }
        map[r][c].draw(myBuffer, c - curC + 6, r - curR + 4);

      }
    }

    myBuffer.drawImage(curSprite.getImage(),
                       WIDTH / 2 - SPRITE_WIDTH / 2,
                       HEIGHT / 2 - SPRITE_HEIGHT / 2 - 5,
                       null); // -5 for visual purposes
  }

  private void handleMovementOfPlayer() {
    //gate stepping code:
    if (map[curR][curC].isGate()) {
      int newR = map[curR][curC].getMapLinkRow();
      int newC = map[curR][curC].getMapLinkCol();
      try {
        loadMap(map[curR][curC].getMapLink());
      } catch (IOException a) {
        System.out.println("Error: loadMap() has failed in GameScreen @ RefreshListener: " + map[curR][curC]
            .getMapLink());
      }
      curR = newR;
      curC = newC;
    }
    //^^^^^
    if (canMove) {
      boolean hasMoved = true; //for wild pokemon in grass/ocean
      if (downPressed) {
        if (map[curR + 1][curC].canMoveHere()) {
          curR++;
        }

        moveSpriteIndex++;
        if (moveSpriteIndex == ImageLibrary.moveDown_walk.length)
          moveSpriteIndex = 0;
        curSprite = ImageLibrary.moveDown_walk[moveSpriteIndex];
        curDirection = 0;
      } else if (leftPressed) {
        if (map[curR][curC - 1].canMoveHere()) {
          curC--;

        }

        moveSpriteIndex++;
        if (moveSpriteIndex == ImageLibrary.moveLeft_walk.length)
          moveSpriteIndex = 0;
        curSprite = ImageLibrary.moveLeft_walk[moveSpriteIndex];
        curDirection = 1;
      } else if (upPressed) {
        if (map[curR - 1][curC].canMoveHere()) {
          curR--;
          //move bg objects
        }

        moveSpriteIndex++;
        if (moveSpriteIndex == ImageLibrary.moveUp_walk.length)
          moveSpriteIndex = 0;
        curSprite = ImageLibrary.moveUp_walk[moveSpriteIndex];
        curDirection = 2;
      } else if (rightPressed) {
        if (map[curR][curC + 1].canMoveHere()) {
          curC++;
        }

        moveSpriteIndex++;
        if (moveSpriteIndex == ImageLibrary.moveRight_walk.length)
          moveSpriteIndex = 0;
        curSprite = ImageLibrary.moveRight_walk[moveSpriteIndex];
        curDirection = 3;
      } else {
        //no key is pressed so you're resting
        hasMoved = false;
        moveSpriteIndex = -1;
        if (curDirection == 0)
          curSprite = ImageLibrary.faceDown;
        else if (curDirection == 1)
          curSprite = ImageLibrary.faceLeft;
        else if (curDirection == 2)
          curSprite = ImageLibrary.faceUp;
        else // if(curDirection = 3)
          curSprite = ImageLibrary.faceRight;
      }
      //code for wild pokemon encoutner:
      if (hasMoved) {
        if (map[curR][curC].getType() == Terrain.GRASS) {
          int randomNum = (int) (Math.random() * 256);
          if (randomNum <= 32)//<--should be 32 for normal run, 128 for test
            controlPanel.toBattle(true);
        }
      }
    }
    //healing:
    if (map[curR][curC].getType() == Terrain.HEALING_TILE) {
      for (int i = 0; i < 6; i++) {
        if (PlayPanel.myPokemon[i] != null)
          PlayPanel.myPokemon[i].heal(PlayPanel.myPokemon[i].getMaxHP() - PlayPanel.myPokemon[i]
              .getCurrentHP());
      }
    }
  }

  private void drawSummaryScreen() {
    bgColor = Color.WHITE;
    ImageIcon poke = new ImageIcon("./resources/images/Pokemon/" + summaryPoke.getName() +
                                       ".png");
    myBuffer.drawImage(poke.getImage(), 150, 10, null);

    myBuffer.setColor(Color.BLACK);
    myBuffer.setFont(largerLargeFont);
    myBuffer.drawString(summaryPoke.getName().toUpperCase(), 10, 20);
    myBuffer.drawString("Level: " + summaryPoke.getLevel(), 10, 40);

    //stats:
    myBuffer.drawString("Attack:", 10, 150);
    myBuffer.drawString("" + summaryPoke.getAttackLevel(), 70, 150);
    myBuffer.drawString("Defense:", 10, 170);
    myBuffer.drawString("" + summaryPoke.getDefenseLevel(), 70, 170);
    //hp
    myBuffer.setFont(normalFont);
    myBuffer.drawString(summaryPoke.getCurrentHP() + " / " + summaryPoke.getMaxHP(), 40, 80);
    myBuffer.setFont(smallFont);
    myBuffer.drawString("HP:", 10, 65);
    myBuffer.drawRect(22, 59, 101, 7);
    myBuffer.setColor(getPokemonHealthBarColor(summaryPoke));
    myBuffer.fillRect(23,
                      60,
                      (int) (100.0 * summaryPoke.getCurrentHP() / summaryPoke.getMaxHP()),
                      6);
    //exp
    myBuffer.setColor(Color.BLACK);
    //myBuffer.drawString("EXP: ",
    myBuffer.setColor(Color.BLUE);
    myBuffer.fillRect(23,
                      80,
                      (int) (100.0 * (summaryPoke.getMyEXP() - summaryPoke.getMyPastLevelEXP()) / (summaryPoke
                          .getMyNextLevelEXP() - summaryPoke.getMyPastLevelEXP())),
                      5);
    myBuffer.drawString(summaryPoke.getMyEXP() + " out of " + summaryPoke.getMyNextLevelEXP(),
                        23,
                        100);
  }

  private void drawBattleScene() {
    bgColor = Color.WHITE;

    //sprite of pokemon:
    myPokeIcon = new ImageIcon("./resources/images/Pokemon/" + myPoke.getName() + "Back.png");
    myBuffer.drawImage(myPokeIcon.getImage(), attackX, attackY, null);
    myBuffer.drawImage(enemyIcon.getImage(), attackXEnemy, attackYEnemy, null);
    //draw display of hp and stuff:
    int enemyX = 15;
    int enemyY = 15;
    int myX = 100;
    int myY = 100;

    myColor = Color.BLACK;
    myBuffer.setColor(myColor);
    //bg box
    myBuffer.drawRect(myX, myY, 140, 50);
    myBuffer.drawRect(enemyX, enemyY, 140, 40);
    //pokemon names
    myBuffer.setFont(largeFont);
    myBuffer.drawString("" + enemy.getName().toUpperCase(), enemyX + 10, enemyY + 20);
    myBuffer.drawString("" + myPoke.getName().toUpperCase(), myX + 10, myY + 20);
    //your hp
    myBuffer.setFont(mediumFont);
    myBuffer.drawString(myPoke.getCurrentHP() + "/" + myPoke.getMaxHP(), myX + 25, myY + 40);
    //healthbar outline
    myBuffer.drawRect(myX + 23 - 1, myY + 23 - 1, 101, 7 + 1);
    myBuffer.drawRect(enemyX + 23 - 1, enemyY + 23 - 1, 101, 7 + 1);
    //hp bar text
    myBuffer.setFont(smallFont);
    myBuffer.drawString("HP:", myX + 10, myY + 30);
    myBuffer.drawString("HP:", enemyX + 10, enemyY + 30);
    //pokemon levels
    myBuffer.drawString("LV: " + enemy.getLevel(), enemyX + 90, enemyY + 20);
    myBuffer.drawString("LV: " + myPoke.getLevel(), myX + 90, myY + 20);

    //me:
    myBuffer.setColor(getPokemonHealthBarColor(myPoke));
    myBuffer.fillRect(myX + 23,
                      myY + 23,
                      (int) ((100) * (1.0 * myPoke.getCurrentHP() / myPoke.getMaxHP())),
                      7);

    //enemy:
    myBuffer.setColor(getPokemonHealthBarColor(enemy));
    myBuffer.fillRect(enemyX + 23,
                      enemyY + 23,
                      (int) ((100) * (1.0 * enemy.getCurrentHP() / enemy.getMaxHP())),
                      7);

    //EXP Bar
    myBuffer.setColor(Color.BLACK);
    myBuffer.drawRect(myX + 23, myY + 40, 100, 4);
    myBuffer.setColor(Color.BLUE);

    myBuffer.fillRect(myX + 23,
                      myY + 40,
                      (int) (100.0 * (myPoke.getMyEXP() - myPoke.getMyPastLevelEXP()) / (myPoke.getMyNextLevelEXP() - myPoke
                          .getMyPastLevelEXP())),
                      5);

    //textbox:
    myBuffer.setColor(Color.BLACK);
    myBuffer.drawRect(0, myY + 60, WIDTH - 1, HEIGHT - 50 - myY);
    myBuffer.setFont(normalFont);

    if (controlPanel.options.switchingPokemon) {
      myBuffer.drawString("Which pokemon do you want to switch out?", 5, 170);
    }

    if (swapPokemon) {
      controlPanel.options.switchingPokemon = false;
      if (switchingMove < 16)//drawing pokemon out
      {
        attackX -= 5;
        switchingMove++;
        myBuffer.drawString("Come back " + myPoke.getName() + "!", 5, 170);
        OptionsNavigationHelper.toBlack();
      } else if (switchingMove == 16)//switch the two pokemon
      {
        //switch pokemon
        myPoke = controlPanel.myPokemon[0];//controlPanel.options.switchPokemonInd];
        switchingMove++;
      } else if (switchingMove > 16 && switchingMove < 32) //putting pokemon in
      {
        attackX += 5;
        switchingMove++;
        myBuffer.drawString("Go get them " + myPoke.getName() + "!", 5, 170);
      } else if (switchingMove >= 32) {
        swapPokemon = false;
        switchingMove = 1;

        isAttacking = false;
        enemyIsAttacking = true;
      }
    } else if (isAttacking) {
      if (myMove <= 5) {//moves towards
        OptionsNavigationHelper.toBlack();
        attackX = attackX + 3;
        //attackY=attackY-3;
        myMove++;
        myBuffer.drawString(myPoke.getName() + " used " + myAttackName, 5, 170);

      } else if (myMove > 5 && myMove < 11) {//moves back
        attackX = attackX - 3;
        //attackY=attackY+3;
        myMove++;

        myBuffer.drawString(myPoke.getName() + " used " + myAttackName, 5, 170);

      } else if (myMove >= 11) {//gives the attack

        double accuracy = Math.random() * 100;
        if (accuracy > 5) {
          AttackLibrary.fill();
          Attack a = new Attack(myAttackName,
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

      if (enemy.isFainted() == true) {
        myPoke.gainIsFast = true;
        myPoke.EXPgain();
        //put image somewhere outside of the screen
        attackXEnemy = 300;
        attackYEnemy = 300;
        enemyIsAttacking = false;
        isAttacking = false;

        resetOrderOfPokemonInParty();
        OptionsNavigationHelper.toNormal();
      }
    } else if (enemyIsAttacking) {
      if (enemyMove == 1) {
        AttackLibrary.fill();
        enemyAttackName = enemy.chooseAttackEnemy();
        //b = new Attack(enemyAttackName,AttackLibrary.getType(enemyAttackName),10,
        // AttackLibrary.getAttackDamage(enemyAttackName));
      }

      if (enemyMove <= 5) {
        attackXEnemy = attackXEnemy - 3;
        attackYEnemy = attackYEnemy + 3;
        enemyMove++;
        myBuffer.drawString("Wild " + enemy.getName() + " used " + enemyAttackName, 5, 170);

      } else if (enemyMove > 5 && enemyMove < 11) {
        attackXEnemy = attackXEnemy + 3;
        attackYEnemy = attackYEnemy - 3;
        enemyMove++;
        myBuffer.drawString("Wild " + enemy.getName() + " used " + enemyAttackName, 5, 170);

      } else if (enemyMove >= 11) {

        // AttackLibrary.fill();
        // enemyAttackName = enemy.chooseAttackEnemy();
        double accuracy = Math.random() * 100;
        if (accuracy > 5) {
          Attack b = new Attack(enemyAttackName,
                                AttackLibrary.getType(enemyAttackName),
                                10,
                                AttackLibrary.getAttackDamage(enemyAttackName));
          b.giveDamage(enemy, myPoke, b.getAttackDamage()); //actually attack the enemy
        }
        //a.giveDamage(enemy,myPoke);
        enemyMove = 1;
        enemyIsAttacking = false;

        OptionsNavigationHelper.toBattle();
      }
      if (myPoke.isFainted() == true) {
        //put image somewhere outside of the screen
        myPoke.setCurrentHP(0);

        // attackX = 300;
        // attackY = 300;
        isAttacking = false;
        enemyIsAttacking = false;

        int pokeLeft = 0;
        for (int i = 0; i < 6; i++) {
          if (PlayPanel.myPokemon[i] == null)
            break;
          if (!PlayPanel.myPokemon[i].isFainted())
            pokeLeft++;
        }
        if (pokeLeft == 0) {
          resetOrderOfPokemonInParty();
          OptionsNavigationHelper.toNormal();
        } else {
          controlPanel.options.toSwitchPokemon();
        }
      }
    }
  }

  public void saveParty() {
    for (int i = 0; i < normalParty.length; i++)
      normalParty[i] = PlayPanel.myPokemon[i];
  }

  public void resetOrderOfPokemonInParty() {
    for (int i = 0; i < normalParty.length; i++)
      PlayPanel.myPokemon[i] = normalParty[i];
  }

  public void toBattle() {
    canMove = false;
    inBattle = true;

    saveParty();
    //gets first usable pokemon
    int myPokeInd = -1;
    for (int i = 0; i < 6; i++) {
      if (PlayPanel.myPokemon[i] == null)
        continue;
      if (PlayPanel.myPokemon[i].isFainted())
        continue;
      myPokeInd = i;
      break;
    }
    if (myPokeInd < 0) //error message
    {
      JOptionPane.showMessageDialog(null,
                                    "How in the world were you allowed into the wild with no " +
                                        "usable pokemon???\nNow go rant the game makers.");
      System.exit(0);
    }
    Pokemon temp = PlayPanel.myPokemon[myPokeInd];
    PlayPanel.myPokemon[myPokeInd] = PlayPanel.myPokemon[0];
    PlayPanel.myPokemon[0] = temp;

    myPoke = PlayPanel.myPokemon[0];


    enemy = new Pokemon("Raikou",
                        "Electric",
                        "Tackle",
                        "Shock",
                        "Thunder",
                        "Headbutt",
                        50,
                        0,
                        55,
                        30,
                        150,
                        150); //default
    setRandomEnemy();
    enemyIcon = new ImageIcon("./resources/images/Pokemon/" + enemy.getName() + ".png");

    //pokedex:
    controlPanel.hasSeenPokemon[Pokemon.getIndex(enemy.getName())] = true;

    attackX = 5;
    attackY = 80;
    attackXEnemy = 170;
    attackYEnemy = 1;
  }

  public String[] loadEnemyArray()  //Naveen
  {
    Scanner infile = null;

    try {
      infile = new Scanner(new File("./resources/enemies.txt"));
    } catch (FileNotFoundException e) {
      JOptionPane.showMessageDialog(null, "Error: File not found.");
      System.exit(0);
    }

    int count = Integer.parseInt(infile.nextLine());
    String[] enemyArray = new String[count];

    for (int enemyIndex = 0; enemyIndex < enemyArray.length; enemyIndex++)
      enemyArray[enemyIndex] = infile.nextLine();
    return enemyArray;
  }

  public void setRandomEnemy() //Naveen
  {
    String name = "";
    String type = "";
    String a1 = "";
    String a2 = "";
    String a3 = "";
    String a4 = "";
    String[] enemyArray = loadEnemyArray();

    int enemyUsed = (int) (Math.random() * wildPokemonArray.length);//enemyArray.length);

    //String[] enemyInfo = enemyArray[enemyUsed].split("\\.");
    String[] enemyInfo = wildPokemonArray[enemyUsed].split("\\.");

    name = enemyInfo[0];
    type = enemyInfo[1];
    a1 = enemyInfo[2];
    a2 = enemyInfo[3];
    a3 = enemyInfo[4];
    a4 = enemyInfo[5];

    int minLevel = Integer.parseInt(enemyInfo[6]);
    int maxLevel = Integer.parseInt(enemyInfo[7]);

    enemy.setName(name);
    enemy.setType(type);
    int level = (int) (Math.random() * (maxLevel - minLevel + 1) + minLevel);
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

    enemy.setAttackOne(a1);
    enemy.setAttackTwo(a2);
    enemy.setAttackThree(a3);
    enemy.setAttackFour(a4);
  }

  public Color getPokemonHealthBarColor(Pokemon poke) {
    if (1.0 * poke.getCurrentHP() / poke.getMaxHP() < .25)
      return Color.RED;
    if (1.0 * poke.getCurrentHP() / poke.getMaxHP() < .5)
      return Color.YELLOW;
    return Color.GREEN;
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
    for (int r = 0; r < height; r++)
      for (int c = 0; c < width; c++) {
        buildings[r][c] = false;
        items[r][c] = false;
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