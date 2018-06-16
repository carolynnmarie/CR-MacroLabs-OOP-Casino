package io.zipcoder.casino.diceAndCoins;

import io.zipcoder.casino.diceAndCoins.DiceManager;
import io.zipcoder.casino.diceAndCoins.Die;
import io.zipcoder.casino.diceAndCoins.DieFace;
import org.junit.Assert;
import org.junit.Test;

import java.util.ArrayList;
import java.util.Arrays;

public class DiceManagerTest {

    @Test
    public void amountOfDiceCreatedTest() {
        DiceManager allDice = new DiceManager(5);
        int expected = 5;
        int actual = allDice.getAllDieFaces().length;
        Assert.assertEquals(expected, actual);
    }

    @Test
    public void rollAllDiceTest() {
        // This test may occasionally fail if the dice rolled all wind up on the same face, but that should be very rare
        DiceManager allDice = new DiceManager(5);
        allDice.rollAllDice();
        int firstDieTotal = allDice.getTotalValue();
        allDice.rollAllDice();
        int secondDieTotal = allDice.getTotalValue();
        Assert.assertNotEquals(firstDieTotal, secondDieTotal);
    }

    @Test
    public void setSpecificDieFaceTest() {
        DiceManager allDice = new DiceManager(3);
        allDice.setSpecificDie(2, DieFace.ONE);
        DieFace expected = DieFace.ONE;
        DieFace actual = allDice.getAllDieFaces()[2];
        Assert.assertEquals(expected, actual);
    }

    @Test
    public void getDiceArrayTest() {
        DiceManager allDice = new DiceManager(3);
        ArrayList<Die> expected = new ArrayList<>(Arrays.asList(new Die(DieFace.THREE),new Die(DieFace.SIX),new Die(DieFace.FIVE)));
        allDice.setDiceArray(expected);
        ArrayList<Die> actual = allDice.getDiceArray();
        Assert.assertEquals(expected, actual);
    }

    @Test
    public void getTotalValueTest() {
        DiceManager allDice = new DiceManager(2);
        allDice.setSpecificDie(0, DieFace.TWO);
        allDice.setSpecificDie(1,DieFace.TWO);
        int expected = 4;
        int actual = allDice.getTotalValue();
        Assert.assertEquals(expected, actual);
    }

}
