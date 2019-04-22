package pokemon;

import pokemon.controllers.PlayerController;
import pokemon.entities.PokeBall;
import pokemon.entities.Pokemon;
import pokemon.enums.Menu;
import pokemon.ui.ClickListenerFactory;
import pokemon.ui.OnClickListener;
import pokemon.ui.OptionBoard;
import pokemon.ui.Styles;

import javax.swing.*;
import java.awt.*;
import java.awt.event.ActionEvent;
import java.awt.event.ActionListener;

public class OptionsPanel extends GamePanel {

  private PlayPanel mPlayPanel;
  private ClickListenerFactory mClickListenerFactory;
  private Color mBackgroundColor;

  static boolean switchingPokemon;
  private static boolean inBattle;
  private static boolean usingItem;
  private static Menu curMenu;

  private static boolean showPokemonSelectOptions = false;
  private static int switchPokemonInd = -1;
  private static int pokedexStartInd;
  private static String partyText = "";

  // todo refactor each into a controller
  OptionBoard[] defaultOptions;
  OptionBoard[] pokemonOptions;
  OptionBoard[] pokemonSelectOptions;
  OptionBoard[] pokedexOptions;
  OptionBoard[] battleOptions;
  OptionBoard[] attackOptions;
  OptionBoard[] bagOptions;
  OptionBoard[] saveOptions;
  OptionBoard[] personalOptions;

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
        pokemonOptions[i].shouldShowHighlight(false);
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
    mClickListenerFactory = new ClickListenerFactory(playPanel);
    mBackgroundColor = Color.BLACK;
    inBattle = usingItem = switchingPokemon = false;

    PlayerController player = mPlayPanel.getPlayer();
    defaultOptions = OptionsHelper.setUpFor(Menu.main, player);
    pokemonOptions = OptionsHelper.setUpFor(Menu.party, player);
    pokedexOptions = OptionsHelper.setUpFor(Menu.pokedex, player);
    battleOptions = OptionsHelper.setUpFor(Menu.battle, player);
    attackOptions = OptionsHelper.setUpFor(Menu.attackSelection, player);
    bagOptions = OptionsHelper.setUpFor(Menu.bag, player);
    pokemonSelectOptions = OptionsHelper.setUpFor(Menu.pokemonSelect, player);
    saveOptions = OptionsHelper.setUpFor(Menu.save, player);
    personalOptions = OptionsHelper.setUpFor(Menu.personal, player);
    initOnClickListeners();

    curMenu = Menu.main;

    setAndStartActionListener(30, new RefreshListener());
  }

  private void initOnClickListeners() {
    defaultOptions[0].setOnClickListener(new OnClickListener() {
      @Override
      public void onClick() {
        toParty();
      }
    });
    defaultOptions[1].setOnClickListener(mClickListenerFactory.toStateClickListener(GameState.VIEW_POKEDEX));
    defaultOptions[2].setOnClickListener(mClickListenerFactory.toStateClickListener(GameState.VIEW_SELF));
    defaultOptions[3].setOnClickListener(mClickListenerFactory.comingSoonListener());
    defaultOptions[4].setOnClickListener(mClickListenerFactory.toStateClickListener(GameState.SAVE));
    defaultOptions[5].setOnClickListener(mClickListenerFactory.comingSoonListener());

    saveOptions[0].setOnClickListener(new OnClickListener() {
      @Override
      public void onClick() {
        mPlayPanel.save();
        mPlayPanel.setState(GameState.DEFAULT);
      }
    });

    personalOptions[0].setOnClickListener(mClickListenerFactory.toStateClickListener(GameState.DEFAULT));
  }

  public void updateHighlightsForOptions(int x, int y) {
    if (curMenu == Menu.party && !showPokemonSelectOptions) {
      // can come here when viewing pokemon, in battle and switching out pokemon
      for (int i = 0; i < pokemonOptions.length; i++) {
        pokemonOptions[i].shouldShowHighlight(
            isMouseOver(x, y, pokemonOptions[i]) && shouldShowHighlightForPokemon(i));
      }
    } else {
      updateHighlights(x, y, getOptionsForCurrentPanel());
    }
  }

  private OptionBoard[] getOptionsForCurrentPanel() {
    switch (curMenu) {
      case main:
        return defaultOptions;
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
      board.shouldShowHighlight(isMouseOver(x, y, board));
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
        if (option.hasOnClickListener()) {
          option.onClick();
        } else {
          searchButton(option.getText());
        }
      }
    }
  }

  //where the click code is run
  public void searchButton(String s) {
    if (curMenu == Menu.party) {
      if (s.equals("Back")) {
        if (!showPokemonSelectOptions) {
          if (!switchingPokemon) {
            mPlayPanel.setState(GameState.DEFAULT);
          } else {
            mPlayPanel.setState(GameState.BATTLE_DEFAULT);
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
          mPlayPanel.setSummaryIndex(switchPokemonInd);
          mPlayPanel.setState(GameState.VIEW_POKEMON);
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
        mPlayPanel.setState(GameState.DEFAULT);
      }
    } else if (curMenu == Menu.battle) {
      if (s.equals("Fight")) {
        mPlayPanel.setState(GameState.BATTLE_CHOOSE_ATTACK);
      }
      if (s.equals("Bag")) {
        mPlayPanel.setState(GameState.VIEW_ITEMS);
      }
      if (s.equals("Pokemon")) {
        toSwitchPokemon();
      }
      if (s.equals("Run")) {
        mPlayPanel.setState(GameState.DEFAULT);
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
        mPlayPanel.setState(GameState.BATTLE_DEFAULT);
      }
    } else if (curMenu == Menu.bag) {
      if (s.equals("Back")) {
        mPlayPanel.setState(GameState.BATTLE_DEFAULT);
      } else if (s.equals("Pokeball")) {
        if (p.isCaught()) {
          addNewPokemonToParty();
          mPlayPanel.setState(GameState.DEFAULT);
        } else {
          AlertHelper.alert("Awww, the pokemon broke out of the pokeball!");
          mPlayPanel.setState(GameState.BATTLE_WAIT_ON_ENEMY);
        }
      }

    }
  }

  private void addNewPokemonToParty() {
    int index = 0;
    for (; index < PlayPanel.myPokemon.length && PlayPanel.myPokemon[index] != null; index++) {
    }

    Pokemon enemy = mPlayPanel.getGameScreen().enemy;
    mPlayPanel.getPlayer().markCaughtPokemon(enemy.getName());
    mPlayPanel.getPlayer().markSeenPokemon(enemy.getName());

    PlayPanel.myPokemon[index] = Pokemon.buildFrom(enemy).build();
  }

  @Override
  public void handleStateChange(GameState currentState, GameState newState) {
    switch (newState) {
      case DEFAULT:
        curMenu = Menu.main;
        switchingPokemon = false;
        showPokemonSelectOptions = false;
        switchPokemonInd = -1;
        break;
      case BATTLE_DEFAULT:
        curMenu = Menu.battle;
        switchingPokemon = false;
        break;
      case BATTLE_CHOOSE_ATTACK:
        curMenu = Menu.attackSelection;
        break;
      case BATTLE_WAIT_ON_ENEMY:
      case BATTLE_ATTACK_ANIMATION:
      case BATTLE_SWITCH_ANIMATION:
        curMenu = Menu.blackScreen;
        break;
      case VIEW_ITEMS:
        curMenu = Menu.bag;
      case VIEW_SELF:
        curMenu = Menu.personal;
        break;
      case VIEW_POKEDEX:
        curMenu = Menu.pokedex;
        pokedexStartInd = 1;
        break;
      case VIEW_POKEMON:
        showPokemonSelectOptions = false;
        break;
      case SAVE:
        curMenu = Menu.save;
        break;
    }
  }

  public class RefreshListener implements ActionListener {
    public void actionPerformed(ActionEvent e) {
      Graphics myBuffer = getImageBuffer();
      myBuffer.setColor(mBackgroundColor);
      myBuffer.fillRect(0, 0, GameDriver.SCREEN_WIDTH, GameDriver.SCREEN_HEIGHT);
      // todo: move set font code to the specific panels
      myBuffer.setFont(Styles.normalFont);

      if (curMenu == Menu.blackScreen) {
        myBuffer.setColor(Color.BLACK);
        myBuffer.fillRect(0, 0, GameDriver.SCREEN_WIDTH, GameDriver.SCREEN_HEIGHT);
        repaint();
        return;
      } else if (curMenu == Menu.party) {
        drawOptions(myBuffer);
        drawPokemonPartyScreen(myBuffer);
        if (showPokemonSelectOptions) {
          drawOptions(myBuffer);
        }
        repaint();
        return;
      }

      drawOptions(myBuffer);

      // drawSprites special overlays
      if (curMenu == Menu.pokedex) {
        drawPokedexScreen(myBuffer);
      } else if (curMenu == Menu.save) {
        drawSaveScreen(myBuffer);
      } else if (curMenu == Menu.personal) {
        drawPersonalScreen(myBuffer);
      } else if (curMenu == Menu.attackSelection) {
        // todo: move updateAttackOptions to wherever init code should go
        updateAttackOptions();
        drawOptions(myBuffer);
      }

      repaint();
    }
  }

  private void drawPokemonPartyScreen(Graphics myBuffer) {
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
          pokemonOptions[i].setBackgroundColor(Color.ORANGE);
          myBuffer.drawString(
              "FNT",
              pokemonOptions[i].getX() + 2,
              pokemonOptions[i].getY() + pokemonOptions[i].getHeight() - 3);
        } else {
          pokemonOptions[i].setBackgroundColor(Color.WHITE);
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
        DrawingHelper.drawImage(
            myBuffer,
            pokeImage,
            pokemonOptions[i].getX() + pokemonOptions[i].getWidth() - 40,
            pokemonOptions[i].getY(),
            40,
            40);
      }
    }
  }

  private void drawPokedexScreen(Graphics myBuffer) {
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
          DrawingHelper.drawImage(
              myBuffer,
              curImage,
              GameDriver.SCREEN_WIDTH - 130,
              GameDriver.SCREEN_HEIGHT - 190);
        }
      }
      //pokeballs next to pokemon you have caught
      if (mPlayPanel.getPlayer().hasCaughtPokemon(pokedexStartInd + i - 1)) {
        DrawingHelper.drawImage(
            myBuffer,
            pokeball,
            pokedexOptions[i].getX() + pokedexOptions[i].getWidth() - 20,
            pokedexOptions[i].getY(),
            20,
            20);
      }
    }
  }

  private void drawSaveScreen(Graphics myBuffer) {
    ImageIcon me = SpriteHelper.getMisc("boy_walk_down_rest");
    DrawingHelper.drawImage(
        myBuffer,
        me,
        30,
        30,
        GameDriver.SCREEN_WIDTH - 240,
        GameDriver.SCREEN_HEIGHT - 180);
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
        "Location: " + mPlayPanel.getMapController().getMapName(),
        GameDriver.SCREEN_WIDTH - 250,
        GameDriver.SCREEN_HEIGHT - 45);
  }

  private void drawPersonalScreen(Graphics myBuffer) {
    ImageIcon me = SpriteHelper.getMisc("boy_walk_down_rest");
    DrawingHelper.drawImage(
        myBuffer,
        me,
        30,
        30,
        GameDriver.SCREEN_WIDTH - 240,
        GameDriver.SCREEN_HEIGHT - 180);
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
      attackOptions[i].setBackgroundColor(c);

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

  private void drawOptions(Graphics myBuffer) {
    for (OptionBoard option : getOptionsForCurrentPanel()) {
      option.draw(myBuffer);
    }
  }
}