package io.zipcoder.casino.diceAndCoins;

import java.util.Random;

public class Coin {

    Random random = new Random();

    public String flip() {
        int coinLogicInteger = coinLogic();
        if (coinLogicInteger == 1) {
            return "heads";
        } else {
            return "tails";
        }
    }

    int coinLogic() {
        return random.nextInt(2) + 1;
    }

}
