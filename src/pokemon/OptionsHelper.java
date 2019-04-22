package pokemon;

import pokemon.controllers.PlayerController;
import pokemon.enums.Menu;
import pokemon.ui.OptionBoard;
import pokemon.ui.TextPlacement;

import java.awt.*;

public class OptionsHelper {

  private final static Color GREEN = Color.GREEN;
  private final static Color PLAIN = Color.WHITE;

  public static OptionBoard[] setUpFor(Menu menu, PlayerController player) {
    switch (menu) {
      case main:
        return new OptionBoard[]{
            new OptionBoard(10, 10, 100, 35, GREEN, "Pokemon"),
            new OptionBoard(150, 10, 100, 35, GREEN, "Pokedex"),
            new OptionBoard(10, 60, 100, 35, GREEN, player.getName()),
            new OptionBoard(150, 60, 100, 35, GREEN, "Bag"),
            new OptionBoard(10, 110, 100, 35, GREEN, "Save"),
            new OptionBoard(150, 110, 100, 35, GREEN, "Options")};
      case party:
        return new OptionBoard[]{
            new OptionBoard(10, 10, 120, 45, PLAIN, "", TextPlacement.HIGH),
            new OptionBoard(135, 10, 120, 45, PLAIN, "", TextPlacement.HIGH),
            new OptionBoard(10, 65, 120, 45, PLAIN, "", TextPlacement.HIGH),
            new OptionBoard(135, 65, 120, 45, PLAIN, "", TextPlacement.HIGH),
            new OptionBoard(10, 120, 120, 45, PLAIN, "", TextPlacement.HIGH),
            new OptionBoard(135, 120, 120, 45, PLAIN, "", TextPlacement.HIGH),
            new OptionBoard(
                GameDriver.SCREEN_WIDTH - 60,
                GameDriver.SCREEN_HEIGHT - 40,
                50,
                35,
                Color.BLUE,
                "Back")};
      case pokedex:
        return new OptionBoard[]{
            new OptionBoard(10, 10, 120, 20, PLAIN, ""),
            new OptionBoard(10, 40, 120, 20, PLAIN, ""),
            new OptionBoard(10, 70, 120, 20, PLAIN, ""),
            new OptionBoard(10, 100, 120, 20, PLAIN, ""),
            new OptionBoard(10, 130, 120, 20, PLAIN, ""),
            new OptionBoard(10, 160, 120, 20, PLAIN, ""),
            new OptionBoard(10, 190, 120, 20, PLAIN, ""),
            new OptionBoard(
                GameDriver.SCREEN_WIDTH - 130,
                GameDriver.SCREEN_HEIGHT - 60,
                50,
                20,
                PLAIN,
                "Up"),
            new OptionBoard(
                GameDriver.SCREEN_WIDTH - 130,
                GameDriver.SCREEN_HEIGHT - 30,
                50,
                20,
                PLAIN,
                "Down"),
            new OptionBoard(
                GameDriver.SCREEN_WIDTH - 70,
                GameDriver.SCREEN_HEIGHT - 60,
                50,
                50,
                PLAIN,
                "Back")};
      case battle:
        return new OptionBoard[]{
            new OptionBoard(
                GameDriver.SCREEN_WIDTH - 260,
                GameDriver.SCREEN_HEIGHT - 45,
                70,
                35,
                PLAIN,
                "Bag"),
            new OptionBoard(
                GameDriver.SCREEN_WIDTH - 205,
                GameDriver.SCREEN_HEIGHT - 195,
                150,
                50,
                PLAIN,
                "Fight"),
            new OptionBoard(
                GameDriver.SCREEN_WIDTH - 170,
                GameDriver.SCREEN_HEIGHT - 45,
                70,
                35,
                PLAIN,
                "Run"),
            new OptionBoard(
                GameDriver.SCREEN_WIDTH - 85,
                GameDriver.SCREEN_HEIGHT - 45,
                70,
                35,
                PLAIN,
                "Pokemon")};
      case attackSelection:
        return new OptionBoard[]{
            new OptionBoard(
                GameDriver.SCREEN_WIDTH - 245,
                GameDriver.SCREEN_HEIGHT - 195,
                70,
                35,
                PLAIN,
                "1"),
            new OptionBoard(
                GameDriver.SCREEN_WIDTH - 245,
                GameDriver.SCREEN_HEIGHT - 115,
                70,
                35,
                PLAIN,
                "2"),
            new OptionBoard(
                GameDriver.SCREEN_WIDTH - 165,
                GameDriver.SCREEN_HEIGHT - 195,
                70,
                35,
                PLAIN,
                "3"),
            new OptionBoard(
                GameDriver.SCREEN_WIDTH - 165,
                GameDriver.SCREEN_HEIGHT - 115,
                70,
                35,
                PLAIN,
                "4"),
            new OptionBoard(
                GameDriver.SCREEN_WIDTH - 60,
                GameDriver.SCREEN_HEIGHT - 45,
                50,
                35,
                PLAIN,
                "Back")};
      case bag:
        return new OptionBoard[]{
            new OptionBoard(
                GameDriver.SCREEN_WIDTH - 60,
                GameDriver.SCREEN_HEIGHT - 45,
                50,
                35,
                PLAIN,
                "Back"),
            new OptionBoard(
                GameDriver.SCREEN_WIDTH - 245,
                GameDriver.SCREEN_HEIGHT - 195,
                70,
                35,
                PLAIN,
                "Pokeball")
        };
      case pokemonSelect:
        return new OptionBoard[]{
            new OptionBoard(0, 0, 50, 20, GREEN, "Move"),
            new OptionBoard(-10, -10, 0, 0, Color.BLACK, "dud"),
            new OptionBoard(0, 0, 50, 20, GREEN, "Summary"),
            new OptionBoard(0, 0, 50, 20, GREEN, "Back")
        };
      case save:
        return new OptionBoard[]{
            new OptionBoard(
                20,
                20,
                GameDriver.SCREEN_WIDTH - 50,
                GameDriver.SCREEN_HEIGHT - 50,
                Color.lightGray,
                "")
        };
      case personal:
        return new OptionBoard[]{
            new OptionBoard(
                20,
                20,
                GameDriver.SCREEN_WIDTH - 50,
                GameDriver.SCREEN_HEIGHT - 50,
                Color.ORANGE,
                "")
        };
    }
    return null;
  }
}
