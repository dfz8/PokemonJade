package pokemon.util;

import pokemon.entities.Pokemon;

import javax.swing.*;
import java.awt.*;

public class DrawingHelper {
  public static final int SCREEN_WIDTH = 275;
  public static final int SCREEN_HEIGHT = 215;

  public static void drawImage(Graphics buffer, ImageIcon icon, int x, int y) {
    buffer.drawImage(icon.getImage(), x, y, null /* observer */);
  }

  public static void drawImage(
      Graphics buffer,
      ImageIcon icon,
      int x,
      int y,
      int width,
      int height) {
    buffer.drawImage(icon.getImage(), x, y, width, height, null /* observer */);
  }

  public static void drawFullSizeHPBar(Graphics buffer, Pokemon pokemon, int x, int y) {
    double percentage = 1.0 * pokemon.getCurrentHP() / pokemon.getMaxHP();
    drawPercentageBar(
        buffer,
        x,
        y,
        100,
        7,
        getPokemonHealthBarColor(percentage),
        percentage);
  }

  public static void drawHalfSizeHPBar(Graphics buffer, Pokemon pokemon, int x, int y) {
    double percentage = 1.0 * pokemon.getCurrentHP() / pokemon.getMaxHP();
    drawPercentageBar(
        buffer,
        x,
        y,
        50,
        5,
        getPokemonHealthBarColor(percentage),
        percentage);
  }

  public static void drawExpBar(Graphics buffer, Pokemon pokemon, int x, int y) {
    double percentage = (1.0 * pokemon.getMyEXP() - pokemon.getMyPastLevelEXP())
                        / (pokemon.getMyNextLevelEXP() - pokemon.getMyPastLevelEXP());
    drawPercentageBar(
        buffer,
        x,
        y,
        100,
        5,
        Color.BLUE,
        percentage);
  }

  private static Color getPokemonHealthBarColor(double healthPercent) {
    if (healthPercent < .25) {
      return Color.RED;
    }
    if (healthPercent < .5) {
      return Color.YELLOW;
    }
    return Color.GREEN;
  }

  private static void drawPercentageBar(
      Graphics buffer,
      int x,
      int y,
      int barWidth,
      int barHeight,
      Color barColor,
      double fillPercent) {
    buffer.setColor(Color.BLACK);
    buffer.drawRect(x - 1, y - 1, barWidth + 1, barHeight + 1);
    buffer.setColor(barColor);
    buffer.fillRect(
        x,
        y,
        (int) (barWidth * fillPercent),
        barHeight);
  }

}
