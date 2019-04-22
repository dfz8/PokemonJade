package pokemon.ui;

import pokemon.AlertHelper;
import pokemon.GameState;
import pokemon.PlayPanel;

public class ClickListenerFactory {
  private PlayPanel mPanel;

  public ClickListenerFactory(PlayPanel panel) {
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
