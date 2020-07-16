package pokemon;

import pokemon.controllers.PlayerController;
import pokemon.enums.Menu;
import pokemon.ui.OptionBoard;
import pokemon.ui.TextPlacement;
import pokemon.util.DrawingHelper;

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
                DrawingHelper.SCREEN_WIDTH - 60,
                DrawingHelper.SCREEN_HEIGHT - 40,
                50,
                35,
                Color.BLUE,
                "Back")};
      case battle:
        return new OptionBoard[]{
            new OptionBoard(
                DrawingHelper.SCREEN_WIDTH - 260,
                DrawingHelper.SCREEN_HEIGHT - 45,
                70,
                35,
                PLAIN,
                "Bag"),
            new OptionBoard(
                DrawingHelper.SCREEN_WIDTH - 205,
                DrawingHelper.SCREEN_HEIGHT - 195,
                150,
                50,
                PLAIN,
                "Fight"),
            new OptionBoard(
                DrawingHelper.SCREEN_WIDTH - 170,
                DrawingHelper.SCREEN_HEIGHT - 45,
                70,
                35,
                PLAIN,
                "Run"),
            new OptionBoard(
                DrawingHelper.SCREEN_WIDTH - 85,
                DrawingHelper.SCREEN_HEIGHT - 45,
                70,
                35,
                PLAIN,
                "Pokemon")};
      case attackSelection:
        return new OptionBoard[]{
            new OptionBoard(
                DrawingHelper.SCREEN_WIDTH - 245,
                DrawingHelper.SCREEN_HEIGHT - 195,
                70,
                35,
                PLAIN,
                "1"),
            new OptionBoard(
                DrawingHelper.SCREEN_WIDTH - 245,
                DrawingHelper.SCREEN_HEIGHT - 115,
                70,
                35,
                PLAIN,
                "2"),
            new OptionBoard(
                DrawingHelper.SCREEN_WIDTH - 165,
                DrawingHelper.SCREEN_HEIGHT - 195,
                70,
                35,
                PLAIN,
                "3"),
            new OptionBoard(
                DrawingHelper.SCREEN_WIDTH - 165,
                DrawingHelper.SCREEN_HEIGHT - 115,
                70,
                35,
                PLAIN,
                "4"),
            new OptionBoard(
                DrawingHelper.SCREEN_WIDTH - 60,
                DrawingHelper.SCREEN_HEIGHT - 45,
                50,
                35,
                PLAIN,
                "Back")};
      case pokemonSelect:
        return new OptionBoard[]{
            new OptionBoard(0, 0, 50, 20, GREEN, "Move"),
            new OptionBoard(0, 0, 50, 20, GREEN, "Summary"),
            new OptionBoard(0, 0, 50, 20, GREEN, "Back")
        };
    }
    return null;
  }
}
