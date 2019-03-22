package pokemon;

import pokemon.enums.Menu;

class OptionsNavigationHelper {

  static void toNormal() {
    OptionsPanel.curMenu = Menu.main;
    GameScreen.inBattle = false;
    GameScreen.canMove = true;
    GameScreen.inSummary = false;

    OptionsPanel.switchingPokemon = false;
    OptionsPanel.showPokemonSelectOptions = false;
    OptionsPanel.switchPokemonInd = -1;
  }

  static void toBag() {
    OptionsPanel.curMenu = Menu.bag;
    GameScreen.canMove = false;
    GameScreen.inBattle = true;
  }

  static void toBattle() {
    OptionsPanel.curMenu = Menu.battle;
    OptionsPanel.switchingPokemon = false;
  }

  static void toBlack() {
    OptionsPanel.curMenu = Menu.blackScreen;
  }

  static void toSummary(int i) {
    //game.summaryIndex = i;
    GameScreen.inSummary = true;
    GameScreen.summaryPoke = PlayPanel.myPokemon[i];

    OptionsPanel.showPokemonSelectOptions = false;
  }

  static void toPersonal() {
    OptionsPanel.curMenu = Menu.personal;
    GameScreen.canMove = false;
  }

  static void toPokedex() {
    OptionsPanel.curMenu = Menu.pokedex;
    OptionsPanel.pokedexStartInd = 1; //not array
    GameScreen.canMove = false;
  }

  static void toAttacks() {
    OptionsPanel.curMenu = Menu.attackSelection;
    GameScreen.canMove = false;
    GameScreen.inBattle = true;
  }

  static void toSave() {
    OptionsPanel.curMenu = Menu.save;
    GameScreen.inSave = true;
    GameScreen.canMove = false;
  }
}
