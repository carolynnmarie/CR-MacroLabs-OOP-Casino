package io.zipcoder.casino.games;

import io.zipcoder.casino.cards.Card;
import io.zipcoder.casino.cards.Deck;
import io.zipcoder.casino.people.Dealer;
import io.zipcoder.casino.people.Person;

import java.util.*;

public class War extends Game implements GameInterface, CardGameInterface {

    private boolean gameIsRunning;
    private Dealer dealer;
    private Person player;
    private ArrayList<Card> playerPlayedCards;
    private ArrayList<Card> dealerPlayedCards;
    private Scanner input = new Scanner(System.in);

    public War(Person player) {
        this.player = player;
        this.dealer = new Dealer();
        this.playerPlayedCards = new ArrayList<>();
        this.dealerPlayedCards = new ArrayList<>();
    }

    public void start() {
        gameIsRunning = true;
        System.out.println("Welcome to WAR! Enter anything into the console to play a card");
        System.out.println("Enter 'exit' at any time to end the game");
        Deck dealerDeck = new Deck();
        for (int i = 0; i < dealerDeck.getDeckOfCards().size(); i++) {
            dealer.receiveCards(dealerDeck.getDeckOfCards().get(i));
        }
        dealer.shuffleHand();
        dealCards();
        engine();
    }


    public void engine() {
        // String playerInput = input.nextLine();
        if (nextLineIsNotExit()) {
            while (gameIsRunning) {
                while (!handOfPersonIsEmpty(dealer) && !handOfPersonIsEmpty(player)) {
                    playerPlayedCards.add(player.drawCardfromHand());
                    dealerPlayedCards.add(dealer.drawCardfromHand());
                    System.out.println("You played " + playerPlayedCards + " and the dealer played " + dealerPlayedCards);
                    int winner =
                            compareCards(
                                    getLastCardPlayedOnTable(playerPlayedCards),
                                    getLastCardPlayedOnTable(dealerPlayedCards));
                    announceWinner(winner);
                    if (!nextLineIsNotExit()) {
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

    private Card getLastCardPlayedOnTable(ArrayList<Card> cardsOnTable) {
        return cardsOnTable.get(cardsOnTable.size() - 1);
    }

    private boolean nextLineIsNotExit() {
        return !("exit").equals(input.nextLine());
    }

    public int compareCards(Card p1card, Card p2card) {
        if (p1card.getRank().toInt() == p2card.getRank().toInt()) {
            iDeclareWar();
        } else if (p1card.getRank().toInt() > p2card.getRank().toInt()) {
            return 1;
        } else {return 2;}
        return 0;
    }

    public void playerWins() {
        System.out.println("You won this round!");
        giveCardsFromTheTableToTheWinner(playerPlayedCards, player);
        giveCardsFromTheTableToTheWinner(dealerPlayedCards, player);
        somebodyWonMessage();
    }

    public void dealerWins() {
        System.out.println("You lost this round!");
        giveCardsFromTheTableToTheWinner(playerPlayedCards, dealer);
        giveCardsFromTheTableToTheWinner(dealerPlayedCards, dealer);
        somebodyWonMessage();
    }

    public void giveCardsFromTheTableToTheWinner(ArrayList<Card> tableDeck, Person person) {
        while (tableDeck.size() != 0) {
            person.receiveCards(tableDeck.remove(0));
        }
    }

    public void somebodyWonMessage() {
        System.out.println("You have " + player.getPlayerHand().size() +
                " cards and the dealer has " + dealer.getPlayerHand().size() + " cards");
    }

    // Make private after testing / Make public for testing
    public void iDeclareWar() {
        System.out.println("I   D E C L A R E   W A R!");
        int amountPlayerCards = checkNumberOfCards(player);
        int amountDealerCards = checkNumberOfCards(dealer);
        iDeclareWarLogic(playerPlayedCards, player, amountPlayerCards);
        iDeclareWarLogic(dealerPlayedCards, dealer, amountDealerCards);
    }

    // Make private after testing / Make public for testing
    public void iDeclareWarLogic(ArrayList<Card> playedCards, Person person, int amountOfCardsAvailable) {
        for (int i = 0; i < decideOnHowManyTimesToIterateBasedOn(amountOfCardsAvailable); i++) {
            playCardInHandForPerson(playedCards, person, i);
        }
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
