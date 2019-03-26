package io.zipcoder.casino.Games;

import io.zipcoder.casino.Dice.DiceManager;
import io.zipcoder.casino.People.Person;

import java.lang.reflect.Array;
import java.util.*;

public class Craps extends Game implements DiceGameInterface, GamblingInterface {

    private DiceManager crapsDice;
    private int point;
    private int passLineBet;
    private int dontPassLineBet;
    private int comeBet;
    private int dontComeBet;
    private int fieldBet;
    private int passOddsBet;
    private int dontPassOddsBet;
    private HashMap<Integer, Integer> comePoints;
    private HashMap<Integer, Integer> dontComePoints;
    private HashMap<Integer, Integer> comePointOdds;
    private HashMap<Integer, Integer> dontComePointOdds;
    private HashMap<Integer, Integer> placeWinBets;
    private HashMap<Integer, Integer> placeLoseBets;
    private Scanner userInput;
    private Person player;


    public Craps(Person player) {
        this.player = player;
        this.crapsDice = new DiceManager(2);
        this.point= 0;
        this.passLineBet = 0;
        this.dontPassLineBet = 0;
        this.comeBet = 0;
        this.dontComeBet = 0;
        this.fieldBet = 0;
        this.passOddsBet = 0;
        this.dontPassOddsBet=0;
        this.comePoints = new HashMap<>();
        this.dontComePoints = new HashMap<>();
        this.comePointOdds = new HashMap<>();
        this.dontComePointOdds = new HashMap<>();
        this.placeWinBets = new HashMap<>();
        this.placeLoseBets = new HashMap<>();
        this.userInput = new Scanner(System.in);
    }


    public void start() {
        System.out.println("Welcome to Craps!");
        if (checkChipAmount(player) < 5) {
            bootPlayerFromGame(player);
            end();
        }
        placeInitialBet();
        comeOutRoll();
        end();
    }

    public void comeOutRoll() {
        StringBuilder builder = new StringBuilder();
        rollDice();
        builder.append("Time to make your first roll!\nYou rolled a ")
                .append(getDiceValue());
        if (getDiceValue() == 2 || getDiceValue() == 3) {
            builder.append("You crapped out. Pass line bets loose and Don't Pass bets win.\n")
                    .append(passLineBetLose())
                    .append(dontPassLineBetWin());
            System.out.println(builder.toString());
        } else if (getDiceValue() == 7 || getDiceValue() == 11) {
            builder.append("You rolled a natural! Pass Line bets win and Don't Pass loses.\n")
                    .append(passLineBetWin())
                    .append(dontPassLineBetLose());
            System.out.println(builder.toString());
        } else if (getDiceValue() == 12) {
            builder.append("Pass Line looses and Don't Pass bets are pushed to next round.\n")
                    .append(passLineBetLose());
            System.out.println(builder.toString());
        } else {
            builder.append("The point is now ")
                    .append(getDiceValue());
            System.out.println(builder.toString());
            point = getDiceValue();
            phaseTwoRolls();
        }
    }

    public void placeInitialBet(){
        do {
            placeInitialPassBet();
            if (checkChipAmount(player) < 5 ) {
                System.out.println("You don't have enough money for another bet!");
            } else {
                placeInitialDontPassBet();
            }
            if (passLineBet == 0 && dontPassLineBet == 0) {
                System.out.println("You must place an initial bet!");
            }
        } while (passLineBet == 0 && dontPassLineBet == 0);
    }

    private void placeInitialPassBet() {
        System.out.println("Would you like to place a Pass bet? Yes or no.");
        String userAnswer =  userInput.nextLine().toLowerCase();
        if (userAnswer.equals("yes")) {
            System.out.println("How much would you like to bet on the Pass Line?");
            passLineBet += getBetAmount();
            placeBet(player, passLineBet);
        }
    }

    private void placeInitialDontPassBet() {
        System.out.println("Type yes if you would like to place a bet on the Don't Pass Line. If not press Enter");
        String userAnswer = userInput.nextLine().toLowerCase();
        if (userAnswer.equals("yes")) {
            System.out.println("How much would you like to bet on the Don't Pass Line?");
            dontPassLineBet += getBetAmount();
            placeBet(player, dontPassLineBet);
        }
    }

    private void phaseTwoRolls() {
        System.out.println("Time for phase two! Roll a " + point + " and not a 7!");
        do {
            placeBetPhaseTwoHandler();
            rollDice();
            String result = "You rolled a " + getDiceValue() + "!\n";
            if(getDiceValue() == point){
                result += "You rolled the point! Pass Line wins and Don't Pass loses!";
            } else if(getDiceValue() == 7){
                result += "You rolled a 7. Don't Pass wins and Pass Line loses!";
            }
            System.out.println(result);
            checkBetHandler();
        } while (!(getDiceValue() == point) && !(getDiceValue() == 7));
    }

    public void checkBetHandler() {
        if(passLineBet != 0){
            checkPassLine();
        }
        if(dontPassLineBet != 0){
            checkDontPassLine();
        }
        if(comeBet != 0){
            checkComeBet();
        }
        if(dontComeBet != 0){
            checkDontComeBet();
        }
        if(fieldBet != 0){
            checkFieldBet();
        }
        if (passOddsBet != 0) {
            checkPassLineOdds();
        }
        if(dontPassOddsBet !=0 ){
            checkDontPassLineOdds();
        }
        if(!(comePoints.isEmpty())){
            checkComeBetPoint();
        }
        if(!(dontComePoints.isEmpty())){
            checkDontComeBetPoint();
        }
        if(!(comePointOdds.isEmpty())){
            checkComeBetPointOdds();
        }
        if(!(dontComePointOdds.isEmpty())) {
            checkDontComeBetPointOdds();
        }
        if(!(placeWinBets.isEmpty())){
            checkPlaceWinBet();
        }
        if(!(placeLoseBets.isEmpty())){
            checkPlaceLoseBet();
        }
    }

    public void checkPassLine() {
        if (getDiceValue() == point) {
            System.out.println(passLineBetWin());
        } else if (getDiceValue() == 7) {
            System.out.println(passLineBetLose());
        }
    }

    public String passLineBetWin() {
        addChipsToWallet(passLineBet);
        passLineBet = 0;
        return "Pass line bets pay 1:1. You won " + passLineBet + " chips!\n";
    }

    public String passLineBetLose() {
        passLineBet = 0;
        return "Pass Line lost. You lost " + passLineBet + " chips.\n";
    }

    public void checkDontPassLine() {
        if (getDiceValue() == point) {
            System.out.println(dontPassLineBetLose());
        } else if (getDiceValue() == 7) {
            System.out.println(dontPassLineBetWin());
        }
    }

    public String dontPassLineBetWin() {
        addChipsToWallet(dontPassLineBet);
        dontPassLineBet = 0;
        return "Don't Pass bets pay 1:1. You won " + dontPassLineBet + " chips!\n";
    }

    public String dontPassLineBetLose() {
        dontPassLineBet = 0;
        return "Don't Pass Line lost. You lost " + dontPassLineBet + " chips.\n";
    }

    public void checkComeBet() {
        if ((getDiceValue() == 7 || getDiceValue() == 11)) {
            System.out.println("Come bet wins! Come bet pays 1:1. You won " + comeBet + " chips!");
            addChipsToWallet(comeBet);
        } else if ((getDiceValue() == 2 || getDiceValue() == 3 || getDiceValue() == 12)) {
            System.out.println("Come Bet loses. You lost " + comeBet + " chips.");
        } else {
            System.out.println("Your Come bet is now a point.");
            comePoints.put(getDiceValue(), comeBet);
        }
        comeBet = 0;
    }

    public void checkDontComeBet() {
        if ((getDiceValue() == 7 || getDiceValue() == 11)) {
            System.out.println("Don't Come bet loses. You lost " + dontComeBet + " chips.");
            dontComeBet = 0;
        } else if ((getDiceValue() == 2 || getDiceValue() == 3)) {
            System.out.println("Don't Come bet wins! You won " + dontComeBet + " chips!");
            addChipsToWallet(dontComeBet);
            dontComeBet = 0;
        } else if (!(getDiceValue() ==12)) {
            System.out.println("You Don't Come bet is now a point.");
            dontComePoints.put(getDiceValue(), dontComeBet);
            dontComeBet = 0;
        }
    }

    public void checkComeBetPoint() {
        StringBuilder builder = new StringBuilder();
        if (comePoints.containsKey(getDiceValue())) {
            builder.append("Your Come bet on point ")
                    .append(getDiceValue())
                    .append(" wins! Come Bet points are 1:1. You won ")
                    .append(comePoints.get(getDiceValue())).append(" chips!\n");
            addChipsToWallet(comePoints.get(getDiceValue()));
            comePoints.remove(getDiceValue());
            System.out.println(builder.toString());
        } else if (getDiceValue() == 7) {
            int totalValueOfPoints = 0;
            for (Map.Entry<Integer, Integer> entry: comePoints.entrySet()) {
                totalValueOfPoints += entry.getValue();
            }
            builder.append("Your Come bet points lost! You lost a total of ")
                    .append(totalValueOfPoints)
                    .append(" chips.\n");
            comePoints.clear();
            System.out.println(builder.toString());
        }
    }

    public void checkDontComeBetPoint() {
        StringBuilder builder = new StringBuilder();
        if (dontComePoints.containsKey(getDiceValue())) {
            builder.append("Your Don't Come bet on point ")
                    .append(getDiceValue())
                    .append(" lost.  You lost ")
                    .append(dontComePoints.get(getDiceValue()))
                    .append(" chips!\n");
            dontComePoints.remove(crapsDice.getTotalValue());
            System.out.println(builder.toString());
        } else if (getDiceValue() == 7) {
            int totalValueOfPoints = 0;
            for (Map.Entry<Integer, Integer> entry: dontComePoints.entrySet()) {
                totalValueOfPoints += entry.getValue();
            }
            builder.append("Your Don't Come bet points won! You won a total of ")
                    .append(totalValueOfPoints)
                    .append(" chips.");
            addChipsToWallet(totalValueOfPoints);
            dontComePoints.clear();
            System.out.println(builder.toString());
        }

    }

    private void checkFieldBet() {
        ArrayList<Integer> fieldValues = new ArrayList<>(Arrays.asList(2,3,4,9,10,11,12));
        String answer = "";
        if (fieldValues.contains(getDiceValue())){
            if (getDiceValue() == 2 || getDiceValue() == 12) {
                answer = "Field win is doubled! You wn " + fieldBet * 2 + " chips!";
                addChipsToWallet(fieldBet * 2);
            } else {
                answer = "Field wins! Payout is 1:1. You won " + fieldBet + " chips!";
                addChipsToWallet(fieldBet);
            }
        } else {
            answer = "Field loses. You lost " + fieldBet + " chips.";
        }
        System.out.println(answer);
        fieldBet = 0;
    }

    public void checkPassLineOdds() {
        if (getDiceValue() == point) {
            int chips = passLineOddsWin();
            addChipsToWallet(chips);
        } else if (getDiceValue() ==7) {
            System.out.println("Pass Line odds lose! You lost " + passOddsBet + " chips");
            passOddsBet = 0;
        }
    }

    private int passLineOddsWin() {
        int chips = 0;
        if (point == 4 || point == 10) {
            chips = passOddsBet + passOddsBet * 2;
            System.out.println("Pass Line odds wins! Payout is 2:1. You won " + chips + " chips!");
            passOddsBet = 0;
        } else if (point == 5 || point == 9) {
            chips = passOddsBet +(int) Math.floor(passOddsBet * .5);
            System.out.println("Pass Line odds wins! Payout is 3:2. You won " + chips + " chips!");
            passOddsBet = 0;
        } else if (point == 6 || point == 8) {
            chips = passOddsBet + (int) Math.floor(passOddsBet * .2);
            System.out.println("Pass Line odds wins! Payout is 6:5. You won " + chips + " chips!");
            passOddsBet = 0;
        }
        return chips;
    }

    public void checkDontPassLineOdds() {
        if (getDiceValue() == point) {
            System.out.println("Don't Pass odds lose! You lost " + dontPassOddsBet + " chips.");
            dontPassOddsBet = 0;
        } else if (getDiceValue() ==7) {
            int chips = dontPassLineOddsWin();
            addChipsToWallet(chips);

        }
    }

    private int dontPassLineOddsWin() {
        int chips = 0;
        if (point == 4 || point == 10) {
            chips = dontPassOddsBet + (int)Math.floor(dontPassOddsBet * .5);
            System.out.println("Don't Pass odds wins! Payout is 1:2. You won " + chips + " chips!");
            dontPassOddsBet = 0;
        } else if (point == 5 || point == 9) {
            chips = dontPassOddsBet + (int)Math.floor(dontPassOddsBet * .66);
            System.out.println("Don't Pass odds wins! Payout is 2:3. You won " + chips + " chips!");
            dontPassOddsBet = 0;
        } else if (point == 6 || point == 8) {
            chips = dontPassOddsBet + (int)Math.floor(dontPassOddsBet * .83);
            System.out.println("Don't Pass odds wins! Payout is 5:6. You won " + chips + " chips!");
            dontPassOddsBet = 0;
        }
        return chips;
    }


    public void checkComeBetPointOdds() {
        if (comePointOdds.containsKey(getDiceValue())) {
            comeBetPointOddsWin();
        } else if (getDiceValue() == 7) {
            comeBetPointOddsLose();
        }
    }

    private void comeBetPointOddsWin() {
        int chips = 0;
        if (getDiceValue() == 4 || getDiceValue() == 10) {
            chips = comePointOdds.get(getDiceValue()) + comePointOdds.get(getDiceValue()) * 2;
            System.out.println("Your Come bet Odds on point " + getDiceValue() + " wins! Payout is 2:1. You won " + chips  + " chips!");
            addChipsToWallet(chips);
            comePointOdds.remove(getDiceValue());
        } else if (getDiceValue() == 5 || getDiceValue() == 9) {
            chips = (comePointOdds.get(getDiceValue()) + (int) Math.floor(comePointOdds.get(getDiceValue()) * 1.5));
            System.out.println("Your Come bet Odds on point " + getDiceValue() + " wins! Payout is 3:2. You won " + chips  + " chips!");
            addChipsToWallet(chips);
            comePointOdds.remove(getDiceValue());
        } else if (getDiceValue() == 6 || getDiceValue() == 8) {
            chips = (comePointOdds.get(getDiceValue()) + (int) Math.floor(comePointOdds.get(getDiceValue()) * 1.2));
            System.out.println("Your Come bet Odds on point " + getDiceValue() + " wins! Payout is 6:5. You won " + chips  + " chips!");
            addChipsToWallet(chips);
            comePointOdds.remove(getDiceValue());
        }
    }

    private void comeBetPointOddsLose() {
        int totalValueOfPoints = 0;
        for (Map.Entry<Integer, Integer> entry: comePointOdds.entrySet()) {
            totalValueOfPoints += entry.getValue();
        }
        System.out.println("Your Come bet odds lost! You lost a total of " + totalValueOfPoints + " chips.");
        comePointOdds.clear();
    }

    public void checkDontComeBetPointOdds() {
        if (dontComePointOdds.containsKey(getDiceValue())) {
            dontComePoints.remove(getDiceValue());
            System.out.println("Your Don't Come Odds bet on point " + getDiceValue() + " lost. You lost " + dontComePointOdds.get(getDiceValue())
                    + " chips!");
        } else if (getDiceValue() == 7) {
            dontComeBetPointOddsWin();
            dontComePointOdds.clear();
        }
    }

    private void dontComeBetPointOddsWin() {
        int grandTotal = 0;
        StringBuilder builder = new StringBuilder();
        for (Map.Entry<Integer, Integer> entry: dontComePointOdds.entrySet()) {
            int chips = 0;
            if (entry.getKey() == 4 || entry.getKey() == 10) {
                chips = (entry.getValue() + (int) Math.floor(entry.getValue() * .5));
                addChipsToWallet(chips);
                grandTotal += chips;
                builder.append("Your Don't Come bet Odds on point " + entry.getKey() + " wins! Payout is 1:2. You won " + chips + " chips!\n");
            } else if (entry.getKey() == 5 || entry.getKey() == 9) {
                chips = (entry.getValue() + (int) Math.floor(entry.getValue() * .66));
                addChipsToWallet(chips);
                grandTotal += chips;
                builder.append("Your Don't Come bet Odds on point " + entry.getKey() + " wins! Payout is 2:3. You won " + chips + " chips!\n");
            } else if (entry.getKey() == 6 || entry.getKey() == 8) {
                chips = (entry.getValue() + (int) Math.floor(entry.getValue() * .83));
                addChipsToWallet(chips);
                grandTotal += chips;
                builder.append("Your Don't Come bet Odds on point " + entry.getKey() + " wins! Payout is 5:6. You won " + chips + " chips!\n");
            }
        }
        builder.append("You won a total of " + grandTotal + " chips on Don't Come Point Odds bets!");
        System.out.println(builder.toString());
    }


    private void checkPlaceWinBet() {
        if (placeWinBets.containsKey(getDiceValue())) {
            placeWinBetWin();
        } else if (getDiceValue() == 7) {
            placeWinBetLose();
            placeWinBets.clear();
        }
    }

    private void placeWinBetWin() {
        int diceValue = getDiceValue();
        int chips = 0;
        if (diceValue == 6 || diceValue == 8) {
            chips = (int) Math.floor(placeWinBets.get(diceValue) + placeWinBets.get(diceValue) * 1.16);
            addChipsToWallet(chips);
            placeWinBets.remove(diceValue);
            System.out.println("Your Place Win bet won! Payout is 7:6. You won " + chips +  " chips!");
        } else if (diceValue == 5 || diceValue == 9) {
            chips = (int) Math.floor(placeWinBets.get(diceValue) + placeWinBets.get(diceValue) * 1.4);
            addChipsToWallet(chips);
            placeWinBets.remove(diceValue);
            System.out.println("Your Place Win bet won! Payout is 7:5. You won " + chips +  " chips!");
        } else if (diceValue == 4 || diceValue == 10) {
            chips =(int) Math.floor(placeWinBets.get(diceValue) + placeWinBets.get(diceValue) * 1.8);
            addChipsToWallet(chips);
            placeWinBets.remove(diceValue);
            System.out.println("Your Place Win bet won! Payout is 9:5. You won " + chips + " chips!");
        }
    }

    private void placeWinBetLose() {
        int totalValueOfPoints = 0;
        for (Map.Entry<Integer, Integer> entry: placeWinBets.entrySet()) {
            totalValueOfPoints += entry.getValue();
        }
        System.out.println("Your Place Win bets lost! You lost a total of " + totalValueOfPoints + " chips.");

    }

    private void checkPlaceLoseBet() {
        if (placeLoseBets.containsKey(getDiceValue())) {
            System.out.println("Your Place Lose bet on point " + getDiceValue() + " lost. You lost " +
                    placeLoseBets.get(getDiceValue()) + " chips!");
            placeLoseBets.remove(getDiceValue());
        } else if (getDiceValue() == 7) {
            placeLoseBetWin();
            placeLoseBets.clear();
        }
    }

    private void placeLoseBetWin() {
        int grandTotal = 0;
        StringBuilder builder = new StringBuilder();
        for (Map.Entry<Integer, Integer> entry : placeLoseBets.entrySet()) {
            int chips = 0;
            builder.append("Your Place Lose bet on ");
            if (entry.getKey() == 6 || entry.getKey() == 8) {
                chips = entry.getValue() + (int) Math.floor(entry.getValue() * .8);
                addChipsToWallet(chips);
                grandTotal += chips;
                builder.append(entry.getKey())
                        .append(" won! Payout is 4:5. You won ")
                        .append(chips).append(" chips!\n");
            } else if (entry.getKey() == 5 || entry.getKey() == 9) {
                chips = entry.getValue() + (int) Math.floor(entry.getValue() * .62);
                addChipsToWallet(chips);
                grandTotal += chips;
                builder.append(entry.getKey())
                        .append(" won! Payout is 5:8. You won ")
                        .append(chips)
                        .append(" chips!\n");
            } else if (entry.getKey() == 4 || entry.getKey() == 10) {
                chips = entry.getValue() + (int) Math.floor(entry.getValue() * .45);
                addChipsToWallet(chips);
                grandTotal += chips;
                builder.append(entry.getKey())
                        .append(" won! Payout is 5:11. You won ")
                        .append(chips)
                        .append(" chips!\n");
            }
        }
        builder.append("You won a total of ")
                .append(grandTotal)
                .append(" chips on Place Lose bets!");
        System.out.println(builder.toString());
    }

    private void placeBetPhaseTwoHandler() {
        String userAnswer = "";
        do {
            if (checkChipAmount(player) < 5) {
                System.out.println("You don't have enough money for more bets!");
                break;
            }
            System.out.println("What type of bet would you like to place?" +
                    "\nCome\nDon't Come\nField\nOdds\nPlace Win\nPlace Lose\nCheck Wallet Amount\nRoll Dice");
            userAnswer= userInput.nextLine().toLowerCase();
            switch(userAnswer) {
                case "come":
                    comeBet = placeComeDontComeFieldBets(comeBet);
                    break;
                case "don't come":
                    dontComeBet = placeComeDontComeFieldBets(dontComeBet);
                    break;
                case "field":
                    fieldBet = placeComeDontComeFieldBets(fieldBet);
                    break;
                case "odds":
                    oddsTypeHandler();
                    break;
                case "place win":
                    placeWinLose("win");
                    break;
                case "place lose":
                    placeWinLose("lose");
                    break;
                case "check wallet amount":
                    System.out.println(checkChipAmount(player));
                    break;
                case "roll dice":
                    break;
                default:
                    System.out.println("Input not recognized. Please try again.");
            }
        } while (!(userAnswer.equals("roll dice")));
    }

    private int placeComeDontComeFieldBets(int bet) {
        System.out.println("How much would you like to bet?");
            bet += getBetAmount();
            placeBet(player, bet);
        return bet;
    }

    private void placeWinLose(String choice){
        ArrayList<Integer> winLosePlaceValues= new ArrayList<>(Arrays.asList(4,5,6,8,9,10));
        System.out.println("Which number would you like to be on? 4, 5, 6, 8, 9, or 10" + winLosePlaceValues.toString());
        int userAnswer = getBetInput();
        if (winLosePlaceValues.contains(userAnswer)) {
            System.out.println("How much would you like to bet?");
            int userBet = getBetInput();
            if(choice.equals("win")){
                placeWinBets.put(userAnswer, userBet);
            } else if (choice.equals("lose")){
                placeLoseBets.put(userAnswer,userBet);
            }
        } else {
            System.out.println("Input not recognized.");
        }
    }

    private void oddsTypeHandler() {
        String userAnswer = "";
        do {
            System.out.println("What type of odds would you like to place?\nPass\nDon't Pass\nCome\nDon't Come\nNone");
            userAnswer = userInput.nextLine().toLowerCase();
            switch(userAnswer){
                case "pass":
                    placePassOddsBet();
                    break;
                case "don't pass":
                    placeDontPassOddsBet();
                    break;
                case "come":
                    comePointOdds = placeComeDontComeOddsBet(comePoints, comePointOdds);
                    break;
                case "don't come":
                    dontComePointOdds = placeComeDontComeOddsBet(dontComePoints, dontComePointOdds);
                    break;
                case "none":
                    break;
                default:
                    System.out.println("Input is not recognized. Try again");
            }
        } while (!(userAnswer.equals("none")));
    }

    private void placePassOddsBet() {
        if (passLineBet != 0){
            System.out.println("How much would you like to bet on Pass Line odds?");
            passOddsBet += getBetAmount();
            placeBet(player, passOddsBet);
        } else {
            System.out.println("You don't have a Pass Line bet!");
        }
    }

    private void placeDontPassOddsBet() {
        if (dontPassLineBet != 0) {
            System.out.println("How much would you like to bet on Don't Pass odds?");
            dontPassOddsBet += getBetAmount();
            placeBet(player, dontPassOddsBet);
        } else {
            System.out.println("You don't have a Don't Pass bet!");
        }
    }

    private HashMap<Integer,Integer> placeComeDontComeOddsBet(HashMap<Integer,Integer> betPoints, HashMap<Integer,Integer> betPointOdds) {
        if (!(betPoints.isEmpty())) {
            System.out.println("Which point would you like to put odds on?" + betPoints.keySet().toString());
            int userAnswer = getBetInput();
            if (betPoints.containsKey(userAnswer)) {
                System.out.println("How much would you like to bet?");
                betPointOdds.put(userAnswer, getBetInput());
            } else {
                System.out.println("That option is not available.");
            }
        }
        return betPointOdds;
    }

    private int getBetAmount() {
        int betAmount = 0;
        do {
            betAmount = getBetInput();
            if (betAmount < 5) {
                System.out.println("The minimum bet is " + 5 + ". Try again.");
            }
        } while (betAmount < 5);
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

    public void end() {
        System.out.println("Would you like to keep playing? Yes or no.");
        String userAnswer = "";
        do {
            userAnswer = userInput.nextLine().toLowerCase();
            if (userAnswer.equals("yes")) {
                System.out.println("New round starting!");
            } else if (userAnswer.equals("no")) {
                System.out.println("Thanks for playing!");
                start();
            } else {
                System.out.println("Your answer was not recognized. Please try again.");
            }
        }while (!(userAnswer.equals("yes")) && !(userAnswer.equals("no")));
    }

    public int checkChipAmount(Person personToCheck) {
        return personToCheck.getWallet().checkChipAmount();
    }
    public void rollDice() {
        crapsDice.rollAllDice();
    }

    public void bootPlayerFromGame(Person personToBoot) {
        System.out.println("You don't have enough money!");
    }

    public int getDiceValue(){
        return crapsDice.getTotalValue();
    }

    public void addChipsToWallet(int chips){
        player.getWallet().addChips(chips);
    }

    public void placeBet(Person player, int betAmount) {
        player.getWallet().removeChips(betAmount);
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
        comePoints.put(testInput1,testInput2);
    }

    public void setDontComeBetPoints(int testInput1, int testInput2) {
        dontComePoints.put(testInput1, testInput2);
    }

    public void setComeBetPointOdds(int testInput1, int testInput2) {
        comePointOdds.put(testInput1, testInput2);
    }

    public void setDontComeBetPointOdds(int testInput1, int testInput2) {
        dontComePointOdds.put(testInput1,testInput2);
    }

    public void setPlaceWinBets(int testInput1, int testInput2) {
        placeWinBets.put(testInput1, testInput2);
    }

    public void setPlaceLoseBets(int testInput1, int testInput2) {
        placeLoseBets.put(testInput1, testInput2);
    }
}
