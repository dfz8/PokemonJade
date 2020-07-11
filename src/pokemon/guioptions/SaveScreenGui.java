package pokemon.guioptions;

import pokemon.ui.OptionBoard;
import pokemon.ui.Styles;
import pokemon.controllers.PlayerController;
import pokemon.util.DrawingHelper;
import pokemon.util.SpriteHelper;

import java.awt.*;
import javax.swing.*;

public class SaveScreenGui {

	private PlayerController mPlayer;
	private OptionBoard[] mSaveOptions;

	public SaveScreenGui() {

	}

	public void drawScreen(Graphics buffer) {
	    ImageIcon me = SpriteHelper.getMisc("boy_walk_down_rest");
	    DrawingHelper.drawImage(
	        buffer,
	        me,
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