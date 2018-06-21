package io.zipcoder.casino.people;


import io.zipcoder.casino.money.Wallet;
import io.zipcoder.casino.cards.Card;

import java.util.ArrayList;
import java.util.Collections;
import java.util.Scanner;

public class Person {
    private String name = "";
    private Wallet wallet = null;
    private ArrayList<Card> hand;
    private int book;

    public Person(){ }

    public Person(String name) {
        this.name = name;
        this.wallet = new Wallet();
        this.hand = new ArrayList<>();
        this.book = 0;
    }


    public String getName() {
        return this.name;
    }

    public Wallet getWallet() {
        return this.wallet;
    }

    public int checkChips(){return this.wallet.checkChips();}

    public void addChips(int chips){
        this.wallet.addChips(chips);
    }

    public static Person createNewPlayerFromUserInput() {
        String playerName;
        Scanner in = new Scanner(System.in);
        System.out.println("Welcome to our casino! Please enter your name");
        playerName = in.nextLine();
        return new Person(playerName);
    }

    public void setHand(ArrayList<Card> hand){
        this.hand = hand;
    }
    public ArrayList<Card> getHand() {
        return hand;
    }

    public void setBook(int book){
        this.book = book;
    }

    public int getBook(){
        return book;
    }

}
