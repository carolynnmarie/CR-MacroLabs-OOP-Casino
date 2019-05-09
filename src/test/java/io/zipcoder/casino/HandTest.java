package io.zipcoder.casino;

import io.zipcoder.casino.Cards.Card;
import io.zipcoder.casino.Cards.Rank;
import io.zipcoder.casino.Cards.Suit;
import io.zipcoder.casino.People.Hand;
import org.junit.Test;
import org.junit.Assert;

import java.util.ArrayList;
import java.util.Arrays;


public class HandTest {

    private Hand hand = new Hand();
    private ArrayList<Card> handArrayList;

    @Test
    public void handConstructorArrayList() {
        int expectedHandSize = 0;
        int actualHandSize = hand.toArrayList().size();
        Assert.assertEquals(expectedHandSize, actualHandSize);

    }

    @Test
    public void getHandArrayList() {
        // Given
        Card expectedCard = new Card(Rank.SEVEN, Suit.SPADES);
        String expectedCardString = expectedCard.toString();
        // When
        hand.toArrayList().add(expectedCard);
        Card actualCard = hand.toArrayList().get(0);
        String actualCardString = actualCard.toString();
        // Then
        Assert.assertEquals(expectedCardString, actualCardString);
    }

    @Test
    public void receiveCardsFromDealerTest() { // This is like getHand
        // Given
        hand.receiveCards(new Card(Rank.DEUCE, Suit.CLUBS));
        Card expectedCard0 = new Card(Rank.DEUCE, Suit.CLUBS);
        String expectedString = expectedCard0.toString();
        // When
        Card actualCard0 = hand.toArrayList().get(0);
        String actualString = actualCard0.toString();

        // Then
        Assert.assertEquals(expectedString, actualString);

    }

    @Test
    public void drawCardFromHandTest() {
        Card expectedToStay = new Card(Rank.DEUCE, Suit.CLUBS);
        Card expectedToDelete = new Card(Rank.THREE, Suit.CLUBS);
        hand.receiveCards(expectedToStay, expectedToDelete);

        int expectedArraySizeAfterDeletion = 1;
        // When
        hand.drawCard();
        int actualArraySizeAfterDeletion = hand.toArrayList().size();
        // Then
        Assert.assertEquals(expectedArraySizeAfterDeletion, actualArraySizeAfterDeletion);
    }

    @Test
    public void clearHandTest() {
        int expectedArraySize = 0;
        Card expectedToDelete = new Card(Rank.THREE, Suit.CLUBS);
        hand.receiveCards(expectedToDelete);
        // When
        hand.toArrayList().clear();
        int actualArraySize = hand.toArrayList().size();
        // Then
        Assert.assertEquals(expectedArraySize, actualArraySize);

    }

    @Test
    public void shuffleHandTest(){
        Card notShuffled0 = new Card(Rank.DEUCE, Suit.CLUBS);
        Card notShuffled1 = new Card(Rank.THREE, Suit.DIAMONDS);
        Card notShuffled2 = new Card(Rank.FOUR, Suit.HEARTS);
        Card notShuffled3 = new Card(Rank.FIVE, Suit.SPADES);
        hand.receiveCards(notShuffled0, notShuffled1, notShuffled2, notShuffled3);
        // When
        hand.shuffleHand();
        Card cardAtIndex0 = hand.toArrayList().get(0);
        // Then
        Assert.assertNotEquals(notShuffled0, cardAtIndex0);

    }


    @Test
    public void toStringTest() {
        Card notShuffled0 = new Card(Rank.DEUCE, Suit.CLUBS);
        Card notShuffled1 = new Card(Rank.THREE, Suit.DIAMONDS);
        Card notShuffled2 = new Card(Rank.FOUR, Suit.HEARTS);
        Card notShuffled3 = new Card(Rank.FIVE, Suit.SPADES);
        hand.receiveCards(notShuffled0, notShuffled1, notShuffled2, notShuffled3);
        // Print
        System.out.println("My hand: " + "\u270B" + Arrays.toString(hand.toArrayList().toArray()) + "\u270B");
        System.out.println("\u2660" + "\u2663" + "\u2665" + "\u2666");

    }




}
