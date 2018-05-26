package io.zipcoder.casino.games;

import io.zipcoder.casino.*;

import java.util.ArrayList;
import java.util.Arrays;
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
        houseDeck = new Deck();
        startingDrawDeck(houseDeck);
        userTurn();
    }


    public void startingDrawDeck(Deck houseDeck){
        houseDeck.shuffleDeck();
        for (int i = 0; i < 7; i++) currentHand(player).add(houseDeck.drawCard());
        for (int i = 0; i < 7; i++) currentHand(dealer).add(houseDeck.drawCard());
        houseDeck.shuffleDeck();
    }

    public int numberOfCards(Person person){
        return person.getPlayerHand().size();
    }

    private ArrayList<Card> currentHand(Person person){
        return person.getPlayerHand();
    }

    public void userTurn(){
        int in = 0;
        do {
            int userChoice = playerInput();
            in = userChoice;
            if (doYouHaveCard(userChoice, dealer)) {
                swapCards(userChoice, dealer, player);
                bookCountCheck(userChoice, player);
            } else {
                goFishTurn(player);
            }
        } while (doYouHaveCard(in, player));
        checkAfterPlayerGoFish(player);
    }

    private int playerInput(){
        Scanner input = new Scanner(System.in);
        int userChoice = 0;
        do{
            System.out.println("Player's Turn\n Your current hand: " +
                    currentHand(player) + "\nChoose a card to request from dealer\n");
            while(!input.hasNextInt()) input.next();
            userChoice = input.nextInt();
        } while (userChoice <=0 || userChoice>13);
        return userChoice;
    }

    public void dealerTurn(){
        ArrayList<Integer> cardValue = new ArrayList<>(Arrays.asList(1,2,3,4,5,6,7,8,9,10,11,12,13));

        int randomDealerCard = cardValue.get(random.nextInt(cardValue.size()));

        System.out.println("****        Dealer's turn       ****");
        System.out.println("\nDealer has chosen card: " + randomDealerCard + ", do you have it?");
        do {
            if (doYouHaveCard(randomDealerCard, player)) {
                swapCards(randomDealerCard, player, dealer);
                bookCountCheck(randomDealerCard,dealer);
            }
        }
        checkAfterDealerGoFish();
    }

    private boolean doYouHaveCard(int randomCard, Person person) {
        boolean yOrN = false;
        for (int i = 0; i < numberOfCards(person); i++) {
            if (randomCard == currentHand(person).get(i).toInt()) {
                yOrN = true;
                break;
            }
        }
        return yOrN;
    }

    private void swapCards(int userInput, Person giver, Person receiver){
        for(int i = 0; i< numberOfCards(giver); i++){
            if(userInput == currentHand(giver).get(i).toInt()){
                currentHand(receiver).add(currentHand(giver).get(i));
                currentHand(giver).remove(i);
            }
        }
        cardCountCheck(giver);
    }

    public void goFishTurn(Person recieveCardFromDeck){
        cardCountCheck(recieveCardFromDeck);
        System.out.println("******************* Go Fish! ********************");

        if (houseDeck.getDeckOfCards().size() == 0) {
            System.out.println("Out of Cards!");
            whoWonTheGame();

        } else {
            Card card = houseDeck.drawCard();
            currentHand(recieveCardFromDeck).add(card);
            System.out.println("Card fished: " + card);
            bookCountCheck(card.toInt(), recieveCardFromDeck);
        }
        cardCountCheck(recieveCardFromDeck);
    }



    private void bookCountCheck(int userInputSave, Person playerHand){
        int num = 0;
        for(int i = 0; i < numberOfCards(playerHand); i++) {
            if (userInputSave == currentHand(playerHand).get(i).toInt()) {
                num++;
            }
        }
        if (num == 4) {
            for (int j = numberOfCards(playerHand) - 1; j >= 0 ; j--) {
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
        if (houseDeck.getDeckOfCards().size() == 0 || numberOfCards(person) == 0){
            whoWonTheGame();
        }
    }

//    public void checkPlayerCountBefore(int userInputSave, Person playerHand){
//        bookCountCheck(userInputSave,playerHand);
//        checkPlayerAfterDealerSwap();
//    }
//
//    public void checkPlayerCountAfterGoFish(int userInputSave, Person handOfCards){
//        bookCountCheck(userInputSave,handOfCards);
//        checkAfterPlayerGoFish(handOfCards);
//    }

//    public void checkDealerCount(int randomCardFromDealersHand, Person handOfCards){
//        int num = 0;
//        for(int i = 0; i < handOfCards.getPlayerHand().size(); i++) {
//            if (randomCardFromDealersHand == handOfCards.getPlayerHand().get(i).getRank().toInt()) {
//                num++;
//            }
//        }
//        if (num == 4) {
//            for (int j = numberOfCards(handOfCards) - 1; j >= 0 ; j--) {
//                if (randomCardFromDealersHand == currentHand(handOfCards).get(j).getRank().toInt()){
//                    currentHand(handOfCards).remove(currentHand(handOfCards).get(j));
//                }
//            }
//            handOfCards.setBook(handOfCards.getBook()+1);
//            System.out.println("\n" + handOfCards.getName() + " Scored a Book! (Four of a kind) !\n");
//            System.out.println("!!!!Dealer's Book total: " + handOfCards.getBook() + "\n");
//        }
//    }

//    public void checkDealerCountBefore(int randomCardFromDealersHand, Person handOfCards) {
//        checkDealerCount(randomCardFromDealersHand, handOfCards);
//        checkDealerAfterPlayerTurn();
//    }
//
//    public void checkBookCountDealerAfterGoFish(int randomCardFromDealersHand, Person handOfCards){
//        checkDealerCount(randomCardFromDealersHand, handOfCards);
//        checkAfterDealerGoFish();
//    }

//
//    public void moveFromComputerToPlayer(int userInputSave, Person dealer1, Person player){
//        swapCards(userInputSave, dealer1 ,player);
//        checkPlayerCountBefore(userInputSave, player);
//    }
//
//    public void moveFromPlayerToComputer(int card, Person play, Person deal){
//        swapCards(card,play,deal);
//        checkDealerCountBefore(card, deal);
//    }
//


    public void checkAfterPlayerGoFish(Person player1){
        if (houseDeck.getDeckOfCards().size() == 0 || numberOfCards(player1) == 0){
            whoWonTheGame();
        } else {
            dealerTurn();
        }
    }

    public void checkAfterDealerGoFish(Person dealer1){
        if (houseDeck.getDeckOfCards().size() == 0 || numberOfCards(dealer1) == 0){
            whoWonTheGame();
        } else {
            userTurn();
        }
    }

    public void checkPlayerAfterDealerSwap(){
        if (houseDeck.getDeckOfCards().size() == 0 || numberOfCards(player) == 0){
            whoWonTheGame();
        } else {
            userTurn();
        }
    }

    public void checkDealerAfterPlayerTurn(){
        if (houseDeck.getDeckOfCards().size() == 0 || numberOfCards(dealer) == 0){
            whoWonTheGame();
        } else {
            dealerTurn();
        }
    }

    public void whoWonTheGame(){
        if (player.getBook() > dealer.getBook()){
            System.out.println("*************************  You Won!  *************************");
            System.out.println("You won the game with a total Book Score of " + player.getBook() + "!\n" +
                    "Dealer lost game with a total Book Score of " + dealer.getBook() + "!\n");
            System.out.println("*************************  You Won!  *************************\n");
        } else if (player.getBook() == dealer.getBook()) {
            System.out.println("*************************  You Tied!  *************************");
            System.out.println("Both Players Tied with a total Book Score of " + player.getBook()+ "!\n" );
            System.out.println("*************************  You Tied!  *************************\n");
        } else {
            System.out.println("*************************  You Lost!  *************************");
            System.out.println("           You Lost! Dealer had a Book Score of " + dealer.getBook() + "!\n" +
                    "                 You had a Book Score of " + player.getBook() + "!\n");
            System.out.println("*************************  You Lost!  *************************\n");
        }
        end();
    }

    public void end() {
        Scanner anotherRoundScanner = new Scanner(System.in);
        player.setBook(0);
        dealer.setBook(0);

        currentHand(player).clear();
        currentHand(dealer).clear();

        System.out.println("Play another round? yes or no...");
        if (anotherRoundScanner.nextLine().equalsIgnoreCase("yes")) {
            start();
        } else {
            System.out.println("Thanks for playing!");
        }
    }

    @Override
    public int checkNumberOfCards(Hand hand) {
        return 0;
    }

    public void dealCards() {

    }

//    public static void main(String[] args) {
//        GoFish game = new GoFish();
//        game.start();
//
//    }
}
