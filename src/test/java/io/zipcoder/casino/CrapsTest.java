package io.zipcoder.casino;

import io.zipcoder.casino.Dice.DieFace;
import io.zipcoder.casino.Games.Craps;
import io.zipcoder.casino.People.Person;
import org.junit.Assert;
import org.junit.Before;
import org.junit.Test;

public class CrapsTest {
    Person player = new Person("Luis");
    Craps craps = new Craps(player);

    @Before public void addMoney() {
            player.getWallet().addChips(500);
        }

    @Test
    public void checkPassLineBetWin() {
        craps.getDiceManager().setSpecificDie(0,DieFace.TWO);
        craps.getDiceManager().setSpecificDie(1,DieFace.SIX);
        craps.setPassLineBet(5);
        craps.setPoint(8);
        craps.checkBetHandler();
        int expected = 505;
        int actual = craps.getPlayer().getWallet().checkChipAmount();
        Assert.assertEquals(expected,actual);
    }

    @Test
    public void checkPassLineBetLose(){
        craps.getDiceManager().setSpecificDie(0,DieFace.ONE);
        craps.getDiceManager().setSpecificDie(1,DieFace.SIX);
        craps.setPassLineBet(5);
        craps.setPoint(8);
        craps.checkBetHandler();
        int expected = 500;
        int actual = craps.getPlayer().getWallet().checkChipAmount();
        Assert.assertEquals(expected,actual);
    }

    @Test
    public void checkDontPassLineBetWin() {
        craps.getDiceManager().setSpecificDie(0,DieFace.ONE);
        craps.getDiceManager().setSpecificDie(1,DieFace.SIX);
        craps.setDontPassLineBet(5);
        craps.setPoint(8);
        craps.checkBetHandler();
        int expected = 505;
        int actual = craps.getPlayer().getWallet().checkChipAmount();
        Assert.assertEquals(expected,actual);
    }

    @Test
    public void checkDontPassLineBetLose() {
        craps.getDiceManager().setSpecificDie(0,DieFace.TWO);
        craps.getDiceManager().setSpecificDie(1,DieFace.SIX);
        craps.setDontPassLineBet(5);
        craps.setPoint(8);
        craps.checkBetHandler();
        int expected = 500;
        int actual = craps.getPlayer().getWallet().checkChipAmount();
        Assert.assertEquals(expected,actual);
    }

    @Test
    public void checkComeBetWin() {
        craps.getDiceManager().setSpecificDie(0,DieFace.ONE);
        craps.getDiceManager().setSpecificDie(1,DieFace.SIX);
        craps.setPoint(8);
        craps.setComeBet(5);
        craps.checkBetHandler();
        int expected = 505;
        int actual = craps.getPlayer().getWallet().checkChipAmount();
        Assert.assertEquals(expected,actual);
    }

    @Test
    public void checkComeBeLose() {
        craps.getDiceManager().setSpecificDie(0,DieFace.ONE);
        craps.getDiceManager().setSpecificDie(1,DieFace.ONE);
        craps.setPoint(8);
        craps.setComeBet(5);
        craps.checkBetHandler();
        int expected = 500;
        int actual = craps.getPlayer().getWallet().checkChipAmount();
        Assert.assertEquals(expected,actual);
    }

    @Test
    public void checkDontComeBetWin() {
        craps.getDiceManager().setSpecificDie(0,DieFace.ONE);
        craps.getDiceManager().setSpecificDie(1,DieFace.ONE);
        craps.setPoint(8);
        craps.setDontComeBet(5);
        craps.checkBetHandler();
        int expected = 505;
        int actual = craps.getPlayer().getWallet().checkChipAmount();
        Assert.assertEquals(expected,actual);
    }

    @Test
    public void checkDontComeBetLose() {
        craps.getDiceManager().setSpecificDie(0,DieFace.ONE);
        craps.getDiceManager().setSpecificDie(1,DieFace.SIX);
        craps.setPoint(8);
        craps.setDontComeBet(5);
        craps.checkBetHandler();
        int expected = 500;
        int actual = craps.getPlayer().getWallet().checkChipAmount();
        Assert.assertEquals(expected,actual);
    }

    @Test
    public void checkFieldBetDoubleWin() {
        craps.getDiceManager().setSpecificDie(0, DieFace.ONE);
        craps.getDiceManager().setSpecificDie(1, DieFace.ONE);
        craps.setFieldBet(10);
        craps.checkBetHandler();
        int expected = 520;
        int actual = craps.getPlayer().getWallet().checkChipAmount();
        Assert.assertEquals(expected, actual);
    }

    @Test
    public void checkFieldBetWin() {
        craps.getDiceManager().setSpecificDie(0, DieFace.TWO);
        craps.getDiceManager().setSpecificDie(1, DieFace.ONE);
        craps.setFieldBet(10);
        craps.checkBetHandler();
        int expected = 510;
        int actual = craps.getPlayer().getWallet().checkChipAmount();
        Assert.assertEquals(expected, actual);
    }

    @Test
    public void checkFieldBetLose() {
        craps.getDiceManager().setSpecificDie(0, DieFace.TWO);
        craps.getDiceManager().setSpecificDie(1, DieFace.THREE);
        craps.setFieldBet(10);
        craps.checkBetHandler();
        int expected = 500;
        int actual = craps.getPlayer().getWallet().checkChipAmount();
        Assert.assertEquals(expected, actual);
    }

    @Test
    public void checkPassOddsBetWin() {
        craps.getDiceManager().setSpecificDie(0, DieFace.TWO);
        craps.getDiceManager().setSpecificDie(1, DieFace.TWO);
        craps.setPassOddsBet(10);
        craps.setPoint(4);
        craps.checkBetHandler();
        int expected = 530;
        int actual = craps.getPlayer().getWallet().checkChipAmount();
        Assert.assertEquals(expected, actual);
    }

    @Test
    public void checkPassOddsBetLose() {
        craps.getDiceManager().setSpecificDie(0, DieFace.TWO);
        craps.getDiceManager().setSpecificDie(1, DieFace.FIVE);
        craps.setPassOddsBet(10);
        craps.setPoint(4);
        craps.checkBetHandler();
        int expected = 500;
        int actual = craps.getPlayer().getWallet().checkChipAmount();
        Assert.assertEquals(expected, actual);
    }

    @Test
    public void checkDontPassOddsWin() {
        craps.getDiceManager().setSpecificDie(0, DieFace.TWO);
        craps.getDiceManager().setSpecificDie(1, DieFace.FIVE);
        craps.setDontPassOddsBet(10);
        craps.setPoint(4);
        craps.checkBetHandler();
        int expected = 515;
        int actual = craps.getPlayer().getWallet().checkChipAmount();
        Assert.assertEquals(expected, actual);
    }

    @Test
    public void checkDontPassOddsLose() {
        craps.getDiceManager().setSpecificDie(0, DieFace.TWO);
        craps.getDiceManager().setSpecificDie(1, DieFace.TWO);
        craps.setDontPassOddsBet(10);
        craps.setPoint(4);
        craps.checkBetHandler();
        int expected = 500;
        int actual = craps.getPlayer().getWallet().checkChipAmount();
        Assert.assertEquals(expected, actual);
    }

    @Test
    public void checkComePointBetWin() {
        craps.getDiceManager().setSpecificDie(0, DieFace.TWO);
        craps.getDiceManager().setSpecificDie(1, DieFace.THREE);
        craps.setComeBetPoints(5,10);
        craps.setPoint(4);
        craps.checkBetHandler();
        int expected = 510;
        int actual = craps.getPlayer().getWallet().checkChipAmount();
        Assert.assertEquals(expected, actual);
    }

    @Test
    public void checkComePointBetLose() {
        craps.getDiceManager().setSpecificDie(0, DieFace.TWO);
        craps.getDiceManager().setSpecificDie(1, DieFace.FIVE);
        craps.setComeBetPoints(5,10);
        craps.setComeBetPoints(4,10);
        craps.setPoint(4);
        craps.checkBetHandler();
        int expected = 500;
        int actual = craps.getPlayer().getWallet().checkChipAmount();
        Assert.assertEquals(expected, actual);
    }

    @Test
    public void checkDontComePointBetWin() {
        craps.getDiceManager().setSpecificDie(0, DieFace.TWO);
        craps.getDiceManager().setSpecificDie(1, DieFace.FIVE);
        craps.setDontComeBetPoints(5,10);
        craps.setDontComeBetPoints(4,10);
        craps.setPoint(4);
        craps.checkBetHandler();
        int expected = 520;
        int actual = craps.getPlayer().getWallet().checkChipAmount();
        Assert.assertEquals(expected, actual);
    }

    @Test
    public void checkDontComePointBetLose() {
        craps.getDiceManager().setSpecificDie(0, DieFace.TWO);
        craps.getDiceManager().setSpecificDie(1, DieFace.TWO);
        craps.setDontComeBetPoints(4,10);
        craps.setPoint(4);
        craps.checkBetHandler();
        int expected = 500;
        int actual = craps.getPlayer().getWallet().checkChipAmount();
        Assert.assertEquals(expected, actual);
    }

    @Test
    public void checkComePointOddsBetWin1() { // Checks Come Point Odds bet on 5s
        craps.getDiceManager().setSpecificDie(0, DieFace.TWO);
        craps.getDiceManager().setSpecificDie(1, DieFace.THREE);
        craps.setComeBetPointOdds(5,10);
        craps.setPoint(4);
        craps.checkBetHandler();
        int expected = 525;
        int actual = craps.getPlayer().getWallet().checkChipAmount();
        Assert.assertEquals(expected, actual);
    }

    @Test
    public void checkComePointOddsBetWin2() { // Checks Come Point Odds bet on 4
        craps.getDiceManager().setSpecificDie(0, DieFace.TWO);
        craps.getDiceManager().setSpecificDie(1, DieFace.TWO);
        craps.setComeBetPointOdds(4,10);
        craps.setPoint(5);
        craps.checkBetHandler();
        int expected = 530;
        int actual = craps.getPlayer().getWallet().checkChipAmount();
        Assert.assertEquals(expected, actual);
    }

    @Test
    public void checkComePointOddsBetWin3() { // Checks Come Point Odds bet on 6
        craps.getDiceManager().setSpecificDie(0, DieFace.TWO);
        craps.getDiceManager().setSpecificDie(1, DieFace.FOUR);
        craps.setComeBetPointOdds(6,10);
        craps.setPoint(5);
        craps.checkBetHandler();
        int expected = 522;
        int actual = craps.getPlayer().getWallet().checkChipAmount();
        Assert.assertEquals(expected, actual);
    }

    @Test
    public void checkComePointOddsBetLose() {
        craps.getDiceManager().setSpecificDie(0, DieFace.THREE);
        craps.getDiceManager().setSpecificDie(1, DieFace.FOUR);
        craps.setComeBetPointOdds(6,10);
        craps.setComeBetPointOdds(5,10);
        craps.setPoint(5);
        craps.checkBetHandler();
        int expected = 500;
        int actual = craps.getPlayer().getWallet().checkChipAmount();
        Assert.assertEquals(expected, actual);
    }

    @Test
    public void checkDontComePointOddsBetLose() {
        craps.getDiceManager().setSpecificDie(0, DieFace.THREE);
        craps.getDiceManager().setSpecificDie(1, DieFace.THREE);
        craps.setDontComeBetPointOdds(6,10);
        craps.setPoint(6);
        craps.checkBetHandler();
        int expected = 500;
        int actual = craps.getPlayer().getWallet().checkChipAmount();
        Assert.assertEquals(expected, actual);
    }

    @Test
    public void checkDontComePointOddsBetWin() {
        craps.getDiceManager().setSpecificDie(0, DieFace.FOUR);
        craps.getDiceManager().setSpecificDie(1, DieFace.THREE);
        craps.setDontComeBetPointOdds(6,10);
        craps.setDontComeBetPointOdds(5,10);
        craps.setDontComeBetPointOdds(4,10);
        craps.setPoint(6);
        craps.checkBetHandler();
        int expected = 549;
        int actual = craps.getPlayer().getWallet().checkChipAmount();
        Assert.assertEquals(expected, actual);
    }

    @Test
    public void checkPlaceWinBet1() {//Checks Place Win on 6
        craps.getDiceManager().setSpecificDie(0, DieFace.THREE);
        craps.getDiceManager().setSpecificDie(1, DieFace.THREE);
        craps.setPlaceWinBets(6,10);
        craps.setPoint(6);
        craps.checkBetHandler();
        int expected = 521;
        int actual = craps.getPlayer().getWallet().checkChipAmount();
        Assert.assertEquals(expected, actual);
    }

    @Test
    public void checkPlaceWinBet2() {//Checks place win on 5
        craps.getDiceManager().setSpecificDie(0, DieFace.TWO);
        craps.getDiceManager().setSpecificDie(1, DieFace.THREE);
        craps.setPlaceWinBets(5,10);
        craps.setPoint(6);
        craps.checkBetHandler();
        int expected = 524;
        int actual = craps.getPlayer().getWallet().checkChipAmount();
        Assert.assertEquals(expected, actual);
    }

    @Test
    public void checkPlaceWinBet3() {//Checks place win on 4
        craps.getDiceManager().setSpecificDie(0, DieFace.TWO);
        craps.getDiceManager().setSpecificDie(1, DieFace.TWO);
        craps.setPlaceWinBets(4,10);
        craps.setPoint(6);
        craps.checkBetHandler();
        int expected = 528;
        int actual = craps.getPlayer().getWallet().checkChipAmount();
        Assert.assertEquals(expected, actual);
    }

    @Test
    public void checkPlaceWinLose() {
        craps.getDiceManager().setSpecificDie(0, DieFace.TWO);
        craps.getDiceManager().setSpecificDie(1, DieFace.FIVE);
        craps.setPlaceWinBets(4,10);
        craps.setPlaceWinBets(5,10);
        craps.setPlaceWinBets(6,10);
        craps.setPoint(6);
        craps.checkBetHandler();
        int expected = 500;
        int actual = craps.getPlayer().getWallet().checkChipAmount();
        Assert.assertEquals(expected, actual);
    }

    @Test
    public void checkPlaceLoseBetWin() {
        craps.getDiceManager().setSpecificDie(0, DieFace.TWO);
        craps.getDiceManager().setSpecificDie(1, DieFace.FIVE);
        craps.setPlaceLoseBets(4,10);
        craps.setPlaceLoseBets(5,10);
        craps.setPlaceLoseBets(6,10);
        craps.setPoint(6);
        craps.checkBetHandler();
        int expected = 548;
        int actual = craps.getPlayer().getWallet().checkChipAmount();
        Assert.assertEquals(expected, actual);
    }

    @Test
    public void checkPlaceLoseBetLose() {
        craps.getDiceManager().setSpecificDie(0, DieFace.TWO);
        craps.getDiceManager().setSpecificDie(1, DieFace.FOUR);
        craps.setPlaceLoseBets(6, 10);
        craps.setPoint(6);
        craps.checkBetHandler();
        int expected = 500;
        int actual = craps.getPlayer().getWallet().checkChipAmount();
        Assert.assertEquals(expected, actual);
    }
}
