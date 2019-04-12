package pokemon;

import java.util.HashMap;

public class AttackLibrary {
  private static HashMap<String, Attack> mAttacks;

  private static HashMap<String, Attack> getAttackMap() {
    if (mAttacks == null) {
      mAttacks = new HashMap<>();
      mAttacks.put("Flamethrower", new Attack("Flamethrower", "Fire", 10, 95));
      mAttacks.put("Tackle", new Attack("Tackle", "Normal", 10, 35));
      mAttacks.put("Bite", new Attack("Bite", "Dark", 10, 60));
      mAttacks.put("Thundershock", new Attack("Thundershock", "Electric", 10, 40));
      mAttacks.put("Thunder", new Attack("Thunder", "Electric", 10, 120));
      mAttacks.put("Thunderbolt", new Attack("Thunderbolt", "Electric", 10, 95));
      mAttacks.put("Psybeam", new Attack("Psybeam", "Phychic", 10, 65));
      mAttacks.put("Headbutt", new Attack("Headbutt", "Normal", 10, 70));
      mAttacks.put("Dig", new Attack("Dig", "Ground", 10, 80));
      mAttacks.put("Bubble", new Attack("Bubble", "Water", 10, 20));
      mAttacks.put("Surf", new Attack("Surf", "Water", 10, 95));
      mAttacks.put("Peck", new Attack("Peck", "Normal", 10, 35));
      mAttacks.put("Megahorn", new Attack("Megahorn", "Normal", 10, 120));
      mAttacks.put("Earthquake", new Attack("Earthquake", "Ground", 10, 100));
      mAttacks.put("Discharge", new Attack("Discharge", "Electric", 10, 80));
      mAttacks.put("Slam", new Attack("Slam", "Normal", 10, 80));
      mAttacks.put("Rollout", new Attack("Rollout", "Normal", 10, 80));
      mAttacks.put("Crunch", new Attack("Crunch", "Crunch", 10, 80));
      mAttacks.put("Lick", new Attack("Lick", "Normal", 10, 80));
      mAttacks.put("Pursuit", new Attack("Pursuit", "Dark", 10, 80));
      mAttacks.put("Thrash", new Attack("Thrash", "Normal", 10, 80));
      mAttacks.put("Strength", new Attack("Strength", "Fight", 10, 80));
      mAttacks.put("Stomp", new Attack("Stomp", "Normal", 10, 80));
      mAttacks.put("Solarbeam", new Attack("Solarbeam", "Grass", 10, 80));
      mAttacks.put("Ember", new Attack("Ember", "Fire", 10, 40));
      mAttacks.put("Vine Whip", new Attack("Vine Whip", "Grass", 10, 35));
      mAttacks.put("Absorb", new Attack("Absorb", "Grass", 25, 20));
      mAttacks.put("Gust", new Attack("Gust", "Flying", 35, 40));
      mAttacks.put("Quick Attack", new Attack("Quick Attack", "Normal", 30, 40));
    }

    return mAttacks;
  }

  public static String getType(String name) {
    return getAttackMap().get(name).getType();
  }

  public static Attack getAttack(String name) {
    return getAttackMap().get(name);
  }
}