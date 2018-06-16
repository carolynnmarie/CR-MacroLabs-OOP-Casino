package io.zipcoder.casino.diceAndCoins;

import io.zipcoder.casino.diceAndCoins.Die;
import io.zipcoder.casino.diceAndCoins.DieFace;
import org.junit.Assert;
import org.junit.Before;
import org.junit.Test;

public class DieTest {

    private static Die die;

    @Before
    public void setup() {
        this.die = new Die();
        die.rollDie();
    }

    @Test
    public void getDieFaceTest() {
        Integer actual = this.die.getDieFace().getFaceValue();
        System.out.println(actual);
        Assert.assertTrue(1 <= actual && actual <= 6);
    }

    @Test
    public void setDieFaceTest() {
        this.die.setDieFace(DieFace.ONE);
        int expected = 1;
        int actual = this.die.getDieFace().getFaceValue();
        Assert.assertEquals(expected, actual);
    }



}
