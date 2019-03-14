package pokemon;//David Zhao, 3/31/2011

import pokemon.controllers.MovementController;
import pokemon.entities.Pokemon;

import javax.swing.*;
import java.awt.*;
import java.awt.event.*;
import java.io.*;

public class PlayPanel extends JPanel {
  public static GameScreen game;
  public static OptionsPanel options;

  //Personal
  static String myName = "";
  static Pokemon[] myPokemon; //first six is party, empty slots are "null"
  static boolean[] hasPokemon = new boolean[Pokemon.pokemons.length]; //tells the user if they
  // have that pokemon at one time or not.
  static boolean[] hasSeenPokemon = new boolean[hasPokemon.length];
  static int myMoney = 0;

  private MovementController mMovementController;

  public PlayPanel() {
    myName = "tester";
    uploadInformation(myName);

    //initGame();
    mMovementController = new MovementController();

    setLayout(new GridLayout(2, 1));

    game = new GameScreen(this, mMovementController);
    game.canMove = true;
    add(game);

    options = new OptionsPanel(game);
    add(options);


    addKeyListener(new Key());
    addMouseMotionListener(new Mouse());
    addMouseListener(new MouseClicks());
    setFocusable(true);
  }

  private static void initGame() {
    AlertHelper.alert("Welcome to pokemon.entities.Pokemon: Jade!");
    String choice = JOptionPane.showInputDialog("1. New game?\n2. Saved game?");
    if (choice.equals("2")) {
      myName = JOptionPane.showInputDialog("What's your player name? (case sensitive)");
      uploadInformation(myName);
    } else {
      runNux();
      myPokemon = new Pokemon[100];
      myMoney = 3000;
      GameScreen.mapName = "hometown";
      GameScreen.curR = 7;
      GameScreen.curC = 11;
      myPokemon = new Pokemon[100];
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
      hasPokemon[0] = true;
      hasSeenPokemon[0] = true;
      save();
    }
  }

  private static void runNux() {
    AlertHelper.alert("Welcome to the world of pokemon.entities.Pokemon! But first...");
    AlertHelper.alert("I'm Professor Oak, a pokemon.entities.Pokemon expert.");
    AlertHelper.alert(
        "I'm glad you want to be a pokemon trainer!  It takes courage" +
        " and hard work to be a great one.");
    AlertHelper.alert(
        "In this world, you can explore, battle pokemon, and, if you " +
        "can, capture the pokemon.");
    AlertHelper.alert(
        "Since you're the last one to come for your first pokemon, I " +
        "sorta ran out. ^^;");
    AlertHelper.alert(
        "The only pokemon I can give you is Bubbles, a level 50 " +
        "bulbasaur.");
    AlertHelper.alert(
        "Unfortunately, you won't be able to experience the " +
        "bittersweet memories of training a pokemon from scratch " +
        "and watching as the pokemon and you improve everyday.");
    AlertHelper.alert("But actually, it's not that fun. -.-");
    AlertHelper.alert(
        "So now you can expericence the sheer awesomeness of totally " +
        "wrecking at pokemon battles!");
    AlertHelper.alert("Oh by the way...");

    int yesno;
    do {
      myName = JOptionPane.showInputDialog("What is your name?");
      yesno = JOptionPane.showConfirmDialog(null, "Cool! So your name is " + myName + ".");

      boolean fileExists = false;
      try {
        BufferedReader test =
            new BufferedReader(new FileReader(new File("./savefiles/" + myName + ". ")));
        fileExists = true;
      } catch (FileNotFoundException e) {
      }
      if (fileExists) {
        AlertHelper.alert(
            "I'm sorry. Someone already used that player name. " +
            "\n\tPlease use a different one.");
        yesno = JOptionPane.YES_OPTION - 1; //prob def not an option
      }

    }
    while (yesno != JOptionPane.YES_OPTION);

    int noob = JOptionPane.showConfirmDialog(null, "Are you new to this game?");
    if (noob == JOptionPane.YES_OPTION) {
      AlertHelper.alert(
          "To move, the basic game commands still apply.\nJust go " +
          "ahead and use your arrow keys.\nUp: Arrow up\nRight: " +
          "Arrow right\nDown: Arrow down\n Left: Arrow left");
      AlertHelper.alert(
          "To access information or save the game, just click the " +
          "corresponding button under the game screen.\nREMEMBER " +
          "TO SAVE OFTEN");
      AlertHelper.alert(
          "And catching a pokemon's just as easy!\nClick the option " +
          "when you think it's time to capture the pokemon.\nIf " +
          "the pokemon's health is low enough or you get lucky, " +
          "you'll catch a pokemon!");
      AlertHelper.alert("YEAH! GOTTA CATCH 'EM ALL, POKEMON!");
    }
    AlertHelper.alert(
        "Pay close attention to white tiles with a red cross. " +
        "\nThat's the healing tile. \nWalk over it to heal your " +
        "pokemon.");
  }

  public static void uploadInformation(String name) {
    try {
      BufferedReader in = new BufferedReader(new FileReader(new File("./savefiles/" + name + ".pksf"
      )));
      myName = name;

      myMoney = Integer.parseInt(in.readLine());

      //for(int i = 0; i < hasPokemon.length; i++)
      //hasPokemon[i] = false;

      //your pokemon
      myPokemon = new Pokemon[Integer.parseInt(in.readLine())];
      for (int i = 0; i < myPokemon.length; i++) {
        String input = in.readLine();
        if (input.equals("null"))
          myPokemon[i] = null;
        else {
          myPokemon[i] = new Pokemon.Builder().createFrom(input);
          hasPokemon[Pokemon.getIndex(myPokemon[i].getName())] = true; ///<----remove later,
          hasSeenPokemon[Pokemon.getIndex(myPokemon[i].getName())] = true; // also remove later
        }
      }

      GameScreen.mapName = in.readLine();
      GameScreen.curR = Integer.parseInt(in.readLine());
      GameScreen.curC = Integer.parseInt(in.readLine());

      for (int i = 0; i < hasPokemon.length; i++) {
        String[] info = in.readLine().split(" ");


        if (info[1].equals("true"))
          hasSeenPokemon[i] = true;
        // else
        // hasSeenPokemon[i] = false;

        if (info[0].equals("true")) {
          hasPokemon[i] = true;
          hasSeenPokemon[i] = true;
        }
        // else
        // hasPokemon[i] = false;

      }
    } catch (IOException e) {
    }
  }

  public static void save() {
    try {
      PrintStream out = new PrintStream(new FileOutputStream("./savefiles/" + myName + ".pksf"));
      out.println(myMoney + "");

      //your pokemon
      out.println(myPokemon.length + "");
      for (int i = 0; i < myPokemon.length; i++) {
        if (myPokemon[i] == null)
          out.println("null");
        else
          out.println(myPokemon[i].toString());
      }
      out.println(game.mapName);
      out.println("" + game.curR);
      out.println("" + game.curC);

      for (int i = 0; i < hasPokemon.length; i++) {
        if (hasPokemon[i])
          out.print("true");
        else
          out.print("false");

        if (hasSeenPokemon[i])
          out.print(" true");
        else
          out.print(" false");
        out.println();
      }

    } catch (IOException e) {
    }

  }

  public static boolean hasRepeat() {
    for (int pokeIndex = 0; pokeIndex < myPokemon.length; pokeIndex++) {

      if (myPokemon[pokeIndex] != null)
        if (game.enemy.getName().equals(myPokemon[pokeIndex].getName()))
          return true;
    }
    return false;

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
      options.updateHighlightsForOptions(e.getX(), e.getY());
    }
  }

  public class MouseClicks extends MouseAdapter {
    public void mouseClicked(MouseEvent e) {
      options.checkClick(e.getX(), e.getY());
    }
  }

  public static void toBattle(boolean tf) {
    if (tf) {
      OptionsNavigationHelper.toBattle();
      game.toBattle();
    } else {
      OptionsNavigationHelper.toNormal();
    }
  }

}