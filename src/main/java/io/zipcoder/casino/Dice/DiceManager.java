package io.zipcoder.casino.Dice;

import java.util.Arrays;

public class DiceManager {

    // Die faces will be stored in an array
    private Die[] diceArray;

    public DiceManager(int amountOfDice) {
        this.diceArray = new Die[amountOfDice];
        for(int i = 0; i < this.diceArray.length; i++) {
            this.diceArray[i] = new Die();
        }
    }

    public void setSpecificDie(int index, DieFace desiredFace) {
        this.diceArray[index].setDieFace(desiredFace);
    }

    public Die getDie(int indexOfDie) {
        return this.diceArray[indexOfDie];
    }

    public void rollAllDice() {
        for (int i = 0; i < diceArray.length; i++) {
            diceArray[i].rollDie();
        }
    }

    public DieFace[] getAllDieFaces() {
        DieFace[] dieFaces = new DieFace[diceArray.length];
        for(int i = 0; i < this.diceArray.length; i++) {
            dieFaces[i] = this.diceArray[i].getDieFace();
        }
        return dieFaces;
    }

    public int getTotalValue() {
        int sum = 0;
        for (int i = 0; i < diceArray.length; i++) {
            sum += diceArray[i].getDieFace().toInt();
        }
        return sum;
    }

}