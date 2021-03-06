package pokemon;

import pokemon.controllers.BattleController;
import pokemon.controllers.MapController;
import pokemon.controllers.MovementController;
import pokemon.controllers.PlayerController;
import pokemon.entities.Pokemon;

import javax.swing.*;
import java.awt.*;
import java.awt.event.*;
import java.io.*;

import static pokemon.AlertHelper.alert;

/**
 * Contains and draws the two DS screens that are used to render the game and handles UI
 */
public class GameContainer extends JPanel {
  private static final String SAVE_DELIMITER = " ";

  private static GameplayScreen mGameplayScreen;
  private static GuiScreen mGuiScreen;

  public static Pokemon[] myPokemon; //first six is party, empty slots are "null"
  private static int mSummaryIndex;

  private PlayerController mPlayerController;
  private MovementController mMovementController;
  private MapController mMapController;
  private BattleController mBattleController;

  private String mInitialMapToLoad;

  public GameContainer() {
    initPreSetupControllers();
    initGameSetup();
    initPostSetupControllers();

    setLayout(new GridLayout(2, 1));
    mGameplayScreen = new GameplayScreen(this);
    add(mGameplayScreen);
    mGuiScreen = new GuiScreen(this);
    add(mGuiScreen);

    addKeyListener(new Key());
    addMouseMotionListener(new Mouse());
    addMouseListener(new MouseClicks());
    setFocusable(true);
  }

  private void initPreSetupControllers() {
    mMovementController = new MovementController();
  }

  private void initPostSetupControllers() {
    mMapController = new MapController(mInitialMapToLoad, mMovementController);
    mBattleController = new BattleController();
  }

  public PlayerController getPlayer() {
    return mPlayerController;
  }

  public MovementController getMovementController() {
    return mMovementController;
  }

  public MapController getMapController() {
    return mMapController;
  }

  public BattleController getBattleController() {
    return mBattleController;
  }

  public GuiScreen getOptionsPanel() {
    return mGuiScreen;
  }

  public GameplayScreen getGameScreen() {
    return mGameplayScreen;
  }

  public void setSummaryIndex(int index) {
    mSummaryIndex = index;
  }

  public Pokemon getPokemonForSummary() {
    return myPokemon[mSummaryIndex];
  }

  private void initGameSetup() {
    //    alert("Welcome to pokemon.entities.Pokemon: Jade!");
    //    String choice = JOptionPane.showInputDialog("1. New game?\n2. Saved game?");
    String choice = "2";
    if (choice.equals("2")) {
      String playerName = "tester";
      //      playerName = JOptionPane.showInputDialog("What's your player name? (case sensitive)");
      mPlayerController = loadSaveFileFor(playerName);
    } else {
      String playerName = runNux();
      mPlayerController = new PlayerController(playerName, 3000);

      mInitialMapToLoad = "hometown";
      mMovementController.setCoord(7, 11);

      myPokemon = new Pokemon[Pokemon.getNumPokemon()];
      myPokemon[0] = new Pokemon.Builder()
          .setName("Bulbasaur")
          .setType("Grass")
          .setFirstAttack("Tackle")
          .setSecondAttack("Vine Whip")
          .setThirdAttack("Absorb")
          .setFourthAttack("Headbutt")
          .setLevel(5)
          .setExp((int) (Math.pow(Pokemon.expBase, 5)))
          .setAttack(14)
          .setDefense(16)
          .setHp(26)
          .setMaxHp(26)
          .build();
      mPlayerController.initCaughtPokemon(myPokemon[0]);
      mPlayerController.markCaughtPokemon(myPokemon[0].getName());
      mPlayerController.markSeenPokemon(myPokemon[0].getName());
    }
  }

  private String runNux() {
    alert("Welcome to the world of pokemon.entities.Pokemon! But first...");
    alert("I'm Professor Oak, a pokemon.entities.Pokemon expert.");
    alert(
        "I'm glad you want to be a pokemon trainer!  It takes courage" +
        " and hard work to be a great one.");
    alert(
        "In this world, you can explore, battle pokemon, and, if you " +
        "can, capture the pokemon.");
    alert(
        "Since you're the last one to come for your first pokemon, I " +
        "sorta ran out. ^^;");
    alert(
        "The only pokemon I can give you is Bubbles, a level 50 " +
        "bulbasaur.");
    alert(
        "Unfortunately, you won't be able to experience the " +
        "bittersweet memories of training a pokemon from scratch " +
        "and watching as the pokemon and you improve everyday.");
    alert("But actually, it's not that fun. -.-");
    alert(
        "So now you can expericence the sheer awesomeness of totally " +
        "wrecking at pokemon battles!");
    alert("Oh by the way...");

    int yesno;
    String name;
    do {
      name = JOptionPane.showInputDialog("What is your name?");
      yesno = JOptionPane.showConfirmDialog(null, "Cool! So your name is " + name + ".");
      File test = new File("./savefiles/" + name + ". ");
      if (test.exists()) {
        alert(
            "I'm sorry. Someone already used that player name. " +
            "\n\tPlease use a different one.");
        yesno = JOptionPane.YES_OPTION - 1;
      }
    }
    while (yesno != JOptionPane.YES_OPTION);

    int noob = JOptionPane.showConfirmDialog(null, "Are you new to this game?");
    if (noob == JOptionPane.YES_OPTION) {
      alert(
          "To move, the basic game commands still apply.\nJust go " +
          "ahead and use your arrow keys.\nUp: Arrow up\nRight: " +
          "Arrow right\nDown: Arrow down\n Left: Arrow left");
      alert(
          "To access information or save the game, just click the " +
          "corresponding button under the game screen.\nREMEMBER " +
          "TO SAVE OFTEN");
      alert(
          "And catching a pokemon's just as easy!\nClick the option " +
          "when you think it's time to capture the pokemon.\nIf " +
          "the pokemon's health is low enough or you get lucky, " +
          "you'll catch a pokemon!");
      alert("YEAH! GOTTA CATCH 'EM ALL, POKEMON!");
    }
    alert(
        "Pay close attention to white tiles with a red cross. " +
        "\nThat's the healing tile. \nWalk over it to heal your " +
        "pokemon.");

    return name;
  }

  private PlayerController loadSaveFileFor(String name) {
    try {
      BufferedReader in = new BufferedReader(new FileReader(new File(
          "./savefiles/" + name + ".pksf")));
      PlayerController player = new PlayerController(name, Integer.parseInt(in.readLine()));

      int numPokemonInSaveFile = Integer.parseInt(in.readLine());

      myPokemon = new Pokemon[Pokemon.getNumPokemon()];
      // todo: switch to numPokemonInSaveFile after refactor
      String input;
      for (int i = 0; i < numPokemonInSaveFile; i++) {
        input = in.readLine();
        if (!input.equals("null")) {
          myPokemon[i] = new Pokemon.Builder().createFrom(input);
          player.initCaughtPokemon(myPokemon[i]);
        }
      }

      mInitialMapToLoad = in.readLine();
      mMovementController.setCoord(
          Integer.parseInt(in.readLine()),
          Integer.parseInt(in.readLine()));

      String[] pokedexInfo;
      for (int i = 0; i < numPokemonInSaveFile; i++) {
        pokedexInfo = in.readLine().split(SAVE_DELIMITER);
        if (pokedexInfo[0].equals("true")) {
          player.markCaughtPokemon(i);
          player.markSeenPokemon(i);
        } else if (pokedexInfo[1].equals("true")) {
          player.markSeenPokemon(i);
        }
      }
      return player;
    } catch (IOException e) {
      AlertHelper.fatal("Can not find save file for " + name);
      return null;
    }
  }

  public void save() {
    try {
      PrintStream out = new PrintStream(new FileOutputStream(
          "./savefiles/" + mPlayerController.getName() + ".pksf"));
      out.println(mPlayerController.getMoney());

      int numCaughtPokemon = mPlayerController.getNumCaughtPokemon();
      out.println(numCaughtPokemon);
      for (int i = 0; i < numCaughtPokemon; i++) {
        out.println(mPlayerController.getCaughtPokemon(i));
      }

      out.println(mMapController.getMapName());
      out.println(mMovementController.getRow());
      out.println(mMovementController.getCol());

      for (int i = 0; i < Pokemon.getNumPokemon(); i++) {
        out.print(mPlayerController.hasCaughtPokemon(i));
        out.print(SAVE_DELIMITER);
        out.println(mPlayerController.hasSeenPokemon(i));
      }

      alert("Saved!");
    } catch (IOException e) {
      AlertHelper.debug("Error when saving: " + e.getMessage());
    }
  }

  public void setState(GameState state) {
    mGameplayScreen.toState(state);
    mGuiScreen.toState(state);
  }

  public class Key extends KeyAdapter {
    public void keyPressed(KeyEvent e) {
      if (e.getKeyCode() == KeyEvent.VK_UP) {
        mMovementController.setUpPressed(true);
        mMovementController.setDownPressed(false);
        mMovementController.setLeftPressed(false);
        mMovementController.setRightPressed(false);
      } else if (e.getKeyCode() == KeyEvent.VK_DOWN) {
        mMovementController.setUpPressed(false);
        mMovementController.setDownPressed(true);
        mMovementController.setLeftPressed(false);
        mMovementController.setRightPressed(false);
      } else if (e.getKeyCode() == KeyEvent.VK_LEFT) {
        mMovementController.setUpPressed(false);
        mMovementController.setDownPressed(false);
        mMovementController.setLeftPressed(true);
        mMovementController.setRightPressed(false);
      } else if (e.getKeyCode() == KeyEvent.VK_RIGHT) {
        mMovementController.setUpPressed(false);
        mMovementController.setDownPressed(false);
        mMovementController.setLeftPressed(false);
        mMovementController.setRightPressed(true);
      }
    }

    public void keyReleased(KeyEvent e) {
      if (e.getKeyCode() == KeyEvent.VK_UP) {
        mMovementController.setUpPressed(false);
      } else if (e.getKeyCode() == KeyEvent.VK_DOWN) {
        mMovementController.setDownPressed(false);
      } else if (e.getKeyCode() == KeyEvent.VK_LEFT) {
        mMovementController.setLeftPressed(false);
      } else if (e.getKeyCode() == KeyEvent.VK_RIGHT) {
        mMovementController.setRightPressed(false);
      }
    }
  }

  public class Mouse extends MouseMotionAdapter {
    public void mouseMoved(MouseEvent e) {
      mGuiScreen.updateHighlightsForOptions(e.getX(), e.getY());
    }
  }

  public class MouseClicks extends MouseAdapter {
    public void mouseClicked(MouseEvent e) {
      mGuiScreen.checkClick(e.getX(), e.getY());
    }
  }
}