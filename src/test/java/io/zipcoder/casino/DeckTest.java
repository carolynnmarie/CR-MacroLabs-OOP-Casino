package io.zipcoder.casino;

import io.zipcoder.casino.cards.Card;
import io.zipcoder.casino.cards.Deck;
import org.junit.Assert;
import org.junit.Test;

import java.util.ArrayList;

public class DeckTest {

    private Deck deckTest = new Deck();
    private Card cardTest;

    @Test
    public void deckConstructorTest() {
        //When
        ArrayList<Card> expected = deckTest.getDeckOfCards();
        ArrayList<Card> actual = deckTest.getDeckOfCards();
        //Then
        Assert.assertEquals(expected, actual);
    }

    @Test
    public void ShuffleDeckTest(){
        Deck expected = deckTest;
        deckTest.shuffleDeck();
        //When
        ArrayList<Card> actual = deckTest.getDeckOfCards();
        //Then
        Assert.assertNotEquals(expected, actual);
    }

    @Test
    public void DrawCardTest(){
        //When
        Card actual = deckTest.getCard();
        Card expected = deckTest.drawCard();
        //Then
        Assert.assertEquals(expected, actual);
    }

    @Test
    public void clearDeckTest(){
        ArrayList<Card> expected = new ArrayList<>();
        deckTest.clearDeck();
        ArrayList<Card> actual = deckTest.getDeckOfCards();
        Assert.assertEquals(expected,actual);
    }

    @Test
    public void dealHand(){
        int expected = 2;
        ArrayList<Card> hand = deckTest.dealHand(2);
        int actual = hand.size();
        Assert.assertEquals(expected,actual);
    }

}
