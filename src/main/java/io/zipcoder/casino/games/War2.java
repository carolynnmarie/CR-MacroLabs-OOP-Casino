package io.zipcoder.casino.games;

import io.zipcoder.casino.cards.Card;
import io.zipcoder.casino.cards.Deck;
import io.zipcoder.casino.people.Person;

import java.util.ArrayList;
import java.util.Scanner;

public class War2 extends Game implements CardGameInterface {

    private Person player;
    private Person dealer;
    private ArrayList<Card> playerHand;
    private ArrayList<Card> dealerHand;
    private ArrayList<Card> playerTableStack;
    private ArrayList<Card> dealerTableStack;

    public War2(Person player){
        this.player = player;
        this.dealer = new Person("Dealer");
        Deck deck = new Deck();
        deck.shuffleDeck();
        dealerHand = deck.dealHand(deck.getDeckOfCards().size()/2);
        playerHand = deck.getDeckOfCards();
        this.playerTableStack = new ArrayList<>();
        this.dealerTableStack = new ArrayList<>();
    }

    @Override
    public void start() {
        Scanner in = new Scanner(System.in);
        System.out.println("Welcome to War!\n Enter \"exit\" at any time to leave game");
        String answer = "";
        while(!answer.equals("exit")){
            answer = in.nextLine();
            war();
        }
        end();
    }

    public void war(){

    }

    @Override
    public void end() {

    }

    @Override
    public int checkNumberOfCards(Person person) {
        return 0;
    }

    @Override
    public void dealCards() {

    }
}
