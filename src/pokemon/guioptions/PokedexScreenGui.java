package pokemon.guioptions;

import pokemon.controllers.PlayerController;
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

public class PokedexScreenGui implements GuiOption {

	private PlayerController mPlayer;
	private OptionBoard[] mPokedexOptions;
  private int mPokedexStartInd;


  public PokedexScreenGui(GameContainer gameContainer) {
    mPlayer = gameContainer.getPlayer();

    mPokedexOptions = new OptionBoard[]{
      new OptionBoard(10, 10, 120, 20, Styles.PLAIN, ""),
      new OptionBoard(10, 40, 120, 20, Styles.PLAIN, ""),
      new OptionBoard(10, 70, 120, 20, Styles.PLAIN, ""),
      new OptionBoard(10, 100, 120, 20, Styles.PLAIN, ""),
      new OptionBoard(10, 130, 120, 20, Styles.PLAIN, ""),
      new OptionBoard(10, 160, 120, 20, Styles.PLAIN, ""),
      new OptionBoard(10, 190, 120, 20, Styles.PLAIN, ""),
      new OptionBoard(
          DrawingHelper.SCREEN_WIDTH - 130,
          DrawingHelper.SCREEN_HEIGHT - 60,
          50,
          20,
          Styles.PLAIN,
          "Up"),
      new OptionBoard(
          DrawingHelper.SCREEN_WIDTH - 130,
          DrawingHelper.SCREEN_HEIGHT - 30,
          50,
          20,
          Styles.PLAIN,
          "Down"),
      new OptionBoard(
          DrawingHelper.SCREEN_WIDTH - 70,
          DrawingHelper.SCREEN_HEIGHT - 60,
          50,
          50,
          Styles.PLAIN,
          "Back")};

      int numPokedexSlots = mPokedexOptions.length - 3;
      mPokedexOptions[numPokedexSlots].setOnClickListener(
          new OnClickListener() {
                @Override
                public void onClick() {
                  mPokedexStartInd = Math.max(mPokedexStartInd - numPokedexSlots, 1);
                }
          });
      mPokedexOptions[numPokedexSlots+1].setOnClickListener(
          new OnClickListener() {
              @Override
              public void onClick() {
                mPokedexStartInd = Math.min(
                    mPokedexStartInd + mPokedexOptions.length - 3,
                    Pokemon.getNumPokemon() - numPokedexSlots);
              }
          });
      mPokedexOptions[numPokedexSlots+2].setOnClickListener(
          new OnClickListener() {
            @Override
            public void onClick() {
              gameContainer.setState(GameState.DEFAULT);
            }
          });
  }

  public OptionBoard[] getOptions() {
    return mPokedexOptions;
  }

  public void onShow() {
    mPokedexStartInd = 0;
  }

  public void drawScreen(Graphics buffer) {
    // has seen/ has owned info box :
    buffer.setColor(Color.WHITE);
    buffer.fillRect(DrawingHelper.SCREEN_WIDTH - 130, DrawingHelper.SCREEN_HEIGHT - 100, 110, 30);
    buffer.setFont(Styles.normalFont);
    buffer.setColor(Color.BLACK);
    buffer.drawString(
        "Seen: " + mPlayer.getNumSeenPokemon(),
        DrawingHelper.SCREEN_WIDTH - 125,
        DrawingHelper.SCREEN_HEIGHT
        - 80);
    buffer.drawString(
        "Caught: " + mPlayer.getNumCaughtPokemon(),
        DrawingHelper.SCREEN_WIDTH - 70,
        DrawingHelper.SCREEN_HEIGHT
        - 80);

    ImageIcon pokeball = SpriteHelper.getMisc("pokeball");

    for (int i = 0; i < mPokedexOptions.length - 3; i++) {
      int pokemonIndex = mPokedexStartInd + i;

      mPokedexOptions[i].setText(String.format("%03d", pokemonIndex + 1) + ". \t" 
          + (mPlayer.hasSeenPokemon(pokemonIndex)
              ? Pokemon.getPokemon(pokemonIndex)
              : "?????"));

      //image of pokemon you're hovering over
      if (mPlayer.hasSeenPokemon(pokemonIndex)) {
          if (mPokedexOptions[i].isHighlighted()) {
            ImageIcon curImage = SpriteHelper.getPokemonFront(Pokemon.getPokemon(pokemonIndex));
            DrawingHelper.drawImage(
                buffer,
                curImage,
                DrawingHelper.SCREEN_WIDTH - 130,
                DrawingHelper.SCREEN_HEIGHT - 190);
          }
      }

      //pokeballs next to pokemon you have caught
      if (mPlayer.hasCaughtPokemon(pokemonIndex)) {
        DrawingHelper.drawImage(
            buffer,
            pokeball,
            mPokedexOptions[i].getX() + mPokedexOptions[i].getWidth() - 20,
            mPokedexOptions[i].getY(),
            20,
            20);
      }
    }
  }
}
