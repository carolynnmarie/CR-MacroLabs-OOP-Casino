package io.zipcoder.casino.games;


import io.zipcoder.casino.diceAndCoins.DiceManager;
import io.zipcoder.casino.money.GamblingInterface;
import io.zipcoder.casino.people.Person;

import java.util.*;

public class Craps extends Game implements DiceGameInterface, GamblingInterface {

    private DiceManager crapsDice = new DiceManager(2);
    private int point;
    private int passLineBet;
    private int dontPassLineBet;
    private int comeBet;
    private int dontComeBet;
    private int fieldBet;
    private int passOddsBet;
    private int dontPassOddsBet;
    private ArrayList<Integer> fieldValues;
    private ArrayList<Integer> winLosePlaceValues;
    private HashMap<Integer, Integer> comeBetPoints = new HashMap<>();
    private HashMap<Integer, Integer> dontComeBetPoints = new HashMap<>();
    private HashMap<Integer, Integer> comeBetPointOdds = new HashMap<>();
    private HashMap<Integer, Integer> dontComeBetPointOdds = new HashMap<>();
    private HashMap<Integer, Integer> placeWinBets = new HashMap<>();
    private HashMap<Integer, Integer> placeLoseBets = new HashMap<>();

    private Scanner userInput = new Scanner(System.in);

    private Person player;

    public Craps(){
        this.player = new Person("Player");
        this.fieldValues = new ArrayList<>(Arrays.asList(2,3,4,9,10,11,12));
        this.winLosePlaceValues = new ArrayList<>(Arrays.asList(4,5,6,8,9,10));
    }

    public Craps(Person player) {
        this.player = player;
        this.fieldValues = new ArrayList<>(Arrays.asList(2,3,4,9,10,11,12));
        this.winLosePlaceValues = new ArrayList<>(Arrays.asList(4,5,6,8,9,10));
    }

    public int rollDice() {
        crapsDice.rollAllDice();
        return crapsDice.getTotalValue();
    }

    public int checkChipAmount(Person personToCheck) {
        return personToCheck.getWallet().checkChips();
    }

    public void placeBet(Person personPlacingBet, int betAmount) {
        personPlacingBet.getWallet().removeChips(betAmount);
    }

    public void start() {
        boolean keep;
        System.out.println("Welcome to Craps!");
        do {
            if (player.getWallet().checkChips() < 5) {
                System.out.println("You don't have enough money!");
                break;
            }
            firstBetChecker();
            comeOutRoll();
            keep = quitProgram();
        } while (keep);

    }



    public void comeOutRoll() {
        String x ="Time to make your first roll!\n";
        int value = rollDice();
        System.out.println(x + "You rolled a " + value);
        if (value == 2 || value == 3) {
            System.out.println("You crapped out. Pass line bets loose and Don't Pass bets win." + passLineBetLose() + dontPassLineBetWin());
        } else if (value == 7 || value == 11) {
            System.out.println("You rolled a natural! Pass Line bets win and Don't Pass loses." + passLineBetWin() + dontPassLineBetLose());
        } else if (value == 12) {
            System.out.println("Pass Line looses and Don't Pass bets are pushed to next round." + passLineBetLose());
        } else {
            System.out.println("The point is now " + value);
            point = value;
            phaseTwoRolls(value);
        }
    }

    private void phaseTwoRolls(int point) {
        System.out.println("Time for phase two! Roll a " + point + " and not a 7!");
        do {
            placeBetPhaseTwoHandler();
            int value = rollDice();
            System.out.println("You rolled a " + value + "!");
            if (value == point) {
                System.out.println("You rolled the point! Pass Line wins and Don't Pass loses!");
                checkBetHandler(value);
            } else if (value == 7) {
                System.out.println("You rolled a 7. Don't Pass wins and Pass Line loses!");
                checkBetHandler(value);
            } else {
                checkBetHandler(value);
            }
        } while (!(crapsDice.getTotalValue() == point) && !(crapsDice.getTotalValue() == 7));
    }


    public void checkBetHandler(int value) {
        passLineChecker(value);
        dontPassLineChecker(value);
        comeBetPointChecker(value);
        comeBetChecker(value);
        dontComeBetPointChecker(value);
        dontComeBetChecker(value);
        checkPassLineOdds();
        checkDontPassLineOdds();
        checkComeBetPointOdds();
        checkDontComeBetPointOdds();
        checkFieldBet();
        placeWinBetChecker();
        placeLoseBetChecker();
    }

    public void passLineChecker(int value) {
        String bet = "";
        if (value == point && passLineBet != 0) {
            bet = passLineBetWin();
        } else if (value == 7 && passLineBet != 0) {
            bet = passLineBetLose();
        }
        if(!bet.equals("")){
            System.out.println(bet);
        }
    }

    public String passLineBetWin() {
        String bet = "Pass line bets pay 1:1. You won " + passLineBet + " chips!";
        player.getWallet().addChips(passLineBet);
        passLineBet = 0;
        return bet;

    }

    public String passLineBetLose() {
        String bet = "Pass Line lost. You lost " + passLineBet + " chips.";
        passLineBet = 0;
        return bet;
    }

    public void dontPassLineChecker(int value) {
        String bet = "";
        if (value == point && dontPassLineBet != 0) {
            bet = dontPassLineBetLose();
        } else if (value == 7 && dontPassLineBet != 0) {
            bet = dontPassLineBetWin();
        }
        if(!bet.equals("")){
            System.out.println(bet);
        }
    }

    public String dontPassLineBetWin() {
        String bet = "Don't Pass bets pay 1:1. You won " + dontPassLineBet + " chips!";
        player.getWallet().addChips(dontPassLineBet);
        dontPassLineBet = 0;
        return bet;
    }

    public String dontPassLineBetLose() {
        String bet = "Don't Pass Line lost. You lost " + dontPassLineBet + " chips.";
        dontPassLineBet = 0;
        return bet;
    }

    public void comeBetChecker(int diceValue) {
        String x = "";
        int comeBet = placeComeBet();
        if (comeBet != 0) {
            if ((diceValue == 7 || diceValue == 11)) {
                x = "Come bet wins! You won " + comeBet + " chips!";
                player.getWallet().addChips(comeBet);
            } else if ((diceValue == 2 || diceValue == 3 || diceValue == 12)) {
                x = "Come Bet loses. You lost " + comeBet + " chips.";
            } else {
                x = "Your Come bet is now a point.";
                comeBetPoints.put(diceValue, comeBet);
            }
            System.out.println(x);
            comeBet = 0;
        }
    }


    public void dontComeBetChecker(int diceValue) {
        String bet = "";
        int dontComeBet = placeDontComeBet();
        if ((diceValue == 7 || diceValue == 11) && dontComeBet != 0) {
            bet = "Don't Come bet loses. You lost " + dontComeBet + " chips.";
        } else if ((diceValue == 2 || diceValue == 3)
                && dontComeBet != 0) {
            bet = "Don't Come bet wins! You won " + dontComeBet + " chips!";
            player.addChips(dontComeBet);
        } else if (dontComeBet > 4 && !(diceValue ==12)) {
            bet = "You Don't Come bet is now a point.";
            dontComeBetPoints.put(crapsDice.getTotalValue(), dontComeBet);
        }
        if(!bet.equals("")) {
            System.out.println(bet);
            dontComeBet = 0;
        }
    }


    public void comeBetPointChecker(int dieValue) {
        int totalValueOfPoints = 0;
        String bet = "";
        if (comeBetPoints.containsKey(dieValue)) {
            bet = "Your Come bet on point " + dieValue + " wins! Come Bet points are 1:1.  You won "
                    + comeBetPoints.get(dieValue) + " chips!";
            player.addChips(comeBetPoints.get(dieValue));
            comeBetPoints.remove(dieValue);
        } else if (dieValue == 7 && !(comeBetPoints.isEmpty())) {
            for (Map.Entry<Integer, Integer> entry: comeBetPoints.entrySet()) {
                totalValueOfPoints += entry.getValue();
            }
            bet = "Your Come bet points lost! You lost a total of " + totalValueOfPoints + " chips.";
            comeBetPoints.clear();
        }
        if(!bet.equals("")){
            System.out.println(bet);
        }
    }

    public void dontComeBetPointChecker(int diceValue) {
        String bet = "";
        if (dontComeBetPoints.containsKey(diceValue)) {
            bet = "Your Don't Come bet on point " + diceValue + " lost." +
                    " You lost " + dontComeBetPoints.get(diceValue) + " chips!";
            dontComeBetPoints.remove(diceValue);
        } else if (diceValue == 7 && !(dontComeBetPoints.isEmpty())) {
            int totalValueOfPoints = 0;
            for (Map.Entry<Integer, Integer> entry: dontComeBetPoints.entrySet()) {
                totalValueOfPoints += entry.getValue();
            }
            bet = "Your Don't Come bet points won! You won a total of " + totalValueOfPoints + " chips.";
            player.addChips(totalValueOfPoints);
            dontComeBetPoints.clear();
        }
        if(!bet.equals("")) {
            System.out.println(bet);
        }
    }


    private void checkFieldBet() {
        String bet = "";
        int fieldBet = placeFieldBet();
        if (fieldValues.contains(crapsDice.getTotalValue()) && fieldBet != 0){
            if (crapsDice.getTotalValue() == 2 || crapsDice.getTotalValue() == 12) {
                bet = "Field win is doubled! You wn " + fieldBet * 2 + " chips!";
                player.addChips(fieldBet * 2);
            } else {
                bet = "Field wins! Payout is 1:1. You won " + fieldBet + " chips!";
                player.getWallet().addChips(fieldBet);
            }
        } else if (fieldBet != 0) {
            bet = "Field loses. You lost " + fieldBet + " chips.";
        }
        if(!bet.equals("")){
            fieldBet = 0;
            System.out.println(bet);
        }
    }


    public void checkPassLineOdds() {
        String bet = "";
        if (passOddsBet != 0 && crapsDice.getTotalValue() == point) {
            if (point == 4 || point == 10) {
                bet = "Pass Line odds wins! Payout is 2:1. You won " + (passOddsBet + passOddsBet * 2) + " chips!";
                player.addChips(passOddsBet = passOddsBet * 2);
            } else if (point == 5 || point == 9) {
                bet = "Pass Line odds wins! Payout is 3:2. You won " + (passOddsBet +(int) Math.floor(passOddsBet * .5)) + " chips!";
                player.addChips(passOddsBet + (int)Math.floor(passOddsBet * 1.5));
            } else if (point == 6 || point == 8) {
                bet = "Pass Line odds wins! Payout is 6:5. You won " + (passOddsBet + (int) Math.floor(passOddsBet * .2)) + " chips!";
                player.addChips(passOddsBet + (int)Math.floor(passOddsBet * 1.2));
            }
        } else if (passOddsBet != 0 && crapsDice.getTotalValue() ==7) {
            bet = "Pass Line odds lose! You lost " + passOddsBet + " chips";
            passOddsBet = 0;
        }
        if(!bet.equals("")) {
            passOddsBet = 0;
            System.out.println(bet);
        }
    }


    public void checkDontPassLineOdds() {
        String bet = "";
        if (dontPassOddsBet != 0 && crapsDice.getTotalValue() == point) {
            bet = "Don't Pass odds lose! You lost " + dontPassOddsBet + " chips.";
        } else if (dontPassOddsBet != 0 && crapsDice.getTotalValue() == 7) {
            if (point == 4 || point == 10) {
                bet = "Don't Pass odds wins! Payout is 1:2. You won " + (dontPassOddsBet + (int) Math.floor(dontPassOddsBet * .5)) + " chips!";
                player.getWallet().addChips(dontPassOddsBet + (int) Math.floor(dontPassOddsBet * .5));
            } else if (point == 5 || point == 9) {
                bet = "Don't Pass odds wins! Payout is 2:3. You won " + (dontPassOddsBet + (int) Math.floor(dontPassOddsBet * .66)) + " chips!";
                player.getWallet().addChips(dontPassOddsBet + (int) Math.floor(dontPassOddsBet * .66));
            } else if (point == 6 || point == 8) {
                bet = "Don't Pass odds wins! Payout is 5:6. You won " + (dontPassOddsBet + (int) Math.floor(dontPassOddsBet * .83)) + " chips!";
                player.getWallet().addChips((dontPassOddsBet + (int) Math.floor(dontPassOddsBet * .83)));
            }
        }
        if (!bet.equals("")) {
            dontPassOddsBet = 0;
            System.out.println(bet);
        }
    }

    public void checkComeBetPointOdds() {
        int totalValueOfPoints = 0;
        if (comeBetPointOdds.containsKey(crapsDice.getTotalValue())) {
            comeBetPointOddsWin();
        } else if (crapsDice.getTotalValue() == 7 && !(comeBetPointOdds.isEmpty())) {
            for (Map.Entry<Integer, Integer> entry: comeBetPointOdds.entrySet()) {
                totalValueOfPoints += entry.getValue();
            }
            System.out.println("Your Come bet odds lost! You lost a total of " + totalValueOfPoints + " chips.");
            comeBetPointOdds.clear();
        }
    }

    private void comeBetPointOddsWin() {
        int winnings = comeBetPointOdds.get(crapsDice.getTotalValue());
        int diceValue = crapsDice.getTotalValue();
        String bet = "";
        if (diceValue == 4 || diceValue == 10) {
            bet = "Your Come bet Odds on point " + diceValue + " wins! Payout is 2:1. You won "
                    + (winnings + (winnings*2)) + " chips!";
            player.getWallet().addChips(winnings + winnings * 2);
        } else if (diceValue == 5 || diceValue == 9) {
            bet = "Your Come bet Odds on point " + diceValue+ " wins! Payout is 3:2. You won "
                    + (winnings + (int) Math.floor(winnings * 1.5))  + " chips!";
            player.getWallet().addChips(winnings + (int) Math.floor(winnings * 1.5));
        } else if (diceValue == 6 || diceValue == 8) {
            bet = "Your Come bet Odds on point " + diceValue + " wins! Payout is 6:5. You won "
                    + (winnings + (int) Math.floor(winnings * 1.2))  + " chips!";
            player.getWallet().addChips(winnings + (int) Math.floor( winnings* 1.2));
        }
        if(!bet.equals("")){
            comeBetPointOdds.remove(diceValue);
            System.out.println(bet);
        }
    }


    public void checkDontComeBetPointOdds() {
        String bet = "";
        if (dontComeBetPointOdds.containsKey(crapsDice.getTotalValue())) {
            bet = "Your Don't Come Odds bet on point " + crapsDice.getTotalValue() + " lost." +
                    " You lost " + dontComeBetPointOdds.get(crapsDice.getTotalValue()) + " chips!";
            dontComeBetPoints.remove(crapsDice.getTotalValue());
        } else if (crapsDice.getTotalValue() == 7 && !(dontComeBetPointOdds.isEmpty())) {
            int grandTotal = 0;
            for (Map.Entry<Integer, Integer> entry: dontComeBetPointOdds.entrySet()) {
                Integer key = entry.getKey();
                Integer value = entry.getValue();
                if (key == 4 || key == 10) {
                    grandTotal = value + (int)Math.floor(value * .5);
                    bet = "Your Don't Come bet Odds on point " + key + " wins! Payout is 1:2. You won " + (grandTotal) + " chips!";
                    player.addChips(grandTotal);
                } else if (key == 5 || key == 9) {
                    grandTotal = (value + (int)Math.floor(value * .66));
                    bet = "Your Don't Come bet Odds on point " + key + " wins! Payout is 2:3. You won " + (grandTotal) + " chips!";
                    player.addChips(grandTotal);
                } else if (key == 6 || key == 8) {
                    grandTotal = (value + (int)Math.floor(value * .83));
                    bet = "Your Don't Come bet Odds on point " + key + " wins! Payout is 5:6. You won " + (grandTotal) + " chips!";
                    player.addChips(grandTotal);

                }
            }
            dontComeBetPointOdds.clear();
            bet += "You won a total of " + grandTotal + " chips on Don't Come Point Odds bets!";
        }
        System.out.println(bet);
    }


    private void placeWinBetChecker() {
        int totalValueOfPoints = 0;
        if (placeWinBets.containsKey(crapsDice.getTotalValue())) {
            placeWinBetWin();
        } else if (crapsDice.getTotalValue() == 7 && !(placeWinBets.isEmpty())) {
            for (Map.Entry<Integer, Integer> entry: placeWinBets.entrySet()) {
                totalValueOfPoints += entry.getValue();
            }
            System.out.println("Your Place Win bets lost! You lost a total of " + totalValueOfPoints + " chips.");
            placeWinBets.clear();
        }
    }

    private void placeWinBetWin() {
        int diceValue = crapsDice.getTotalValue();
        int winnings = placeWinBets.get(crapsDice.getTotalValue());
        String bet = "";
        if (diceValue == 6 || diceValue == 8) {
            bet = "Your Place Win bet won! Payout is 7:6. You won " + (int) Math.floor(winnings + (winnings * 1.16)) +  " chips!";
            player.addChips((int) Math.floor(winnings + (winnings * 1.16)));
        } else if (diceValue == 5 || diceValue == 9) {
            bet = "Your Place Win bet won! Payout is 7:5. You won " + (int) Math.floor(winnings +(winnings * 1.4)) +  " chips!";
            player.addChips((int) Math.floor(winnings + (winnings * 1.4)));
        } else if (diceValue == 4 || diceValue == 10) {
            bet = "Your Place Win bet won! Payout is 9:5. You won " + ((int) Math.floor(winnings + (winnings * 1.8))) +  " chips!";
            player.addChips((int) Math.floor(winnings + (winnings * 1.8)));
        }
        if(!bet.equals("")){
            System.out.println(bet);
            placeWinBets.remove(diceValue);
        }
    }

    private void placeLoseBetChecker() {
        if (placeLoseBets.containsKey(crapsDice.getTotalValue())) {
            System.out.println("Your Place Lose bet on point " + crapsDice.getTotalValue() + " lost." +
                    " You lost " + placeLoseBets.get(crapsDice.getTotalValue()) + " chips!");
            placeLoseBets.remove(crapsDice.getTotalValue());
        } else if (crapsDice.getTotalValue() == 7 && !(placeLoseBets.isEmpty())) {
            placeLoseBetWin();
        }
    }

    private void placeLoseBetWin() {
        int grandTotal = 0;
        String bet = "";
        for (Map.Entry<Integer, Integer> entry : placeLoseBets.entrySet()) {
            if (entry.getKey() == 6 || entry.getKey() == 8) {
                grandTotal = entry.getValue() + (int) Math.floor(entry.getValue() * .8);
                bet = "Your Place Lose bet on " + entry.getKey() + " won! Payout is 4:5. You won " + grandTotal + " chips!";
                player.getWallet().addChips(grandTotal);
            } else if (entry.getKey() == 5 || entry.getKey() == 9) {
                grandTotal = entry.getValue() + (int) Math.floor(entry.getValue() * .62);
                bet = "Your Place Lose bet on " + entry.getKey() + " won! Payout is 5:8. You won " + grandTotal + " chips!";
                player.getWallet().addChips(grandTotal);
            } else if (entry.getKey() == 4 || entry.getKey() == 10) {
                grandTotal = entry.getValue()+ (int) Math.floor(entry.getValue() * .45);
                bet = "Your Place Lose bet on " + entry.getKey() + " won! Payout is 5:11. You won " + grandTotal + " chips!";
                player.getWallet().addChips(grandTotal);
            }
        }
        placeLoseBets.clear();
        System.out.println(bet + "You won a total of " + grandTotal + " chips on Place Lose bets!");
    }

    private void placeBetPhaseTwoHandler() {
        boolean correct = false;
        do {
            if (checkChipAmount(player) < 5) {
                System.out.println("You don't have enough money for more bets!");
            } else {
                System.out.println("You have " + checkChipAmount(player) + "chips. What type of bet would you like to place?" +
                        "\nCome" +
                        "\nDon't Come" +
                        "\nField" +
                        "\nOdds" +
                        "\nPlace Win" +
                        "\nPlace Lose");
                String x = getBetTypeInput();
                if (x.equalsIgnoreCase("Come") || x.equalsIgnoreCase("don't come") || x.equalsIgnoreCase("odds")
                        || x.equalsIgnoreCase("field") || x.equalsIgnoreCase("place win")
                        || x.equalsIgnoreCase("place lose")) {
                    placeBetSelection(x);
                    correct = true;
                }
            }
        }while (correct == false);
    }

    public void placeBetSelection(String userAnswer) {
        int bet = 0;
        switch (userAnswer){
            case "come":
                placeComeBet();
                break;
            case "don't come":
                placeDontComeBet();
                break;
            case "field":
                placeFieldBet();
                break;
            case "odds":
                oddsTypeHandler();
                break;
            case "place win":
                placeWinChecker();
                break;
            case "place lose":
                placeLoseChecker();
                break;
        }
    }

    private int placeComeBet() {
        System.out.println("How much would you like to bet for the Come bet?");
        int comeBet = minimumBetChecker();
        placeBet(player, comeBet);
        return comeBet;
    }

    private int placeDontComeBet() {
        System.out.println("How much would you like to bet for the Don't Come bet?");
        int dontComeBet = minimumBetChecker();
        placeBet(player, dontComeBet);
        return dontComeBet;
    }

    private int placeFieldBet() {
        System.out.println("How much would you like to bet on the Field?");
        int fieldBet = minimumBetChecker();
        placeBet(player, fieldBet);
        return fieldBet;
    }

    private void placeWinChecker() {
        System.out.println("Which number would you like to be on? 4, 5, 6, 8, 9, or 10\n" + winLosePlaceValues.toString());
        int userAnswer = getBetInput();
        if (winLosePlaceValues.contains(userAnswer)) {
            System.out.println("How much would you like to bet?");
            int userBet = getBetInput();
            placeWinBets.put(userAnswer, userBet);
        }
    }

    private void placeLoseChecker() {
        System.out.println("Which number would you like to be on? 4, 5, 6, 8, 9, or 10\n");
        int userAnswer = getBetInput();
        if (winLosePlaceValues.contains(userAnswer)) {
            System.out.println("How much would you like to bet?");
            int userBet = getBetInput();
            placeLoseBets.put(userAnswer, userBet);
        }
    }

    public void firstBetChecker(){
        do {
            System.out.println("Please place initial bet\nWould you like to place a Pass bet? Type y for yes, press any other key for no.\n");
            String userAnswer  = getBetTypeInput();
            if (userAnswer.equals("yes")) {
                System.out.println("How much would you like to bet on the Pass Line?");
                passLineBet += minimumBetChecker();
                placeBet(player, passLineBet);
            }
            if (player.getWallet().checkChips() < 5 ) {
                System.out.println("You don't have enough money for another bet");
            } else {
                System.out.println("Would you like to place a Don't Pass bet? Type y for yes, any other key for no.");
                userAnswer =  getBetTypeInput();
                if (userAnswer.equals("yes")) {
                    System.out.println("\nHow much would you like to bet on the Don't Pass Line?");
                    dontPassLineBet += minimumBetChecker();
                    placeBet(player, dontPassLineBet);
                }
            }
        } while (passLineBet == 0 || dontPassLineBet == 0);
    }


    private void oddsTypeHandler() {
        String userAnswer = "";
        do {
            System.out.println("What type of odds would you like to place?\nPass\nDon't Pass\nCome\nDon't Come\nNone");
            userAnswer = getBetTypeInput();
            oddsTypeSelector(userAnswer);
        } while (!(userAnswer.equals("none")));

    }

    public void oddsTypeSelector(String userAnswer) {

        switch(userAnswer) {
            case "pass":
                placePassOddsBet();
                break;
            case "don't pass":
                placeDontPassOddsBet();
                break;
            case "come":
                placeComeOddsBet();
                break;
            case "don't come":
                placeDontComeOddsBet();
                break;
            default:
                System.out.println("Input is not recognized. Try again.");
        }
    }

    private void placePassOddsBet() {

        if (passLineBet != 0){
            System.out.println("How much would you like to bet on Pass Line odds?");
            passOddsBet += minimumBetChecker();
            placeBet(player, passOddsBet);
        } else {
            System.out.println("You don't have a Pass Line bet.");
        }

    }

    private void placeDontPassOddsBet() {
        if (dontPassLineBet != 0) {
            System.out.println("How much would you like to bet on Don't Pass odds?");
            dontPassOddsBet += minimumBetChecker();
            placeBet(player, dontPassOddsBet);
        } else {
            System.out.println("You don't have a Don't Pass bet!");
        }
    }

    private void placeComeOddsBet() {
        if (comeBetPoints.isEmpty()) {
            System.out.println("You have no Come Bet points.");
        } else {
            System.out.println("Which point would you like to put odds on?\n" +comeBetPoints.keySet().toString() + "\n");
            int userAnswer = getBetInput();
            if (comeBetPoints.containsKey(userAnswer)) {
                System.out.println("How much would you like to bet?");
                int userBet = getBetInput();
                comeBetPointOdds.put(userAnswer, userBet);
            } else {
                System.out.println("That option is not available.");
            }
        }

    }

    private void placeDontComeOddsBet() {
        if (dontComeBetPoints.isEmpty()) {
            System.out.println("You have no Don't Come Bet points.");
        } else {
            System.out.println("Which point would you like to put odds on?" + dontComeBetPoints.keySet().toString());
            int userAnswer = getBetInput();
            if (dontComeBetPoints.containsKey(userAnswer)) {
                System.out.println("How much would you like to bet?");;
                int userBet = getBetInput();
                dontComeBetPointOdds.put(userAnswer, userBet);
            } else {
                System.out.println("That option is not available.");
            }
        }
    }

    private int minimumBetChecker() {
        int betAmount = 0;
        do {
            betAmount = getBetInput();
            if (betAmount < getAnte()) {
                System.out.println("The minimum bet is " + getAnte() + ". Try again.");
            }
        } while (betAmount < getAnte());
        return betAmount;
    }

    private int getBetInput() {
        try {
            int betInput = userInput.nextInt();
            userInput.nextLine();
            return betInput;
        } catch (InputMismatchException e) {
            System.out.println("That is not a number. Setting bet to 5.");
            userInput.nextLine();
            return 5;
        }
    }

    private String getBetTypeInput() {
        return userInput.nextLine().toLowerCase();
    }


    private boolean quitProgram() {
        boolean keepPlaying = true;
        System.out.println("Would you like to keep playing? Yes or no.");
        String userAnswer = "";
        do {
            userAnswer = getBetTypeInput();
            if (userAnswer.equals("yes")) {
                System.out.println("New round starting!");
            } else if (userAnswer.equals("no")) {
                System.out.println("Thanks for playing!");
                keepPlaying = false;
            } else {
                System.out.println("Your answer was not recognized. Please try again.");
            }
        }while (!(userAnswer.equals("yes")) && !(userAnswer.equals("no")));
        return keepPlaying;
    }

    public int getAnte() {
        return 5;
    }

    public int checkPot() {
        return 0;
    }

    //Testing methods
    public Person getPlayer() {
        return player;
    }
    public DiceManager getDiceManager() {
        return crapsDice;
    }
    public void setPassLineBet(int testInput) {
        passLineBet = testInput;
    }
    public void setDontPassLineBet(int testInput) {
        dontPassLineBet = testInput;
    }
    public void setPoint(int testInput) {
        point = testInput;
    }
    public void setComeBet(int testInput) {
        comeBet = testInput;
    }
    public void setDontComeBet(int testInput) {
        dontComeBet = testInput;
    }
    public void setFieldBet( int testInput) {
        fieldBet = testInput;
    }
    public void setPassOddsBet(int testInput) {
        passOddsBet = testInput;
    }
    public void setDontPassOddsBet(int testInput) {
        dontPassOddsBet = testInput;
    }
    public void setComeBetPoints(int testInput1, int testInput2){
        comeBetPoints.put(testInput1,testInput2);
    }
    public void setDontComeBetPoints(int testInput1, int testInput2) {
        dontComeBetPoints.put(testInput1, testInput2);
    }
    public void setComeBetPointOdds(int testInput1, int testInput2) {
        comeBetPointOdds.put(testInput1, testInput2);
    }
    public void setDontComeBetPointOdds(int testInput1, int testInput2) {
        dontComeBetPointOdds.put(testInput1,testInput2);
    }
    public void setPlaceWinBets(int testInput1, int testInput2) {
        placeWinBets.put(testInput1, testInput2);
    }
    public void setPlaceLoseBets(int testInput1, int testInput2) {
        placeLoseBets.put(testInput1, testInput2);
    }


    @Override
    public void bootPlayerFromGame(Person personToBoot) { }
    @Override
    public void end() {

    }
}
