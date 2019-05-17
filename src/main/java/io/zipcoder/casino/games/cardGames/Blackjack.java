package io.zipcoder.casino.games.cardGames;

import io.zipcoder.casino.Scoreboard;
import io.zipcoder.casino.cards.*;
import io.zipcoder.casino.games.Game;
import io.zipcoder.casino.money.*;
import io.zipcoder.casino.people.Person;

import java.util.*;

public class Blackjack extends Game implements CardGameInterface, GamblingInterface {

    private Person player;
    private Person dealer;
    private ArrayList<Card> playerHand;
    private ArrayList<Card> dealerHand;
    private Wallet playerWallet;
    private int playerChips;
    private int playerBet;
    private Scoreboard scoreboard;


    private Deck deck;

    public Blackjack(Person player) {
        this.player = player;
        this.dealer = new Person("Dealer");
        this.playerHand = new ArrayList<>();
        this.dealerHand = new ArrayList<>();
        this.playerWallet = player.getWallet();
        this.playerChips = player.getWallet().checkChips();
        this.deck = new Deck();
        this.playerBet = 0;
        Person[] people = {this.player, dealer};
        this.scoreboard = new Scoreboard(people);
    }

    public Blackjack(Person player, int playerChips){
        this.player = player;
        this.dealer = new Person("Dealer");
        this.playerHand = new ArrayList<>();
        this.dealerHand = new ArrayList<>();
        this.playerWallet = player.getWallet();
        this.playerChips = playerChips;
        playerWallet.addChips(playerChips);
        this.deck = new Deck();
        this.playerBet = 0;
        Person[] people = {this.player, dealer};
        this.scoreboard = new Scoreboard(people);
    }

    public Person getPlayer() {
        return player;
    }

    public Deck getDeck() {
        return deck;
    }

    public int getPlayerChips(){ return this.playerChips; }

    public ArrayList<Card> getPlayerHand(){ return this.playerHand;}
    public void setPlayerHand(ArrayList<Card> cards){ this.playerHand = cards; }

    public Wallet getPlayerWallet(){ return this.playerWallet; }

    public void setPlayerBet(int playerBet){
        this.playerBet = playerBet;
    }
    public int getPlayerBet(){
        return this.playerBet;
    }

    public ArrayList<Card> getDealerHand() {
        return dealerHand;
    }

    public void setDealerHand(ArrayList<Card> dealerHand) {
        this.dealerHand = dealerHand;
    }

    public void start() {
        boolean keepPlaying = true;
        int personHandSum = 0;
        do{
            if (getPlayerChips() <= 0) {
                System.out.println("You don't have anymore chips to play");
                return;
            }
            setPlayerBet(starterBet());
            setPlayerHand(deck.dealHand(2));
            setDealerHand(deck.dealHand(2));
            System.out.println("+++ PLAY BLACKJACK +++");
            personHandSum = engine();
            int dealerHandSum = rankSum(getDealerHand());
            System.out.println(whoWins(personHandSum, dealerHandSum));
            keepPlaying = doYouWantToContinuePlaying();
        } while (keepPlaying);
    }

    private int engine(){
        String playerDecisionString = "";
        int dealerHandSum = rankSum(getDealerHand());
        int personHandSum = rankSum(getPlayerHand());
        System.out.println(showHands());
        if (personHandSum == 21) {
            System.out.println("\n+++++ BLACKJACK! +++++");
            getPlayerWallet().addChips(2 * getPlayerBet());
        } else if (personHandSum > 21) {
            System.out.println("+++++ BUST! +++++");
        } else {
            do{
                System.out.println("Do you want to \"hit\" or \"stay\"?: ");
                Scanner scan = new Scanner(System.in);
                playerDecisionString = scan.nextLine();
                if (playerDecisionString.equals("hit")) {
                    hit(playerHand);
                    personHandSum = rankSum(getPlayerHand());
                    System.out.println(showHands());
                }
                if(playerDecisionString.equals("stay")){
                    break;
                }
            } while(personHandSum<21);

            if(personHandSum>21) {
                System.out.println("++++ Bust! ++++");
            } else if (personHandSum==21){
                System.out.println("++++ BlackJack! ++++");
            } else if (personHandSum <= 21 && dealerHandSum <= 16) {
                this.hit(dealerHand);
            }
        }
        return personHandSum;
    }

    public int rankSum(ArrayList<Card> hand) {
        int handSum = 0;
        int aceRepeats = rankRepeats(hand, 1);
        for(Card card: hand){
            int x = card.toInt();
            int value = (x == 11)? 10:(x == 12)? 10:(x == 13) ? 10: x;
            handSum +=value;
        }
        if(aceRepeats == 1){
            if(handSum <= 11){
                handSum +=10;
            }
        }
        return handSum;
    }

    private int rankRepeats(ArrayList<Card> hand, int value) {
        int rankReps = 0;
        for (Card card : hand) {
            if (card.toInt() == value) { rankReps++; }
        }
        return rankReps;
    }

    @Override
    public String displayCards(ArrayList<Card> hand) {
        StringBuilder sb = new StringBuilder();
        for (Card card : hand) {
            sb.append(card.toString()).append(" ");
        }
        sb.deleteCharAt(sb.length()-1);
        return sb.toString();
    }

    public ArrayList<Card> hit(ArrayList<Card> hand) {
        Card cardFromDealer = getDeck().drawCard();
        hand.add(cardFromDealer);
        return hand;
    }

    private int starterBet(){
        int betPlaced = 0;
        Scanner scanner = new Scanner(System.in);
        do {
            System.out.println("How many chips do you want to bet?");
            Integer input = scanner.nextInt();
            try {
                betPlaced = input;
                if (betPlaced <= getPlayerWallet().checkChips()) {
                    placeBet(getPlayer(), betPlaced);
                } else {
                    System.out.println("Insufficient funds. You have " + getPlayerChips() +
                            " chip(s). Please enter number of chips you wish to bet.");
                }
            } catch (NumberFormatException ne) {
                System.out.println("Not an integer. Please enter number of chips you wish to bet.");
            }
        } while (betPlaced == 0);
        return betPlaced;
    }

    public String showHands(){
       return "\n" + player.getName() + ": \u270B"+ displayCards(playerHand) + "\u270B, hand = " + rankSum(playerHand) + "\nDealer: "
                + dealerHand.get(1).toString();
    }

    private String whoWins(int personHandSum, int dealerHandSum) {
            int playerScore = 0;
            int dealerScore = 0;
        StringBuilder builder = new StringBuilder("\nFINAL SCORE:\n");
        builder.append(player.getName())
                .append("\u270B "+ displayCards(playerHand))
                .append(" \u270B hand = " + personHandSum)
                .append("\nDealer: " + displayCards(dealerHand))
                .append(" \u270B hand = " + dealerHandSum + "\n\n");
        if(personHandSum>21){
            builder.append("You Busted! Dealer wins!\nYou lost ");
            dealerScore = 1;
        } else if (personHandSum<=21 && dealerHandSum>21){
            builder.append("Dealer Busted! You win ");
            playerScore = 1;
            playerWallet.addChips(2 * getPlayerBet());
        } else if (personHandSum<=21 && dealerHandSum<=21 && personHandSum>dealerHandSum){
            builder.append("You win! You won ");
            playerWallet.addChips(2 * getPlayerBet());
            playerScore = 1;
        } else if (personHandSum<=21 && dealerHandSum<=21 && personHandSum<dealerHandSum){
            builder.append("Dealer Wins! You lost ");
            dealerScore = 1;
        } else if (personHandSum<=21 && dealerHandSum<=21 && personHandSum==dealerHandSum) {
            builder.append("You tied! You get to keep your bet of ");
            playerWallet.addChips(getPlayerBet());
        }
        scoreboard.addScore(dealer, dealerScore);
        scoreboard.addScore(player,playerScore);
        builder.append(getPlayerBet())
                .append(" chip(s)\nYour current chip total is ")
                .append(playerWallet.checkChips() + "\n");
        return builder.toString();
    }

    private boolean doYouWantToContinuePlaying() {
        getDealerHand().clear();
        getDealerHand().clear();
        Scanner scanner = new Scanner(System.in);
        System.out.println(scoreboard.displayRunningGameTally());
        System.out.println("Keep playing? (yes/no) ");
        String test = scanner.nextLine();
        if (test.equals("yes")) {
            return true;
        }
        return false;
    }

    public void placeBet(Person player, int betAmount) {
        player.getWallet().removeChips(betAmount);
    }

    public void end() {
        System.out.println("Thank you for playing");
    }

    public int checkChipAmount(Person personToCheck) { return 0; }
    public int getAnte() { return 0; }
    public boolean bootPlayerFromGame(Person personToBoot) { return false; }
    public int checkPot() { return 0; }
    @Override
    public int checkNumberOfCards() { return 0; }
    @Override
    public ArrayList<Card> dealCards() {
        return null; }

    public static void main (String[] args) {
        Person player = new Person("Luis");
        Blackjack blackjack = new Blackjack(player, 10);
        blackjack.start();
        blackjack.end();
    }



}
