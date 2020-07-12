package pokemon.guioptions;

import pokemon.controllers.PlayerController;
import pokemon.GameContainer;
import pokemon.GameState;
import pokemon.ui.OnClickListener;
import pokemon.ui.OptionBoard;
import pokemon.ui.Styles;
import pokemon.util.DrawingHelper;
import pokemon.util.SpriteHelper;

import java.awt.*;
import javax.swing.*;

public class PersonalScreenGui implements GuiOption {

	private PlayerController mPlayer;
	private OptionBoard[] mPersonalOptions;

	public PersonalScreenGui(GameContainer gameContainer) {
		mPlayer = gameContainer.getPlayer();
    mPersonalOptions = 
    		new OptionBoard[]{
            new OptionBoard(
                20,
                20,
                DrawingHelper.SCREEN_WIDTH - 50,
                DrawingHelper.SCREEN_HEIGHT - 50,
                Color.ORANGE,
                "")};
            
    mPersonalOptions[0].setOnClickListener(
	    	new OnClickListener() {
	      		@Override
			      public void onClick() {
			        gameContainer.setState(GameState.DEFAULT);
			      }
		    });
	}

	public OptionBoard[] getOptions() {
		return mPersonalOptions;
	}

	public void drawScreen(Graphics buffer) {
    ImageIcon playerSprite = SpriteHelper.getMisc("boy_walk_down_rest");
    DrawingHelper.drawImage(
        buffer,
        playerSprite,
        30,
        30,
        DrawingHelper.SCREEN_WIDTH - 240,
        DrawingHelper.SCREEN_HEIGHT - 180);
    buffer.setColor(Color.BLACK);
    buffer.setFont(Styles.extraLargeFont);
    buffer.drawString(
        "Name: " + mPlayer.getName(),
        DrawingHelper.SCREEN_WIDTH - 180,
        DrawingHelper.SCREEN_HEIGHT - 160);
    buffer.setFont(Styles.largerLargeFont);
    buffer.drawString(
        "Money: " + mPlayer.getMoney(),
        DrawingHelper.SCREEN_WIDTH - 250,
        DrawingHelper.SCREEN_HEIGHT - 120);
    buffer.drawString(
        "Number of Pokemon Seen: " + mPlayer.getNumSeenPokemon(),
        DrawingHelper.SCREEN_WIDTH - 250,
        DrawingHelper.SCREEN_HEIGHT - 95);
    buffer.drawString(
        "Number of Pokemon Caught: " + mPlayer.getNumCaughtPokemon(),
        DrawingHelper.SCREEN_WIDTH - 250,
        DrawingHelper.SCREEN_HEIGHT - 70);
	}
}