package pokemon.controllers;

import pokemon.AlertHelper;
import pokemon.GameDriver;
import pokemon.ImageLibrary;
import pokemon.entities.Pokemon;
import pokemon.entities.Terrain;

import javax.swing.*;
import java.awt.*;
import java.io.BufferedReader;
import java.io.File;
import java.io.FileReader;
import java.io.IOException;

import static pokemon.GameScreen.SPRITE_HEIGHT;
import static pokemon.GameScreen.SPRITE_WIDTH;

public class MapController {

  private final static String MAP_DIR = "./resources/maps/";
  private final static String SPLIT_CHAR = " ";

  private final MovementController mMovementController;

  private String mName;
  private Terrain[][] mMap;
  private boolean[][] mIsBuilding;
  private boolean[][] mHasItem;
  private String[] mAvailableWildPokemon;


  public MapController(String initialMapToLoad, MovementController movementController) {
    mName = initialMapToLoad;
    mMovementController = movementController;
    loadMap(mName);
  }

  public void maybeLoadNextMap() {
    int curR = mMovementController.getRow();
    int curC = mMovementController.getCol();
    if (mMap[curR][curC].isGate()) {

      int newR = mMap[curR][curC].getMapLinkRow();
      int newC = mMap[curR][curC].getMapLinkCol();
      loadMap(mMap[curR][curC].getMapLink());
      mMovementController.setCoord(newR, newC);

    }
  }

  private void loadMap(String name) {
    try {
      BufferedReader in = new BufferedReader(new FileReader(new File(MAP_DIR + name)));
      String[] mapDimens = split(in.readLine());
      int width = Integer.parseInt(mapDimens[0]);
      int height = Integer.parseInt(mapDimens[1]);

      String[][] mapSymbolForm = new String[height][width];
      Terrain[][] terrainMap = new Terrain[height][width];
      int gateCount = 0;
      int buildingCount = 0;
      for (int r = 0; r < height; r++) {
        mapSymbolForm[r] = split(in.readLine());
        for (int c = 0; c < width; c++) {
          terrainMap[r][c] = new Terrain(mapSymbolForm[r][c]);

          if (terrainMap[r][c].isGate()) {
            gateCount++;
          } else if (terrainMap[r][c].isBuilding()) {
            buildingCount++;
          }
        }
      }

      mIsBuilding = new boolean[height][width];
      mHasItem = new boolean[height][width];
      for (int r = 0; r < height; r++) {
        for (int c = 0; c < width; c++) {
          mIsBuilding[r][c] = false;
          mHasItem[r][c] = false;
        }
      }

      //setting up links between maps
      for (int i = 0; i < gateCount; i++) {
        String[] info = split(in.readLine());
        int c = Integer.parseInt(info[0]);
        int r = Integer.parseInt(info[1]);
        terrainMap[r][c].setMapLink(info[2]);
        terrainMap[r][c].setMapLinkCoordinates(
            Integer.parseInt(info[4]),
            Integer.parseInt(info[3]));
      }

      String[] locationInfo;

      //setting up buildings
      for (int i = 0; i < buildingCount; i++) {
        locationInfo = split(in.readLine());
        mIsBuilding[Integer.parseInt(locationInfo[1])][Integer.parseInt(locationInfo[0])] = true;
      }

      //setting up items (i.e pokeballs)
      int itemCount = Integer.parseInt(in.readLine());
      for (int i = 0; i < itemCount; i++) {
        locationInfo = split(in.readLine());
        mHasItem[Integer.parseInt(locationInfo[1])][Integer.parseInt(locationInfo[0])] = true;
      }

      //setting up wild pokemons
      mAvailableWildPokemon = new String[Integer.parseInt(in.readLine())];
      for (int i = 0; i < mAvailableWildPokemon.length; i++) {
        mAvailableWildPokemon[i] = in.readLine();
      }

      in.close();

      mName = name;
      mMap = terrainMap;
    } catch (IOException e) {
      AlertHelper.fatal("Could not load map: " + mName);
    }
  }

  public void drawMap(
      Graphics myBuffer,
      ImageIcon playerSprite) {
    int curR = mMovementController.getRow();
    int curC = mMovementController.getCol();
    for (int r = curR - 5; r < mMap.length && r < curR + 5; r++) {
      if (r < 0) {
        continue;
      }
      for (int c = curC - 8; c < mMap[r].length && c < curC + 8; c++) {
        if (c < 0) {
          continue;
        }

        if (mMap[r][c].getType() != Terrain.GROUND) {
          // drawSprites ground as a background for all sprites
          myBuffer.drawImage(
              ImageLibrary.ground.getImage(),
              (c - curC + 6) * SPRITE_WIDTH - SPRITE_WIDTH / 2,
              (r - curR + 4) * SPRITE_HEIGHT - SPRITE_HEIGHT / 2,
              null);
        }
        mMap[r][c].draw(myBuffer, c - curC + 6, r - curR + 4);
      }
    }

    myBuffer.drawImage(
        playerSprite.getImage(),
        GameDriver.SCREEN_WIDTH / 2 - SPRITE_WIDTH / 2,
        GameDriver.SCREEN_HEIGHT / 2 - SPRITE_HEIGHT / 2 - 5,
        null); // -5 for visual purposes
  }

  public boolean canMoveHere(int r, int c) {
    return mMap[r][c].canMoveHere();
  }

  public boolean isOnHealingTile() {
    return mMap[mMovementController.getRow()][mMovementController.getCol()].getType()
           == Terrain.HEALING_TILE;
  }

  public boolean canTriggerWildPokemonEncounter() {
    return mMap[mMovementController.getRow()][mMovementController.getCol()].getType()
           == Terrain.GRASS;
  }

  public Pokemon getRandomWildPokemon() {
    return Pokemon.generateRandomEnemy(
        mAvailableWildPokemon[(int) (Math.random() * mAvailableWildPokemon.length)]);
  }

  public String getMapName() {
    return mName;
  }

  private static String[] split(String s) {
    return s.split(SPLIT_CHAR);
  }
}
