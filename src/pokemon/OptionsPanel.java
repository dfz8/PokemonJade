package pokemon;

import pokemon.controllers.PlayerController;
import pokemon.entities.PokeBall;
import pokemon.entities.Pokemon;
import pokemon.enums.Menu;
import pokemon.ui.OptionBoard;
import pokemon.ui.Styles;

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
  public static boolean usingItem;
  public static boolean switchingPokemon;

  private PlayPanel mPlayPanel;
  static Menu curMenu;

  static boolean showPokemonSelectOptions = false;
  static int switchPokemonInd = -1;
  static int pokedexStartInd;
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
    curMenu = Menu.pokemonSelect;
    switchingPokemon = true;
    toParty();

    mPlayPanel.getMovementController().setCanMove(false);
  }

  public void toParty() {
    for (int i = 0; i < 6; i++) {
      if (PlayPanel.myPokemon[i] == null) {
        pokemonOptions[i].showHighlight(false);
      } else {
        pokemonOptions[i].setText(PlayPanel.myPokemon[i].getName());
      }
    }
    curMenu = Menu.party;
    partyText = "Select a Pokemon.";

    mPlayPanel.getMovementController().setCanMove(false);
  }

  public OptionsPanel(PlayPanel playPanel) {
    mPlayPanel = playPanel;

    myImage = new BufferedImage(
        GameDriver.SCREEN_WIDTH,
        GameDriver.SCREEN_HEIGHT,
        BufferedImage.TYPE_INT_RGB);
    myBuffer = myImage.getGraphics();
    background = Color.BLACK;
    inBattle = usingItem = switchingPokemon = false;

    PlayerController player = mPlayPanel.getPlayer();
    normalOptions = OptionsHelper.setUpFor(Menu.main, player);
    pokemonOptions = OptionsHelper.setUpFor(Menu.party, player);
    pokedexOptions = OptionsHelper.setUpFor(Menu.pokedex, player);
    battleOptions = OptionsHelper.setUpFor(Menu.battle, player);
    attackOptions = OptionsHelper.setUpFor(Menu.attackSelection, player);
    bagOptions = OptionsHelper.setUpFor(Menu.bag, player);
    pokemonSelectOptions = OptionsHelper.setUpFor(Menu.pokemonSelect, player);
    saveOptions = OptionsHelper.setUpFor(Menu.save, player);
    personalOptions = OptionsHelper.setUpFor(Menu.personal, player);

    curMenu = Menu.main;

    t = new Timer(30, new RefreshListener());
    t.start();
  }

  public void updateHighlightsForOptions(int x, int y) {
    if (curMenu == Menu.party && !showPokemonSelectOptions) {
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
    switch (curMenu) {
      case main:
        return normalOptions;
      case party:
        return showPokemonSelectOptions ? pokemonSelectOptions : pokemonOptions;
      case battle:
        return battleOptions;
      case pokedex:
        return pokedexOptions;
      case attackSelection:
        return attackOptions;
      case bag:
        return bagOptions;
      case save:
        return saveOptions;
      case personal:
        return personalOptions;
      case blackScreen:
        return null;
      default:
        AlertHelper.fatal("Unrecognized panel id:" + curMenu);
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
    if (curMenu == Menu.main) {
      if (s.equals("Pokemon")) {
        toParty();
      } else if (s.equals("Pokedex")) {
        mPlayPanel.getOptionsNavigationHelper().toPokedex();
      } else if (s.equals("Save")) {
        mPlayPanel.getOptionsNavigationHelper().toSave();
      } else if (s.equals(mPlayPanel.getPlayer().getName())) {
        mPlayPanel.getOptionsNavigationHelper().toPersonal();
      } else if (s.equals("Bag")) {
        AlertHelper.alert("Coming Soon");
      } else if (s.equals("Options")) {
        AlertHelper.alert("Coming Soon");
      }
    } else if (curMenu == Menu.party) {
      if (s.equals("Back")) {
        if (!showPokemonSelectOptions) {
          if (!switchingPokemon) {
            mPlayPanel.getOptionsNavigationHelper().toNormal();
          } else {
            mPlayPanel.getOptionsNavigationHelper().toBattle();
          }
        } else {
          showPokemonSelectOptions = false;
          toParty();
        }
      } else if (switchingPokemon) {
        for (int i = 0; i < 6; i++) {
          if (PlayPanel.myPokemon[i] != null) {
            if (PlayPanel.myPokemon[i].getName().equals(s)) {
              //switch
              if (GameScreen.inBattle) {
                if (i == 0) {
                  partyText = "That pokemon is already out!";
                } else {
                  switchPokemonInd = i;
                  mPlayPanel.getGameScreen().swapPokemon = true;
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
        }
      } else if (showPokemonSelectOptions) {
        if (s.equals("Move")) {
          switchingPokemon = true;
          showPokemonSelectOptions = false;
        } else if (s.equals("Summary")) {
          mPlayPanel.getOptionsNavigationHelper().toSummary(switchPokemonInd);
        } else if (s.equals("Back")) {
          showPokemonSelectOptions = false;
          toParty();
        }
      } else //clicked on one of the pokemon tiles in the regular(?) party "looking" mode
      {
        showPokemonSelectOptions = true;
        for (int i = 0; i < 6; i++) {
          if (PlayPanel.myPokemon[i] != null) {
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
      }
    } else if (curMenu == Menu.pokedex) {
      if (s.equals("Up")) {
        pokedexStartInd -= pokedexOptions.length - 3;
        if (pokedexStartInd < 1) {
          pokedexStartInd = 1;
        }
      } else if (s.equals("Down")) {
        pokedexStartInd += pokedexOptions.length - 3;
        if (pokedexStartInd + pokedexOptions.length - 3 > Pokemon.getNumPokemon()) {
          pokedexStartInd = Pokemon.getNumPokemon() - pokedexOptions.length + 4;
        }
      } else if (s.equals("Back")) {
        mPlayPanel.getOptionsNavigationHelper().toNormal();
      } else //click on a pokemon tile
      {

      }

    } else if (curMenu == Menu.battle) {
      if (s.equals("Fight")) {
        mPlayPanel.getOptionsNavigationHelper().toAttacks();
      }
      if (s.equals("Bag")) {
        mPlayPanel.getOptionsNavigationHelper().toBag();
      }
      if (s.equals("Pokemon")) {
        toSwitchPokemon();
      }
      if (s.equals("Run")) {
        mPlayPanel.getGameScreen().resetOrderOfPokemonInParty();
        mPlayPanel.getOptionsNavigationHelper().toNormal();
      }
    } else if (curMenu == Menu.attackSelection) {
      if (s.equals(mPlayPanel.getGameScreen().myPoke.getAttackOne())) {
        GameScreen.isAttacking = true;
        GameScreen.myAttackName = mPlayPanel.getGameScreen().myPoke.getAttackOne();
      } else if (s.equals(mPlayPanel.getGameScreen().myPoke.getAttackTwo())) {
        GameScreen.isAttacking = true;
        GameScreen.myAttackName = mPlayPanel.getGameScreen().myPoke.getAttackTwo();
      } else if (s.equals(mPlayPanel.getGameScreen().myPoke.getAttackThree())) {
        GameScreen.isAttacking = true;
        GameScreen.myAttackName = mPlayPanel.getGameScreen().myPoke.getAttackThree();
      } else if (s.equals(mPlayPanel.getGameScreen().myPoke.getAttackFour())) {
        GameScreen.isAttacking = true;
        GameScreen.myAttackName = mPlayPanel.getGameScreen().myPoke.getAttackFour();
      } else if (s.equals("Back")) {
        mPlayPanel.getOptionsNavigationHelper().toBattle();
      }
    } else if (curMenu == Menu.bag) {
      if (s.equals("Back")) {
        mPlayPanel.getOptionsNavigationHelper().toBattle();
      } else if (s.equals("Pokeball")) {
        if (p.isCaught()) {
          int index = 0;
          for (int occupy = 0; occupy < PlayPanel.myPokemon.length; occupy++) {
            if (PlayPanel.myPokemon[occupy] == null) {
              index = occupy;
              break;
            }
          }
          mPlayPanel.getPlayer().markCaughtPokemon(mPlayPanel.getGameScreen().enemy.getName());
          mPlayPanel.getPlayer().markSeenPokemon(mPlayPanel.getGameScreen().enemy.getName());

          PlayPanel.myPokemon[index] = new Pokemon.Builder()
              .setName(mPlayPanel.getGameScreen().enemy.getName())
              .setType(mPlayPanel.getGameScreen().enemy.getType())
              .setFirstAttack(mPlayPanel.getGameScreen().enemy.getAttackOne())
              .setSecondAttack(mPlayPanel.getGameScreen().enemy.getAttackTwo())
              .setThirdAttack(mPlayPanel.getGameScreen().enemy.getAttackThree())
              .setFourthAttack(mPlayPanel.getGameScreen().enemy.getAttackFour())
              .setLevel(mPlayPanel.getGameScreen().enemy.getLevel())
              .setExp(mPlayPanel.getGameScreen().enemy.getMyEXP())
              .setAttack(mPlayPanel.getGameScreen().enemy.getAttackLevel())
              .setDefense(mPlayPanel.getGameScreen().enemy.getDefenseLevel())
              .setHp(mPlayPanel.getGameScreen().enemy.getCurrentHP())
              .setMaxHp(mPlayPanel.getGameScreen().enemy.getMaxHP())
              .build();
          mPlayPanel.getOptionsNavigationHelper().toNormal();
        } else {
          AlertHelper.alert("Awww, the pokemon broke out of the pokeball!");
          mPlayPanel.getOptionsNavigationHelper().toBlack();
          GameScreen.isAttacking = false;
          GameScreen.enemyIsAttacking = true;
        }
      }

    } else if (curMenu == Menu.save) {
      if (s.equals("")) {
        mPlayPanel.save();
        mPlayPanel.getOptionsNavigationHelper().toNormal();
      }
    } else if (curMenu == Menu.personal) {
      if (s.equals("")) {
        mPlayPanel.getOptionsNavigationHelper().toNormal();
      }
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
      myBuffer.setFont(Styles.normalFont);

      if (curMenu == Menu.blackScreen) {
        myBuffer.setColor(Color.BLACK);
        myBuffer.fillRect(0, 0, GameDriver.SCREEN_WIDTH, GameDriver.SCREEN_HEIGHT);
        repaint();
        return;
      } else if (curMenu == Menu.party) {
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
      if (curMenu == Menu.pokedex) {
        drawPokedexScreen();
      } else if (curMenu == Menu.save) {
        drawSaveScreen();
      } else if (curMenu == Menu.personal) {
        drawPersonalScreen();
      } else if (curMenu == Menu.attackSelection) {
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
    myBuffer.fillRect(
        10,
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
          myBuffer.drawString(
              "FNT",
              pokemonOptions[i].getX() + 2,
              pokemonOptions[i].getY() + pokemonOptions[i].getHeight() - 3);
        } else {
          pokemonOptions[i].setColor(Color.WHITE);
        }

        DrawingHelper.drawHalfSizeHPBar(
            myBuffer,
            PlayPanel.myPokemon[i],
            pokemonOptions[i].getX() + x_inc,
            pokemonOptions[i].getY() + y_inc + 2);

        //hp text
        myBuffer.setFont(Styles.smallFont);
        myBuffer.setColor(Color.BLACK);
        myBuffer.drawString(
            "HP:",
            pokemonOptions[i].getX() + x_inc - 12,
            pokemonOptions[i].getY() + y_inc + 7);
        myBuffer.setFont(Styles.normalFont);
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
        ImageIcon pokeImage = SpriteHelper.getPokemonFront(PlayPanel.myPokemon[i].getName());
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
    myBuffer.setFont(Styles.normalFont);
    myBuffer.setColor(Color.BLACK);
    myBuffer.drawString(
        "Seen: " + mPlayPanel.getPlayer().getNumSeenPokemon(),
        GameDriver.SCREEN_WIDTH - 125,
        GameDriver.SCREEN_HEIGHT
        - 80);
    myBuffer.drawString(
        "Caught: " + mPlayPanel.getPlayer().getNumCaughtPokemon(),
        GameDriver.SCREEN_WIDTH - 70,
        GameDriver.SCREEN_HEIGHT
        - 80);

    ImageIcon pokeball = SpriteHelper.getMisc("pokeball");

    for (int i = 0; i < pokedexOptions.length - 3; i++) {
      //-3 to leave out up, down, and back options
      // todo: switch to numberformat
      String numText = "";
      if (pokedexStartInd + i + 1 < 100) {
        numText += "0";
      }
      if (pokedexStartInd + i + 1 < 10) {
        numText += "0";
      }
      //text
      if (mPlayPanel.getPlayer().hasSeenPokemon(pokedexStartInd + i - 1)) {
        pokedexOptions[i].setText(
            numText + (pokedexStartInd + i) + ". \t" + Pokemon.getPokemon(
                pokedexStartInd + i - 1));
      } else {
        pokedexOptions[i].setText(numText + (pokedexStartInd + i) + ". \t?????");
      }

      //image of pokemon you're hovering over
      if (mPlayPanel.getPlayer().hasSeenPokemon(pokedexStartInd + i - 1)) {
        if (pokedexOptions[i].isHighlighted()) {
          ImageIcon curImage = SpriteHelper.getPokemonFront(Pokemon.getPokemon(
              pokedexStartInd + i - 1));
          myBuffer.drawImage(
              curImage.getImage(),
              GameDriver.SCREEN_WIDTH - 130,
              GameDriver.SCREEN_HEIGHT - 190,
              null);
        }
      }
      //pokeballs next to pokemon you have caught
      if (mPlayPanel.getPlayer().hasCaughtPokemon(pokedexStartInd + i - 1)) {
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
    ImageIcon me = SpriteHelper.getMisc("boy_walk_down_rest");
    myBuffer.drawImage(
        me.getImage(),
        30,
        30,
        GameDriver.SCREEN_WIDTH - 240,
        GameDriver.SCREEN_HEIGHT - 180,
        null);
    myBuffer.setColor(Color.BLACK);
    myBuffer.setFont(Styles.extraLargeFont);
    myBuffer.drawString(
        "Name: " + mPlayPanel.getPlayer().getName(),
        GameDriver.SCREEN_WIDTH - 180,
        GameDriver.SCREEN_HEIGHT - 160);
    myBuffer.setFont(Styles.largerLargeFont);
    myBuffer.drawString(
        "Money: " + mPlayPanel.getPlayer().getMoney(),
        GameDriver.SCREEN_WIDTH - 250,
        GameDriver.SCREEN_HEIGHT - 120);
    myBuffer.drawString(
        "Number of Pokemon Seen: " + mPlayPanel.getPlayer().getNumSeenPokemon(),
        GameDriver.SCREEN_WIDTH - 250,
        GameDriver.SCREEN_HEIGHT - 95);
    myBuffer.drawString(
        "Number of pokemon Caught: " + mPlayPanel.getPlayer().getNumCaughtPokemon(),
        GameDriver.SCREEN_WIDTH - 250,
        GameDriver.SCREEN_HEIGHT - 70);
    myBuffer.drawString(
        "Location: " + GameScreen.mapName,
        GameDriver.SCREEN_WIDTH - 250,
        GameDriver.SCREEN_HEIGHT - 45);
  }

  private void drawPersonalScreen() {
    ImageIcon me = SpriteHelper.getMisc("boy_walk_down_rest");
    myBuffer.drawImage(
        me.getImage(),
        30,
        30,
        GameDriver.SCREEN_WIDTH - 240,
        GameDriver.SCREEN_HEIGHT - 180,
        null);
    myBuffer.setColor(Color.BLACK);
    myBuffer.setFont(Styles.extraLargeFont);
    myBuffer.drawString(
        "Name: " + mPlayPanel.getPlayer().getName(),
        GameDriver.SCREEN_WIDTH - 180,
        GameDriver.SCREEN_HEIGHT - 160);
    myBuffer.setFont(Styles.largerLargeFont);
    myBuffer.drawString(
        "Money: " + mPlayPanel.getPlayer().getMoney(),
        GameDriver.SCREEN_WIDTH - 250,
        GameDriver.SCREEN_HEIGHT - 120);
    myBuffer.drawString(
        "Number of Pokemon Seen: " + mPlayPanel.getPlayer().getNumSeenPokemon(),
        GameDriver.SCREEN_WIDTH - 250,
        GameDriver.SCREEN_HEIGHT - 95);
    myBuffer.drawString(
        "Number of Pokemon Caught: " + mPlayPanel.getPlayer().getNumCaughtPokemon(),
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
        attackOptions[i].setText("" + mPlayPanel.getGameScreen().myPoke.getAttackOne());
      } else if (i == 1) {
        attackOptions[i].setText("" + mPlayPanel.getGameScreen().myPoke.getAttackTwo());
      } else if (i == 2) {
        attackOptions[i].setText("" + mPlayPanel.getGameScreen().myPoke.getAttackThree());
      } else {
        attackOptions[i].setText("" + mPlayPanel.getGameScreen().myPoke.getAttackFour());
      }
    }
  }

  private void drawOptions() {
    for (OptionBoard option : getOptionsForCurrentPanel()) {
      option.draw(myBuffer);
    }
  }
}