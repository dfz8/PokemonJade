package pokemon.entities;

public class Pokemon {
  public static double rate = 1.5;//1.124478;

  private String myName;
  private String myType;
  private String attackOne;
  private String attackTwo;
  private String attackThree;
  private String attackFour;
  private int myLevel;
  private int myAttackLevel;
  private int mySpecialAttack;
  private int myDefenseLevel;
  private int mySpecialDefense;
  private int mySpeed;
  private int myEXP;
  private int myCurrentHP;
  private int myMaxHP;
  private int myNextLevelEXP = (int) (Math.pow(rate, myLevel + 1));

  public boolean gainIsSlow = false;
  public boolean gainIsMedium = false;
  public boolean gainIsFast = false;
      
   	/*
   	lvl 1 = 25xp
   	Slow Leveler = 300 000 xp +61xp/lvl
   	medium leveler = 200 000 xp +41xp/lvl
   	frast leveler = 125 000 xp +25xp/lvl
   
   	*/

  //list of first 151 pokemon:
  public static String[] pokemons = {"Bulbasaur", "Ivysaur", "Venusaur", "Charmander", "Charmeleon",
      "Charizard", "Squirtle", "Wartortle", "Blastoise", "Caterpie", "Metapod", "Butterfree",
      "Weedle", "Kakuna", "Beedrill", "Pidgey", "Pidgeotto", "Pidgeot", "Rattata", "Raticate",
      "Spearow", "Fearow", "Ekans", "Arbok", "Pikachu", "Raichu", "Sandshrew", "Sandslash",
      "Nidoran F", "Nidorina", "Nidoqueen", "Nidoran M", "Nidorino", "Nidoking", "Clefairy",
      "Clefable", "Vulpix", "Ninetales", "Jigglypuff", "Wigglytuff", "Zubat", "Golbat", "Oddish",
      "Gloom", "Vileplume", "Paras", "Parasect", "Venonat", "Venomoth", "Diglett", "Dugtrio",
      "Meowth", "Persian", "Psyduck", "Golduck", "Mankey", "Primeape", "Growlithe", "Arcanine",
      "Polywag", "Polywhirl", "Polywrath", "Abra", "Kadabra", "Alakazam", "Machop", "Machoke",
      "Machamp", "Bellsprout",
      "Weepinbell", "Victreebel", "Tentacool", "Tentacruel", "Geodude", "Graveler", "Golem",
      "Ponyta", "Rapidash", "Slowpoke", "Slowbro", "Magnemite", "Magneton", "Farfetch'd", "Doduo"
      , "Dodrio", "Seel", "Dewgong", "Grimer", "Muk", "Shellder", "Cloyster", "Gastly", "Haunter"
      , "Gengar", "Onix", "Drowzee", "Hypno", "Krabby", "Kingler", "Voltorb", "Electrode",
      "Exeggcute", "Exeggutor", "Cubone", "Marowak", "Hitmonlee", "Hitmonchan", "Lickitung",
      "Koffing", "Weezing", "Rhyhorn", "Rhydon", "Chansey", "Tangela", "Kangaskhan", "Horse",
      "aSeadra", "Goldeen", "Seaking", "Staryu", "Starmie", "Mr. Mime", "Scyther", "Jynx",
      "Electabuzz", "Magmar", "Pinsir", "Tauros", "Magikarp", "Gyarados", "Lapras", "Ditto",
      "Eevee", "Vaporeon", "Jolteon", "Flareon", "Porygon", "Omanyte", "Omastar", "Kabuto",
      "Kabutops", "Aerodactyl", "Snorlax", "Articuno", "Zapdos", "Moltres", "Dratini", "Dragonair"
      , "Dragonite", "Mewtwo", "Mew", "Raikou"};
         /*
   		false index numbers:
   		Raikou is currently: 152, should be: 243
   		*/


  public Pokemon(String n,
                 String t,
                 String a1,
                 String a2,
                 String a3,
                 String a4,
                 int l,
                 int aL,
                 int sA,
                 int dL,
                 int sD,
                 int s,
                 int e,
                 int hp) {
    myName = n;
    myType = t;
    attackOne = a1;
    attackTwo = a2;
    attackThree = a3;
    attackFour = a4;
    myLevel = l;
    myAttackLevel = aL;
    mySpecialAttack = sA;
    myDefenseLevel = dL;
    mySpecialDefense = sD;
    mySpeed = s;
    myEXP = e;
    //not sure which hp to set to
    myMaxHP = hp;

    myNextLevelEXP = myEXP * 3;

  }

  public Pokemon(String n,
                 String t,
                 String a1,
                 String a2,
                 String a3,
                 String a4,
                 int l,
                 int e,
                 int al,
                 int dl,
                 int hp,
                 int mhp) {
    myName = n;
    myType = t;
    attackOne = a1;
    attackTwo = a2;
    attackThree = a3;
    attackFour = a4;
    myLevel = l;
    myEXP = e;
    //not sure which hp to set to
    myCurrentHP = hp;
    myMaxHP = mhp;
    myAttackLevel = al;
    myDefenseLevel = dl;

    myNextLevelEXP = (int) (Math.pow(rate, myLevel + 1));

  }


  public Pokemon(String allData) {
    String[] info = allData.split(", ");
    myName = info[0];
    myType = info[1];
    attackOne = info[2];
    attackTwo = info[3];
    attackThree = info[4];
    attackFour = info[5];
    myLevel = Integer.parseInt(info[6]);
    myAttackLevel = Integer.parseInt(info[7]);
    myDefenseLevel = Integer.parseInt(info[8]);
    myEXP = Integer.parseInt(info[9]);
    myCurrentHP = Integer.parseInt(info[10]);
    myMaxHP = Integer.parseInt(info[11]);

    myNextLevelEXP = (int) (Math.pow(rate, myLevel + 1));

  }

  public static String getPokemon(int index) {
    return pokemons[index];
  }

  public static int getIndex(String n) {
    for (int i = 0; i < pokemons.length; i++)
      if (n.equals(pokemons[i]))
        return i;
    return -1;
  }

  // get Methods
  public String getName() {
    return myName;
  }

  public String getType() {
    return myType;
  }

  public String getAttackOne() {
    return attackOne;
  }

  public String getAttackTwo() {
    return attackTwo;
  }

  public String getAttackThree() {
    return attackThree;
  }

  public String getAttackFour() {
    return attackFour;
  }

  public int getLevel() {
    return myLevel;
  }

  public int getAttackLevel() {
    return myAttackLevel;
  }

  public int getSpecialAttack() {
    return mySpecialAttack;
  }

  public int getSpeed() {
    return mySpeed;
  }

  public int getDefenseLevel() {
    return myDefenseLevel;
  }

  public int getSpecialDefense() {
    return mySpecialDefense;
  }

  public int getCurrentHP() {
    return myCurrentHP;
  }

  public int getMaxHP() {
    return myMaxHP;
  }

  public int getMyEXP() {
    return myEXP;
  }

  public int getMyNextLevelEXP() {
    return myNextLevelEXP;
  }

  public int getMyPastLevelEXP() {
    return (int) (Math.pow(rate, myLevel));
  }

  //setMethods

  public void setName(String n) {
    myName = n;
  }

  public void setType(String t) {
    myType = t;
  }

  public void setAttackOne(String a1) {
    attackOne = a1;
  }

  public void setAttackTwo(String a2) {
    attackTwo = a2;
  }

  public void setAttackThree(String a3) {
    attackThree = a3;
  }

  public void setAttackFour(String a4) {
    attackFour = a4;
  }

  public void setLevel(int l) {
    myLevel = l;
  }

  public void setAttackLevel(int aL) {
    myAttackLevel = aL;
  }

  public void setSpecialAttack(int sA) {
    mySpecialAttack = sA;
  }

  public void setSpeed(int s) {
    mySpeed = s;
  }

  public void setDefenseLevel(int dL) {
    myDefenseLevel = dL;
  }

  public void setSpecialDefense(int sD) {
    mySpecialDefense = sD;
  }

  public void setCurrentHP(int hp) {
    myCurrentHP = hp;
  }

  public void setMaxHP(int hp) {
    myMaxHP = hp;
  }


  public String toString() {
    return myName + ", " + myType + ", " + attackOne + ", " + attackTwo + ", " + attackThree + "," +
        " " + attackFour + ", " + myLevel + ", " + myAttackLevel + ", " + myDefenseLevel + ", " + myEXP + ", " + myCurrentHP + ", " + myMaxHP;
  }

  public void revive() {
    heal(getMaxHP());
  }

  public void heal(int h) {
    setCurrentHP(getCurrentHP() + h);
  }

  public boolean isFainted() {
    if (myCurrentHP <= 0)
      return true;
    return false;
  }

  public void EXPgain() {
    if (gainIsSlow) {
      myEXP += 5;
    }
    if (gainIsMedium) {
      myEXP += 10;
    }
    if (gainIsFast) {
      myEXP += 15;
    }

    while (myEXP >= myNextLevelEXP) {
      myLevel++;

      myAttackLevel += (int) (Math.random() * 3 + 1);
      myDefenseLevel += (int) (Math.random() * 3 + 1);
      int hp = (int) (Math.random() * 2 + 2);
      myCurrentHP += hp;
      myMaxHP += hp;
      myNextLevelEXP = (int) (Math.pow(rate, myLevel + 1));
    }

  }

  public String chooseAttackEnemy() {
    int decider = (int) (Math.random() * 4);
    if (decider == 0)
      return attackOne;
    else if (decider == 1)
      return attackTwo;
    else if (decider == 2)
      return attackThree;
    else
      return attackFour;

  }
}