//David Zhao, 5/17/2011

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
  static final int GATE_GROUND = -1;       // !
  static final int GATE_BLACK_SPACE = -2;  // @
  static final int GATE_FOREST_GRASS_NORMAL = -3; //#
  static final int BLACK_SPACE = 0;         // _
  static final int DOORMAT_UP_LEFT = 1;     // A
  static final int DOORMAT_DOWN_LEFT = 2;   // a
  static final int DOORMAT_UP_RIGHT = 3;    // B
  static final int DOORMAT_DOWN_RIGHT = 4;  // b
  static final int GROUND = 5;              // .
  static final int GRASS = 6;               // g
  static final int TESTING = 9001;          // asdfg <--- it's over 9000!

  static final int TREE_NORMAL = -10;       // t
  static final int TREE_DARK = -11;         // T
  static final int FOREST_GRASS_NORMAL = 7; // ,
  static final int FLOWERS = 8;             // y
  static final int SIGNPOST = -12;          // s

  static final int HEALING_TILE = 500; // +

  static final int POKEMON_CENTER_UL = 1001; //P1
  static final int POKEMON_CENTER_UM = 1002; //P2
  static final int POKEMON_CENTER_UR = 1003; //P3
  static final int POKEMON_CENTER_ML = 1004; //P4
  static final int POKEMON_CENTER_MM = 1005; //P5
  static final int POKEMON_CENTER_MR = 1006; //P6
  static final int POKEMON_CENTER_TL = 1007; //P7
  static final int POKEMON_CENTER_TM = 1008; //P8
  static final int POKEMON_CENTER_TR = 1009; //P9

  private ImageIcon myImage = null;

  private int myType;
  private int myX;
  private int myY;
  private boolean canGoHere = true;

  private String mapLink;
  private int mapLink_r;
  private int mapLink_c;

  public Terrain(String s) {
    if (s.equals("_"))
      myType = BLACK_SPACE;
    else if (s.equals("@"))
      myType = GATE_BLACK_SPACE;
    else if (s.equals("!"))
      myType = GATE_GROUND;
    else if (s.equals("#"))
      myType = GATE_FOREST_GRASS_NORMAL;
    else if (s.equals("A"))
      myType = DOORMAT_UP_LEFT;
    else if (s.equals("a"))
      myType = DOORMAT_DOWN_LEFT;
    else if (s.equals("B"))
      myType = DOORMAT_UP_RIGHT;
    else if (s.equals("b"))
      myType = DOORMAT_DOWN_RIGHT;
    else if (s.equals("."))
      myType = GROUND;
    else if (s.equals("g"))
      myType = GRASS;
    else if (s.equals("asdfg"))
      myType = TESTING;
      // forest
    else if (s.equals("t"))
      myType = TREE_NORMAL;
    else if (s.equals("T"))
      myType = TREE_DARK;
    else if (s.equals(","))
      myType = FOREST_GRASS_NORMAL;
    else if (s.equals("y"))
      myType = FLOWERS;
    else if (s.equals("S"))
      myType = SIGNPOST;
      //building
    else if (s.equals("P1"))
      myType = POKEMON_CENTER_UL;
    else if (s.equals("P2"))
      myType = POKEMON_CENTER_UM;
    else if (s.equals("P3"))
      myType = POKEMON_CENTER_UR;
    else if (s.equals("P4"))
      myType = POKEMON_CENTER_ML;
    else if (s.equals("P5"))
      myType = POKEMON_CENTER_MM;
    else if (s.equals("P6"))
      myType = POKEMON_CENTER_MR;
    else if (s.equals("P7"))
      myType = POKEMON_CENTER_TL;
    else if (s.equals("P8"))
      myType = POKEMON_CENTER_TM;
    else if (s.equals("P9"))
      myType = POKEMON_CENTER_TR;
    else if (s.equals("+"))
      myType = HEALING_TILE;


    if (myType == BLACK_SPACE || myType <= TREE_NORMAL || myType > 1000)
      canGoHere = false;

    setImage();
  }

  public void canMoveHere(boolean tf) //update 1.01, added
  {
    canGoHere = tf;
  }

  public boolean canMoveHere() {
    return canGoHere;
  }

  public boolean isGate() //update 1.01, added
  {
    if (myType <= -1 && myType > TREE_NORMAL)//(myType == GATE_GROUND || myType == GATE_BLACK_SPACE)
      return true;
    return false;
  }

  public boolean isBuilding() //update 1.01, added
  {
    if (myType >= 1000)
      return true;
    return false;
  }

  public int getType() {
    return myType;
  }

  public String getMapLink() {
    return mapLink;
  }

  public int getMapLink_r() {
    return mapLink_r;
  }

  public int getMapLink_c() {
    return mapLink_c;
  }

  public void setMapLink(String m) {
    mapLink = m;
  }

  public void setMapLinkCoordinates(int r, int c) {
    mapLink_r = r;
    mapLink_c = c;
  }

  public void setImage() //update 1.01, changed if-else to a switch
  {

    if (myType < 1000)
      switch (myType) {
        case -2: //GATE_BLACK_SPACE
        case 0: // BLACK_SPACE
          myImage = ImageLibrary.blackSpace;
          break;
        case -1: //GATE_GROUND
        case 5://GROUND
          myImage = ImageLibrary.ground;
          break;
        case 1:
          myImage = ImageLibrary.doorMat_up_left;
          break;
        case 2:
          myImage = ImageLibrary.doorMat_down_left;
          break;
        case 3:
          myImage = ImageLibrary.doorMat_up_right;
          break;
        case 4:
          myImage = ImageLibrary.doorMat_down_right;
          break;
        case 6:
          myImage = ImageLibrary.grass;
          break;
        case -3:  //GATE_FOREST_GRASS_NORMAL
        case 7:  //FREST_GRASS_NORMAL
          myImage = ImageLibrary.grass_normal;
          break;
        case 8:
          myImage = ImageLibrary.flowers;
          break;
        case -10:
          myImage = ImageLibrary.tree_normal;
          break;
        case -11:
          myImage = ImageLibrary.tree_dark;
          break;
        case -12:
          myImage = ImageLibrary.signpost;
          break;
        case 500:
          myImage = ImageLibrary.healTile;
          break;
        default:
          myImage = ImageLibrary.blackSpace;
          break;
      }
    else if (myType >= 1000)
      switch (myType - 1000) {
        //pokemon center
        case 1:
          myImage = ImageLibrary.building_pokemonCenter_top_left;
          break;
        case 2:
          myImage = ImageLibrary.building_pokemonCenter_top_middle;
          break;
        case 3:
          myImage = ImageLibrary.building_pokemonCenter_top_right;
          break;
        case 4:
          myImage = ImageLibrary.building_pokemonCenter_middle_left;
          break;
        case 5:
          myImage = ImageLibrary.building_pokemonCenter_middle_middle;
          break;
        case 6:
          myImage = ImageLibrary.building_pokemonCenter_middle_right;
          break;

        case 7:
          myImage = ImageLibrary.building_pokemonCenter_bottom_left;
          break;
        case 8:
          myImage = ImageLibrary.building_pokemonCenter_bottom_middle;
          break;
        case 9:
          myImage = ImageLibrary.building_pokemonCenter_bottom_right;
          break;
      }
  }

  public void draw(Graphics g, int x, int y) {
    g.drawImage(myImage.getImage(),
                x * 23 - GameScreen.SPRITE_WIDTH / 2,
                y * 26 - GameScreen.SPRITE_HEIGHT / 2,
                null);
  }
}