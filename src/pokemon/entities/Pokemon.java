package pokemon.entities;

import pokemon.AlertHelper;

public class Pokemon {
  public static double expBase = 1.5;//1.124478;

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
  private int myNextLevelEXP;

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
  // note: Raikou is currently: 152, should be: 243
  public static String[] pokemons = {
      "Bulbasaur", "Ivysaur", "Venusaur", "Charmander", "Charmeleon", "Charizard", "Squirtle", "Wartortle", "Blastoise",
      "Caterpie", "Metapod", "Butterfree", "Weedle", "Kakuna", "Beedrill", "Pidgey", "Pidgeotto", "Pidgeot", "Rattata",
      "Raticate", "Spearow", "Fearow", "Ekans", "Arbok", "Pikachu", "Raichu", "Sandshrew", "Sandslash", "Nidoran F",
      "Nidorina", "Nidoqueen", "Nidoran M", "Nidorino", "Nidoking", "Clefairy", "Clefable", "Vulpix", "Ninetales",
      "Jigglypuff", "Wigglytuff", "Zubat", "Golbat", "Oddish", "Gloom", "Vileplume", "Paras", "Parasect", "Venonat",
      "Venomoth", "Diglett", "Dugtrio", "Meowth", "Persian", "Psyduck", "Golduck", "Mankey", "Primeape", "Growlithe",
      "Arcanine", "Polywag", "Polywhirl", "Polywrath", "Abra", "Kadabra", "Alakazam", "Machop", "Machoke", "Machamp",
      "Bellsprout", "Weepinbell", "Victreebel", "Tentacool", "Tentacruel", "Geodude", "Graveler", "Golem", "Ponyta",
      "Rapidash", "Slowpoke", "Slowbro", "Magnemite", "Magneton", "Farfetch'd", "Doduo", "Dodrio", "Seel", "Dewgong",
      "Grimer", "Muk", "Shellder", "Cloyster", "Gastly", "Haunter", "Gengar", "Onix", "Drowzee", "Hypno", "Krabby",
      "Kingler", "Voltorb", "Electrode", "Exeggcute", "Exeggutor", "Cubone", "Marowak", "Hitmonlee", "Hitmonchan",
      "Lickitung", "Koffing", "Weezing", "Rhyhorn", "Rhydon", "Chansey", "Tangela", "Kangaskhan", "Horse", "aSeadra",
      "Goldeen", "Seaking", "Staryu", "Starmie", "Mr. Mime", "Scyther", "Jynx", "Electabuzz", "Magmar", "Pinsir",
      "Tauros", "Magikarp", "Gyarados", "Lapras", "Ditto", "Eevee", "Vaporeon", "Jolteon", "Flareon", "Porygon",
      "Omanyte", "Omastar", "Kabuto", "Kabutops", "Aerodactyl", "Snorlax", "Articuno", "Zapdos", "Moltres", "Dratini",
      "Dragonair", "Dragonite", "Mewtwo", "Mew", "Raikou"};

  private Pokemon(Builder builder) {
    myName = builder.name;
    myType = builder.type;
    attackOne = builder.firstAttack;
    attackTwo = builder.secondAttack;
    attackThree = builder.thirdAttack;
    attackFour = builder.fourthAttack;
    myLevel = builder.level;
    myAttackLevel = builder.attack;
    mySpecialAttack = builder.specialAttack;
    myDefenseLevel = builder.defense;
    mySpecialDefense = builder.specialDefense;
    mySpeed = builder.speed;
    myEXP = builder.exp;
    myMaxHP = builder.maxHp;

    myNextLevelEXP = myEXP * 3;
  }

  private Pokemon(String allData) {
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

    myNextLevelEXP = (int) (Math.pow(expBase, myLevel + 1));
  }

  public static String getPokemon(int index) {
    return pokemons[index];
  }

  public static int getIndex(String name) {
    for (int i = 0; i < pokemons.length; i++) {
      if (name.equals(pokemons[i])) {
        return i;
      }
    }
    AlertHelper.fatal("Pokemon: " + name + ", not supported yet.");
    return -1;
  }

  public static int getNumPokemon() {
    return pokemons.length;
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
    return (int) (Math.pow(expBase, myLevel));
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

  @Override
  public String toString() {
    return myName + ", " + myType + ", " + attackOne + ", " + attackTwo + ", " + attackThree + "," +
           " " + attackFour + ", " + myLevel + ", " + myAttackLevel + ", " + myDefenseLevel + ", "
           + myEXP + ", " + myCurrentHP + ", " + myMaxHP;
  }

  public void revive() {
    heal(getMaxHP());
  }

  public void heal(int h) {
    setCurrentHP(getCurrentHP() + h);
  }

  public boolean isFainted() {
    if (myCurrentHP <= 0) {
      return true;
    }
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
      myNextLevelEXP = (int) (Math.pow(expBase, myLevel + 1));
    }

  }

  public String chooseAttackEnemy() {
    int decider = (int) (Math.random() * 4);
    if (decider == 0) {
      return attackOne;
    } else if (decider == 1) {
      return attackTwo;
    } else if (decider == 2) {
      return attackThree;
    } else {
      return attackFour;
    }

  }

  public static class Builder {
    private String name;
    private String type;
    private String firstAttack;
    private String secondAttack;
    private String thirdAttack;
    private String fourthAttack;
    private int level;
    private int attack;
    private int specialAttack;
    private int defense;
    private int specialDefense;
    private int speed;
    private int exp;
    private int hp;
    private int maxHp;
    private String allData;

    public Builder setName(String name) {
      this.name = name;
      return this;
    }

    public Builder setType(String type) {
      this.type = type;
      return this;
    }

    public Builder setFirstAttack(String firstAttack) {
      this.firstAttack = firstAttack;
      return this;
    }

    public Builder setSecondAttack(String secondAttack) {
      this.secondAttack = secondAttack;
      return this;
    }

    public Builder setThirdAttack(String thirdAttack) {
      this.thirdAttack = thirdAttack;
      return this;
    }

    public Builder setFourthAttack(String fourthAttack) {
      this.fourthAttack = fourthAttack;
      return this;
    }

    public Builder setLevel(int level) {
      this.level = level;
      return this;
    }

    public Builder setAttack(int attack) {
      this.attack = attack;
      return this;
    }

    public Builder setSpecialAttack(int specialAttack) {
      this.specialAttack = specialAttack;
      return this;
    }

    public Builder setDefense(int defense) {
      this.defense = defense;
      return this;
    }

    public Builder setSpecialDefense(int specialDefense) {
      this.specialDefense = specialDefense;
      return this;
    }

    public Builder setSpeed(int speed) {
      this.speed = speed;
      return this;
    }

    public Builder setExp(int exp) {
      this.exp = exp;
      return this;
    }

    public Builder setHp(int hp) {
      this.hp = hp;
      return this;
    }

    public Builder setMaxHp(int maxHp) {
      this.maxHp = maxHp;
      return this;
    }

    public Pokemon createFrom(String allData) {
      return new Pokemon(allData);
    }

    public Pokemon build() {
      return new Pokemon(this);
    }
  }
}