package pokemon;//David Zhao, 5/14/2011 2:34 AM

import javax.swing.*;

public class ImageLibrary {
  //rest
  public static ImageIcon faceUp = new ImageIcon("resources/images/boy_walk_up_rest.png");
  public static ImageIcon faceDown = new ImageIcon("resources/images/boy_walk_down_rest.png");
  public static ImageIcon faceLeft = new ImageIcon("resources/images/boy_walk_left_rest.png");
  public static ImageIcon faceRight = new ImageIcon("resources/images/boy_walk_right_rest.png");
  //walking
  public static ImageIcon faceUpLeft = new ImageIcon("resources/images/boy_walk_up_left.png");
  public static ImageIcon faceDownLeft = new ImageIcon("resources/images/boy_walk_down_left.png");
  public static ImageIcon faceLeftLeft = new ImageIcon("resources/images/boy_walk_left_left.png");
  public static ImageIcon faceRightLeft = new ImageIcon("resources/images/boy_walk_right_left.png");
  public static ImageIcon faceUpRight = new ImageIcon("resources/images/boy_walk_up_right.png");
  public static ImageIcon faceDownRight = new ImageIcon("resources/images/boy_walk_down_right.png");
  public static ImageIcon faceLeftRight = new ImageIcon("resources/images/boy_walk_left_right.png");
  public static ImageIcon faceRightRight = new ImageIcon("resources/images/boy_walk_right_right.png");
  public static ImageIcon[] moveUp_walk = {faceUpLeft, faceUp, faceUpRight, faceUp};
  public static ImageIcon[] moveDown_walk = {faceDownLeft, faceDown, faceDownRight, faceDown};
  public static ImageIcon[] moveLeft_walk = {faceLeftLeft, faceLeft, faceLeftRight, faceLeft};
  public static ImageIcon[] moveRight_walk = {faceRightLeft, faceRight, faceRightRight, faceRight};
  public static ImageIcon[][] movementSprites = {moveUp_walk, moveLeft_walk, moveRight_walk,
      moveDown_walk};
  //terrain
  public static ImageIcon blackSpace = new ImageIcon("resources/images/terrain_blackSpace.png");
  public static ImageIcon gate = blackSpace;  //new ImageIcon("resources/images/terrain_gate.png");
  public static ImageIcon grass = new ImageIcon("resources/images/terrain_grass.png");
  public static ImageIcon ground = new ImageIcon("resources/images/terrain_ground.png");
  public static ImageIcon doorMat_up_left = new ImageIcon("resources/images/terrain_doorMat_up_left.png");
  public static ImageIcon doorMat_down_left = new ImageIcon(
      "resources/images/terrain_doorMat_down_left.png");
  public static ImageIcon doorMat_up_right = new ImageIcon("resources/images/terrain_doorMat_up_right" +
                                                           ".png");
  public static ImageIcon doorMat_down_right = new ImageIcon(
      "resources/images/terrain_doorMat_down_right.png");
  //forest
  public static ImageIcon grass_normal = new ImageIcon("resources/images/terrain_forest_grass.png");
  public static ImageIcon grass_light = new ImageIcon("resources/images/terrain_forest_grass_light.png");
  public static ImageIcon grass_dark = new ImageIcon("resources/images/terrain_forest_grass_dark.png");
  public static ImageIcon tree_normal = new ImageIcon("resources/images/tree_normal.png");
  public static ImageIcon tree_dark = new ImageIcon("resources/images/tree_dark.png");
  public static ImageIcon signpost = new ImageIcon("resources/images/signpost.png");
  public static ImageIcon flowers = new ImageIcon("resources/images/terrain_flowers.png");
  //buildings
  public static ImageIcon building_pokemonCenter_bottom_left = new ImageIcon(
      "resources/images/pokemonCenter_bottom_left.png");
  public static ImageIcon building_pokemonCenter_bottom_middle = new ImageIcon(
      "resources/images/pokemonCenter_bottom_middle.png");
  public static ImageIcon building_pokemonCenter_bottom_right = new ImageIcon(
      "resources/images/pokemonCenter_bottom_right.png");

  public static ImageIcon building_pokemonCenter_middle_left = new ImageIcon(
      "resources/images/pokemonCenter_middle_left.png");
  public static ImageIcon building_pokemonCenter_middle_middle = new ImageIcon(
      "resources/images/pokemonCenter_middle_middle.png");
  public static ImageIcon building_pokemonCenter_middle_right = new ImageIcon(
      "resources/images/pokemonCenter_middle_right.png");

  public static ImageIcon building_pokemonCenter_top_left = new ImageIcon(
      "resources/images/pokemonCenter_top_left.png");
  public static ImageIcon building_pokemonCenter_top_middle = new ImageIcon(
      "resources/images/pokemonCenter_top_middle.png");
  public static ImageIcon building_pokemonCenter_top_right = new ImageIcon(
      "resources/images/pokemonCenter_top_right.png");

  public static ImageIcon healTile = new ImageIcon("resources/images/terrain_heal.jpg");

}