package io.zipcoder.casino.people;


import io.zipcoder.casino.money.Wallet;
import io.zipcoder.casino.cards.Card;

import java.util.ArrayList;
import java.util.Collections;
import java.util.Scanner;

public class Person {
    private String name = "";
    private Wallet wallet = null;
    private ArrayList<Card> playerHand;
    private int book;

    protected Person(){ }

    public Person(String name) {
        this.name = name;
        this.wallet = new Wallet();
        this.playerHand = new ArrayList<>();
        this.book = 0;
    }


    public String getName() {
        return this.name;
    }

    public Wallet getWallet() {
        return this.wallet;
    }


    public static Person createNewPlayerFromUserInput() {
        String playerName;
        Scanner in = new Scanner(System.in);
        System.out.println("Welcome to our casino! Please enter your name");
        playerName = in.nextLine();
        return new Person(playerName);
    }




    public ArrayList<Card> getPlayerHand() {
        return playerHand;
    }

    public String displayHand(){
        String hand = "";
        for(Card card: playerHand){
            hand += card.toString() + " ";
        }
        return hand;
    }

    public void receiveCards(ArrayList<Card> cards) {
        playerHand.addAll(cards);
    }


    public Card drawCardfromHand() {
        return playerHand.remove(playerHand.size() - 1);
    }

    public void clearHand() {
        playerHand.clear();
    }

    public void setBook(int book){
        this.book = book;
    }

    public int getBook(){
        return book;
    }

}
