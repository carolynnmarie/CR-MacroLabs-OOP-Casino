package io.zipcoder.casino.Dice;

import java.util.Random;

public class Die {

    private DieFace dieFace;
    Random random;

    public Die() {
        this.dieFace = null;
        this.random = new Random();
    }

    public DieFace getDieFace() {
        return this.dieFace;
    }

    public void setDieFace(DieFace desiredDieFace) {
        this.dieFace = desiredDieFace;
    }


    public void rollDie() {
        int randomNumber = random.nextInt(6) + 1;
        switch (randomNumber) {
            case 1:
                setDieFace(DieFace.ONE);
                break;
            case 2:
                setDieFace(DieFace.TWO);
                break;
            case 3:
                setDieFace(DieFace.THREE);
                break;
            case 4:
                setDieFace(DieFace.FOUR);
                break;
            case 5:
                setDieFace(DieFace.FIVE);
                break;
            case 6:
                setDieFace(DieFace.SIX);
                break;
        }
    }
}
