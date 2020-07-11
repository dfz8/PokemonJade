package pokemon;

import pokemon.controllers.PlayerController;
import pokemon.entities.PokeBall;
import pokemon.entities.Pokemon;
import pokemon.enums.Menu;
import pokemon.ui.ClickListenerFactory;
import pokemon.ui.OnClickListener;
import pokemon.ui.OptionBoard;
import pokemon.ui.Styles;
import pokemon.util.DrawingHelper;
import pokemon.util.SpriteHelper;

import javax.swing.*;
import java.awt.*;
import java.awt.event.ActionEvent;
import java.awt.event.ActionListener;

/**
 * Bottom screen of the DS that displays useful text information, as well as provide options for
 * the user to interact with e.g choose an attack.
 */
public class GuiScreen extends DrawableScreen {

  private GameContainer mGameContainer;
  private ClickListenerFactory mClickListenerFactory;
  private Color mBackgroundColor;

  private Menu curMenu;

  private boolean showPokemonSelectOptions = false;
  private int switchPokemonInd = -1;
  private int pokedexStartInd;
  private String partyText = "";

  // todo refactor each into a controller
  private OptionBoard[] defaultOptions;
  private OptionBoard[] pokemonOptions;
  private OptionBoard[] pokemonSelectOptions;
  private OptionBoard[] pokedexOptions;
  private OptionBoard[] battleOptions;
  private OptionBoard[] attackOptions;
  private OptionBoard[] bagOptions;
  private OptionBoard[] saveOptions;
  private OptionBoard[] personalOptions;

  public GuiScreen(GameContainer gameContainer) {
    mGameContainer = gameContainer;
    mClickListenerFactory = new ClickListenerFactory(gameContainer);
    mBackgroundColor = Color.BLACK;

    PlayerController player = mGameContainer.getPlayer();
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
    defaultOptions[0].setOnClickListener(mClickListenerFactory.toStateClickListener(GameState.VIEW_PARTY));
    defaultOptions[1].setOnClickListener(mClickListenerFactory.toStateClickListener(GameState.VIEW_POKEDEX));
    defaultOptions[2].setOnClickListener(mClickListenerFactory.toStateClickListener(GameState.VIEW_SELF));
    defaultOptions[3].setOnClickListener(mClickListenerFactory.comingSoonListener());
    defaultOptions[4].setOnClickListener(mClickListenerFactory.toStateClickListener(GameState.SAVE));
    defaultOptions[5].setOnClickListener(mClickListenerFactory.comingSoonListener());

    battleOptions[0].setOnClickListener(mClickListenerFactory.toStateClickListener(GameState.VIEW_ITEMS));
    battleOptions[1].setOnClickListener(mClickListenerFactory.toStateClickListener(GameState.BATTLE_CHOOSE_ATTACK));
    battleOptions[2].setOnClickListener(mClickListenerFactory.toStateClickListener(GameState.DEFAULT));
    battleOptions[3].setOnClickListener(mClickListenerFactory.toStateClickListener(GameState.BATTLE_VIEW_POKEMON));

    final int numPokedexSlots = pokedexOptions.length - 3;
    pokedexOptions[7].setOnClickListener(new OnClickListener() {
      @Override
      public void onClick() {
        pokedexStartInd = Math.max(pokedexStartInd - numPokedexSlots, 1);
      }
    });
    pokedexOptions[8].setOnClickListener(new OnClickListener() {
      @Override
      public void onClick() {
        pokedexStartInd = Math.min(
            pokedexStartInd + pokedexOptions.length - 3,
            Pokemon.getNumPokemon() - numPokedexSlots);
      }
    });
    pokedexOptions[9].setOnClickListener(mClickListenerFactory.toStateClickListener(GameState.DEFAULT));

    pokemonSelectOptions[0].setOnClickListener(new OnClickListener() {
      @Override
      public void onClick() {
        mGameContainer.getBattleController().setIsSwappingPokemon(true);
        showPokemonSelectOptions = false;
      }
    });
    pokemonSelectOptions[1].setOnClickListener(new OnClickListener() {
      @Override
      public void onClick() {
        mGameContainer.setSummaryIndex(switchPokemonInd);
        mGameContainer.setState(GameState.VIEW_POKEMON);

      }
    });
    pokemonSelectOptions[2].setOnClickListener(new OnClickListener() {
      @Override
      public void onClick() {
        showPokemonSelectOptions = false;
      }
    });

    bagOptions[0].setOnClickListener(mClickListenerFactory.toStateClickListener(GameState.BATTLE_DEFAULT));
    bagOptions[1].setOnClickListener(new OnClickListener() {
      @Override
      public void onClick() {
        if (new PokeBall().isCaught()) {
          addNewPokemonToParty();
          mGameContainer.setState(GameState.DEFAULT);
        } else {
          AlertHelper.alert("Awww, the pokemon broke out of the pokeball!");
          mGameContainer.setState(GameState.BATTLE_WAIT_ON_ENEMY);
        }
      }
    });

    saveOptions[0].setOnClickListener(new OnClickListener() {
      @Override
      public void onClick() {
        mGameContainer.save();
        mGameContainer.setState(GameState.DEFAULT);
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
           || (GameContainer.myPokemon[index] != null
               && (!mGameContainer.getBattleController().isInBattle()
                   || !GameContainer.myPokemon[index].isFainted()));
  }

  private void updateHighlights(int x, int y, OptionBoard[] boards) {
    for (OptionBoard board : boards) {
      board.shouldShowHighlight(isMouseOver(x, y, board));
    }
  }

  private boolean isMouseOver(int x, int y, OptionBoard board) {
    // HEIGHT displacement since two screens are stacked on top of each other
    return (board.getX() <= x && x <= board.getX() + board.getWidth())
           && (board.getY() + DrawingHelper.SCREEN_HEIGHT <= y
               && y <= board.getY() + board.getHeight() + DrawingHelper.SCREEN_HEIGHT);
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
      if (mGameContainer.getBattleController().isSwappingPokemon()) {
        if (s.equals("Back")) {
          mGameContainer.setState(GameState.BATTLE_DEFAULT);
          return;
        }
        if (mGameContainer.getBattleController().isInBattle()) {
          for (int i = 0; i < 6; i++) {
            Pokemon p = GameContainer.myPokemon[i];
            if (p != null && p.getName().equals(s)) {
              if (i == 0) {
                partyText = "That pokemon is already out!";
              } else {
                switchPokemonInd = i;
                mGameContainer.getBattleController().setIsSwappingPokemon(true);
                GameContainer.myPokemon[i] = GameContainer.myPokemon[0];
                GameContainer.myPokemon[0] = p;
              }
            }
          }
        } else {
          // normal viewing mode
          for (int i = 0; i < 6; i++) {
            Pokemon p = GameContainer.myPokemon[i];
            if (p != null && p.getName().equals(s)) {
              //not in battle, "looking mode"
              GameContainer.myPokemon[i] = GameContainer.myPokemon[switchPokemonInd];
              GameContainer.myPokemon[switchPokemonInd] = p;

              pokemonOptions[i].setText(GameContainer.myPokemon[i].getName());
              pokemonOptions[switchPokemonInd].setText(GameContainer.myPokemon[switchPokemonInd].getName());

              mGameContainer.getBattleController().setIsSwappingPokemon(false);
              break;
            }

          }
        }
      } else if (s.equals("Back")) {
        mGameContainer.setState(GameState.DEFAULT);
      } else {
        //clicked on one of the pokemon tiles
        showPokemonSelectOptions = true;
        for (int i = 0; i < 6; i++) {
          Pokemon p = GameContainer.myPokemon[i];
          if (p != null && p.getName().equals(s)) {
            repositionSelectOptions(i);
            break;
          }
        }
      }
    } else if (curMenu == Menu.attackSelection) {
      if (s.equals(mGameContainer.getGameScreen().myPoke.getAttackOne())) {
        GameplayScreen.isAttacking = true;
        GameplayScreen.myAttackName = mGameContainer.getGameScreen().myPoke.getAttackOne();
      } else if (s.equals(mGameContainer.getGameScreen().myPoke.getAttackTwo())) {
        GameplayScreen.isAttacking = true;
        GameplayScreen.myAttackName = mGameContainer.getGameScreen().myPoke.getAttackTwo();
      } else if (s.equals(mGameContainer.getGameScreen().myPoke.getAttackThree())) {
        GameplayScreen.isAttacking = true;
        GameplayScreen.myAttackName = mGameContainer.getGameScreen().myPoke.getAttackThree();
      } else if (s.equals(mGameContainer.getGameScreen().myPoke.getAttackFour())) {
        GameplayScreen.isAttacking = true;
        GameplayScreen.myAttackName = mGameContainer.getGameScreen().myPoke.getAttackFour();
      } else if (s.equals("Back")) {
        mGameContainer.setState(GameState.BATTLE_DEFAULT);
      }
    }
  }

  private void repositionSelectOptions(int indexOfSelectedPokemon) {
    showPokemonSelectOptions = true;
    switchPokemonInd = indexOfSelectedPokemon;

    int selectedX = pokemonOptions[indexOfSelectedPokemon].getX();
    int optionWidth = pokemonOptions[indexOfSelectedPokemon].getWidth();

    pokemonSelectOptions[0].setX(
        selectedX + optionWidth
        - pokemonSelectOptions[0].getWidth());
    pokemonSelectOptions[0].setY(pokemonOptions[indexOfSelectedPokemon].getY());

    pokemonSelectOptions[1].setX(
        selectedX + optionWidth
        - pokemonSelectOptions[1].getWidth());
    pokemonSelectOptions[1].setY(
        pokemonSelectOptions[0].getY() + pokemonSelectOptions[0].getHeight() + 5);

    pokemonSelectOptions[2].setX(
        selectedX + optionWidth
        - pokemonSelectOptions[2].getWidth());
    pokemonSelectOptions[2].setY(
        pokemonSelectOptions[1].getY()
        + pokemonSelectOptions[1].getHeight() + 5);
  }

  private void addNewPokemonToParty() {
    int index = 0;
    for (; index < GameContainer.myPokemon.length && GameContainer.myPokemon[index] != null;
         index++) {
    }

    Pokemon enemy = mGameContainer.getGameScreen().enemy;
    mGameContainer.getPlayer().markCaughtPokemon(enemy.getName());
    mGameContainer.getPlayer().markSeenPokemon(enemy.getName());

    GameContainer.myPokemon[index] = Pokemon.buildFrom(enemy).build();
  }

  private void setupForPartyViewing() {
    for (int i = 0; i < 6; i++) {
      if (GameContainer.myPokemon[i] == null) {
        pokemonOptions[i].shouldShowHighlight(false);
      } else {
        pokemonOptions[i].setText(GameContainer.myPokemon[i].getName());
      }
    }
  }

  @Override
  public void handleStateChange(GameState currentState, GameState newState) {
    switch (newState) {
      case DEFAULT:
        curMenu = Menu.main;
        mGameContainer.getBattleController().setIsSwappingPokemon(false);
        showPokemonSelectOptions = false;
        switchPokemonInd = -1;
        break;
      case BATTLE_DEFAULT:
        curMenu = Menu.battle;
        mGameContainer.getBattleController().setIsSwappingPokemon(false);
        break;
      case BATTLE_CHOOSE_ATTACK:
        curMenu = Menu.attackSelection;
        break;
      case BATTLE_VIEW_POKEMON:
        mGameContainer.getBattleController().setIsSwappingPokemon(true);
        setupForPartyViewing();
        curMenu = Menu.party;
        partyText = "Select a Pokemon.";
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
      case VIEW_PARTY:
        setupForPartyViewing();
        curMenu = Menu.party;
        partyText = "";
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
      myBuffer.fillRect(0, 0, DrawingHelper.SCREEN_WIDTH, DrawingHelper.SCREEN_HEIGHT);
      // todo: move set font code to the specific panels
      myBuffer.setFont(Styles.normalFont);

      if (curMenu == Menu.blackScreen) {
        myBuffer.setColor(Color.BLACK);
        myBuffer.fillRect(0, 0, DrawingHelper.SCREEN_WIDTH, DrawingHelper.SCREEN_HEIGHT);
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
        DrawingHelper.SCREEN_HEIGHT - 40,
        DrawingHelper.SCREEN_WIDTH - 60 - 10 - 10,
        35);
    myBuffer.setColor(Color.BLACK);
    myBuffer.drawString(partyText, 10 + 5, DrawingHelper.SCREEN_HEIGHT - 40 + 10);
    //tiles w/ pokemon:
    for (int i = 0; i < pokemonOptions.length; i++) {
      pokemonOptions[i].draw(myBuffer);
      //pokemon health bar
      if (GameContainer.myPokemon[i] != null) {

        int x_inc = 27;
        int y_inc = 15;

        if (GameContainer.myPokemon[i].isFainted()) {
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
            GameContainer.myPokemon[i],
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
            GameContainer.myPokemon[i].getCurrentHP() + "/" + GameContainer.myPokemon[i]
                .getMaxHP(),
            pokemonOptions[i].getX() + x_inc,
            pokemonOptions[i].getY() + y_inc + 17);
        //Level text:
        myBuffer.drawString(
            "LV: " + GameContainer.myPokemon[i].getLevel(),
            pokemonOptions[i].getX() + x_inc,
            pokemonOptions[i].getY() + pokemonOptions[i].getHeight() - 3);
        //Image
        ImageIcon pokeImage = SpriteHelper.getPokemonFront(GameContainer.myPokemon[i].getName());
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
    myBuffer.fillRect(DrawingHelper.SCREEN_WIDTH - 130, DrawingHelper.SCREEN_HEIGHT - 100, 110, 30);
    myBuffer.setFont(Styles.normalFont);
    myBuffer.setColor(Color.BLACK);
    myBuffer.drawString(
        "Seen: " + mGameContainer.getPlayer().getNumSeenPokemon(),
        DrawingHelper.SCREEN_WIDTH - 125,
        DrawingHelper.SCREEN_HEIGHT
        - 80);
    myBuffer.drawString(
        "Caught: " + mGameContainer.getPlayer().getNumCaughtPokemon(),
        DrawingHelper.SCREEN_WIDTH - 70,
        DrawingHelper.SCREEN_HEIGHT
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
      if (mGameContainer.getPlayer().hasSeenPokemon(pokedexStartInd + i - 1)) {
        pokedexOptions[i].setText(
            numText + (pokedexStartInd + i) + ". \t" + Pokemon.getPokemon(
                pokedexStartInd + i - 1));
      } else {
        pokedexOptions[i].setText(numText + (pokedexStartInd + i) + ". \t?????");
      }

      //image of pokemon you're hovering over
      if (mGameContainer.getPlayer().hasSeenPokemon(pokedexStartInd + i - 1)) {
        if (pokedexOptions[i].isHighlighted()) {
          ImageIcon curImage = SpriteHelper.getPokemonFront(Pokemon.getPokemon(
              pokedexStartInd + i - 1));
          DrawingHelper.drawImage(
              myBuffer,
              curImage,
              DrawingHelper.SCREEN_WIDTH - 130,
              DrawingHelper.SCREEN_HEIGHT - 190);
        }
      }
      //pokeballs next to pokemon you have caught
      if (mGameContainer.getPlayer().hasCaughtPokemon(pokedexStartInd + i - 1)) {
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
        DrawingHelper.SCREEN_WIDTH - 240,
        DrawingHelper.SCREEN_HEIGHT - 180);
    myBuffer.setColor(Color.BLACK);
    myBuffer.setFont(Styles.extraLargeFont);
    myBuffer.drawString(
        "Name: " + mGameContainer.getPlayer().getName(),
        DrawingHelper.SCREEN_WIDTH - 180,
        DrawingHelper.SCREEN_HEIGHT - 160);
    myBuffer.setFont(Styles.largerLargeFont);
    myBuffer.drawString(
        "Money: " + mGameContainer.getPlayer().getMoney(),
        DrawingHelper.SCREEN_WIDTH - 250,
        DrawingHelper.SCREEN_HEIGHT - 120);
    myBuffer.drawString(
        "Number of Pokemon Seen: " + mGameContainer.getPlayer().getNumSeenPokemon(),
        DrawingHelper.SCREEN_WIDTH - 250,
        DrawingHelper.SCREEN_HEIGHT - 95);
    myBuffer.drawString(
        "Number of pokemon Caught: " + mGameContainer.getPlayer().getNumCaughtPokemon(),
        DrawingHelper.SCREEN_WIDTH - 250,
        DrawingHelper.SCREEN_HEIGHT - 70);
    myBuffer.drawString(
        "Location: " + mGameContainer.getMapController().getMapName(),
        DrawingHelper.SCREEN_WIDTH - 250,
        DrawingHelper.SCREEN_HEIGHT - 45);
  }

  private void drawPersonalScreen(Graphics myBuffer) {
    ImageIcon me = SpriteHelper.getMisc("boy_walk_down_rest");
    DrawingHelper.drawImage(
        myBuffer,
        me,
        30,
        30,
        DrawingHelper.SCREEN_WIDTH - 240,
        DrawingHelper.SCREEN_HEIGHT - 180);
    myBuffer.setColor(Color.BLACK);
    myBuffer.setFont(Styles.extraLargeFont);
    myBuffer.drawString(
        "Name: " + mGameContainer.getPlayer().getName(),
        DrawingHelper.SCREEN_WIDTH - 180,
        DrawingHelper.SCREEN_HEIGHT - 160);
    myBuffer.setFont(Styles.largerLargeFont);
    myBuffer.drawString(
        "Money: " + mGameContainer.getPlayer().getMoney(),
        DrawingHelper.SCREEN_WIDTH - 250,
        DrawingHelper.SCREEN_HEIGHT - 120);
    myBuffer.drawString(
        "Number of Pokemon Seen: " + mGameContainer.getPlayer().getNumSeenPokemon(),
        DrawingHelper.SCREEN_WIDTH - 250,
        DrawingHelper.SCREEN_HEIGHT - 95);
    myBuffer.drawString(
        "Number of Pokemon Caught: " + mGameContainer.getPlayer().getNumCaughtPokemon(),
        DrawingHelper.SCREEN_WIDTH - 250,
        DrawingHelper.SCREEN_HEIGHT - 70);
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
        attackOptions[i].setText("" + mGameContainer.getGameScreen().myPoke.getAttackOne());
      } else if (i == 1) {
        attackOptions[i].setText("" + mGameContainer.getGameScreen().myPoke.getAttackTwo());
      } else if (i == 2) {
        attackOptions[i].setText("" + mGameContainer.getGameScreen().myPoke.getAttackThree());
      } else {
        attackOptions[i].setText("" + mGameContainer.getGameScreen().myPoke.getAttackFour());
      }
    }
  }

  private void drawOptions(Graphics myBuffer) {
    for (OptionBoard option : getOptionsForCurrentPanel()) {
      option.draw(myBuffer);
    }
  }
}