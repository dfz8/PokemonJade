class OptionsNavigationHelper {

  static void toNormal() {
    OptionsPanel.curPanel = OptionsPanel.normalPanel;
    GameScreen.inBattle = false;
    GameScreen.canMove = true;
    GameScreen.inSummary = false;

    OptionsPanel.switchingPokemon = false;
    OptionsPanel.showPokemonSelectOptions = false;
    OptionsPanel.switchPokemonInd = -1;
  }

  static void toBag() {
    OptionsPanel.curPanel = OptionsPanel.useItemPanel;
    GameScreen.canMove = false;
    GameScreen.inBattle = true;
  }

  static void toBattle() {
    OptionsPanel.curPanel = OptionsPanel.battlePanel;
    OptionsPanel.switchingPokemon = false;
  }

  static void toBlack() {
    OptionsPanel.curPanel = OptionsPanel.blackPanel;
  }

  static void toSummary(int i) {
    //game.summaryIndex = i;
    GameScreen.inSummary = true;
    GameScreen.summaryPoke = PlayPanel.myPokemon[i];

    OptionsPanel.showPokemonSelectOptions = false;
  }

  static void toPersonal() {
    OptionsPanel.curPanel = OptionsPanel.personalPanel;
    GameScreen.canMove = false;

    updatePokemonCount();
  }

  static void toPokedex() {
    OptionsPanel.curPanel = OptionsPanel.pokedexPanel;
    OptionsPanel.pokedexStartInd = 1; //not array
    GameScreen.canMove = false;

    updatePokemonCount();
  }

  static void toAttacks() {
    OptionsPanel.curPanel = OptionsPanel.attackSelectionPanel;
    GameScreen.canMove = false;
    GameScreen.inBattle = true;
  }

  static void toSave() {
    OptionsPanel.curPanel = OptionsPanel.savePanel;
    GameScreen.inSave = true;
    GameScreen.canMove = false;
    PlayPanel.save();
    updatePokemonCount();
  }

  private static void updatePokemonCount() {
    // todo: update counts on catch, not every time
    OptionsPanel.numPokemonSeen = 0;
    OptionsPanel.numPokemonOwned = 0;

    for (int i = 0; i < PlayPanel.hasSeenPokemon.length; i++) {
      if (PlayPanel.hasSeenPokemon[i])
        OptionsPanel.numPokemonSeen++;
      if (PlayPanel.hasPokemon[i])
        OptionsPanel.numPokemonOwned++;
    }
  }
}
