package com.towerdefense.game;

public class GameState {

    private static GameState instance;

    private int gold;
    private int lives;

    private GameState() {
        this.gold = 100;
        this.lives = 20;
    }

    public static GameState getInstance() {
        if (instance == null) {
            instance = new GameState();
        }
        return instance;
    }

    public int getGold() {
        return gold;
    }

    public void addGold(int amount) {
        gold += amount;
    }

    public boolean spendGold(int amount) {
        if (gold < amount) {
            return false;
        }
        gold -= amount;
        return true;
    }

    public int getLives() {
        return lives;
    }

    public void loseLife(int amount) {
        lives -= amount;
    }

    public boolean isGameOver() {
        return lives <= 0;
    }
}