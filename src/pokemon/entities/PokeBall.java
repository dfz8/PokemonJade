package pokemon.entities;

import pokemon.AlertHelper;

public class PokeBall {
  public int myType;
  public int myTotalHealth;
  public int myCurrentHealth;

  public PokeBall(int type, int totalHealth, int currentHealth) {
    myType = type;
    myTotalHealth = totalHealth;
    myCurrentHealth = currentHealth;
  }

  public PokeBall() {

  }

  public boolean isCaught() {
    int randomNum = (int) (Math.random() * 2);

    //if(myCurrentHealth < randomNum * myTotalHealth * typeFactor())
    //return true;
    //return false;
    if (randomNum == 1) {
      AlertHelper.alert("You Caught It!!");
      return true;
    } else
      return false;
  }

}