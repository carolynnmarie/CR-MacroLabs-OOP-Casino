package io.zipcoder.casino.Dice;

import java.util.Random;

public class Coin {

    Random random = new Random();
    String[] sides;

    public Coin(){
        this.sides = new String[]{"Heads","Tails"};
    }

    public String flip(){
        Random random = new Random();

        int index = (int)Math.round(Math.random());
        return sides[index];
    }

    /*public String flip() {
        int coinLogicInteger = coinLogic();
        if (coinLogicInteger == 1) {
            return "heads";
        } else {
            return "tails";
        }
    }

    public int coinLogic() {
        return random.nextInt(2) + 1;
    }*/

}
