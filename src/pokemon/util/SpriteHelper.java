package pokemon.util;

import javax.swing.*;
import java.util.HashMap;
import java.util.Map;

public class SpriteHelper {
  private static final String BASE_RES_DIR = "./resources/images/";
  private static final String POKEMON_RES_DIR = "./resources/images/Pokemon/";

  private static Map<String, ImageIcon> mImages = new HashMap<>();

  public static ImageIcon getPokemonFront(String pokemonName) {
    if (!mImages.containsKey(pokemonName)) {
      System.out.println(pokemonName);
      mImages.put(pokemonName, new ImageIcon(POKEMON_RES_DIR + pokemonName + ".png"));
    }
    return mImages.get(pokemonName);
  }

  public static ImageIcon getPokemonBack(String pokemonName) {
    if (!mImages.containsKey(pokemonName)) {
      mImages.put(pokemonName, new ImageIcon(POKEMON_RES_DIR + pokemonName + "Back.png"));
    }
    return mImages.get(pokemonName);
  }

  public static ImageIcon getMisc(String spriteName) {
    if (!mImages.containsKey(spriteName)) {
      mImages.put(spriteName, new ImageIcon(BASE_RES_DIR + spriteName + ".png"));
    }
    return mImages.get(spriteName);

  }
}
