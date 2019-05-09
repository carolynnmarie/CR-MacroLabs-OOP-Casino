package io.zipcoder.casino;

import io.zipcoder.casino.Cards.Card;
import io.zipcoder.casino.Cards.Deck;
import org.junit.Assert;
import org.junit.Test;

import java.util.ArrayList;

public class DeckTest {

    private Deck deckTest = new Deck();

    @Test
    public void DeckTest() {
        int actual = deckTest.getDeck().size();
        int expected = 52;

        Assert.assertEquals(expected, actual);
    }

    @Test
    public void ShuffleDeckTest(){
        Deck deck = new Deck();
        deckTest.shuffleDeck();
        ArrayList<Card> expected = deck.getDeck();
        ArrayList<Card> actual = deckTest.getDeck();
        System.out.println(expected);
        System.out.println(actual);

        Assert.assertNotEquals(expected, actual);
    }

    @Test
    public void DrawCardTest(){
        Card card = deckTest.drawCard();
        Card card1 = deckTest.drawCard();
        int expected = 50;
        int actual = deckTest.deckSize();
        Assert.assertEquals(expected,actual);
    }

}
