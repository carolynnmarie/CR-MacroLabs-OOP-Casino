package io.zipcoder.casino.People;


import io.zipcoder.casino.Money.Wallet;

import java.util.Scanner;

public class Person {
    private String name;
    private Hand hand;
    private Wallet wallet;


    protected Person(){ }

    public Person(String name) {
        this.name = name;
        this.hand = new Hand();
        this.wallet = new Wallet();
    }


    public String getName() {
        return this.name;
    }

    public Hand getHand() {
        return this.hand;
    }

    public Wallet getWallet() {
        return this.wallet;
    }

    public void setWallet(int chips){
        this.wallet = new Wallet();
        wallet.addChips(chips);
    }

    public static Person createNewPlayerFromUserInput() {
        String playerName;
        int chips = 0;
        Scanner in = new Scanner(System.in);
        System.out.println("Welcome to our casino! Please enter your name");
        playerName = in.nextLine();
        System.out.println("How many chips would you like to start with?");
        chips = in.nextInt();
        Person person = new Person(playerName);
        person.setWallet(chips);
        return person;
    }



}
