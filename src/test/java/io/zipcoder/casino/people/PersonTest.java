package io.zipcoder.casino.people;

import io.zipcoder.casino.cards.Card;
import io.zipcoder.casino.cards.Rank;
import io.zipcoder.casino.cards.Suit;
import io.zipcoder.casino.people.Person;
import org.junit.Assert;
import org.junit.Test;

import java.util.ArrayList;
import java.util.Arrays;

public class PersonTest {


    @Test
    public void constructorTest() {
        Person testPerson = new Person("Joe");
        String expected = "Joe";
        String actual = testPerson.getName();
        Assert.assertEquals(expected, actual);
    }


    @Test
    public void getWalletTest() {
        Person person = new Person("Joe");
        int expected = 10;
        person.getWallet().addChips(10);
        int actual = person.checkChips();
        Assert.assertEquals(actual, expected);
    }

    @Test
    public void getBookTest(){
        Person person = new Person("Carolynn");
        int expected = 1;
        person.setBook(1);
        int actual = person.getBook();
        Assert.assertEquals(actual, expected);
    }
//
//    @Test
//    public void getPlayerHandTest(){
//        Person person = new Person("Carolynn");
//        ArrayList<Card> hand = new ArrayList<>(Arrays.asList(new Card(Rank.SEVEN,Suit.SPADES),new Card(Rank.SEVEN,Suit.HEARTS)));
//        person.setPlayerHand(hand);
//        ArrayList<Card> e = new ArrayList<>(Arrays.asList(new Card(Rank.SEVEN,Suit.SPADES),new Card(Rank.SEVEN,Suit.HEARTS)));
//        String expected = e.get(0).toString();
//        String actual = person.getPlayerHand().get(0).toString();
//        Assert.assertEquals(expected,actual);
//    }


}
