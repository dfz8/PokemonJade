package pokemon.entities;

import pokemon.GameScreen;
import pokemon.ImageLibrary;

import javax.swing.*;
import java.awt.*;

public class Terrain {
  /*
    mapGuidelines:
    -on the top and bottom of the walkable area, put 5 lines of black space
    -on the left and right of the walkable area, put 6 lines of black space
    -for each gate, put down where it is located, which map it links to, and its destination row
    and column on that map
    -for each building, put down the coordinate of the top left corner.
    -look at terrainTest, terrainTest2, and terrainTest3 for examples
    -look at mapPart1, mapPart2, and mapPart3 for an example of how to break up a map into
    smaller chunks

    key:
    neg numbers = terrain you can't walk through
  */

  public static final int UNKNOWN = -12345;

  public static final int POKEMON_CENTER_UL = -1001; //P1
  public static final int POKEMON_CENTER_UM = -1002; //P2
  public static final int POKEMON_CENTER_UR = -1003; //P3
  public static final int POKEMON_CENTER_ML = -1004; //P4
  public static final int POKEMON_CENTER_MM = -1005; //P5
  public static final int POKEMON_CENTER_MR = -1006; //P6
  public static final int POKEMON_CENTER_TL = -1007; //P7
  public static final int POKEMON_CENTER_TM = -1008; //P8
  public static final int POKEMON_CENTER_TR = -1009; //P9

  public static final int SIGNPOST = -12;          // s
  public static final int TREE_NORMAL = -11;       // t
  public static final int TREE_DARK = -10;         // T

  public static final int BLACK_SPACE = 0;         // _
  public static final int DOORMAT_UP_LEFT = 1;     // A
  public static final int DOORMAT_DOWN_LEFT = 2;   // a
  public static final int DOORMAT_UP_RIGHT = 3;    // B
  public static final int DOORMAT_DOWN_RIGHT = 4;  // b
  public static final int GROUND = 5;              // .
  public static final int GRASS = 6;               // g
  public static final int FOREST_GRASS_NORMAL = 7; // ,
  public static final int FLOWERS = 8;             // y

  public static final int HEALING_TILE = 500;      // +
  public static final int GATE_GROUND = 501;       // !
  public static final int GATE_BLACK_SPACE = 501;  // @
  public static final int GATE_FOREST_GRASS_NORMAL = 502; //#

  private ImageIcon myImage;
  private int myTerrainType;
  private boolean canGoHere;

  private String mapLink;
  private int mapLinkR;
  private int mapLinkC;

  public Terrain(String s) {
    switch (s) {
      case "_":
        myTerrainType = BLACK_SPACE;
        myImage = ImageLibrary.blackSpace;
        break;
      case "@":
        myTerrainType = GATE_BLACK_SPACE;
        myImage = ImageLibrary.blackSpace;
        break;
      case "!":
        myTerrainType = GATE_GROUND;
        myImage = ImageLibrary.ground;
        break;
      case "#":
        myTerrainType = GATE_FOREST_GRASS_NORMAL;
        myImage = ImageLibrary.grass_normal;
        break;
      case "A":
        myTerrainType = DOORMAT_UP_LEFT;
        myImage = ImageLibrary.doorMat_up_left;
        break;
      case "a":
        myTerrainType = DOORMAT_DOWN_LEFT;
        myImage = ImageLibrary.doorMat_down_left;
        break;
      case "B":
        myTerrainType = DOORMAT_UP_RIGHT;
        myImage = ImageLibrary.doorMat_up_right;
        break;
      case "b":
        myTerrainType = DOORMAT_DOWN_RIGHT;
        myImage = ImageLibrary.doorMat_down_right;
        break;
      case ".":
        myTerrainType = GROUND;
        myImage = ImageLibrary.ground;
        break;
      case "g":
        myTerrainType = GRASS;
        myImage = ImageLibrary.grass;
        break;
      case "t":
        myTerrainType = TREE_NORMAL;
        myImage = ImageLibrary.tree_normal;
        break;
      case "T":
        myTerrainType = TREE_DARK;
        myImage = ImageLibrary.tree_dark;
        break;
      case ",":
        myTerrainType = FOREST_GRASS_NORMAL;
        myImage = ImageLibrary.grass_normal;
        break;
      case "y":
        myTerrainType = FLOWERS;
        myImage = ImageLibrary.flowers;
        break;
      case "S":
        myTerrainType = SIGNPOST;
        myImage = ImageLibrary.signpost;
        break;
      case "P1":
        myTerrainType = POKEMON_CENTER_UL;
        myImage = ImageLibrary.building_pokemonCenter_top_left;
        break;
      case "P2":
        myTerrainType = POKEMON_CENTER_UM;
        myImage = ImageLibrary.building_pokemonCenter_top_middle;
        break;
      case "P3":
        myTerrainType = POKEMON_CENTER_UR;
        myImage = ImageLibrary.building_pokemonCenter_top_right;
        break;
      case "P4":
        myTerrainType = POKEMON_CENTER_ML;
        myImage = ImageLibrary.building_pokemonCenter_middle_left;
        break;
      case "P5":
        myTerrainType = POKEMON_CENTER_MM;
        myImage = ImageLibrary.building_pokemonCenter_middle_middle;
        break;
      case "P6":
        myTerrainType = POKEMON_CENTER_MR;
        myImage = ImageLibrary.building_pokemonCenter_middle_right;
        break;
      case "P7":
        myTerrainType = POKEMON_CENTER_TL;
        myImage = ImageLibrary.building_pokemonCenter_bottom_left;
        break;
      case "P8":
        myTerrainType = POKEMON_CENTER_TM;
        myImage = ImageLibrary.building_pokemonCenter_bottom_middle;
        break;
      case "P9":
        myTerrainType = POKEMON_CENTER_TR;
        myImage = ImageLibrary.building_pokemonCenter_bottom_right;
        break;
      case "+":
        myTerrainType = HEALING_TILE;
        myImage = ImageLibrary.healTile;
        break;
      default:
        myTerrainType = UNKNOWN;
        myImage = ImageLibrary.blackSpace;
    }

    canGoHere = myTerrainType >= 0;
  }

  public boolean canMoveHere() {
    return canGoHere;
  }

  public boolean isGate() {
    return myTerrainType <= GATE_FOREST_GRASS_NORMAL && myTerrainType >= GATE_GROUND;
  }

  public boolean isBuilding() {
    return myTerrainType != UNKNOWN && myTerrainType <= -1000;
  }

  public int getType() {
    return myTerrainType;
  }

  public String getMapLink() {
    return mapLink;
  }

  public int getMapLinkRow() {
    return mapLinkR;
  }

  public int getMapLinkCol() {
    return mapLinkC;
  }

  public void setMapLink(String m) {
    mapLink = m;
  }

  public void setMapLinkCoordinates(int r, int c) {
    mapLinkR = r;
    mapLinkC = c;
  }

  public void draw(Graphics g, int x, int y) {
    g.drawImage(myImage.getImage(),
                x * GameScreen.SPRITE_WIDTH - GameScreen.SPRITE_WIDTH / 2,
                y * GameScreen.SPRITE_HEIGHT - GameScreen.SPRITE_HEIGHT / 2,
                null);
  }
}