package pokemon;

import pokemon.enums.Menu;

class OptionsNavigationHelper {
  private PlayPanel mPlayPanel;

  public OptionsNavigationHelper(PlayPanel playPanel) {
    mPlayPanel = playPanel;
  }

  public void toNormal() {
    OptionsPanel.curMenu = Menu.main;
    GameScreen.inBattle = false;
    mPlayPanel.getMovementController().setCanMove(true);
    GameScreen.inSummary = false;

    OptionsPanel.switchingPokemon = false;
    OptionsPanel.showPokemonSelectOptions = false;
    OptionsPanel.switchPokemonInd = -1;
  }

  public void toBag() {
    OptionsPanel.curMenu = Menu.bag;
    mPlayPanel.getMovementController().setCanMove(false);
    GameScreen.inBattle = true;
  }

  public void toBattle() {
    OptionsPanel.curMenu = Menu.battle;
    OptionsPanel.switchingPokemon = false;
  }

  public void toBlack() {
    OptionsPanel.curMenu = Menu.blackScreen;
  }

  public void toSummary(int i) {
    //game.summaryIndex = i;
    GameScreen.inSummary = true;
    GameScreen.summaryPoke = PlayPanel.myPokemon[i];

    OptionsPanel.showPokemonSelectOptions = false;
  }

  public void toPersonal() {
    OptionsPanel.curMenu = Menu.personal;
    mPlayPanel.getMovementController().setCanMove(false);
  }

  public void toPokedex() {
    OptionsPanel.curMenu = Menu.pokedex;
    OptionsPanel.pokedexStartInd = 1; //not array
    mPlayPanel.getMovementController().setCanMove(false);
  }

  public void toAttacks() {
    OptionsPanel.curMenu = Menu.attackSelection;
    mPlayPanel.getMovementController().setCanMove(false);
    GameScreen.inBattle = true;
  }

  public void toSave() {
    OptionsPanel.curMenu = Menu.save;
    GameScreen.inSave = true;
    mPlayPanel.getMovementController().setCanMove(false);
  }
}
