package io.zipcoder.casino.cards;

import io.zipcoder.casino.cards.Card;
import io.zipcoder.casino.cards.Rank;
import io.zipcoder.casino.cards.Suit;
import org.junit.Assert;
import org.junit.Test;

public class CardTest {

    private Card card;

    @Test
    public void CardConstructorTest() {
        // Given
        Rank expectedRank = Rank.DEUCE;
        Suit expectedSuit = Suit.CLUBS;
        card = new Card(Rank.DEUCE, Suit.CLUBS);
        // When
        Rank actualRank = card.getRank();
        Suit actualSuit = card.getSuit();
        // Then
        Assert.assertEquals(expectedRank, actualRank);
        Assert.assertEquals(expectedSuit, actualSuit);
    }

    @Test
    public void getRankTest() {
        // Given
        Rank expectedRank = Rank.SEVEN;
        card = new Card(Rank.SEVEN, Suit.SPADES);
        // When
        Rank actualRank = card.getRank();
        // Then
        Assert.assertEquals(expectedRank, actualRank);
    }

    @Test
    public void getSuitTest() {
        // Given
        Suit expectedSuit = Suit.DIAMONDS;
        card = new Card(Rank.EIGHT, Suit.DIAMONDS);
        // When
        Suit actualSuit = card.getSuit();
        // Then
        Assert.assertEquals(expectedSuit, actualSuit);

    }

    @Test
    public void toStringTest() {
        // Given
        String expectedString = "J\u2665";
        card = new Card(Rank.JACK, Suit.HEARTS);
        // When
        String actualString = card.toString();
        // Then
        Assert.assertEquals(expectedString, actualString);
    }

}