package pokemon.ui;

import pokemon.AlertHelper;
import pokemon.GameContainer;
import pokemon.GameState;

public class ClickListenerFactory {
  private GameContainer mPanel;

  public ClickListenerFactory(GameContainer panel) {
    mPanel = panel;
  }

  public OnClickListener toStateClickListener(GameState state) {
    return new OnClickListener() {
      @Override
      public void onClick() {
        mPanel.setState(state);
      }
    };
  }

  public OnClickListener comingSoonListener() {
    return new OnClickListener() {
      @Override
      public void onClick() {
        AlertHelper.alert("Coming soon.");
      }
    };
  }
}
