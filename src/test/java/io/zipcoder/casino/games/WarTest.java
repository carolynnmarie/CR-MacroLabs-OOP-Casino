package io.zipcoder.casino.games;

import io.zipcoder.casino.cards.Card;
import io.zipcoder.casino.cards.Rank;
import io.zipcoder.casino.cards.Suit;
import io.zipcoder.casino.games.cardGames.War;
import io.zipcoder.casino.people.Person;
import org.junit.Assert;
import org.junit.Test;

import java.util.ArrayList;
import java.util.Arrays;

public class WarTest {


    Person player = new Person();
    War warGame = new War(player);
    ArrayList<Card> playerHand = new ArrayList<>(Arrays.asList(new Card(Rank.THREE, Suit.DIAMONDS), new Card(Rank.SEVEN,Suit.HEARTS),
            new Card(Rank.EIGHT, Suit.CLUBS), new Card(Rank.DEUCE, Suit.SPADES)));
    ArrayList<Card> dealerHand = new ArrayList<>(Arrays.asList(new Card(Rank.NINE, Suit.HEARTS), new Card(Rank.ACE, Suit.HEARTS),
            new Card(Rank.QUEEN, Suit.SPADES), new Card(Rank.FIVE, Suit.HEARTS)));


    @Test
    public void checkNumberOfCards() {
        int expected = 26;
        int actual = warGame.getPlayerHand().size();
        Assert.assertEquals(expected, actual);
    }

    @Test
    public void iDeclareWarTest() {
        int expected = 2;
        int actual = warGame.iDeclareWar(playerHand, dealerHand);
        Assert.assertEquals(expected, actual);
    }

//    @Test
//    public void compareCardsTest2() {
//        Person player = new Person();
//        War warGame = new War(player);
//        Card smallCard = new Card(Rank.EIGHT, Suit.DIAMONDS);
//        Card bigCard = new Card(Rank.DEUCE, Suit.DIAMONDS);
//        int expected = 1;
//        int actual = warGame.compareCards(smallCard, bigCard);
//        Assert.assertEquals(expected, actual);
//    }
//
//    @Test
//    public void dealCardsTest() {
//        Person player = new Person();
//        War warGame = new War(player);
//        warGame.start();
//        int expected = 26;
//        int actual = player.getHand().getHandArrayList().size();
//        Assert.assertEquals(expected, actual);
//    }
//
//    @Test
//    public void decideHowManyYaddaYaddaTest1() {
//        Person player = new Person();
//        War warGame = new War(player);
//        int expected = 4;
//        int actual = warGame.decideOnHowManyTimesToIterateBasedOn(4);
//        Assert.assertEquals(expected, actual);
//    }
//
//    @Test
//    public void decideHowManyYaddaYaddaTest2() {
//        Person player = new Person();
//        War warGame = new War(player);
//        int expected = 4;
//        int actual = warGame.decideOnHowManyTimesToIterateBasedOn(7);
//        Assert.assertEquals(expected, actual);
//    }
//
//    @Test
//    public void decideHowManyYaddaYaddaTest3() {
//        Person player = new Person();
//        War warGame = new War(player);
//        int expected = 2;
//        int actual = warGame.decideOnHowManyTimesToIterateBasedOn(2);
//        Assert.assertEquals(expected, actual);
//    }

}
