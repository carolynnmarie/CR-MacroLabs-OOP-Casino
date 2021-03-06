package io.zipcoder.casino;

import io.zipcoder.casino.Cards.Card;
import io.zipcoder.casino.Cards.Rank;
import io.zipcoder.casino.Cards.Suit;
import io.zipcoder.casino.Games.War;
import io.zipcoder.casino.People.Hand;
import io.zipcoder.casino.People.Person;
import org.junit.Assert;
import org.junit.Test;

public class WarTest {

    // psvm for isolated War testing
//    public static void main(String[] args) {
//        Person player = new Person();
//        War war = new War(player);
//        war.start();
//    }

    Person player = new Person("Player");
    War warGame = new War(player);

    @Test
    public void checkNumberOfCards() {

        Hand hand = new Hand();
        hand.toArrayList().add(new Card(Rank.DEUCE, Suit.CLUBS));
        hand.toArrayList().add(new Card(Rank.NINE, Suit.HEARTS));
        int expected = 2;
        int actual = warGame.checkHandSize(hand);
        Assert.assertEquals(expected, actual);
    }

    @Test
    public void compareCardsTest() {
        Card smallCard = new Card(Rank.THREE, Suit.DIAMONDS);
        Card bigCard = new Card(Rank.SEVEN, Suit.DIAMONDS);
        int expected = 2;
        int actual = warGame.compareCards(smallCard, bigCard);
        Assert.assertEquals(expected, actual);
    }

    @Test
    public void compareCardsTest2() {
        Card smallCard = new Card(Rank.EIGHT, Suit.DIAMONDS);
        Card bigCard = new Card(Rank.DEUCE, Suit.DIAMONDS);
        int expected = 1;
        int actual = warGame.compareCards(smallCard, bigCard);
        Assert.assertEquals(expected, actual);
    }

    @Test
    public void dealCardsTest() {
        warGame.start();
        int expected = 26;
        int actual = player.getHand().toArrayList().size();
        Assert.assertEquals(expected, actual);
    }

}
