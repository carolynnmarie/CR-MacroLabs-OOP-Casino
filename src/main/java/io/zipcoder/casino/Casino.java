package io.zipcoder.casino;


import io.zipcoder.casino.Games.Game;
import io.zipcoder.casino.People.Person;

import java.util.Scanner;

import static io.zipcoder.casino.People.Person.createNewPlayerFromUserInput;


public class Casino {

    public static Person newPlayer() {
        Person player = createNewPlayerFromUserInput();
        player.getWallet().fillPlayerWalletFromUserInput();
        return player;
    }


    public static void sendPlayerToGame(Person player) {
        MainMenu menu = new MainMenu();
        boolean exitCasino = true;
        do {
            String choice = menu.displayGameChoices();
            Game myGame = MainMenu.chooseGame(choice, player);
            myGame.start();
            exitCasino = exitCasino();
        }while (exitCasino);
    }

    private static boolean exitCasino() {
        Scanner userInput = new Scanner(System.in);
        String userChoice;
        do {
            System.out.println("Would you like to play a different game? Yes or no.");
            userChoice = userInput.nextLine().toLowerCase();
        } while (!(userChoice.equals("yes")) && !(userChoice.equals("no")));
        if (userChoice.equals("yes")) {
            return true;
        } else {
            return false;
        }
    }

}
