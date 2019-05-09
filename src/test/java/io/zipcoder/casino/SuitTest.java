package io.zipcoder.casino;

import io.zipcoder.casino.Cards.Card;
import io.zipcoder.casino.Cards.Rank;
import io.zipcoder.casino.Cards.Suit;
import org.junit.Assert;
import org.junit.Test;

public class SuitTest {

    private Card card = new Card(Rank.NINE, Suit.CLUBS);

    @Test
    public void getSuitWordTest() {
        String expectedSuitWord = "clubs";
        String actualSuitWord = card.getSuit().getSuitWord();

        // Then
        Assert.assertEquals(expectedSuitWord, actualSuitWord);
    }

    @Test
    public void suitToStringTest() {
        String expected = "\u2663";
        String actual = card.getSuit().toSymbol();

        // Then
        Assert.assertEquals(expected, actual);
    }
}