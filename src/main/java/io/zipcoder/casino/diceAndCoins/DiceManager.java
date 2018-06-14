package io.zipcoder.casino.diceAndCoins;

import java.util.ArrayList;
import java.util.Arrays;

public class DiceManager {


    private ArrayList<Die> diceArray;

    public DiceManager(int amountOfDice) {
        this.diceArray = new ArrayList<>(amountOfDice);
        for(int i = 0; i<amountOfDice; i++){
            this.diceArray.add(new Die());
        }
    }

    public Die rollSpecificDie(int index) {
        diceArray.get(index).rollDie();
        return diceArray.get(index);
    }

    public void setSpecificDie(int index, DieFace desiredFace) {
        this.diceArray.get(index).setDieFace(desiredFace);
    }

    public void setDiceArray(ArrayList<Die> diceArray){
        this.diceArray = diceArray;
    }
    public ArrayList<Die> getDiceArray(){
        return diceArray;
    }

    public void rollAllDice() {
        for (int i = 0; i < diceArray.size(); i++) {
            diceArray.get(i).rollDie();
        }
    }

    public DieFace[] getAllDieFaces() {
        DieFace[] dieFaces = new DieFace[diceArray.size()];
        for(int i = 0; i < this.diceArray.size(); i++) {
            dieFaces[i] = this.diceArray.get(i).getDieFace();
        }
        return dieFaces;
    }

    public int getTotalValue() {
        int sum = 0;
        for (int i = 0; i < diceArray.size(); i++) {
            sum += diceArray.get(i).getDieValue();
        }
        return sum;
    }

}