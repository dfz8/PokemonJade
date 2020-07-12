package pokemon.guioptions;

import pokemon.ui.OptionBoard;

import java.awt.Graphics;

public interface GuiOption {

	public OptionBoard[] getOptions();

	public void drawScreen(Graphics buffer);
	
}