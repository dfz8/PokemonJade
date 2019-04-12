package pokemon;

import pokemon.entities.Pokemon;

public class Attack {
  private String mName;
  private String mType;
  private int mPP;
  private int mPower;

  public Attack(String name, String type, int pp, int attackPower) {
    mName = name;
    mType = type;
    mPP = pp;
    mPower = attackPower;
  }

  public void giveDamage(Pokemon user, Pokemon victim) {
    int damage = user.getLevel() * 2 / 5 + 2;
    damage = damage * mPower * user.getAttackLevel() / 50 / victim.getDefenseLevel() + 2;

    double criticalHit = Math.random() * 256;
    if (criticalHit < 8.0) {
      damage *= 2;
    }
    int random = (int) ((Math.random() * 39 + 217) * 100 / 255);
    damage = damage * random / 100;

    if (mType.equals(user.getType())) {
      damage = (int) (damage * 1.5);
    }

    if (isSuperEffective(user.getType(), victim.getType())) {
      damage *= 2;
    }
    if (isSuperEffective(victim.getType(), user.getType())) {
      damage /= 2;
    }

    if (damage == 0) {
      damage++;
    }

    victim.setCurrentHP(victim.getCurrentHP() - (damage));
    mPP--;
  }


  public static boolean isSuperEffective(String t1, String t2) {
    if (t1.equals("Grass")) {
      if (t2.equals("Water") || t2.equals("Ground")) {
        return true;
      }
    }
    if (t1.equals("Fire")) {
      if (t2.equals("Grass") || t2.equals("Steel") || t2.equals("Ice")) {
        return true;
      }
    }
    if (t1.equals("Electric")) {
      if (t2.equals("Water") || t2.equals("Flying")) {
        return true;
      }
    }
    if (t1.equals("Fighting")) {
      if (t2.equals("Rock") || t2.equals("Ice") || t2.equals("Normal")) {
        return true;
      }
    }
    if (t1.equals("Bug")) {
      if (t2.equals("Psychic")) //?
      {
        return true;
      }
    }
    if (t1.equals("Ice")) {
      if (t2.equals("Ground") || t2.equals("Grass")) {
        return true;
      }
    }
    if (t1.equals("Flying")) {
      if (t2.equals("Fighting") || t2.equals("Ground") || t2.equals("Grass")) {
        return true;
      }
    }
    if (t1.equals("Rock")) {
      if (t2.equals("Flying") || t2.equals("Water")) {
        return true;
      }
    }
    //more to come
    return false;
  }

  public String getName() {
    return mName;
  }

  public String getType() {
    return mType;
  }
}