package pokemon.controllers;

import static pokemon.entities.Pokemon.getIndex;

import pokemon.entities.Pokemon;

import java.util.ArrayList;

public class PlayerController {
  private String mName;
  private int mMoney;
  private Pokemon[] mParty = new Pokemon[6];
  private ArrayList<Pokemon> mCaughtPokemon = new ArrayList<>();
  private Pokedex mPokedex = new Pokedex();

  public PlayerController(String name, int money) {
    mName = name;
    mMoney = money;
  }

  public void markSeenPokemon(String name) {
    markSeenPokemon(getIndex(name));
  }

  public void markSeenPokemon(int index) {
    mPokedex.markSeen(index);
  }

  public void markCaughtPokemon(String name) {
    markCaughtPokemon(getIndex(name));
  }

  public void markCaughtPokemon(int index) {
    mPokedex.markCaught(index);
  }

  public boolean hasSeenPokemon(int index) {
    return mPokedex.mHasSeenPokemon[index];
  }

  public boolean hasCaughtPokemon(int index) {
    return mPokedex.mHasCaughtPokemon[index];
  }

  public String getName() {
    return mName;
  }

  public int getMoney() {
    return mMoney;
  }

  public int getNumCaughtPokemon() {
    return mCaughtPokemon.size();
  }

  public void initCaughtPokemon(Pokemon pokemon) {
    mCaughtPokemon.add(pokemon);
  }

  public Pokemon getPokemon(int index) {
    return mCaughtPokemon.get(index);
  }

  private class Pokedex {
    private boolean[] mHasCaughtPokemon;
    private boolean[] mHasSeenPokemon;

    public Pokedex() {
      mHasCaughtPokemon = new boolean[Pokemon.getNumPokemon()];
      mHasSeenPokemon = new boolean[Pokemon.getNumPokemon()];
    }

    public void markSeen(int index) {
      mHasSeenPokemon[index] = true;
    }

    public void markCaught(int index) {
      mHasCaughtPokemon[index] = true;
    }
  }
}
