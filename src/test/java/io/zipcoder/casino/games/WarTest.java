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


    private Person player = new Person("Carolynn");
    private Person dealer = new Person("Dealer");
    private War warGame = new War(player);
    private ArrayList<Card> playerHand = new ArrayList<>(Arrays.asList(new Card(Rank.THREE, Suit.DIAMONDS),
            new Card(Rank.SEVEN,Suit.HEARTS), new Card(Rank.EIGHT, Suit.CLUBS), new Card(Rank.DEUCE, Suit.SPADES)));
    private ArrayList<Card> dealerHand = new ArrayList<>(Arrays.asList(new Card(Rank.NINE, Suit.HEARTS),
            new Card(Rank.ACE, Suit.HEARTS), new Card(Rank.QUEEN, Suit.SPADES), new Card(Rank.FIVE, Suit.HEARTS),
            new Card(Rank.FOUR, Suit.CLUBS)));


    @Test
    public void getPlayerHandTest() {
        int expected = 26;
        int actual = warGame.getPlayerHand().size();
        Assert.assertEquals(expected, actual);
    }

    @Test
    public void getDealerHandTest(){
        int expected = 26;
        int actual = warGame.getDealerHand().size();
        Assert.assertEquals(expected, actual);
    }

    @Test
    public void iDeclareWarTest() {
        int expected = 2;
        int actual = warGame.iDeclareWar(playerHand, dealerHand);
        Assert.assertEquals(expected, actual);
    }

    @Test
    public void warCardSwapTest(){
        ArrayList<Card> array = new ArrayList<>(Arrays.asList(new Card(Rank.FIVE, Suit.HEARTS), new Card(Rank.FOUR, Suit.CLUBS),
                new Card(Rank.THREE, Suit.DIAMONDS),new Card(Rank.NINE, Suit.HEARTS), new Card(Rank.SEVEN,Suit.HEARTS),
                new Card(Rank.ACE, Suit.HEARTS), new Card(Rank.EIGHT, Suit.CLUBS), new Card(Rank.QUEEN, Suit.SPADES)));
        String expected = array.toString();
        warGame.warCardSwap(playerHand,dealerHand);
        String actual = dealerHand.toString();
        Assert.assertEquals(expected, actual);
    }

    @Test
    public void getWarTablePileTest(){
        ArrayList<Card> array = new ArrayList<>(Arrays.asList(new Card(Rank.THREE, Suit.DIAMONDS),new Card(Rank.NINE, Suit.HEARTS),
                new Card(Rank.SEVEN,Suit.HEARTS),new Card(Rank.ACE, Suit.HEARTS), new Card(Rank.EIGHT, Suit.CLUBS),
                new Card(Rank.QUEEN, Suit.SPADES)));
        String expected = array.toString();
        String actual = warGame.getWarTablePile(playerHand,dealerHand).toString();
        Assert.assertEquals(expected, actual);
    }

    @Test
    public void warCardNumberTest(){
        int expected = 3;
        int actual = warGame.warCardsNumber(playerHand,dealerHand);
        Assert.assertEquals(expected, actual);
    }

    @Test
    public void addToHandTest(){
        Card card1 = new Card(Rank.NINE, Suit.CLUBS);
        Card card2 = new Card(Rank.THREE, Suit.HEARTS);
        ArrayList<Card> expectedArray = new ArrayList<>(Arrays.asList(new Card(Rank.THREE, Suit.DIAMONDS),
                new Card(Rank.SEVEN,Suit.HEARTS), new Card(Rank.EIGHT, Suit.CLUBS),
                new Card(Rank.DEUCE, Suit.SPADES),new Card(Rank.NINE, Suit.CLUBS),
                new Card(Rank.THREE, Suit.HEARTS)));
        String expected = expectedArray.toString();
        warGame.addToHand(card1, card2, playerHand);
        Assert.assertEquals(expected, playerHand.toString());
    }

    @Test
    public void declareWinnerTest(){
        String expected = "And the winner is the dealer!";
        String actual = warGame.declareWinner(playerHand, dealerHand);
        Assert.assertEquals(expected, actual);
    }


}
