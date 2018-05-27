package io.zipcoder.casino.games;

import io.zipcoder.casino.cards.Card;
import io.zipcoder.casino.cards.Deck;
import io.zipcoder.casino.people.Person;

import java.util.ArrayList;
import java.util.Random;
import java.util.Scanner;

public class GoFish extends Game implements GameInterface, CardGameInterface {

    private Person player;
    private Person dealer;
    private Deck houseDeck;

    private int booksTotalPlayer = 0;
    private int booksTotalDealer = 0;

    private Random random = new Random();
    private int randomDealerCard;

    public GoFish(Person player) {
        this.player = player;
        this.dealer = new Person("Dealer");
        this.houseDeck = new Deck();
    }

    public GoFish() {
    }


    public void start() {
        System.out.println("*************************  Welcome to Go Fish!  *************************");
        System.out.println("\u270B\u270B\u270B\u270B\u270B\u270B\u270B\u270B\u270B\u270B\u270B\u270B" +
                "\u270B\u270B\u270B\u270B\u270B\u270B\u270B\u270B\u270B\u270B\u270B\u270B\u270B" +
                "\u270B\u270B\u270B\u270B\u270B\u270B\u270B\u270B\u270B\u270B\u270B\u270B\u270B\u270B");
        System.out.println("*************************  Welcome to Go Fish!  *************************\n\n" +
                "When choosing card enter 1 for Ace, 11 for Jack, 12 for Queen, 13 for King\n");
        dealCards();
        userTurn();
    }


    public void dealCards(){
        houseDeck = new Deck();
        houseDeck.shuffleDeck();
        for (int i = 0; i < 7; i++) currentHand(player).add(houseDeck.drawCard());
        for (int i = 0; i < 7; i++) currentHand(dealer).add(houseDeck.drawCard());
        houseDeck.shuffleDeck();
    }

    public int checkNumberOfCards(Person person){
        return person.getPlayerHand().size();
    }

    private ArrayList<Card> currentHand(Person person){
        return person.getPlayerHand();
    }

    private String displayCards(Person person){ return person.displayHand(); }

    public void userTurn(){
        int fish = 0;
        do {
            int userChoice = playerInput();
            if (doYouHaveCard(userChoice, dealer)) {
                swapCards(userChoice, dealer, player);
            } else {
                System.out.println("Dealer doesn't have any " + userChoice + ", Go Fish!");
                fish = goFishTurn(userChoice, player);
            }
        } while (fish == 1);
        checkPlayerAfterPlayerTurn(player);
    }

    private int playerInput(){
        Scanner input = new Scanner(System.in);
        int userChoice = 0;
        do{
            System.out.println("****Player's Turn****\n Your current hand: " +
                    displayCards(player) + "\nChoose a card to request from dealer\n");
            while(!input.hasNextInt()) input.next();
            userChoice = input.nextInt();
        } while (userChoice <=0 || userChoice>13);
        return userChoice;
    }

    public void dealerTurn(){
        int fish = 0;
        System.out.println("****Dealer's turn****");
        Deck deck = new Deck();
        int cardValue = 0;
        do{
            cardValue = deck.drawCard().toInt();
            System.out.println("\nDo you have any " + cardValue + "s ?\n");
            if (doYouHaveCard(cardValue, player)) {
                swapCards(cardValue, player, dealer);
            } else {
                fish = goFishTurn(cardValue,dealer);
                doYouHaveCard(cardValue, dealer);
                bookCountCheck(cardValue,dealer);
            }
        } while(fish == 1);
        checkDealerAfterDealerTurn(dealer);
    }

    private boolean doYouHaveCard(int randomCard, Person person) {
        boolean yOrN = false;
        for (int i = 0; i < checkNumberOfCards(person); i++) {
            if (randomCard == currentHand(person).get(i).toInt()) {
                yOrN = true;
                break;
            }
        }
        return yOrN;
    }

    private void swapCards(int userInput, Person giver, Person receiver){
        for(int i = 0; i< checkNumberOfCards(giver); i++){
            if(userInput == currentHand(giver).get(i).toInt()){
                currentHand(receiver).add(currentHand(giver).get(i));
                currentHand(giver).remove(i);
            }
        }
        bookCountCheck(userInput, receiver);
        cardCountCheck(giver);
    }

    private int goFishTurn(int desiredCard, Person pullsCard){
        int wish =0;
        cardCountCheck(pullsCard);
        System.out.println("******************* Go Fish! ********************");
        Card card = houseDeck.drawCard();
        currentHand(pullsCard).add(card);
        System.out.println("Card fished: " + card.toString());
        if(card.toInt()== desiredCard){
            wish = 1;
            System.out.println("You fished your wish! You get to take another turn");
        }
        bookCountCheck(card.toInt(), pullsCard);
        cardCountCheck(pullsCard);
        return wish;
    }



    private void bookCountCheck(int userInputSave, Person playerHand){
        int num = 0;
        for(int i = 0; i < checkNumberOfCards(playerHand); i++) {
            if (userInputSave == currentHand(playerHand).get(i).toInt()) {
                num++;
            }
        }
        if (num == 4) {
            for (int j = checkNumberOfCards(playerHand) - 1; j >= 0 ; j--) {
                if (userInputSave == currentHand(playerHand).get(j).toInt()){
                    currentHand(playerHand).remove(currentHand(playerHand).get(j));
                }
            }
            playerHand.setBook(playerHand.getBook()+1);
            System.out.println("\n!$!$!$!$!$!$! You Scored a Book! (Four of a kind) !$!$!$!$!$!$!\n" +
                    "!!!!Your Book Total: " + playerHand.getBook() + "\n");
        }
    }

    public void cardCountCheck(Person person){
        if (houseDeck.getDeckOfCards().size() == 0 || checkNumberOfCards(person) == 0){
            System.out.println("Out of cards!");
            whoWonTheGame();
        }
    }

    public void checkPlayerAfterPlayerTurn(Person player1){
        if (houseDeck.getDeckOfCards().size() == 0 || checkNumberOfCards(player1) == 0){
            whoWonTheGame();
        } else {
            dealerTurn();
        }
    }

    public void checkDealerAfterDealerTurn(Person dealer1){
        if (houseDeck.getDeckOfCards().size() == 0 || checkNumberOfCards(dealer1) == 0){
            whoWonTheGame();
        } else {
            userTurn();
        }
    }

    public void whoWonTheGame(){
        if (player.getBook() > dealer.getBook()){
            System.out.println("*************************  You Won!  *************************" +
                    "You won the game with a total Book Score of " + player.getBook() + "!\n" +
                    "Dealer lost game with a total Book Score of " + dealer.getBook() + "!\n" +
                    "*************************  You Won!  *************************\n");
        } else if (player.getBook() == dealer.getBook()) {
            System.out.println("*************************  You Tied!  *************************"+
                    "Both Players Tied with a total Book Score of " + player.getBook()+ "!\n"+
                    "*************************  You Tied!  *************************\n");
        } else {
            System.out.println("*************************  You Lost!  *************************"+
                    "           You Lost! Dealer had a Book Score of " + dealer.getBook() + "!\n" +
                    "                 You had a Book Score of " + player.getBook() + "!\n"+
                    "*************************  You Lost!  *************************\n");
        }
        end();
    }

    public void end() {
        player.setBook(0);
        dealer.setBook(0);
        currentHand(player).clear();
        currentHand(dealer).clear();
        Scanner anotherRoundScanner = new Scanner(System.in);
        System.out.println("Play another round? yes or no...");
        if (anotherRoundScanner.nextLine().equalsIgnoreCase("yes")) {
            start();
        } else {
            System.out.println("Thanks for playing!");
        }
    }

    public static void main(String[] args) {
        GoFish game = new GoFish();
        game.start();
    }
}
