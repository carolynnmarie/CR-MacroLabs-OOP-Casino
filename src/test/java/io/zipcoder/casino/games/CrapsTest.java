package io.zipcoder.casino.games;

import io.zipcoder.casino.games.Craps;
import io.zipcoder.casino.people.Person;
import org.junit.Assert;
import org.junit.Before;
import org.junit.Test;

import java.util.HashMap;

public class CrapsTest {

    private Person person = new Person("Luis");
    private Craps craps = new Craps(person);

    @Before public void setUp(){
        person.getWallet().addChips(5000);
        craps.setPassLineBet(50);
        craps.setDontPassLineBet(50);
        craps.setPassOddsBet(50);
        craps.setDontPassOddsBet(50);
    }


    @Test
    public void constructorTest(){
        String expected = "Luis";
        String actual = craps.getPlayer().getName();
        Assert.assertEquals(expected, actual);
    }

    @Test
    public void checkChipsTest(){
        int expected = 500;
        int actual = craps.checkChipAmount(person);
        Assert.assertEquals(expected, actual);
    }

    @Test
    public void placeBetTest(){
        int expected = 450;
        craps.placeBet(person, 50);
        int actual = craps.checkChipAmount(person);
        Assert.assertEquals(expected, actual);
    }

    @Test
    public void comeOutRollTest(){
        int expected = 4;
        craps.comeOutRoll(4);
        int actual = craps.getPoint();
        Assert.assertEquals(expected, actual);
    }

    @Test
    public void phaseTwoRollsTest(){

    }

    @Test
    public void passLineBetWinTest(){ }

    @Test
    public void passLineBetLoseTest(){ }

    @Test
    public void dontPassLineBetWinTest(){ }

    @Test
    public void dontPassLineBetLoseTest(){ }

    @Test
    public void comeBetResultTest(){ }

    @Test
    public void comeBetPointResultTest(){ }

    @Test
    public void dontComeBetResultPointTest(){ }

    @Test
    public void fieldBetResultTest(){ }

    @Test
    public void passLineOddsCheckTest(){ }

    @Test
    public void dontPassLineOddsCheckTest(){ }

    @Test
    public void comeBetPointOddsTest(){}

    @Test
    public void dontComeBetPointOddsTest(){}

    @Test
    public void placeWinBetMapTest(){
        craps.setPlaceWinBets(new HashMap<>());
        craps.getPlaceWinBets().put(10,10);
    }

    @Test
    public void placeLoseBetMapTest(){
        craps.setPlaceLoseBets(new HashMap<>());
        craps.getPlaceLoseBets().put(10,10);
    }

}
