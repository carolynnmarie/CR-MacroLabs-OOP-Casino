package io.zipcoder.casino.Games;

import io.zipcoder.casino.Dice.Coin;
import io.zipcoder.casino.People.Person;

import java.util.Scanner;

public class CoinFlipper extends Game {

    private boolean gameIsRunning;
    private Person player;
    private Scanner input;
    private Coin coin;

    public CoinFlipper(Person player) {
        this.player = player;
        this.input = new Scanner(System.in);
        this.coin = new Coin();
        this.gameIsRunning = true;
    }

    public void start() {
        System.out.println("Welcome to Coin Flipper!\nIn this simple game you can bet chips on a lucky coin.\n"
                + "If it lands on heads, you get double your money!  But if it lands on tails, you lose your bet!\n" +
                "" +
                "If you would like to check how many chips you currently have type \'chips\'. Otherwise, just press enter.");
        if(input.nextLine().equals("chips")){
            System.out.println("Your current balance is: " + checkChipAmount(player));
        }
        engine();
    }

    public void engine() {
        while (gameIsRunning) {
            int thisRoundsBet = makeBet();
            if (gameIsRunning) {
                String coinFace = coin.flip();
                if (coinFace.equals("Heads")) {
                    int payout = thisRoundsBet * 3;
                    player.getWallet().addChips(payout);
                    System.out.println("Heads!  You win!  Your payout: " + payout + ".  Your current balance is " + checkChipAmount(player));
                } else {
                    System.out.println("Tails!  You lose! Your current balance is " + checkChipAmount(player));
                }
                gameIsRunning = playAgain();
            }
        }
        end();
    }

    public int makeBet(){
        if (checkChipAmount(player) == 0) {
            bootPlayerFromGame();
        }
        System.out.println("Please enter the number of chips you would like to bet:");
        int thisRoundsBet = 0;
        do{
            if (input.hasNextInt()) {
                thisRoundsBet = input.nextInt();
                placeBet(player, thisRoundsBet);
            } else {
                System.out.println("Invalid input. Please try again.");
            }
        }while(thisRoundsBet == 0);
        return thisRoundsBet;
    }

    public boolean playAgain(){
        System.out.println("Would you like to play again? yes/no");
        input.nextLine();
        String in;
        Boolean stay = true;
        do{
            in = input.nextLine();
            if(in.equalsIgnoreCase("yes")){
                stay = true;
            } else if (in.equalsIgnoreCase("no")){
                stay = false;
            } else {
                System.out.println("Invalid input. Type \'yes\' to play again and \'no\' to exit the game.");
            }
        } while(!in.equals("yes")&&!in.equals("no"));
        return stay;
    }

    public int checkChipAmount(Person personToCheck) {
        return personToCheck.getWallet().checkChipAmount();
    }

    public void placeBet(Person personPlacingBet, int betAmount) {
        personPlacingBet.getWallet().removeChips(betAmount);
    }

    public void bootPlayerFromGame() {
        gameIsRunning = false;
        System.out.println("Sorry, you're out of money!  Bye-bye!");
    }

    public void end() {
        System.out.println("Thanks for playing!");
        gameIsRunning = false;
    }


    public static void main(String[] args){
        Person player = new Person("Joe");
        player.setWallet(10);
        CoinFlipper flipper = new CoinFlipper(player);
        flipper.start();

    }

}
