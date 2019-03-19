package io.zipcoder.casino.Games;

import io.zipcoder.casino.Cards.Card;
import io.zipcoder.casino.Cards.Deck;
import io.zipcoder.casino.People.Hand;
import io.zipcoder.casino.People.Person;

import java.util.*;

public class GoFish extends Game implements GameInterface, CardGameInterface {

    private Person player1;
    private Person dealer;
    private Deck houseDeck;
    private Scanner userInput;
    private int booksTotalPlayer;
    private int booksTotalDealer;

    public GoFish(Person player) {
        this.player1 = player;
        this.dealer = new Person("Dealer");
        this.houseDeck = new Deck();
        this.userInput = new Scanner(System.in);
        this.booksTotalPlayer = 0;
        this.booksTotalDealer = 0;
    }

    public void start() {
        System.out.println("*************************  Welcome to Go Fish!  *************************\n" +
                "\u270B\u270B\u270B\u270B\u270B\u270B\u270B\u270B\u270B\u270B\u270B\u270B" +
                "\u270B\u270B\u270B\u270B\u270B\u270B\u270B\u270B\u270B\u270B\u270B\u270B\u270B" +
                "\u270B\u270B\u270B\u270B\u270B\u270B\u270B\u270B\u270B\u270B\u270B\u270B\u270B\u270B\n" +
                "*************************  Welcome to Go Fish!  *************************\n" +
                "Enter: 1 for Ace, 11 for Jack, 12 for Queen, & 13 for King\n");
        houseDeck.shuffleDeck();
        dealCards();
        userTurn();
    }

    public void dealCards() {
        for (int i = 0; i < 7; i++){
            player1.getHand().toArrayList().add(houseDeck.drawCard());
            dealer.getHand().toArrayList().add(houseDeck.drawCard());
        }
    }

    public void userTurn(){
        boolean hasCard;
        int userChoice = 0;
        do {
            do {
                System.out.println("\n*****************************\nPlayer's turn! Choose a card to request from the dealer\n" +
                        "Enter: 1 for Ace, 11 for Jack, 12 for Queen, & 13 for King");
                checkNumberOfCards(player1.getHand());
                while (!userInput.hasNextInt()) {
                    userInput.next();
                }
                userChoice = userInput.nextInt();
            } while (userChoice < 0 && userChoice >= 13);
            hasCard= doesDealerHaveCard(userChoice, dealer);
            if(checkDeckAndHand()) {
                break;
            }
        } while(hasCard);
        if(checkDeckAndHand()) {
            whoWonTheGame();
            end();
        } else {
            dealerTurn();
        }
    }

    public void dealerTurn() {
        boolean hasCard;
        do {
            Collections.shuffle(dealer.getHand().toArrayList());
            int randomDealerCard = dealer.getHand().toArrayList().get(0).getRankInt();
            System.out.println("\n******************************\nDealer's turn! Dealer has chosen card: " + randomDealerCard);
            hasCard =doesPlayerHaveCard(randomDealerCard, player1);
            Collections.shuffle(dealer.getHand().toArrayList());
            if(checkDeckAndHand()) {
                break;
            }
        } while(hasCard);
        if(checkDeckAndHand()) {
            whoWonTheGame();
            end();
        } else {
            userTurn();
        }
    }

    // Asks Player if they have the card requested
    public boolean doesDealerHaveCard(Integer userInputSave, Person handOfCards){
        String rank = (userInputSave==13)? "K":(userInputSave==1)?"A":(userInputSave==11)?"J":
                (userInputSave==12)?"Q": userInputSave.toString();
        boolean hasCard;
            int counter = 0;
            for(int i = 0; i < handOfCards.getHand().toArrayList().size(); i++){
                if (userInputSave == handOfCards.getHand().toArrayList().get(i).getRank().toInt()){
                    counter++;
                }
            }
            if (counter > 0){
                System.out.println("Dealer has " + counter + ": " + rank + "'s!");
                giveDealerCardsToPlayer(userInputSave, dealer, player1);
                hasCard = true;
            } else {
                System.out.println("Dealer does not have a :" + rank);
                hasCard = goFishPlayer(userInputSave);
            }
        return hasCard;
    }

    public boolean doesPlayerHaveCard(Integer dealersChoice, Person handOfCards){
        String rank = (dealersChoice==13)? "K":(dealersChoice==1)?"A":(dealersChoice==11)?"J":
                (dealersChoice==12)?"Q": dealersChoice.toString();
        int counter = 0;
        boolean fished;
            for (int i = 0; i < handOfCards.getHand().toArrayList().size(); i++) {
                if (dealersChoice == handOfCards.getHand().toArrayList().get(i).getRankInt()) {
                    counter++;
                }
            }
            if (counter > 0) {
                System.out.println("Dealer takes all your " + rank + "'s.");
                givePlayerCardsToDealer(dealersChoice,player1, dealer);
                fished = true;
            } else {
                System.out.println("You do not have any " + rank + "'s.");
                fished = goFishDealer(dealersChoice);
            }
            return fished;
    }


    public int giveDealerCardsToPlayer(int userInputSave, Person giver, Person recipient){
        for(int i = 0; i <giver.getHand().toArrayList().size(); i++){
            if (userInputSave == giver.getHand().toArrayList().get(i).getRank().toInt()){
                recipient.getHand().receiveCard(giver.getHand().toArrayList().get(i));
                giver.getHand().toArrayList().remove(i);
            }
        }
        int books = checkBookCount(userInputSave, player1);
        booksTotalPlayer += books;
        if(books>0){
            System.out.println("!$!$!$! You Scored a Book! (Four of a kind) !$!$!$!\n!!!!Your Book Total: " + booksTotalPlayer);
        }
        return userInputSave;
    }

    public void givePlayerCardsToDealer(int userChoice,Person giver, Person recipient){
        for(int i = giver.getHand().toArrayList().size() - 1; i >= 0; i--){
            if (userChoice == giver.getHand().toArrayList().get(i).getRankInt()){
                recipient.getHand().receiveCard(giver.getHand().toArrayList().get(i));
                giver.getHand().toArrayList().remove(i);
            }
        }
        int books = checkBookCount(userChoice,dealer);
        booksTotalDealer += books;
        if(books>0){
            System.out.println("Dealer Scored a Book! (Four of a kind)\nDealer's Book total: " + booksTotalDealer);
        }
    }

    public boolean goFishPlayer(int desiredRank){
        StringBuilder builder = new StringBuilder("********************\n  Player Go Fish!\n********************\n");
        boolean fishedCard;
        Card card = houseDeck.drawCard();
        player1.getHand().receiveCard(card);
        if(card.getRankInt() == desiredRank){
            builder.append("You fished your wish!\n");
            fishedCard=true;
        } else {
            builder.append("You did not fish your wish\n");
            fishedCard=false;
        }
        builder.append("\u270B You pulled a ")
                .append(card.toString())
                .append(" \u270B\n");
        int books = checkBookCount(card.getRankInt(),player1);
        int rBook = checkBookCount(desiredRank,player1);
        booksTotalPlayer += books + rBook;
        if(books>0){
            builder.append("You Scored a Book! (Four of a kind)\nYour Book Total: ")
                    .append(booksTotalPlayer)
                    .append("\n");
        }
        System.out.println(builder.toString());
        return fishedCard;
    }

    public boolean goFishDealer(int desiredRank){
        boolean fishedCard = false;
        int cardValue=0;
        System.out.println("********************\n  Dealer Go Fish!\n********************");
        if (houseDeck.getDeck().size() == 0 || dealer.getHand().toArrayList().size() == 0) {
            System.out.println("Out of Cards!");
            whoWonTheGame();
        } else {
            Card card = houseDeck.drawCard();
            cardValue= card.getRankInt();
            dealer.getHand().toArrayList().add(card);
            if(card.getRankInt() == desiredRank){
                System.out.println("Dealer fished his wish!");
                fishedCard=true;
            } else {
                System.out.println("Dealer did not fish his wish");
            }
        }
        System.out.println("\uD83C\uDCA0 Total deck of cards count: " + houseDeck.getDeck().size());
        int books = checkBookCount(cardValue, dealer);
        int rBook = checkBookCount(desiredRank,dealer);
        booksTotalDealer += books;
        if(books>0){
            System.out.println("!$!$!$! Dealer Scored a Book! Dealer has all 4 " + desiredRank + "'s\nDealer's Book total: " + booksTotalDealer);
        }
        return fishedCard;
        }

    public int checkBookCount(int userInputSave, Person handOfCards){
        int bookCount = 0;
        int num = 0;
        for(int i = 0; i < handOfCards.getHand().toArrayList().size(); i++) {
            if (userInputSave == handOfCards.getHand().toArrayList().get(i).getRank().toInt()) {
                num++;
            }
        }
        if (num == 4) {
            for (int j = handOfCards.getHand().toArrayList().size() - 1; j >= 0 ; j--) {
                if (userInputSave == handOfCards.getHand().toArrayList().get(j).getRank().toInt()){
                    handOfCards.getHand().toArrayList().remove(handOfCards.getHand().toArrayList().get(j));
                }
            }
            bookCount++;
        }
        return bookCount;
    }

    public int checkNumberOfCards(Hand hand) {
        System.out.println("Your hand: " + "\u270B" + hand.toArrayList() + "\u270B");
        return player1.getHand().toArrayList().size();
    }

    public boolean checkDeckAndHand(){
        if(houseDeck.getDeck().size()==0|| player1.getHand().toArrayList().size()==0 || dealer.getHand().toArrayList().size()==0){
            return true;
        }
        return false;
    }

    public void whoWonTheGame(){
        StringBuilder builder = new StringBuilder("*******************  ");
        if (booksTotalPlayer > booksTotalDealer){
            builder.append("You Won!  *******************\nYou won the game with a total Book Score of ")
                    .append(booksTotalPlayer)
                    .append("!\nDealer lost game with a total Book Score of ")
                    .append(booksTotalDealer);
        } else if (booksTotalPlayer == booksTotalDealer) {
            builder.append("You Tied!  *******************\nYou both had a book score of ")
                    .append(booksTotalPlayer);
        } else {
            builder.append("You Lost!  *******************\nDealer had a Book Score of ")
                    .append(booksTotalDealer).append("!\nYou had a Book Score of ")
                    .append(booksTotalPlayer);

        }
        builder.append("!\n");
        System.out.println(builder.toString());
    }

    public void end() {
        Scanner scanner = new Scanner(System.in);
        booksTotalPlayer = 0;
        booksTotalDealer = 0;
        player1.getHand().toArrayList().clear();
        dealer.getHand().toArrayList().clear();
        System.out.println("Play another round? yes or no...");
        if (scanner.nextLine().equalsIgnoreCase("yes")) {
            start();
        } else {
            System.out.println("Thanks for playing!");
        }
    }

    public static void main(String[] args) {
        Person Joe = new Person("Joe");
        GoFish game = new GoFish(Joe);
        game.start();
    }
}
