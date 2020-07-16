package pokemon.guioptions;

import pokemon.AlertHelper;
import pokemon.controllers.PlayerController;
import pokemon.entities.PokeBall;
import pokemon.entities.Pokemon;
import pokemon.GameContainer;
import pokemon.GameState;
import pokemon.ui.OnClickListener;
import pokemon.ui.OptionBoard;
import pokemon.ui.Styles;
import pokemon.util.DrawingHelper;
import pokemon.util.SpriteHelper;

import java.awt.*;
import javax.swing.*;

public class BagScreenGui implements GuiOption {

  private PlayerController mPlayer;
  private OptionBoard[] mBagOptions;


  public BagScreenGui(GameContainer gameContainer) {
    mPlayer = gameContainer.getPlayer();

    mBagOptions = new OptionBoard[]{
        new OptionBoard(
            DrawingHelper.SCREEN_WIDTH - 60,
            DrawingHelper.SCREEN_HEIGHT - 45,
            50,
            35,
            Styles.PLAIN,
            "Back"),
        new OptionBoard(
            DrawingHelper.SCREEN_WIDTH - 245,
            DrawingHelper.SCREEN_HEIGHT - 195,
            70,
            35,
            Styles.PLAIN,
            "Pokeball")};

    mBagOptions[0].setOnClickListener(
        new OnClickListener() {
            @Override
            public void onClick() {
              gameContainer.setState(GameState.BATTLE_DEFAULT);
            }
        });
    mBagOptions[1].setOnClickListener(
        new OnClickListener() {
            @Override
            public void onClick() {
              if (new PokeBall().isCaught()) {
                addNewPokemonToParty();
                gameContainer.setState(GameState.DEFAULT);
              } else {
                AlertHelper.alert("Awww, the pokemon broke out of the pokeball!");
                gameContainer.setState(GameState.BATTLE_WAIT_ON_ENEMY);
              }
            }

            private void addNewPokemonToParty() {
              Pokemon enemy = gameContainer.getGameScreen().enemy;
              mPlayer.markCaughtPokemon(enemy.getName());
              mPlayer.markSeenPokemon(enemy.getName());

              int index = 0;
              while (
                  index < GameContainer.myPokemon.length 
                  && GameContainer.myPokemon[index] != null) {
                index++;
              }
              GameContainer.myPokemon[index] = Pokemon.buildFrom(enemy).build();
            }
        });
  }

  public OptionBoard[] getOptions() {
    return mBagOptions;
  }

  public void onShow() {}

  public void drawScreen(Graphics buffer) {

  }
}
