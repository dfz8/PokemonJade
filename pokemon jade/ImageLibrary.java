//David Zhao, 5/14/2011 2:34 AM

import javax.swing.*;

public class ImageLibrary {
  static ImageIcon[] moveUp_bike;
  static ImageIcon[] moveDown_bike;
  static ImageIcon[] moveRight_bike;
  static ImageIcon[] moveLeft_bike;
  //rest
  static ImageIcon faceUp = new ImageIcon("resources/images/boy_walk_up_rest.png");
  static ImageIcon faceDown = new ImageIcon("resources/images/boy_walk_down_rest.png");
  static ImageIcon faceLeft = new ImageIcon("resources/images/boy_walk_left_rest.png");
  static ImageIcon faceRight = new ImageIcon("resources/images/boy_walk_right_rest.png");
  //walking
  static ImageIcon faceUpLeft = new ImageIcon("resources/images/boy_walk_up_left.png");
  static ImageIcon faceDownLeft = new ImageIcon("resources/images/boy_walk_down_left.png");
  static ImageIcon faceLeftLeft = new ImageIcon("resources/images/boy_walk_left_left.png");
  static ImageIcon faceRightLeft = new ImageIcon("resources/images/boy_walk_right_left.png");
  static ImageIcon faceUpRight = new ImageIcon("resources/images/boy_walk_up_right.png");
  static ImageIcon faceDownRight = new ImageIcon("resources/images/boy_walk_down_right.png");
  static ImageIcon faceLeftRight = new ImageIcon("resources/images/boy_walk_left_right.png");
  static ImageIcon faceRightRight = new ImageIcon("resources/images/boy_walk_right_right.png");
  static ImageIcon[] moveUp_walk = {faceUpLeft, faceUp, faceUpRight, faceUp};
  static ImageIcon[] moveDown_walk = {faceDownLeft, faceDown, faceDownRight, faceDown};
  static ImageIcon[] moveLeft_walk = {faceLeftLeft, faceLeft, faceLeftRight, faceLeft};
  static ImageIcon[] moveRight_walk = {faceRightLeft, faceRight, faceRightRight, faceRight};
  static ImageIcon[][] movementSprites = {moveUp_walk, moveDown_walk, moveLeft_walk,
      moveRight_walk};
  //terrain
  static ImageIcon blackSpace = new ImageIcon("resources/images/terrain_blackSpace.png");
  static ImageIcon gate = blackSpace;  //new ImageIcon("resources/images/terrain_gate.png");
  static ImageIcon grass = new ImageIcon("resources/images/terrain_grass.png");
  static ImageIcon ground = new ImageIcon("resources/images/terrain_ground.png");
  static ImageIcon doorMat_up_left = new ImageIcon("resources/images/terrain_doorMat_up_left.png");
  static ImageIcon doorMat_down_left = new ImageIcon(
      "resources/images/terrain_doorMat_down_left.png");
  static ImageIcon doorMat_up_right = new ImageIcon("resources/images/terrain_doorMat_up_right" +
                                                    ".png");
  static ImageIcon doorMat_down_right = new ImageIcon(
      "resources/images/terrain_doorMat_down_right.png");
  //forest
  static ImageIcon grass_normal = new ImageIcon("resources/images/terrain_forest_grass.png");
  static ImageIcon grass_light = new ImageIcon("resources/images/terrain_forest_grass_light.png");
  static ImageIcon grass_dark = new ImageIcon("resources/images/terrain_forest_grass_dark.png");
  static ImageIcon tree_normal = new ImageIcon("resources/images/tree_normal.png");
  static ImageIcon tree_dark = new ImageIcon("resources/images/tree_dark.png");
  static ImageIcon signpost = new ImageIcon("resources/images/signpost.png");
  static ImageIcon flowers = new ImageIcon("resources/images/terrain_flowers.png");
  //buildings
  static ImageIcon building_pokemonCenter_bottom_left = new ImageIcon(
      "resources/images/pokemonCenter_bottom_left.png");
  static ImageIcon building_pokemonCenter_bottom_middle = new ImageIcon(
      "resources/images/pokemonCenter_bottom_middle.png");
  static ImageIcon building_pokemonCenter_bottom_right = new ImageIcon(
      "resources/images/pokemonCenter_bottom_right.png");

  static ImageIcon building_pokemonCenter_middle_left = new ImageIcon(
      "resources/images/pokemonCenter_middle_left.png");
  static ImageIcon building_pokemonCenter_middle_middle = new ImageIcon(
      "resources/images/pokemonCenter_middle_middle.png");
  static ImageIcon building_pokemonCenter_middle_right = new ImageIcon(
      "resources/images/pokemonCenter_middle_right.png");

  static ImageIcon building_pokemonCenter_top_left = new ImageIcon(
      "resources/images/pokemonCenter_top_left.png");
  static ImageIcon building_pokemonCenter_top_middle = new ImageIcon(
      "resources/images/pokemonCenter_top_middle.png");
  static ImageIcon building_pokemonCenter_top_right = new ImageIcon(
      "resources/images/pokemonCenter_top_right.png");

  static ImageIcon testing = new ImageIcon("resources/images/building_pokemonCenter_small.png");

  static ImageIcon healTile = new ImageIcon("resources/images/terrain_heal.jpg");

  /// /pokemon
  // static ImageIcon bulbosaur = new ImageIcon("resources/images/bulbosaur.png");
  // static ImageIcon raikou = new ImageIcon("resources/images/raikou.png");

  // static ImageIcon bulbosaurBack = new ImageIcon("resources/images/bulbosaurBack.png");
  // testing icon added as of update 1.01
}