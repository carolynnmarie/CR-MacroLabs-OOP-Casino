package io.zipcoder.casino.diceAndCoins;

import java.util.Arrays;

public class DiceManager {

    // Die faces will be stored in an array
    private Die[] diceArray;

    public DiceManager(int amountOfDice) {
        this.diceArray = new Die[amountOfDice];
    }

    public Die rollSpecificDie(int index) {
        diceArray[index].rollDie();
        return diceArray[index];
    }

    public void setSpecificDie(int index, DieFace desiredFace) {
        this.diceArray[index].setDieFace(desiredFace);
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
            sum += diceArray[i].getDieValue();
        }
        return sum;
    }

}