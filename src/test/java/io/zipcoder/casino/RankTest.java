package io.zipcoder.casino;

import io.zipcoder.casino.Cards.Card;
import io.zipcoder.casino.Cards.Rank;
import io.zipcoder.casino.Cards.Suit;
import org.junit.Assert;
import org.junit.Test;

public class RankTest {

    private Card card;

    @Test
    public void rankToIntTest() {
        int expectedRankValue = 3;
        Card card = new Card(Rank.THREE, Suit.HEARTS);
        int actualRankValue = card.getRank().toInt();

        Assert.assertEquals(expectedRankValue, actualRankValue);
    }

    @Test
    public void rankToStringTest() {
        String expectedRankSymbol = "3";
        Card card = new Card(Rank.THREE, Suit.HEARTS);
        String actualRankSymbol = card.getRank().toString();

        Assert.assertEquals(expectedRankSymbol, actualRankSymbol);
    }



}
