package pokemon;//David Zhao, 3/31/2011

import pokemon.entities.PokeBall;
import pokemon.entities.Pokemon;
import pokemon.ui.OptionBoard;

import javax.swing.*;
import java.awt.*;
import java.awt.image.*;
import java.awt.event.*;

public class OptionsPanel extends JPanel {
  private Graphics myBuffer;
  private BufferedImage myImage;
  private Color background;
  private final int WIDTH = 275;
  private final int HEIGHT = 215;

  public static boolean inBattle;
  // private boolean isAttacking;
  public static boolean usingItem;
  public static boolean switchingPokemon;

  private GameScreen game;
  static final int normalPanel = 0;
  static final int battlePanel = 1;
  static final int switchPokemonPanel = 2;
  static final int partyPanel = 3;
  static final int useItemPanel = 4;
  static final int pokedexPanel = 5;
  static final int bagPanel = 6;
  static final int savePanel = 7;
  static final int personalPanel = 8;
  static final int attackSelectionPanel = 9;
  static final int blackPanel = 10; //added by david
  static int curPanel;

  static boolean showPokemonSelectOptions = false;
  static int switchPokemonInd = -1;
  static int pokedexStartInd;
  static int numPokemonSeen = 0;
  static int numPokemonOwned = 0;
  static String partyText = "";

  OptionBoard[] normalOptions;
  OptionBoard[] pokemonOptions;
  OptionBoard[] pokemonSelectOptions; //shows once you click on a pokemon in the party
  OptionBoard[] pokedexOptions;
  OptionBoard[] battleOptions;
  OptionBoard[] attackOptions;
  OptionBoard[] bagOptions;
  OptionBoard[] saveOptions;
  OptionBoard[] personalOptions;
  Timer t;

  PokeBall p = new PokeBall();

  public void toSwitchPokemon() {
    OptionsPanel.curPanel = OptionsPanel.switchPokemonPanel;
    OptionsPanel.switchingPokemon = true;
    toParty();

    GameScreen.canMove = false;
  }

  public void toParty() {
    for (int i = 0; i < 6; i++) {
      if (PlayPanel.myPokemon[i] == null)
        pokemonOptions[i].showHighlight(false);
      else
        pokemonOptions[i].setText(PlayPanel.myPokemon[i].getName());
    }
    OptionsPanel.curPanel = OptionsPanel.partyPanel;
    OptionsPanel.partyText = "Select a pokemon.entities.Pokemon.";

    GameScreen.canMove = false;
  }

  public OptionsPanel(GameScreen panel) {
    game = panel;

    myImage = new BufferedImage(WIDTH, HEIGHT, BufferedImage.TYPE_INT_RGB);
    myBuffer = myImage.getGraphics();
    background = Color.BLACK;

    inBattle = usingItem = switchingPokemon = false;

    //normal game options:
    //instantiate game buttons which are actually optionboards
    normalOptions = new OptionBoard[6];
    // for(int i = 0; i < normalOptions.length; i++)
    normalOptions[0] = new OptionBoard(10, 10, 100, 35, Color.GREEN, "Pokemon");
    normalOptions[1] = new OptionBoard(150, 10, 100, 35, Color.GREEN, "Pokedex");
    normalOptions[2] = new OptionBoard(10, 60, 100, 35, Color.GREEN, PlayPanel.myName);
    normalOptions[3] = new OptionBoard(150, 60, 100, 35, Color.GREEN, "Bag");
    normalOptions[4] = new OptionBoard(10, 110, 100, 35, Color.GREEN, "Save");
    normalOptions[5] = new OptionBoard(150, 110, 100, 35, Color.GREEN, "Options");

    pokemonOptions = new OptionBoard[7];
    pokemonOptions[0] = new OptionBoard(10, 10, 120, 45, Color.WHITE, "", OptionBoard.Y_HIGH);
    pokemonOptions[1] = new OptionBoard(135, 10, 120, 45, Color.WHITE, "", OptionBoard.Y_HIGH);
    pokemonOptions[2] = new OptionBoard(10, 65, 120, 45, Color.WHITE, "", OptionBoard.Y_HIGH);
    pokemonOptions[3] = new OptionBoard(135, 65, 120, 45, Color.WHITE, "", OptionBoard.Y_HIGH);
    pokemonOptions[4] = new OptionBoard(10, 120, 120, 45, Color.WHITE, "", OptionBoard.Y_HIGH);
    pokemonOptions[5] = new OptionBoard(135, 120, 120, 45, Color.WHITE, "", OptionBoard.Y_HIGH);
    pokemonOptions[6] = new OptionBoard(WIDTH - 60, HEIGHT - 40, 50, 35, Color.BLUE, "Back");

    pokedexOptions = new OptionBoard[10];
    pokedexOptions[0] = new OptionBoard(10, 10, 120, 20, Color.WHITE, "");
    pokedexOptions[1] = new OptionBoard(10, 40, 120, 20, Color.WHITE, "");
    pokedexOptions[2] = new OptionBoard(10, 70, 120, 20, Color.WHITE, "");
    pokedexOptions[3] = new OptionBoard(10, 100, 120, 20, Color.WHITE, "");
    pokedexOptions[4] = new OptionBoard(10, 130, 120, 20, Color.WHITE, "");
    pokedexOptions[5] = new OptionBoard(10, 160, 120, 20, Color.WHITE, "");
    pokedexOptions[6] = new OptionBoard(10, 190, 120, 20, Color.WHITE, "");
    pokedexOptions[7] = new OptionBoard(WIDTH - 130, HEIGHT - 60, 50, 20, Color.WHITE, "Up");
    pokedexOptions[8] = new OptionBoard(WIDTH - 130, HEIGHT - 30, 50, 20, Color.WHITE, "Down");
    pokedexOptions[9] = new OptionBoard(WIDTH - 70, HEIGHT - 60, 50, 50, Color.WHITE, "Back");


    battleOptions = new OptionBoard[4];
    battleOptions[0] = new OptionBoard(WIDTH - 260, HEIGHT - 45, 70, 35, Color.WHITE, "Bag");
    battleOptions[1] = new OptionBoard(WIDTH - 205, HEIGHT - 195, 150, 50, Color.WHITE, "Fight");
    battleOptions[2] = new OptionBoard(WIDTH - 170, HEIGHT - 45, 70, 35, Color.WHITE, "Run");
    battleOptions[3] = new OptionBoard(WIDTH - 85, HEIGHT - 45, 70, 35, Color.WHITE, "Pokemon");

    attackOptions = new OptionBoard[5];
    attackOptions[0] = new OptionBoard(WIDTH - 245, HEIGHT - 195, 70, 35, Color.WHITE, "1");
    attackOptions[1] = new OptionBoard(WIDTH - 245, HEIGHT - 115, 70, 35, Color.WHITE, "2");
    attackOptions[2] = new OptionBoard(WIDTH - 165, HEIGHT - 195, 70, 35, Color.WHITE, "3");
    attackOptions[3] = new OptionBoard(WIDTH - 165, HEIGHT - 115, 70, 35, Color.WHITE, "4");
    attackOptions[4] = new OptionBoard(WIDTH - 60, HEIGHT - 45, 50, 35, Color.WHITE, "Back");

    bagOptions = new OptionBoard[2];
    bagOptions[0] = new OptionBoard(WIDTH - 60, HEIGHT - 45, 50, 35, Color.WHITE, "Back");
    bagOptions[1] = new OptionBoard(WIDTH - 245, HEIGHT - 195, 70, 35, Color.WHITE, "Pokeball");

    pokemonSelectOptions = new OptionBoard[4];
    pokemonSelectOptions[0] = new OptionBoard(0, 0, 50, 20, Color.GREEN, "Move");
    pokemonSelectOptions[1] = new OptionBoard(-10, -10, 0, 0, Color.BLACK, "dud");
    pokemonSelectOptions[2] = new OptionBoard(0, 0, 50, 20, Color.GREEN, "Summary");
    pokemonSelectOptions[3] = new OptionBoard(0, 0, 50, 20, Color.GREEN, "Back");

    saveOptions = new OptionBoard[1];
    saveOptions[0] = new OptionBoard(20, 20, WIDTH - 50, HEIGHT - 50, Color.lightGray, "");

    personalOptions = new OptionBoard[1];
    personalOptions[0] = new OptionBoard(20, 20, WIDTH - 50, HEIGHT - 50, Color.ORANGE, "");

    curPanel = normalPanel;

    t = new Timer(10, new RefreshListener());
    t.start();
  }

  public void updateHighlightsForOptions(int x, int y) {
    if (curPanel == partyPanel && !showPokemonSelectOptions) {
      // can come here when viewing pokemon, in battle and switching out pokemon
      for (int i = 0; i < pokemonOptions.length; i++) {
        pokemonOptions[i].showHighlight(
            isMouseOver(x, y, pokemonOptions[i]) && shouldShowHighlightForPokemon(i));
      }
    } else {
      updateHighlights(x, y, getOptionsForCurrentPanel());
    }
  }

  private OptionBoard[] getOptionsForCurrentPanel() {
    switch (curPanel) {
      case normalPanel:
        return normalOptions;
      case partyPanel:
        return showPokemonSelectOptions ? pokemonSelectOptions : pokemonOptions;
      case battlePanel:
        return battleOptions;
      case pokedexPanel:
        return pokedexOptions;
      case attackSelectionPanel:
        return attackOptions;
      case useItemPanel:
        return bagOptions;
      case savePanel:
        return saveOptions;
      case personalPanel:
        return personalOptions;
      default:
        AlertHelper.fatal("Unrecognized panel id:" + curPanel);
    }
    return null;
  }

  private boolean shouldShowHighlightForPokemon(int index) {
    // Always show highlight for the last option (Back button).
    // If it's a pokemon, then always highlight if not in a battle.
    // If we're in battle then only highlight if pokemon hasn't fainted.
    return index == pokemonOptions.length - 1
           || (PlayPanel.myPokemon[index] != null
               && (!GameScreen.inBattle || !PlayPanel.myPokemon[index].isFainted()));
  }

  private void updateHighlights(int x, int y, OptionBoard[] boards) {
    for (OptionBoard board : boards) {
      board.showHighlight(isMouseOver(x, y, board));
    }
  }

  private boolean isMouseOver(int x, int y, OptionBoard board) {
    // HEIGHT displacement since two screens are stacked on top of each other
    return (board.getX() <= x && x <= board.getX() + board.getXWidth())
           && (board.getY() + HEIGHT <= y && y <= board.getY() + board.getYWidth() + HEIGHT);
  }

  public void checkClick(int x, int y) {
    // click is valid if target is showing highlight aka enabled
    for (OptionBoard option : getOptionsForCurrentPanel()) {
      if (option.isHighlighted() && isMouseOver(x, y, option)) {
        searchButton(option.getText());
      }
    }
  }

  //where the click code is run
  public void searchButton(String s) {
    if (curPanel == normalPanel) {
      if (s.equals("pokemon.entities.Pokemon")) {
        toParty();
      } else if (s.equals("Pokedex"))
        OptionsNavigationHelper.toPokedex();
      else if (s.equals("Save")) {
        OptionsNavigationHelper.toSave();
      } else if (s.equals(PlayPanel.myName)) {
        OptionsNavigationHelper.toPersonal();
      } else if (s.equals("Bag"))
        AlertHelper.alert("Coming Soon");
      else if (s.equals("Options"))
        AlertHelper.alert("Coming Soon");
    } else if (curPanel == partyPanel) {
      if (s.equals("Back")) //back button inside the "looking" at party
      {
        if (!showPokemonSelectOptions) {
          if (!switchingPokemon) //just looking
            OptionsNavigationHelper.toNormal();
          else // in battle and switching out a pokemon
            OptionsNavigationHelper.toBattle();
        } else {
          showPokemonSelectOptions = false;
          toParty();
        }
      } else if (switchingPokemon)
        for (int i = 0; i < 6; i++) {
          if (PlayPanel.myPokemon[i] != null)
            if (PlayPanel.myPokemon[i].getName().equals(s)) {
              //switch
              if (GameScreen.inBattle) {
                if (i == 0)
                  //pokemon.AlertHelper.alert("That pokemon is already out!");
                  partyText = "That pokemon is already out!";
                else {
                  switchPokemonInd = i;
                  game.swapPokemon = true;
                  Pokemon temp = PlayPanel.myPokemon[i];
                  PlayPanel.myPokemon[i] = PlayPanel.myPokemon[0];
                  PlayPanel.myPokemon[0] = temp;
                }
                // break;
              } else//not in battle, "looking mode"
              {
                Pokemon temp = PlayPanel.myPokemon[i];
                PlayPanel.myPokemon[i] = PlayPanel.myPokemon[switchPokemonInd];
                PlayPanel.myPokemon[switchPokemonInd] = temp;


                pokemonOptions[i].setText(PlayPanel.myPokemon[i].getName());
                pokemonOptions[switchPokemonInd].setText(PlayPanel.myPokemon[switchPokemonInd].getName());

                switchingPokemon = false;
                break; //once it finds the right tile, it exits the search to save time
              }
            }
        }
      else if (showPokemonSelectOptions) {
        if (s.equals("Move")) {
          switchingPokemon = true;
          showPokemonSelectOptions = false;
        } else if (s.equals("Summary")) {
          OptionsNavigationHelper.toSummary(switchPokemonInd);
        } else if (s.equals("Back")) {
          showPokemonSelectOptions = false;
          toParty();
        }
      } else //clicked on one of the pokemon tiles in the regular(?) party "looking" mode
      {
        showPokemonSelectOptions = true;
        for (int i = 0; i < 6; i++) {
          if (PlayPanel.myPokemon[i] != null)
            if (PlayPanel.myPokemon[i].getName().equals(s)) {
              showPokemonSelectOptions = true;
              switchPokemonInd = i;

              pokemonSelectOptions[0].setX(
                  pokemonOptions[i].getX() + pokemonOptions[i].getXWidth() - pokemonSelectOptions[0]
                      .getXWidth());
              pokemonSelectOptions[0].setY(pokemonOptions[i].getY());
              pokemonSelectOptions[2].setX(
                  pokemonOptions[i].getX() + pokemonOptions[i].getXWidth() - pokemonSelectOptions[2]
                      .getXWidth());
              pokemonSelectOptions[2].setY(
                  pokemonSelectOptions[0].getY() + pokemonSelectOptions[0].getYWidth() + 5);
              pokemonSelectOptions[3].setX(
                  pokemonOptions[i].getX() + pokemonOptions[i].getXWidth() - pokemonSelectOptions[3]
                      .getXWidth());
              pokemonSelectOptions[3].setY(
                  pokemonSelectOptions[2].getY() + pokemonSelectOptions[2].getYWidth() + 5);

              break;
            }
        }
      }
    } else if (curPanel == pokedexPanel) {
      if (s.equals("Up")) {
        pokedexStartInd -= pokedexOptions.length - 3;
        if (pokedexStartInd < 1)
          pokedexStartInd = 1;
      } else if (s.equals("Down")) {
        pokedexStartInd += pokedexOptions.length - 3;
        if (pokedexStartInd + pokedexOptions.length - 3 > PlayPanel.hasSeenPokemon.length)
          pokedexStartInd = PlayPanel.hasSeenPokemon.length - pokedexOptions.length + 4;
      } else if (s.equals("Back")) {
        OptionsNavigationHelper.toNormal();
      } else //click on a pokemon tile
      {

      }

    } else if (curPanel == battlePanel) {
      if (s.equals("Fight"))
        OptionsNavigationHelper.toAttacks();
      if (s.equals("Bag"))
        OptionsNavigationHelper.toBag();
      if (s.equals("pokemon.entities.Pokemon"))
        toSwitchPokemon();
      if (s.equals("Run")) {
        game.resetOrderOfPokemonInParty();
        OptionsNavigationHelper.toNormal();
      }
    } else if (curPanel == attackSelectionPanel) {
      if (s.equals(game.myPoke.getAttackOne())) {
        GameScreen.isAttacking = true;
        GameScreen.myAttackName = game.myPoke.getAttackOne();
      } else if (s.equals(game.myPoke.getAttackTwo())) {
        GameScreen.isAttacking = true;
        GameScreen.myAttackName = game.myPoke.getAttackTwo();
      } else if (s.equals(game.myPoke.getAttackThree())) {
        GameScreen.isAttacking = true;
        GameScreen.myAttackName = game.myPoke.getAttackThree();
      } else if (s.equals(game.myPoke.getAttackFour())) {
        GameScreen.isAttacking = true;
        GameScreen.myAttackName = game.myPoke.getAttackFour();
      } else if (s.equals("Back"))
        OptionsNavigationHelper.toBattle();
    } else if (curPanel == useItemPanel) {
      if (s.equals("Back"))
        OptionsNavigationHelper.toBattle();
      else if (s.equals("Pokeball")) {

        if (!PlayPanel.hasRepeat()) {
          if (p.isCaught()) {
            int index = 0;
            for (int occupy = 0; occupy < PlayPanel.myPokemon.length; occupy++)
              if (PlayPanel.myPokemon[occupy] == null) {
                index = occupy;
                break;
              }
            PlayPanel.hasPokemon[Pokemon.getIndex(game.enemy.getName())] = true;
            PlayPanel.hasSeenPokemon[Pokemon.getIndex(game.enemy.getName())] = true;

            PlayPanel.myPokemon[index] = new Pokemon.Builder()
                .setName(game.enemy.getName())
                .setType(game.enemy.getType())
                .setFirstAttack(game.enemy.getAttackOne())
                .setSecondAttack(game.enemy.getAttackTwo())
                .setThirdAttack(game.enemy.getAttackThree())
                .setFourthAttack(game.enemy.getAttackFour())
                .setLevel(game.enemy.getLevel())
                .setExp(game.enemy.getMyEXP())
                .setAttack(game.enemy.getAttackLevel())
                .setDefense(game.enemy.getDefenseLevel())
                .setHp(game.enemy.getCurrentHP())
                .setMaxHp(game.enemy.getMaxHP())
                .build();
            OptionsNavigationHelper.toNormal();
          } else {
            AlertHelper.alert("Awww, the pokemon.entities.Pokemon broke out of the pokeball!");
            OptionsNavigationHelper.toBlack();
            GameScreen.isAttacking = false;
            game.enemyIsAttacking = true;
          }
        } else
          JOptionPane.showMessageDialog(null,
                                        "You've already caught this pokemon.entities.Pokemon!\nSave your " +
                                        "pokeballs!");
      }

    } else if (curPanel == savePanel) {
      if (s.equals(""))
        OptionsNavigationHelper.toNormal();
    } else if (curPanel == personalPanel) {
      if (s.equals(""))
        OptionsNavigationHelper.toNormal();
    }
  }

  public void paintComponent(Graphics g) {
    g.drawImage(myImage, 0, 0, WIDTH, HEIGHT, null);
  }

  public class RefreshListener implements ActionListener {
    public void actionPerformed(ActionEvent e) {
      myBuffer.setColor(background);
      myBuffer.fillRect(0, 0, WIDTH, HEIGHT);
      // todo: move set font code to the specific panels
      myBuffer.setFont(GameScreen.normalFont);

      if (curPanel == blackPanel) {
        myBuffer.setColor(Color.BLACK);
        myBuffer.fillRect(0, 0, WIDTH, HEIGHT);
        repaint();
        return;
      } else if (curPanel == partyPanel) {
        drawOptions();
        drawPokemonPartyScreen();
        if (showPokemonSelectOptions) {
          drawOptions();
        }
        repaint();
        return;
      }

      drawOptions();

      // draw special overlays
      if (curPanel == pokedexPanel) {
        drawPokedexScreen();
      } else if (curPanel == savePanel) {
        drawSaveScreen();
      } else if (curPanel == personalPanel) {
        drawPersonalScreen();
      } else if (curPanel == attackSelectionPanel) {
        // todo: move updateAttackOptions to wherever init code should go
        updateAttackOptions();
        drawOptions();
      }

      repaint();
    }

  }

  private void drawPokemonPartyScreen() {
    // text + info box:
    myBuffer.setColor(Color.WHITE);
    myBuffer.fillRect(10, HEIGHT - 40, WIDTH - 60 - 10 - 10, 35);
    myBuffer.setColor(Color.BLACK);
    myBuffer.drawString(partyText, 10 + 5, HEIGHT - 40 + 10);
    //tiles w/ pokemon:
    for (int i = 0; i < pokemonOptions.length; i++) {
      pokemonOptions[i].draw(myBuffer);
      //pokemon health bar
      if (PlayPanel.myPokemon[i] != null) {

        int x_inc = 27;
        int y_inc = 15;

        if (PlayPanel.myPokemon[i].isFainted()) {
          pokemonOptions[i].setColor(Color.ORANGE);
          myBuffer.drawString("FNT",
                              pokemonOptions[i].getX() + 2,
                              pokemonOptions[i].getY() + pokemonOptions[i].getYWidth() - 3);
        } else
          pokemonOptions[i].setColor(Color.WHITE);


        //bar outline
        myBuffer.setColor(Color.BLACK);
        myBuffer.drawRect(pokemonOptions[i].getX() + x_inc - 1,
                          pokemonOptions[i].getY() + y_inc + 1,
                          51,
                          6);
        //hp bar
        Color c;
        if ((1.0 * PlayPanel.myPokemon[i].getCurrentHP()) / PlayPanel.myPokemon[i].getMaxHP()
            > .5)
          c = Color.GREEN;
        else if (
            (1.0 * PlayPanel.myPokemon[i].getCurrentHP()) / PlayPanel.myPokemon[i].getMaxHP()
            > .25)
          c = Color.YELLOW;
        else
          c = Color.RED;
        myBuffer.setColor(c);
        myBuffer.fillRect(
            pokemonOptions[i].getX() + x_inc,
            pokemonOptions[i].getY() + y_inc + 2,
            (int) ((50) * (1.0 * PlayPanel.myPokemon[i].getCurrentHP()
                           / PlayPanel.myPokemon[i]
                               .getMaxHP())),
            5);
        //hp text
        myBuffer.setFont(GameScreen.smallFont);
        myBuffer.setColor(Color.BLACK);
        myBuffer.drawString(
            "HP:",
            pokemonOptions[i].getX() + x_inc - 12,
            pokemonOptions[i].getY() + y_inc + 7);
        myBuffer.setFont(GameScreen.normalFont);
        myBuffer.drawString(
            PlayPanel.myPokemon[i].getCurrentHP() + "/" + PlayPanel.myPokemon[i]
                .getMaxHP(),
            pokemonOptions[i].getX() + x_inc,
            pokemonOptions[i].getY() + y_inc + 17);
        //Level text:
        myBuffer.drawString(
            "LV: " + PlayPanel.myPokemon[i].getLevel(),
            pokemonOptions[i].getX() + x_inc,
            pokemonOptions[i].getY() + pokemonOptions[i].getYWidth() - 3);
        //Image
        ImageIcon pokeImage =
            new ImageIcon("images/Pokemon/" + PlayPanel.myPokemon[i].getName() + ".png");
        myBuffer.drawImage(
            pokeImage.getImage(),
            pokemonOptions[i].getX() + pokemonOptions[i].getXWidth() - 40,
            pokemonOptions[i].getY(),
            40,
            40,
            null);
      }
    }
  }

  private void drawPokedexScreen() {
    // has seen/ has owned info box :
    myBuffer.setColor(Color.WHITE);
    myBuffer.fillRect(WIDTH - 130, HEIGHT - 100, 110, 30);
    myBuffer.setFont(GameScreen.normalFont);
    myBuffer.setColor(Color.BLACK);
    myBuffer.drawString("Seen: " + numPokemonSeen, WIDTH - 125, HEIGHT - 80);
    myBuffer.drawString("Caught: " + numPokemonOwned, WIDTH - 70, HEIGHT - 80);

    for (int i = 0; i < pokedexOptions.length; i++) { //-3 to leave out up, down, and back
      if (i < pokedexOptions.length - 3) {
        //number
        String numText = "";
        if (pokedexStartInd + i + 1 < 100) //+1 for array ind
          numText += "0";
        if (pokedexStartInd + i + 1 < 10)
          numText += "0";
        //text
        if (PlayPanel.hasSeenPokemon[pokedexStartInd + i - 1])
          pokedexOptions[i].setText(
              numText + (pokedexStartInd + i) + ". \t" + Pokemon.getPokemon(
                  pokedexStartInd + i - 1));
        else
          pokedexOptions[i].setText(numText + (pokedexStartInd + i) + ". \t?????");

        //image of pokemon you're hovering over
        if (PlayPanel.hasSeenPokemon[pokedexStartInd + i - 1])
          if (pokedexOptions[i].isHighlighted()) {
            ImageIcon curImage = new ImageIcon("images/Pokemon/" + Pokemon.getPokemon(
                pokedexStartInd + i - 1) + ".png");
            myBuffer.drawImage(curImage.getImage(), WIDTH - 130, HEIGHT - 190, null);
          }
        //pokeballs next to pokemon you have caught
        ImageIcon pokeball = new ImageIcon("images/pokeball.png"); //move to imageLibrary?
        if (PlayPanel.hasPokemon[pokedexStartInd + i - 1])
          myBuffer.drawImage(
              pokeball.getImage(),
              pokedexOptions[i].getX() + pokedexOptions[i].getXWidth() - 20,
              pokedexOptions[i].getY(),
              20,
              20,
              null);
      }
    }
  }

  private void drawSaveScreen() {
    ImageIcon me = new ImageIcon("images/boy_walk_down_rest.png");
    myBuffer.drawImage(me.getImage(), 30, 30, WIDTH - 240, HEIGHT - 180, null);
    myBuffer.setColor(Color.BLACK);
    myBuffer.setFont(GameScreen.extraLargeFont);
    myBuffer.drawString("Name: " + PlayPanel.myName, WIDTH - 180, HEIGHT - 160);
    myBuffer.setFont(GameScreen.largerLargeFont);
    myBuffer.drawString("Money: " + PlayPanel.myMoney, WIDTH - 250, HEIGHT - 120);
    myBuffer.drawString("Number of pokemon.entities.Pokemon Seen: " + numPokemonSeen, WIDTH - 250, HEIGHT - 95);
    myBuffer.drawString(
        "Number of pokemon.entities.Pokemon Caught: " + numPokemonOwned, WIDTH - 250, HEIGHT - 70);
    myBuffer.drawString("Location: " + game.mapName, WIDTH - 250, HEIGHT - 45);
  }

  private void drawPersonalScreen() {
    ImageIcon me = new ImageIcon("images/boy_walk_down_rest.png");
    myBuffer.drawImage(me.getImage(), 30, 30, WIDTH - 240, HEIGHT - 180, null);
    myBuffer.setColor(Color.BLACK);
    myBuffer.setFont(GameScreen.extraLargeFont);
    myBuffer.drawString("Name: " + PlayPanel.myName, WIDTH - 180, HEIGHT - 160);
    myBuffer.setFont(GameScreen.largerLargeFont);
    myBuffer.drawString("Money: " + PlayPanel.myMoney, WIDTH - 250, HEIGHT - 120);
    myBuffer.drawString("Number of pokemon.entities.Pokemon Seen: " + numPokemonSeen,
                        WIDTH - 250,
                        HEIGHT - 95);
    myBuffer.drawString("Number of pokemon.entities.Pokemon Caught: " + numPokemonOwned,
                        WIDTH - 250,
                        HEIGHT - 70);
  }

  private void updateAttackOptions() {
    for (int i = 0; i < attackOptions.length; i++) {
      String type = AttackLibrary.getType(attackOptions[i].getText());
      Color c = Color.WHITE;
      if (type.equals("Fire")) {
        c = Color.RED;
      } else if (type.equals("Grass")) {
        c = Color.GREEN;
      } else if (type.equals("Water")) {
        c = Color.BLUE.brighter();
      } else if (type.equals("Normal")) {
        c = Color.GRAY.brighter();
      }
      attackOptions[i].setColor(c);

      if (i == 0) {
        attackOptions[i].setText("" + game.myPoke.getAttackOne());
      } else if (i == 1) {
        attackOptions[i].setText("" + game.myPoke.getAttackTwo());
      } else if (i == 2) {
        attackOptions[i].setText("" + game.myPoke.getAttackThree());
      } else
        attackOptions[i].setText("" + game.myPoke.getAttackFour());
    }
  }

  private void drawOptions() {
    for (OptionBoard option : getOptionsForCurrentPanel()) {
      option.draw(myBuffer);
    }
  }
}