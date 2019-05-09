package io.zipcoder.casino;

import io.zipcoder.casino.Cards.Card;
import io.zipcoder.casino.Cards.Deck;
import io.zipcoder.casino.Cards.Rank;
import io.zipcoder.casino.Cards.Suit;
import io.zipcoder.casino.Games.GoFish;
import io.zipcoder.casino.People.Person;
import java.util.*;
import org.junit.Assert;
import org.junit.Before;
import org.junit.Test;

public class GoFishTest {

    private Deck houseDeckTest = new Deck();
    private Deck houseDeckTest2 = new Deck();

    private Person player1Test = new Person("Joe");
    private Person dealerTest = new Person("Dealer");
    private Person playerBookTest = new Person("BookTest");

    private Card cardTest1 = new Card(Rank.SEVEN,Suit.SPADES);
    private Card cardTest2 = new Card(Rank.SEVEN,Suit.HEARTS);
    private Card cardTest3 = new Card(Rank.SEVEN,Suit.DIAMONDS);
    private Card cardTest4 = new Card(Rank.SEVEN,Suit.CLUBS);
    private Card cardTest5 = new Card(Rank.ACE,Suit.SPADES);
    private ArrayList<Card> cardList = new ArrayList<>(Arrays.asList(cardTest1,cardTest2,cardTest3,cardTest4,cardTest5));


    @Before
    public void startingUp() {
        for (int i = 0; i < 7; i++) {
            player1Test.getHand().receiveCard(houseDeckTest.drawCard());
            dealerTest.getHand().receiveCard(houseDeckTest.drawCard());
        }

        houseDeckTest2.clearDeck();
        houseDeckTest2.addCards(cardList);
        playerBookTest.getHand().receiveCards(cardList);
    }

//    @Test
//    public void StartingDeckTest() {
//        Person player1 = new Person("Joe");
//        GoFish goFishTest = new GoFish(player1);
//        Deck deckTest = new Deck();
//        goFishTest.startingDrawDeck(deckTest);
//
//        int expected = 38;
//        int actual = deckTest.getDeck().size();
//
//        Assert.assertEquals(expected, actual);
//    }

//    @Test
//    public void doYouHaveTheCardThatIWant() {
//        Person player1 = new Person("Joe");
//        GoFish goFishTest = new GoFish(player1);
//
//        // card counts as int 1
//        Card cardSend = new Card(Rank.ACE, Suit.SPADES);
//
//        int expected = 1;
//        int actual = goFishTest.doesDealerHaveCard(1, dealerTest);
//
//        Assert.assertEquals(expected, actual);
//    }

//    @Test
//    public void removeCardsFromComputerPlayerAndIntoHandTest () {
//        Person player1 = new Person("Joe");
//        GoFish goFishTest = new GoFish(player1);
//
//        Card cardSend = new Card(Rank.ACE, Suit.SPADES);
//
//        int actual = goFishTest.giveCards(1, dealerTest, player1Test);
//        int expected = cardSend.getRank().toInt();
//
//
//        Assert.assertEquals(expected, actual);
//    }

    @Test
    public void BookCheckerTest(){
        System.out.println(playerBookTest.getHand().toArrayList());

        String expected = "[A♠, 7♣, 7♦, 7♥, 7♠]";
        String actual = playerBookTest.getHand().toArrayList().toString();

        System.out.println(playerBookTest.getHand().toArrayList());

        Assert.assertEquals(expected, actual);
    }


    @Test
    public void goFishTest(){
        Card cardExpected = new Card(Rank.KING,Suit.SPADES);

        String expected = "K♠";
        String actual = cardExpected.toString();

        Assert.assertEquals(expected, actual);
    }

//    @Test
//    public void checkNumberOfHandsTest(){
//        Person player1 = new Person("Joe");
//        GoFish goFishTest = new GoFish(player1);
//
//        int expected = 0;
//        int actual = goFishTest.checkNumberOfCard();
//
//        Assert.assertEquals(expected, actual);
//    }

}
