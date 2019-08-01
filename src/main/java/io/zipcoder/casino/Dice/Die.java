package io.zipcoder.casino.Dice;

import java.util.Random;

public class Die {

    private DieFace dieFace;


    public Die() {
        this.dieFace = null;
    }

    public DieFace getDieFace() {
        return this.dieFace;
    }

    public void setDieFace(DieFace desiredDieFace) {
        this.dieFace = desiredDieFace;
    }


    public void rollDie() {
        Random random = new Random();
        int randNum = random.nextInt(6) + 1;
        dieFace = (randNum == 1)? DieFace.ONE: (randNum == 2)? DieFace.TWO: (randNum== 3)? DieFace.THREE: (randNum==4)? DieFace.FOUR:
                (randNum == 5)? DieFace.FIVE: DieFace.SIX;
    }
}
