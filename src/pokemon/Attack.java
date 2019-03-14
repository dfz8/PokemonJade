package pokemon;

import pokemon.entities.Pokemon;

public class Attack {
  private String name;
  private String type;
  private int damage;
  private int PP;
  private int damageOfAttack;

  public Attack(String n, String t, int pp, int damageAmount) {
    name = n;
    type = t;
    PP = pp;
    damageOfAttack = damageAmount;

  }

  // public static int getAttackDamage(pokemon.entities.Pokemon user, pokemon.entities.Pokemon opponent, int moveDamage){
  // double damage = ((((((user.getLevel() * 2 / 5) + 2) * moveDamage * user.getAttackLevel()) /
  // 50) / opponent.getDefenseLevel()) + 2);
  // if(Math.random() < 0.1) //critical hit
  // damage *= 2;
  // damage *= getRandomNumber() / 2; //random modifier x = [85, 100]
  // if(isSuperEffective(user.getType(), opponent.getType()))
  // damage *= 1.5;
  // if(isSuperEffective(opponent.getType(), user.getType()))
  // damage *= 0.5;
  // return (int)damage;
  // }

  public static int getRandomNumber() {
    int number = 0;
    while (number < 85)
      number = (int) (Math.random() * 85 + 1);
    return number;
  }

  public void giveDamage(Pokemon user, Pokemon victim, int attackDamage) {

    // double damage = ((((((user.getLevel() * 2 / 5) + 2) * damageOfAttack * user.getAttackLevel
    // ()) / 50) / victim.getDefenseLevel()) + 2);
    // if(Math.random() < 0.1) //critical hit
    // damage *= 2;
    // damage *= getRandomNumber() / 2; //random modifier x = [85, 100]
    //

    // double criticalHit = Math.random()*256;
    // if(criticalHit < 8.0)
    // damage*=3;
    //
    // damage/=100.0;
    // damage++; //in case its a decimal < 1.00
    //

    //(((((((level * 2 / 5) + 2) * basepower * atk / 50) / def) * mod1) + 2) * CH * mod2 * R/100)
    // *STAB*type1*type2*mod3);

    int damage = user.getLevel() * 2 / 5 + 2;
    damage = damage * damageOfAttack * user.getAttackLevel() / 50 / victim.getDefenseLevel() + 2;

    double criticalHit = Math.random() * 256;
    if (criticalHit < 8.0)
      damage *= 2;
    int random = (int) ((Math.random() * 39 + 217) * 100 / 255);
    damage = damage * random / 100;

    if (type.equals(user.getType()))
      damage = (int) (damage * 1.5);

    if (isSuperEffective(user.getType(), victim.getType()))
      damage *= 2;
    if (isSuperEffective(victim.getType(), user.getType()))
      damage /= 2;


    if (damage == 0)
      damage++;

    // return (int)damage;

    // damage = (int)(((user.getLevel()*.4)+2)*(1.5*attackDamage/victim.getDefenseLevel()));
    // if(isSuperEffective(type, victim.getType()))
    // damage = (int)(damage*1.5);
    // if(isSuperEffective(victim.getType(), type))
    // damage = (int)(damage/2);
    // damage++;

    victim.setCurrentHP(victim.getCurrentHP() - (int) (damage));
    PP--;

    AlertHelper.debug("i just hit a : " + damage);
  }


  public static boolean isSuperEffective(String t1, String t2) {
    if (t1.equals("Grass"))
      if (t2.equals("Water") || t2.equals("Ground"))
        return true;
    if (t1.equals("Fire"))
      if (t2.equals("Grass") || t2.equals("Steel") || t2.equals("Ice"))
        return true;
    if (t1.equals("Electric"))
      if (t2.equals("Water") || t2.equals("Flying"))
        return true;
    if (t1.equals("Fighting"))
      if (t2.equals("Rock") || t2.equals("Ice") || t2.equals("Normal"))
        return true;
    if (t1.equals("Bug"))
      if (t2.equals("Psychic")) //?
        return true;
    if (t1.equals("Ice"))
      if (t2.equals("Ground") || t2.equals("Grass"))
        return true;
    if (t1.equals("Flying"))
      if (t2.equals("Fighting") || t2.equals("Ground") || t2.equals("Grass"))
        return true;
    if (t1.equals("Rock"))
      if (t2.equals("Flying") || t2.equals("Water"))
        return true;
    //more to come
    return false;
  }

  public int getLevelOfAttack(String attackName) {
    Attack[] attackArray = AttackLibrary.returnAttacks();

    for (int attackIndex = 0; attackIndex < attackArray.length; attackIndex++) {
      if ((attackArray[attackIndex].getName()).compareTo(attackName) == 0)
        return attackArray[attackIndex].getAttackDamage();

    }
    return 1;
  }

  public String getName() {
    return name;
  }

  public String getType() {
    return type;
  }

  public int getAttackDamage() {
    return damageOfAttack;
  }
}