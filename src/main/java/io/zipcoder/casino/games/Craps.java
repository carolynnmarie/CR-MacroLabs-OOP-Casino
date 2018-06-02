package io.zipcoder.casino.games;

import io.zipcoder.casino.diceAndCoins.DiceManager;
import io.zipcoder.casino.money.GamblingInterface;
import io.zipcoder.casino.people.Person;

import java.util.*;

public class Craps extends Game implements DiceGameInterface, GamblingInterface {

    private DiceManager crapsDice = new DiceManager(2);
    private int point = 0;
    private int passLineBet = 0;
    private int dontPassLineBet = 0;
    private int comeBet = 0;
    private int dontComeBet = 0;
    private int fieldBet = 0;
    private int passOddsBet = 0;
    private int dontPassOddsBet = 0;
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

    public void rollDice() {
        crapsDice.rollAllDice();
    }

    public int getNumberOfDice() {
        return 2;
    }

    public int checkChipAmount(Person personToCheck) {
        return personToCheck.getWallet().checkChips();
    }

    public void placeBet(Person personPlacingBet, int betAmount) {
        personPlacingBet.getWallet().removeChips(betAmount);
    }

    public int getAnte() {
        return 5;
    }

    public int checkPot() {
        return 0;
    }

    public void start() {
        boolean keep = true;
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

    @Override
    public void end() {

    }

    public void comeOutRoll() {
        System.out.println("Time to make your first roll!");
        rollDice();
        System.out.println("You rolled a " + crapsDice.getTotalValue() + "!");
        checkComeOutRoll();
    }

    private void checkComeOutRoll() {
        if (crapsDice.getTotalValue() == 2 || crapsDice.getTotalValue() == 3) {
            System.out.println("You crapped out. Pass line bets loose and Don't Pass bets win.");
            passLineBetLose();
            dontPassLineBetWin();
        } else if (crapsDice.getTotalValue() == 7 || crapsDice.getTotalValue() == 11) {
            System.out.println("You rolled a natural! Pass Line bets win and Don't Pass loses.");
            passLineBetWin();
            dontPassLineBetLose();
        } else if (crapsDice.getTotalValue() == 12) {
            System.out.println("Pass Line looses and Don't Pass bets are pushed to next round.");
            passLineBetLose();
        } else {
            System.out.println("The point is now " + crapsDice.getTotalValue());
            point = crapsDice.getTotalValue();
            phaseTwoRolls();
        }
    }

    private void phaseTwoRolls() {
        System.out.println("Time for phase two! Roll a " + point + " and not a 7!");
        do {
            placeBetPhaseTwoHandler();
            rollDice();
            System.out.println("You rolled a " + crapsDice.getTotalValue() + "!");
            checkPhaseTwoRolls();
        } while (!(crapsDice.getTotalValue() == point) && !(crapsDice.getTotalValue() == 7));
    }

    public void checkPhaseTwoRolls() {
        if (crapsDice.getTotalValue() == point) {
            System.out.println("You rolled the point! Pass Line wins and Don't Pass loses!");
            checkBetHandler();
        } else if (crapsDice.getTotalValue() == 7) {
            System.out.println("You rolled a 7. Don't Pass wins and Pass Line loses!");
            checkBetHandler();
        } else {
            checkBetHandler();
        }
    }


    public void checkBetHandler() {
        passLineChecker();
        dontPassLineChecker();
        comeBetPointChecker();
        comeBetChecker();
        dontComeBetPointChecker();
        dontComeBetChecker();
        checkPassLineOdds();
        checkDontPassLineOdds();
        checkComeBetPointOdds();
        checkDontComeBetPointOdds();
        checkFieldBet();
        placeWinBetChecker();
        placeLoseBetChecker();
    }

    public void passLineChecker() {
        if (crapsDice.getTotalValue() == point && passLineBet != 0) {
            passLineBetWin();
        } else if (crapsDice.getTotalValue() == 7 && passLineBet != 0) {
            passLineBetLose();
        }
    }

    public void passLineBetWin() {
        System.out.println("Pass line bets pay 1:1. You won " + passLineBet + " chips!");
        player.getWallet().addChips(passLineBet);
        passLineBet = 0;

    }

    public void passLineBetLose() {
        System.out.println("Pass Line lost. You lost " + passLineBet + " chips.");
        passLineBet = 0;
    }

    public void dontPassLineChecker() {
        if (crapsDice.getTotalValue() == point && dontPassLineBet != 0) {
            dontPassLineBetLose();
        } else if (crapsDice.getTotalValue() == 7 && dontPassLineBet != 0) {
            dontPassLineBetWin();
        }
    }

    public void dontPassLineBetWin() {
        System.out.println("Don't Pass bets pay 1:1. You won " + dontPassLineBet + " chips!");
        player.getWallet().addChips(dontPassLineBet);
        dontPassLineBet = 0;
    }

    public void dontPassLineBetLose() {
        System.out.println("Don't Pass Line lost. You lost " + dontPassLineBet + " chips.");
        dontPassLineBet = 0;
    }

    public void comeBetChecker() {
        String x = "";
        if ((crapsDice.getTotalValue() == 7 || crapsDice.getTotalValue() == 11) && comeBet != 0) {
            x ="Come bet wins! Come bet pays 1:1. You won " + comeBet + " chips!";
            player.getWallet().addChips(comeBet);
        } else if ((crapsDice.getTotalValue() == 2 || crapsDice.getTotalValue() == 3 || crapsDice.getTotalValue() == 12)
                && comeBet != 0) {
            x = "Come Bet loses. You lost " + comeBet + " chips.";
        } else if (comeBet > 4){
            x = "Your Come bet is now a point.";
            comeBetPoints.put(crapsDice.getTotalValue(), comeBet);
        }
        System.out.println(x);
        comeBet = 0;
    }

    private void comeBetWin() {

    }



    public void dontComeBetChecker() {
        if ((crapsDice.getTotalValue() == 7 || crapsDice.getTotalValue() == 11) && dontComeBet != 0) {
            System.out.println("Don't Come bet loses. You lost " + dontComeBet + " chips.");
            dontComeBet = 0;
        } else if ((crapsDice.getTotalValue() == 2 || crapsDice.getTotalValue() == 3)
                && dontComeBet != 0) {
            dontComeBetWin();
        } else if (dontComeBet > 4 && !(crapsDice.getTotalValue() ==12)) {
            dontCometoDontComePoint();
        }
    }

    private void dontComeBetWin() {
        System.out.println("Don't Come bet wins! You won " + dontComeBet + " chips!");
        player.getWallet().addChips(dontComeBet);
        dontComeBet = 0;
    }

    public void comeBetPointChecker() {
        if (comeBetPoints.containsKey(crapsDice.getTotalValue())) {
            comeBetPointWin();
        } else if (crapsDice.getTotalValue() == 7 && !(comeBetPoints.isEmpty())) {
            comeBetPointLose();
        }
    }

    private void comeBetPointWin() {
        System.out.println("Your Come bet on point " + crapsDice.getTotalValue() + " wins! Come Bet points are 1:1. " +
                " You won " + comeBetPoints.get(crapsDice.getTotalValue()) + " chips!");
        player.getWallet().addChips(comeBetPoints.get(crapsDice.getTotalValue()));
        comeBetPoints.remove(crapsDice.getTotalValue());
    }

    private void comeBetPointLose() {
        int totalValueOfPoints = 0;
        for (Map.Entry<Integer, Integer> entry: comeBetPoints.entrySet()) {
            totalValueOfPoints += entry.getValue();
        }
        System.out.println("Your Come bet points lost! You lost a total of " + totalValueOfPoints + " chips.");
        comeBetPoints.clear();
    }

    public void dontComeBetPointChecker() {
        if (dontComeBetPoints.containsKey(crapsDice.getTotalValue())) {
            dontComeBetPointLose();
        } else if (crapsDice.getTotalValue() == 7 && !(dontComeBetPoints.isEmpty())) {
            dontComeBetPointWin();
        }
    }

    private void dontComeBetPointWin() {
        int totalValueOfPoints = 0;
        for (Map.Entry<Integer, Integer> entry: dontComeBetPoints.entrySet()) {
            totalValueOfPoints += entry.getValue();
        }
        System.out.println("Your Don't Come bet points won! You won a total of " + totalValueOfPoints + " chips.");
        player.getWallet().addChips(totalValueOfPoints);
        dontComeBetPoints.clear();
    }

    private void dontComeBetPointLose() {
        System.out.println("Your Don't Come bet on point " + crapsDice.getTotalValue() + " lost." +
                " You lost " + dontComeBetPoints.get(crapsDice.getTotalValue()) + " chips!");
        dontComeBetPoints.remove(crapsDice.getTotalValue());
    }

    private void comeBetToComePoint() {

    }

    private void dontCometoDontComePoint() {
        System.out.println("You Don't Come bet is now a point.");
        dontComeBetPoints.put(crapsDice.getTotalValue(), dontComeBet);
        dontComeBet = 0;
    }

    private void checkFieldBet() {
        if (fieldValues.contains(crapsDice.getTotalValue()) && fieldBet != 0){
            if (crapsDice.getTotalValue() == 2 || crapsDice.getTotalValue() == 12) {
                fieldDoubleWin();
            } else {
                fieldWin();
            }
        } else if (fieldBet != 0) {
            fieldLose();
        }
    }

    private void fieldDoubleWin() {
        System.out.println("Field win is doubled! You wn " + fieldBet * 2 + " chips!");
        player.getWallet().addChips(fieldBet * 2);
        fieldBet = 0;
    }

    private void fieldWin() {
        System.out.println("Field wins! Payout is 1:1. You won " + fieldBet + " chips!");
        player.getWallet().addChips(fieldBet);
        fieldBet = 0;
    }

    private void fieldLose() {
        System.out.println("Field loses. You lost " + fieldBet + " chips.");
        fieldBet = 0;
    }

    public void checkPassLineOdds() {
        if (passOddsBet != 0 && crapsDice.getTotalValue() == point) {
            passLineOddsWin();
        } else if (passOddsBet != 0 && crapsDice.getTotalValue() ==7) {
            passLineOddsLose();
        }
    }

    private void passLineOddsWin() {
        if (point == 4 || point == 10) {
            System.out.println("Pass Line odds wins! Payout is 2:1. You won " + (passOddsBet + passOddsBet * 2) + " chips!");
            player.getWallet().addChips(passOddsBet = passOddsBet * 2);
            passOddsBet = 0;
        } else if (point == 5 || point == 9) {
            System.out.println("Pass Line odds wins! Payout is 3:2. You won " + (passOddsBet +(int)
                    Math.floor(passOddsBet * .5)) + " chips!");
            player.getWallet().addChips(passOddsBet + (int)Math.floor(passOddsBet * 1.5));
            passOddsBet = 0;
        } else if (point == 6 || point == 8) {
            System.out.println("Pass Line odds wins! Payout is 6:5. You won " + (passOddsBet + (int)
                    Math.floor(passOddsBet * .2)) + " chips!");
            player.getWallet().addChips(passOddsBet + (int)Math.floor(passOddsBet * 1.2));
            passOddsBet = 0;
        }
    }

    private void passLineOddsLose() {
        System.out.println("Pass Line odds lose! You lost " + passOddsBet + " chips");
        passOddsBet = 0;
    }

    public void checkDontPassLineOdds() {
        if (dontPassOddsBet !=0 && crapsDice.getTotalValue() == point) {
            dontPassLineOddsLose();
        } else if (dontPassOddsBet != 0 && crapsDice.getTotalValue() ==7) {
            dontPassLineOddsWin();
        }
    }

    private void dontPassLineOddsWin() {
        if (point == 4 || point == 10) {
            System.out.println("Don't Pass odds wins! Payout is 1:2. You won " + (dontPassOddsBet +
                    (int)Math.floor(dontPassOddsBet * .5)) + " chips!");
            player.getWallet().addChips(dontPassOddsBet + (int)Math.floor(dontPassOddsBet * .5));
            dontPassOddsBet = 0;
        } else if (point == 5 || point == 9) {
            System.out.println("Don't Pass odds wins! Payout is 2:3. You won " + (dontPassOddsBet +
                    (int)Math.floor(dontPassOddsBet * .66)) + " chips!");
            player.getWallet().addChips(dontPassOddsBet + (int)Math.floor(dontPassOddsBet * .66));
            dontPassOddsBet = 0;
        } else if (point == 6 || point == 8) {
            System.out.println("Don't Pass odds wins! Payout is 5:6. You won " +(dontPassOddsBet +
                    (int)Math.floor(dontPassOddsBet * .83)) + " chips!");
            player.getWallet().addChips((dontPassOddsBet + (int)Math.floor(dontPassOddsBet * .83)));
            dontPassOddsBet = 0;
        }
    }

    private void dontPassLineOddsLose() {
        System.out.println("Don't Pass odds lose! You lost " + dontPassOddsBet + " chips.");
        dontPassOddsBet = 0;
    }

    public void checkComeBetPointOdds() {
        if (comeBetPointOdds.containsKey(crapsDice.getTotalValue())) {
            comeBetPointOddsWin();

        } else if (crapsDice.getTotalValue() == 7 && !(comeBetPointOdds.isEmpty())) {
            comeBetPointOddsLose();
        }
    }

    private void comeBetPointOddsWin() {
        int winnings = comeBetPointOdds.get(crapsDice.getTotalValue());
        int diceValue = crapsDice.getTotalValue();
        if (diceValue == 4 || diceValue == 10) {
            System.out.println("Your Come bet Odds on point " + diceValue + " wins! Payout is 2:1." +
                    " You won " + (winnings + (winnings*2)) + " chips!");
            player.getWallet().addChips(winnings + winnings * 2);
            comeBetPointOdds.remove(diceValue);
        } else if (diceValue == 5 || diceValue == 9) {
            System.out.println("Your Come bet Odds on point " + diceValue+ " wins! Payout is 3:2." +
                    " You won " + (winnings + (int) Math.floor(winnings * 1.5))  + " chips!");
            player.getWallet().addChips(winnings + (int) Math.floor(winnings * 1.5));
            comeBetPointOdds.remove(diceValue);
        } else if (diceValue == 6 || diceValue == 8) {
            System.out.println("Your Come bet Odds on point " + diceValue + " wins! Payout is 6:5." +
                    " You won " + (winnings + (int) Math.floor(winnings * 1.2))  + " chips!");
            player.getWallet().addChips(winnings +
                    (int) Math.floor( winnings* 1.2));
            comeBetPointOdds.remove(diceValue);
        }
    }

    private void comeBetPointOddsLose() {
        int totalValueOfPoints = 0;
        for (Map.Entry<Integer, Integer> entry: comeBetPointOdds.entrySet()) {
            totalValueOfPoints += entry.getValue();
        }
        System.out.println("Your Come bet odds lost! You lost a total of " + totalValueOfPoints + " chips.");
        comeBetPointOdds.clear();
    }

    public void checkDontComeBetPointOdds() {
        if (dontComeBetPointOdds.containsKey(crapsDice.getTotalValue())) {
            dontComeBetPointOddsLose();
        } else if (crapsDice.getTotalValue() == 7 && !(dontComeBetPointOdds.isEmpty())) {
            dontComeBetPointOddsWin();
        }
    }

    private void dontComeBetPointOddsLose() {
        System.out.println("Your Don't Come Odds bet on point " + crapsDice.getTotalValue() + " lost." +
                " You lost " + dontComeBetPointOdds.get(crapsDice.getTotalValue()) + " chips!");
        dontComeBetPoints.remove(crapsDice.getTotalValue());
    }

    private void dontComeBetPointOddsWin() {
        int grandTotal = 0;
        for (Map.Entry<Integer, Integer> entry: dontComeBetPointOdds.entrySet()) {
            if (entry.getKey() == 4 || entry.getKey() == 10) {
                System.out.println("Your Don't Come bet Odds on point " + entry.getKey() + " wins! Payout is 1:2." +
                        " You won " + (entry.getValue() + (int) Math.floor(entry.getValue() * .5)) + " chips!");
                player.getWallet().addChips(entry.getValue() + (int) Math.floor(entry.getValue() * .5));
                grandTotal += (entry.getValue() + (int) Math.floor(entry.getValue() * .5));
            } else if (entry.getKey() == 5 || entry.getKey() == 9) {
                System.out.println("Your Don't Come bet Odds on point " + entry.getKey() + " wins! Payout is 2:3." +
                        " You won " + (entry.getValue() + (int) Math.floor(entry.getValue() * .66)) + " chips!");
                player.getWallet().addChips(entry.getValue() + (int) Math.floor(entry.getValue() * .66));
                grandTotal += (entry.getValue() + (int) Math.floor(entry.getValue() * .66));
            } else if (entry.getKey() == 6 || entry.getKey() == 8) {
                System.out.println("Your Don't Come bet Odds on point " + entry.getKey() + " wins! Payout is 5:6." +
                        " You won " + (entry.getValue() + (int) Math.floor(entry.getValue() * .83)) + " chips!");
                player.getWallet().addChips(entry.getValue() + (int) Math.floor(entry.getValue() * .83));
                grandTotal += (entry.getValue() + (int) Math.floor(entry.getValue() * .83));
            }
        }
        dontComeBetPointOdds.clear();
        System.out.println("You won a total of " + grandTotal + " chips on Don't Come Point Odds bets!");
    }


    private void placeWinBetChecker() {
        if (placeWinBets.containsKey(crapsDice.getTotalValue())) {
            placeWinBetWin();
        } else if (crapsDice.getTotalValue() == 7 && !(placeWinBets.isEmpty())) {
            placeWinBetLose();
        }
    }

    private void placeWinBetWin() {
        int totalDiceValue = crapsDice.getTotalValue();
        int winnings = placeWinBets.get(crapsDice.getTotalValue());
        if (totalDiceValue == 6 || totalDiceValue == 8) {
            System.out.println("Your Place Win bet won! Payout is 7:6. You won " + (int) Math.floor(winnings + (winnings * 1.16)) +  " chips!");
            player.getWallet().addChips((int) Math.floor(winnings + (winnings * 1.16)));
            placeWinBets.remove(totalDiceValue);
        } else if (totalDiceValue == 5 || totalDiceValue == 9) {
            System.out.println("Your Place Win bet won! Payout is 7:5. You won " + (int) Math.floor(winnings +(winnings * 1.4)) +  " chips!");
            player.getWallet().addChips((int) Math.floor(winnings + (winnings * 1.4)));
            placeWinBets.remove(totalDiceValue);
        } else if (totalDiceValue == 4 || totalDiceValue == 10) {
            System.out.println("Your Place Win bet won! Payout is 9:5. You won " + ((int) Math.floor(winnings + (winnings * 1.8))) +  " chips!");
            player.getWallet().addChips((int) Math.floor(winnings + (winnings * 1.8)));
            placeWinBets.remove(totalDiceValue);
        }
    }

    private void placeWinBetLose() {
        int totalValueOfPoints = 0;
        for (Map.Entry<Integer, Integer> entry: placeWinBets.entrySet()) {
            totalValueOfPoints += entry.getValue();
        }
        System.out.println("Your Place Win bets lost! You lost a total of " + totalValueOfPoints + " chips.");
        placeWinBets.clear();
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
        for (Map.Entry<Integer, Integer> entry : placeLoseBets.entrySet()) {
            if (entry.getKey() == 6 || entry.getKey() == 8) {
                System.out.println("Your Place Lose bet on " + entry.getKey() + " won! Payout is 4:5. You won " +
                        (entry.getValue() + (int) Math.floor(entry.getValue() * .8)) + " chips!");
                player.getWallet().addChips(entry.getValue() + (int) Math.floor(entry.getValue() * .8));
                grandTotal += (entry.getValue() + (int) Math.floor(entry.getValue() * .8));
            } else if (entry.getKey() == 5 || entry.getKey() == 9) {
                System.out.println("Your Place Lose bet on " + entry.getKey() + " won! Payout is 5:8. You won " +
                        (entry.getValue() + (int) Math.floor(entry.getValue() * .62)) + " chips!");
                player.getWallet().addChips(entry.getValue() + (int) Math.floor(entry.getValue() * .62));
                grandTotal += (entry.getValue() + (int) Math.floor(entry.getValue() * .62));
            } else if (entry.getKey() == 4 || entry.getKey() == 10) {
                System.out.println("Your Place Lose bet on " + entry.getKey() + " won! Payout is 5:11. You won " +
                        (entry.getValue() + (int) Math.floor(entry.getValue() * .45)) + " chips!");
                player.getWallet().addChips(entry.getValue()+ (int) Math.floor(entry.getValue() * .45));
                grandTotal += (entry.getValue()+ (int) Math.floor(entry.getValue() * .45));
            }
        }
        placeLoseBets.clear();
        System.out.println("You won a total of " + grandTotal + " chips on Place Lose bets!");
    }

    private void placeBetPhaseTwoHandler() {
        String userAnswer = "";
        do {
            if (player.getWallet().checkChips() < 5) {
                System.out.println("You don't have enough money for more bets!");
                break;
            }
            System.out.println("What type of bet would you like to place?" +
                    "\nCome" +
                    "\nDon't Come" +
                    "\nField" +
                    "\nOdds" +
                    "\nPlace Win" +
                    "\nPlace Lose" +
                    "\nCheck Wallet Amount" +
                    "\nRoll Dice");
            userAnswer= getBetTypeInput();
            placeBetSelection(userAnswer);
        } while (!(userAnswer.equals("roll dice")));

    }

    public void placeBetSelection(String userAnswer) {
        if (userAnswer.equals("come")) {
            placeComeBet();
        } else if (userAnswer.equals("don't come")) {
            placeDontComeBet();
        } else if (userAnswer.equals("field")) {
            placeFieldBet();
        } else if (userAnswer.equals("odds")) {
            oddsTypeHandler();
        } else if (userAnswer.equals("place win")) {
            placeWinChecker();
        } else if (userAnswer.equals("place lose")) {
            placeLoseChecker();
        } else if (userAnswer.equals("check wallet amount")) {
            System.out.println(player.getWallet().checkChips());
        } else if (userAnswer.equals("roll dice")) {
        } else {
            System.out.println("Input not recognized. Please try again.");
        }
    }

    private void placeComeBet() {
        System.out.println("How much would you like to bet for the Come bet?");
        comeBet += minimumBetChecker();
        placeBet(player, comeBet);
    }

    private void placeDontComeBet() {
        System.out.println("How much would you like to bet for the Don't Come bet?");
        dontComeBet += minimumBetChecker();
        placeBet(player, dontComeBet);
    }

    private void placeFieldBet() {
        System.out.println("How much would you like to bet on the Field?");
        fieldBet += minimumBetChecker();
        placeBet(player, fieldBet);
    }

    private void placeWinChecker() {
        System.out.println("Which number would you like to be on? 4, 5, 6, 8, 9, or 10");
        System.out.println(winLosePlaceValues.toString());
        int userAnswer = getBetInput();
        if (winLosePlaceValues.contains(userAnswer)) {
            System.out.println("How much would you like to bet?");
            int userBet = getBetInput();
            placeWinBets.put(userAnswer, userBet);
        } else {
            System.out.println("Input not recognized.");
        }

    }

    private void placeLoseChecker() {
        System.out.println("Which number would you like to be on? 4, 5, 6, 8, 9, or 10");
        int userAnswer = getBetInput();
        if (winLosePlaceValues.contains(userAnswer)) {
            System.out.println("How much would you like to bet?");
            int userBet = getBetInput();
            placeLoseBets.put(userAnswer, userBet);
        } else {
            System.out.println("Input not recognized.");
        }
    }

    public void firstBetChecker(){
        do {
            System.out.println("Please place initial bet");
            placePassBetChecker();
            if (player.getWallet().checkChips() < 5 ) {
                System.out.println("You don't have enough money for another bet!");
            } else {
                placeDontPassLineBetChecker();
            }
        } while (passLineBet == 0 && dontPassLineBet == 0);
    }

    private void placePassBetChecker() {
        System.out.println("Would you like to place a Pass bet? Type y for yes, press any other key for no.");
        String userAnswer = "";
            userAnswer = getBetTypeInput();
            if (userAnswer.equals("yes")) {
                placePassLineBet();
            }
    }

    private void placePassLineBet() {
        System.out.println("How much would you like to bet on the Pass Line?");
        passLineBet += minimumBetChecker();
        placeBet(player, passLineBet);
    }

    private void placeDontPassLineBetChecker() {
        System.out.println("Would you like to place a Don't Pass bet? Type y for yes, any other key for no.");
        String userAnswer = "";
            userAnswer = getBetTypeInput();
            if (userAnswer.equals("yes")) {
                placeDontPassLineBet();
            }
    }

    private void placeDontPassLineBet() {
        System.out.println("How much would you like to bet on the Don't Pass Line?");
        dontPassLineBet += minimumBetChecker();
        placeBet(player, dontPassLineBet);
    }

    private void oddsTypeHandler() {
        String userAnswer = "";
        do {
            System.out.println("What type of odds would you like to place?\n" +
                    "Pass\nDon't Pass\nCome\nDon't Come\nNone");
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
            System.out.println("You don't have a Pass Line bet!");
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
        String bet = "";
        if (comeBetPoints.isEmpty()) {
            bet = "You have no Come Bet points.";
        } else {
            bet = "Which point would you like to put odds on?\n" +comeBetPoints.keySet().toString();
            comeBetOddsSelector();
        }
        System.out.println(bet);
    }

    private void comeBetOddsSelector() {
        int userAnswer = getBetInput();
        if (comeBetPoints.containsKey(userAnswer)) {
            System.out.println("How much would you like to bet?");
            int userBet = getBetInput();
            comeBetPointOdds.put(userAnswer, userBet);
        } else {
            System.out.println("That option is not available.");
        }
    }

    private void placeDontComeOddsBet() {
        if (dontComeBetPoints.isEmpty()) {
            System.out.println("You have no Don't Come Bet points.");
        } else {
            System.out.println("Which point would you like to put odds on?");
            System.out.println(dontComeBetPoints.keySet().toString());
            dontComeBetOddsSelector();
        }
    }

    private void dontComeBetOddsSelector() {
        int userAnswer = getBetInput();
        if (dontComeBetPoints.containsKey(userAnswer)) {
            System.out.println("How much would you like to bet?");
            int userBet = getBetInput();
            dontComeBetPointOdds.put(userAnswer, userBet);
        } else {
            System.out.println("That option is not available.");
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


    //Testi ng methods
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
}
