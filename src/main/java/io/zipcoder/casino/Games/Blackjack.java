package io.zipcoder.casino.Games;

import io.zipcoder.casino.Cards.Card;
import io.zipcoder.casino.Cards.Deck;
import io.zipcoder.casino.People.Dealer;
import io.zipcoder.casino.People.Hand;
import io.zipcoder.casino.People.Person;

import java.util.ArrayList;
import java.util.Scanner;

public class Blackjack extends Game implements CardGameInterface, GamblingInterface {

    private Person player;
    private Dealer dealer;
    private Deck deck;


    public Blackjack(Person player) {
        this.player = player;
        this.dealer = new Dealer();
        this.deck = new Deck();
    }


    public Person getPlayer() {
        return player;
    }

    public Dealer getDealer() {
        return dealer;
    }

    public Deck getDeck() {
        return deck;
    }

    public int handTotal(Person person) {
        ArrayList<Card> handCards = person.getHand().toArrayList();
        int handValue = 0;
        if (countAceDuplicates(handCards) != 1) {
            for (Card card : handCards) {
                handValue += (card.getRankInt() == 11 || card.getRankInt() == 12 || card.getRankInt() == 13) ? 10: card.getRankInt();
            }
        } else {
            int sumAceAsOne = 0;
            int sumAceAsEleven = 0;
            for (Card card : handCards) {
                int cardValue = card.getRankInt();
                sumAceAsOne += (cardValue == 11 || cardValue == 12 || cardValue == 13)? 10:(cardValue == 1)?1: cardValue;
                sumAceAsEleven+= (cardValue == 11 || cardValue == 12 || cardValue == 13)? 10:(cardValue == 1)?11: cardValue;
            }
            handValue = ( sumAceAsOne <= 21 && sumAceAsEleven <= 21 ) ?
                    ((sumAceAsOne<=sumAceAsEleven)?sumAceAsOne:sumAceAsEleven) : ((sumAceAsOne>=sumAceAsEleven)?sumAceAsOne:sumAceAsEleven);
        }
        return handValue;
    }

    public int countAceDuplicates(ArrayList<Card> cards) {
        int count = 0;
        for (Card card : cards) {
            if (card.getRankInt() == 1) {
                count++;
            }
        }
        return count;
    }

    public String handToString(Person person) {
        ArrayList<Card> handArrayList = person.getHand().toArrayList();
        StringBuilder sb = new StringBuilder();
        for (Card card : handArrayList) {
            sb.append(card.toString() + " ");
        }
        sb.deleteCharAt(sb.length()-1);
        return sb.toString();
    }

    public void hit(Person person) {
        Card cardFromDealer = this.getDeck().drawCard();
        person.getHand().receiveCards(cardFromDealer);
    }

    public int placeBet(){
        String playerInput="";
        Scanner scanner = new Scanner(System.in);
        int betPlaced = 0;
        do {
            System.out.println("How many chips do you want to bet?");
            playerInput = scanner.nextLine();
            try {
                betPlaced = Integer.parseInt(playerInput);
                if (betPlaced <= 0) {
                    System.out.println("Your bet must be a number greater than zero.\n");
                    continue;
                } else if (betPlaced <= checkChipAmount(player)) {
                    placeBet(player, betPlaced);
                    break;
                } else {
                    System.out.println("You only have " + checkChipAmount(player) + " chip(s)\n");
                }
            } catch (NumberFormatException ne) {
                System.out.println("Please try again.\n");
            }
        } while (true);
        return betPlaced;
    }


    @Override
    public int checkNumberOfCards(Hand hand) {
        return hand.toArrayList().size();
    }

    // CardGameInterface
    public void dealCards() {
        hit(player);
        hit(player);
        hit(dealer);
        hit(dealer);
    }

    // GamblingInterface
    public int checkChipAmount(Person personToCheck) {
        return personToCheck.getWallet().checkChipAmount();
    }

    // GamblingInterface
    public void placeBet(Person personPlacingBet, int betAmount) {
        personPlacingBet.getWallet().removeChips(betAmount);
    }

    public void addChips(Person person, int amount){
        person.getWallet().addChips(amount);
    }


    // GameInterface or Game Class
    public void start() {
        deck.shuffleDeck();
        if (checkChipAmount(player) <= 0) {
            System.out.println("You don't have anymore chips to play");
            return;
        }
        System.out.println("\n+++ PLAY BLACKJACK +++");
        int betPlaced = placeBet();
        dealCards();
        int playerTotal = 0;
        int dealerTotal = 0;
        do {
            playerTotal = handTotal(player);
            dealerTotal = handTotal(dealer);
            String dealerCard = dealer.getHand().toArrayList().get(1).toString();
            if (deck.deckSize() <= 16) {
                deck = new Deck();
                deck.shuffleDeck();
            }
            StringBuilder builder = new StringBuilder("Your cards: \u270B \n" + handToString(player) + "\u270B total = " + playerTotal
                    + "\nDealer: \u270B \uD83C \uDCA0 " + dealerCard + "\u270B");
            if (playerTotal >= 21) {
                break;
            } else {
                builder.append("\nDo you want to \"hit\" or \"stay\"?: ");
                System.out.println(builder.toString());
            }
            Scanner scanner = new Scanner(System.in);
            String playerInput = scanner.nextLine();
            if (playerInput.equals("hit")) {
                hit(player);
                ArrayList<Card> hand = player.getHand().toArrayList();
                System.out.println("Your new card is: " + hand.get(hand.size()-1).toString());
                playerTotal = handTotal(player);
                if(playerTotal>=21){
                    break;
                }
            } else if (playerInput.equals("stay")) {
                break;
            }
        } while (playerTotal < 21);
        if (playerTotal < 21 && dealerTotal <= 16) {
            hit(dealer);
        }
        System.out.println(findWinner(betPlaced));
        keepPlaying();
    }

    public String findWinner(int betPlaced){
        StringBuilder builder = new StringBuilder();
        int dealerTotal = handTotal(dealer);
        int playerTotal = handTotal(player);
        if(playerTotal>21) {
            builder.append("Bust! You lost " + betPlaced + " chips");
        } else if(playerTotal==21) {
            builder.append("\nBLACKJACK!\n You won " + betPlaced + " chip(s)\n");
            addChips(player,(2 * betPlaced));
        } else if ((dealerTotal <= 21) && (dealerTotal > playerTotal)) {
            builder.append("Dealer wins!\nYou lost " + betPlaced + " chip(s)");
        } else if ((dealerTotal <= 21) && (dealerTotal == playerTotal)) {
            builder.append("It's a tie!\nYou keep your " + betPlaced + " chip(s)");
            addChips(player, betPlaced);
        } else {
            builder.append("You win!\nYou won " + betPlaced + " chip(s)");
            addChips(player,(2 * betPlaced));
        }
        builder.append("\nYour new chip total is:" + checkChipAmount(player) + "\nFINAL SCORE:\nYou: \u270B " + handToString(player) +
                "\u270B total = " + playerTotal + "\nDealer: \u270B " + handToString(dealer) + "\u270B total = " + dealerTotal);
        return builder.toString();
    }

    public void keepPlaying() {
        Scanner scanner = new Scanner(System.in);
        String input = "";
        do {
            System.out.println("Keep playing? (yes/no) ");
            input = scanner.nextLine();
            if (input.equals("yes")) {
                player.getHand().clearHand();
                dealer.getHand().clearHand();
                start();
                break;
            } else if (input.equals("no")) {
                end();
                break;
            }
        } while(true);
    }

    // Game Class
    public void end() {
        System.out.print("Thank you for playing\n");
    }

    // GamblingInterface
    public void bootPlayerFromGame(Person personToBoot) {
    }


    public static void main (String[] args) {
        Person jack = new Person("Jack");
        jack.setWallet(20);
        Blackjack blackjack = new Blackjack(jack);
        blackjack.start();
    }
}
