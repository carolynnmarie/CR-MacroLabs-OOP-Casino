package io.zipcoder.casino.games;

import io.zipcoder.casino.cards.Card;
import io.zipcoder.casino.cards.Deck;
import io.zipcoder.casino.people.Person;

import java.util.*;

public class War extends Game implements CardGameInterface {

    private boolean gameIsRunning;
    private Person dealer;
    private Person player;
    private ArrayList<Card> playerStack;
    private ArrayList<Card> dealerStack;
    private Scanner input = new Scanner(System.in);

    public War(Person player) {
        this.player = player;
        this.dealer = new Person("Dealer");
        this.playerStack = new ArrayList<>();
        this.dealerStack = new ArrayList<>();
    }

    public Person getPlayer(){
        return this.player;
    }
    public Person getDealer(){
        return this.dealer;
    }
    public ArrayList<Card> getPlayerStack(){
        return this.playerStack;
    }

    public ArrayList<Card> getDealerStack(){
        return this.dealerStack;
    }

    public void start() {
        gameIsRunning = true;
        System.out.println("Welcome to WAR! Enter anything into the console to play a card");
        System.out.println("Enter 'exit' at any time to end the game");
        Deck dealerDeck = new Deck();
        for (int i = 0; i < dealerDeck.getDeckOfCards().size(); i++) {
            getDealer().receiveCards(dealerDeck.getDeckOfCards().get(i));
        }
        getDealer().shuffleHand();
        dealCards();
        engine();
    }

    public void engine() {
        // String playerInput = input.nextLine();
        String exitStay = input.nextLine();
        if (!("exit").equals(exitStay)) {
            while (gameIsRunning) {
                while (!handOfPersonIsEmpty(getDealer()) && !handOfPersonIsEmpty(getPlayer())) {
                    getPlayerStack().add(getPlayer().drawCardfromHand());
                    dealerStack.add(dealer.drawCardfromHand());
                    System.out.println("You played " + getPlayerStack() + " and the dealer played " + getDealerStack());
                    int playerCard = getPlayerStack().get(getPlayer().cardNumber()-1).toInt();
                    int dealerCard = getDealerStack().get(getDealer().cardNumber()-1).toInt();
                    int winner = (playerCard == dealerCard)? iDeclareWar(): (playerCard > dealerCard)? 1: 2;
                    System.out.println((winner==1) ? playerWins(): dealerWins());
                    if ("exit".equals(exitStay)) {
                        end();
                    }
                    checkIfGameIsOver();
                }
            }
        } else {end();}
    }

    private void checkIfGameIsOver() {
        if (handOfPersonIsEmpty(player) || handOfPersonIsEmpty(dealer)) {
            end();
        }
    }

    private boolean handOfPersonIsEmpty(Person person) {
        return person.getPlayerHand().size() == 0;
    }

    private void announceWinner(int winnerNumber) {
        if (winnerNumber == 1) {
            playerWins();
        } else if (winnerNumber == 2) {
            dealerWins();
        }
    }

    private Card topTableCard(ArrayList<Card> cardsOnTable) {
        return cardsOnTable.get(cardsOnTable.size() - 1);
    }


    public int compareCards(Card p1card, Card p2card) {
        if (p1card.toInt() == p2card.toInt()) {
            iDeclareWar();
        } else if (p1card.getRank().toInt() > p2card.getRank().toInt()) {
            return 1;
        } else {return 2;}
        return 0;
    }

    public String playerWins() {
        System.out.println("You won this round!");
        tableCardsToWinner(playerStack, player);
        tableCardsToWinner(dealerStack, player);
        return somebodyWonMessage();
    }

    public String dealerWins() {
        System.out.println("You lost this round!");
        tableCardsToWinner(playerStack, dealer);
        tableCardsToWinner(dealerStack, dealer);
        return somebodyWonMessage();
    }

    public void tableCardsToWinner(ArrayList<Card> tableDeck, Person person) {
        while (tableDeck.size() != 0) {
            person.receiveCards(tableDeck.remove(0));
        }
    }

    public String somebodyWonMessage() {
        return "You have " + player.cardNumber() + "cards and the dealer has " + dealer.cardNumber() + " cards";
    }

    // Make private after testing / Make public for testing
    public int iDeclareWar() {
        System.out.println("I   D E C L A R E   W A R!");
        int amountPlayerCards = checkNumberOfCards(player);
        int amountDealerCards = checkNumberOfCards(dealer);
        int topPlayerCard = iDeclareWarLogic(getPlayerStack(), getPlayer());
        int topDealerCard = iDeclareWarLogic(getDealerStack(),getDealer());
    }

    // Make private after testing / Make public for testing
    public int iDeclareWarLogic(ArrayList<Card> playedCards, Person person) {
        int topPlayerCard = 0;
        for(int i = 0; i<checkNumberOfCards(person); i++){
            player.drawCardfromHand();
            topPlayerCard = p;
        }
        return topPlayerCard;
    }

    // Make private after testing / Make public for testing
    public int decideOnHowManyTimesToIterateBasedOn(int amountOfCardsAvailable) {
        if(amountOfCardsAvailable <= 4) {
            return amountOfCardsAvailable -1;
        }
        return 4;
    }

    // Make private after testing / Make public for testing
    public void playCardInHandForPerson(ArrayList<Card> playedCards, Person person, int i) {
        playedCards.add(person.drawCardfromHand());
    }


    public void dealCards() {
        for (int i = dealer.getPlayerHand().size()-1; i >= 26; i--) {
            player.getPlayerHand()
                    .add(dealer.getPlayerHand()
                            .remove(i));
        }
    }

    public void end() {
        String winner = "";
        if (player.getPlayerHand().size() > 25) {
            winner += "you!";
        } else {
            winner += "the dealer!";
        }
        System.out.println("And the winner is " + winner);
        player.clearHand();
        dealer.clearHand();
        System.out.println("If you want to play again, enter 'yes', or enter anything else to return to the casino");
        if (input.nextLine().equals("yes")) {
            start();
        }
        gameIsRunning = false;
    }

    public int checkNumberOfCards(Person handToCheck) {
        return handToCheck.getPlayerHand().size();
    }

}
