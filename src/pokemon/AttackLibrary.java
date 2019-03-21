package pokemon;

public class AttackLibrary {
  static Attack[] attackArray = new Attack[29];

  public static void fill() {
    attackArray[0] = new Attack("Flamethrower", "Fire", 10, 95);
    attackArray[1] = new Attack("Tackle", "Normal", 10, 35);
    attackArray[2] = new Attack("Bite", "Dark", 10, 60);
    attackArray[3] = new Attack("Thundershock", "Electric", 10, 40);
    attackArray[4] = new Attack("Thunder", "Electric", 10, 120);
    attackArray[5] = new Attack("Thunderbolt", "Electric", 10, 95);
    attackArray[6] = new Attack("Psybeam", "Phychic", 10, 65);
    attackArray[7] = new Attack("Headbutt", "Normal", 10, 70);
    attackArray[8] = new Attack("Dig", "Ground", 10, 80);
    attackArray[9] = new Attack("Bubble", "Water", 10, 20);
    attackArray[10] = new Attack("Surf", "Water", 10, 95);
    attackArray[11] = new Attack("Peck", "Normal", 10, 35);
    attackArray[12] = new Attack("Megahorn", "Normal", 10, 120);
    attackArray[13] = new Attack("Earthquake", "Ground", 10, 100);
    attackArray[14] = new Attack("Discharge", "Electric", 10, 80);
    attackArray[15] = new Attack("Slam", "Normal", 10, 80);
    attackArray[16] = new Attack("Rollout", "Normal", 10, 80);
    attackArray[17] = new Attack("Crunch", "Crunch", 10, 80);
    attackArray[18] = new Attack("Lick", "Normal", 10, 80);
    attackArray[19] = new Attack("Pursuit", "Dark", 10, 80);
    attackArray[20] = new Attack("Thrash", "Normal", 10, 80);
    attackArray[21] = new Attack("Strength", "Fight", 10, 80);
    attackArray[22] = new Attack("Stomp", "Normal", 10, 80);
    attackArray[23] = new Attack("Solarbeam", "Grass", 10, 80);
    attackArray[24] = new Attack("Ember", "Fire", 10, 40);
    attackArray[25] = new Attack("Vine Whip", "Grass", 10, 35);
    attackArray[26] = new Attack("Absorb", "Grass", 25, 20);
    attackArray[27] = new Attack("Gust", "Flying", 35, 40);
    attackArray[28] = new Attack("Quick Attack", "Normal", 30, 40);
  }

  public static Attack[] returnAttacks() {
    return attackArray;
  }

  public static String getType(String name) {
    fill();
    for (int attackIndex = 0; attackIndex < attackArray.length; attackIndex++) {
      if (attackArray[attackIndex].getName().equals(name))
        return attackArray[attackIndex].getType();
    }
    return "";
  }

  public static int getAttackDamage(String name) {
    for (int attackIndex = 0; attackIndex < attackArray.length; attackIndex++) {
      if (attackArray[attackIndex].getName().equals(name) == true)
        return attackArray[attackIndex].getAttackDamage();
    }
    return 1;
  }
}