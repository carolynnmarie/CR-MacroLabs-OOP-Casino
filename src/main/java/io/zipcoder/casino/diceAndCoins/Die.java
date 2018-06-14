package io.zipcoder.casino.diceAndCoins;


import static java.lang.Math.ceil;
import static java.lang.Math.random;
import static java.lang.Math.round;

public class Die {

    private DieFace dieFace;


    public Die() {
        this.dieFace = null;
    }

    public Die(DieFace dieFace){
        this.dieFace = dieFace;
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
        switch ((int)(ceil(random()*6))) {
            case 1:
                this.dieFace = DieFace.ONE;
                break;
            case 2:
                this.dieFace = DieFace.TWO;
                break;
            case 3:
                this.dieFace =DieFace.THREE;
                break;
            case 4:
                this.dieFace =DieFace.FOUR;
                break;
            case 5:
                this.dieFace =DieFace.FIVE;
                break;
            case 6:
                this.dieFace = DieFace.SIX;
                break;
            default:
                this.dieFace = DieFace.ONE;
        }
    }
}
