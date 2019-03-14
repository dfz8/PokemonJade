package pokemon;

import pokemon.entities.PokeBall;
import pokemon.entities.Pokemon;
import pokemon.ui.OptionBoard;

import javax.swing.*;
import java.awt.*;
import java.awt.event.ActionEvent;
import java.awt.event.ActionListener;
import java.awt.image.BufferedImage;

public class OptionsPanel extends JPanel {
  private Graphics myBuffer;
  private BufferedImage myImage;
  private Color background;

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

  // todo refactor each into a controller
  OptionBoard[] normalOptions;
  OptionBoard[] pokemonOptions;
  OptionBoard[] pokemonSelectOptions;
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
    OptionsPanel.partyText = "Select a Pokemon.";

    GameScreen.canMove = false;
  }

  public OptionsPanel(GameScreen panel) {
    game = panel;

    myImage = new BufferedImage(GameDriver.SCREEN_WIDTH,
                                GameDriver.SCREEN_HEIGHT,
                                BufferedImage.TYPE_INT_RGB);
    myBuffer = myImage.getGraphics();
    background = Color.BLACK;
    inBattle = usingItem = switchingPokemon = false;

    normalOptions = OptionsHelper.setUpFor(OptionsHelper.Menu.main);
    pokemonOptions = OptionsHelper.setUpFor(OptionsHelper.Menu.party);
    pokedexOptions = OptionsHelper.setUpFor(OptionsHelper.Menu.pokedex);
    battleOptions = OptionsHelper.setUpFor(OptionsHelper.Menu.battle);
    attackOptions = OptionsHelper.setUpFor(OptionsHelper.Menu.attacks);
    bagOptions = OptionsHelper.setUpFor(OptionsHelper.Menu.bag);
    pokemonSelectOptions = OptionsHelper.setUpFor(OptionsHelper.Menu.pokemonSelect);
    saveOptions = OptionsHelper.setUpFor(OptionsHelper.Menu.save);
    personalOptions = OptionsHelper.setUpFor(OptionsHelper.Menu.personal);

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
    return (board.getX() <= x && x <= board.getX() + board.getWidth())
           && (board.getY() + GameDriver.SCREEN_HEIGHT <= y
               && y <= board.getY() + board.getHeight() + GameDriver.SCREEN_HEIGHT);
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
      if (s.equals("Pokemon")) {
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
                  pokemonOptions[i].getX() + pokemonOptions[i].getWidth() - pokemonSelectOptions[0]
                      .getWidth());
              pokemonSelectOptions[0].setY(pokemonOptions[i].getY());
              pokemonSelectOptions[2].setX(
                  pokemonOptions[i].getX() + pokemonOptions[i].getWidth() - pokemonSelectOptions[2]
                      .getWidth());
              pokemonSelectOptions[2].setY(
                  pokemonSelectOptions[0].getY() + pokemonSelectOptions[0].getHeight() + 5);
              pokemonSelectOptions[3].setX(
                  pokemonOptions[i].getX() + pokemonOptions[i].getWidth() - pokemonSelectOptions[3]
                      .getWidth());
              pokemonSelectOptions[3].setY(
                  pokemonSelectOptions[2].getY() + pokemonSelectOptions[2].getHeight() + 5);

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
      if (s.equals("Pokemon"))
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
            AlertHelper.alert("Awww, the pokemon broke out of the pokeball!");
            OptionsNavigationHelper.toBlack();
            GameScreen.isAttacking = false;
            game.enemyIsAttacking = true;
          }
        } else
          AlertHelper.alert("You've already caught this Pokemon!\nSave your pokeballs!");
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
    g.drawImage(myImage, 0, 0, GameDriver.SCREEN_WIDTH, GameDriver.SCREEN_HEIGHT, null);
  }

  public class RefreshListener implements ActionListener {
    public void actionPerformed(ActionEvent e) {
      myBuffer.setColor(background);
      myBuffer.fillRect(0, 0, GameDriver.SCREEN_WIDTH, GameDriver.SCREEN_HEIGHT);
      // todo: move set font code to the specific panels
      myBuffer.setFont(GameScreen.normalFont);

      if (curPanel == blackPanel) {
        myBuffer.setColor(Color.BLACK);
        myBuffer.fillRect(0, 0, GameDriver.SCREEN_WIDTH, GameDriver.SCREEN_HEIGHT);
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
    myBuffer.fillRect(10,
                      GameDriver.SCREEN_HEIGHT - 40,
                      GameDriver.SCREEN_WIDTH - 60 - 10 - 10,
                      35);
    myBuffer.setColor(Color.BLACK);
    myBuffer.drawString(partyText, 10 + 5, GameDriver.SCREEN_HEIGHT - 40 + 10);
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
                              pokemonOptions[i].getY() + pokemonOptions[i].getHeight() - 3);
        } else
          pokemonOptions[i].setColor(Color.WHITE);

        DrawingHelper.drawHalfSizeHPBar(
            myBuffer,
            PlayPanel.myPokemon[i],
            pokemonOptions[i].getX() + x_inc,
            pokemonOptions[i].getY() + y_inc + 2);

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
            pokemonOptions[i].getY() + pokemonOptions[i].getHeight() - 3);
        //Image
        ImageIcon pokeImage =
            new ImageIcon("images/Pokemon/" + PlayPanel.myPokemon[i].getName() + ".png");
        myBuffer.drawImage(
            pokeImage.getImage(),
            pokemonOptions[i].getX() + pokemonOptions[i].getWidth() - 40,
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
    myBuffer.fillRect(GameDriver.SCREEN_WIDTH - 130, GameDriver.SCREEN_HEIGHT - 100, 110, 30);
    myBuffer.setFont(GameScreen.normalFont);
    myBuffer.setColor(Color.BLACK);
    myBuffer.drawString("Seen: " + numPokemonSeen,
                        GameDriver.SCREEN_WIDTH - 125,
                        GameDriver.SCREEN_HEIGHT
                        - 80);
    myBuffer.drawString("Caught: " + numPokemonOwned,
                        GameDriver.SCREEN_WIDTH - 70,
                        GameDriver.SCREEN_HEIGHT
                        - 80);

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
            myBuffer.drawImage(curImage.getImage(),
                               GameDriver.SCREEN_WIDTH - 130,
                               GameDriver.SCREEN_HEIGHT
                               - 190,
                               null);
          }
        //pokeballs next to pokemon you have caught
        ImageIcon pokeball = new ImageIcon("images/pokeball.png"); //move to imageLibrary?
        if (PlayPanel.hasPokemon[pokedexStartInd + i - 1])
          myBuffer.drawImage(
              pokeball.getImage(),
              pokedexOptions[i].getX() + pokedexOptions[i].getWidth() - 20,
              pokedexOptions[i].getY(),
              20,
              20,
              null);
      }
    }
  }

  private void drawSaveScreen() {
    ImageIcon me = new ImageIcon("images/boy_walk_down_rest.png");
    myBuffer.drawImage(me.getImage(),
                       30,
                       30,
                       GameDriver.SCREEN_WIDTH - 240,
                       GameDriver.SCREEN_HEIGHT
                       - 180,
                       null);
    myBuffer.setColor(Color.BLACK);
    myBuffer.setFont(GameScreen.extraLargeFont);
    myBuffer.drawString("Name: " + PlayPanel.myName,
                        GameDriver.SCREEN_WIDTH - 180,
                        GameDriver.SCREEN_HEIGHT
                        - 160);
    myBuffer.setFont(GameScreen.largerLargeFont);
    myBuffer.drawString("Money: " + PlayPanel.myMoney,
                        GameDriver.SCREEN_WIDTH - 250,
                        GameDriver.SCREEN_HEIGHT
                        - 120);
    myBuffer.drawString("Number of Pokemon Seen: " + numPokemonSeen,
                        GameDriver.SCREEN_WIDTH
                        - 250,
                        GameDriver.SCREEN_HEIGHT
                        - 95);
    myBuffer.drawString(
        "Number of pokemon Caught: " + numPokemonOwned,
        GameDriver.SCREEN_WIDTH
        - 250,
        GameDriver.SCREEN_HEIGHT
        - 70);
    myBuffer.drawString("Location: " + game.mapName,
                        GameDriver.SCREEN_WIDTH - 250,
                        GameDriver.SCREEN_HEIGHT
                        - 45);
  }

  private void drawPersonalScreen() {
    ImageIcon me = new ImageIcon("images/boy_walk_down_rest.png");
    myBuffer.drawImage(me.getImage(),
                       30,
                       30,
                       GameDriver.SCREEN_WIDTH - 240,
                       GameDriver.SCREEN_HEIGHT
                       - 180,
                       null);
    myBuffer.setColor(Color.BLACK);
    myBuffer.setFont(GameScreen.extraLargeFont);
    myBuffer.drawString("Name: " + PlayPanel.myName,
                        GameDriver.SCREEN_WIDTH - 180,
                        GameDriver.SCREEN_HEIGHT
                        - 160);
    myBuffer.setFont(GameScreen.largerLargeFont);
    myBuffer.drawString("Money: " + PlayPanel.myMoney,
                        GameDriver.SCREEN_WIDTH - 250,
                        GameDriver.SCREEN_HEIGHT
                        - 120);
    myBuffer.drawString("Number of Pokemon Seen: " + numPokemonSeen,
                        GameDriver.SCREEN_WIDTH - 250,
                        GameDriver.SCREEN_HEIGHT - 95);
    myBuffer.drawString("Number of Pokemon Caught: " + numPokemonOwned,
                        GameDriver.SCREEN_WIDTH - 250,
                        GameDriver.SCREEN_HEIGHT - 70);
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