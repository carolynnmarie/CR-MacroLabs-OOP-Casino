package io.zipcoder.casino.diceAndCoins;


import static java.lang.Math.ceil;
import static java.lang.Math.random;

public class Die {

    private DieFace dieFace;


    public Die() {
        this.dieFace = null;
    }

    public DieFace getDieFace() {
        return this.dieFace;
    }

    public void setDieFace(DieFace dieFace) {
        this.dieFace = dieFace;
    }

    public int getDieValue(){
        return dieFace.getFaceValue();
    }

    public void rollDie() {
        switch ((int)(ceil(random())*6)) {
            case 1:
                this.setDieFace(DieFace.ONE);
                break;
            case 2:
                this.setDieFace(DieFace.TWO);
                break;
            case 3:
                this.setDieFace(DieFace.THREE);
                break;
            case 4:
                this.setDieFace(DieFace.FOUR);
                break;
            case 5:
                this.setDieFace(DieFace.FIVE);
                break;
            case 6:
                this.setDieFace(DieFace.SIX);
                break;
        }
    }
}
