package pokemon.controllers;

import pokemon.entities.Pokemon;

import java.util.ArrayList;

import static pokemon.entities.Pokemon.getIndex;

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

  public String getName() {
    return mName;
  }

  public int getMoney() {
    return mMoney;
  }

  public int getNumCaughtPokemon() {
    return mCaughtPokemon.size();
  }

  public int getNumSeenPokemon() {
    return mPokedex.mNumSeen;
  }

  public Pokemon getCaughtPokemon(int index) {
    return mCaughtPokemon.get(index);
  }

  public void initCaughtPokemon(Pokemon pokemon) {
    mCaughtPokemon.add(pokemon);
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

  private class Pokedex {
    private boolean[] mHasCaughtPokemon;
    private boolean[] mHasSeenPokemon;

    private int mNumSeen;
    private int mNumCaught;

    public Pokedex() {
      mHasCaughtPokemon = new boolean[Pokemon.getNumPokemon()];
      mHasSeenPokemon = new boolean[Pokemon.getNumPokemon()];
    }

    public void markSeen(int index) {
      mHasSeenPokemon[index] = true;
      mNumSeen++;
    }

    public void markCaught(int index) {
      mHasCaughtPokemon[index] = true;
      mNumCaught++;
    }
  }
}
