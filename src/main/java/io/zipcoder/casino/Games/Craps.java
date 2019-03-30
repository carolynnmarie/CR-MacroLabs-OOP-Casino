package io.zipcoder.casino.Games;

import io.zipcoder.casino.Dice.DiceManager;
import io.zipcoder.casino.People.Person;

import java.util.*;

public class Craps extends DiceGames implements GamblingInterface {

    private DiceManager crapsDice;
    private int point;
    private int passLineBet;
    private int dontPassLineBet;
    private int comeBet;
    private int dontComeBet;
    private int fieldBet;
    private int passOddsBet;
    private int dontPassOddsBet;
    private HashMap<Integer, Integer> placeWinBets;
    private HashMap<Integer, Integer> placeLoseBets;
    private HashMap<Integer, Integer> comePoints;
    private HashMap<Integer, Integer> dontComePoints;
    private HashMap<Integer, Integer> comePointOdds;
    private HashMap<Integer, Integer> dontComePointOdds;
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
        boolean keepPlaying;
        do{
            System.out.println("Welcome to Craps!");
            if (checkChipAmount(player) < 5) {
                bootPlayerFromGame(player);
                break;
            } else {
                placeInitialBet();
                comeOutRoll();
                keepPlaying = endGame();
            }
        } while(keepPlaying);
        end();
    }

    public void placeInitialBet(){
        if (checkChipAmount(player) < 5 ) {
            System.out.println("You don't have enough money to make a bet!");
        } else {
            System.out.println("Would you like to place a Pass bet? Yes or no.");
            String userAnswer =  userInput.nextLine().toLowerCase();
            do {
                if (userAnswer.equals("yes")) {
                    passLineBet = makeComeFieldPassBets(passLineBet);
                } else {
                    dontPassLineBet = makeComeFieldPassBets(dontPassLineBet);
                }
            } while (passLineBet==0 && dontPassLineBet==0);
        }
    }

    public void comeOutRoll() {
        StringBuilder builder = new StringBuilder("Time to make your first roll!\nYou rolled a ");
        rollDice();
        builder.append(getDiceValue());
        if (getDiceValue() == 2 || getDiceValue() == 3) {
            builder.append(". You crapped out. Pass line bets loose and Don't Pass bets win.\n")
                    .append(passLineBetLose())
                    .append(dontPassLineBetWin());
            System.out.println(builder.toString());
        } else if (getDiceValue() == 7 || getDiceValue() == 11) {
            builder.append(". You rolled a natural! Pass Line bets win and Don't Pass loses.\n")
                    .append(passLineBetWin())
                    .append(dontPassLineBetLose());
            System.out.println(builder.toString());
        } else if (getDiceValue() == 12) {
            builder.append(". Pass Line loses and Don't Pass bets are pushed to next round.\n")
                    .append(passLineBetLose());
            System.out.println(builder.toString());
        } else {
            builder.append(". The point is now ")
                    .append(getDiceValue());
            System.out.println(builder.toString());
            point = getDiceValue();
            phaseTwoRolls();
        }
    }


    private void phaseTwoRolls() {
        System.out.println("Time for phase two! Roll a " + point + " and not a 7!");
        do {
            String secondBet;
            do{
                makePhaseTwoBet();
                System.out.println("If you wish to place another bet, press enter.  If you wish to roll the dice, type \'roll dice\'");
                secondBet = userInput.nextLine().toLowerCase();
            } while(!secondBet.equals("roll dice"));
            rollDice();
            System.out.println("You rolled a " + getDiceValue() + "!\n");
            checkBetHandler();
            System.out.println("Would you like to place another bet continue playing? If so type \'yes\', if not type \'no\'");
        } while (userInput.nextLine().toLowerCase().equals("yes") && getDiceValue()!=point && getDiceValue()!=7);
    }

    public void checkBetHandler() {
        checkPassDontPassBet();
        if(comeBet != 0)
            checkComeBet();
        if(dontComeBet != 0)
            checkDontComeBet();
        if(fieldBet != 0)
            checkFieldBet();
        if(passOddsBet != 0)
            checkPassLineOdds();
        if(dontPassOddsBet != 0)
            checkDontPassLineOdds();
        if(!(comePoints.isEmpty()))
            checkComeBetPoint();
        if(!(dontComePoints.isEmpty()))
            checkDontComeBetPoint();
        if(!(comePointOdds.isEmpty()))
            checkComeBetPointOdds();
        if(!(dontComePointOdds.isEmpty()))
            checkDontComeBetPointOdds();
        if(!(placeWinBets.isEmpty()))
            checkPlaceWinBet();
        if(!(placeLoseBets.isEmpty()))
            checkPlaceLoseBet();
    }

    public void checkPassDontPassBet(){
        if(getDiceValue() == point){
            System.out.println("You rolled the point! Pass Line wins and Don't Pass loses!" + passLineBetWin() + dontPassLineBetLose());
        } else if(getDiceValue() == 7){
            System.out.println("You rolled a 7. Don't Pass wins and Pass Line loses!" + passLineBetLose() + dontPassLineBetWin());
        }
    }

    public String passLineBetWin() {
        addChipsToWallet(passLineBet*2);
        String x = "Pass line bets pay 1:1. You won " + passLineBet + " chips!\n";
        passLineBet = 0;
        return x;
    }

    public String passLineBetLose() {
        String x = "Pass Line lost. You lost " + passLineBet + " chips.\n";
        passLineBet = 0;
        return x;
    }

    public String dontPassLineBetWin() {
        addChipsToWallet(dontPassLineBet*2);
        String x = "Don't Pass bets pay 1:1. You won " + dontPassLineBet + " chips!\n";
        dontPassLineBet = 0;
        return x;
    }

    public String dontPassLineBetLose() {
        String x = "Don't Pass Line lost. You lost " + dontPassLineBet + " chips.\n";
        dontPassLineBet = 0;
        return x;
    }

    public void checkComeBet() {
        if ((getDiceValue() == 7 || getDiceValue() == 11)) {
            System.out.println("Come bet wins! Come bet pays 1:1. You won " + comeBet + " chips!");
            addChipsToWallet(comeBet*2);
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
            addChipsToWallet(dontComeBet*2);
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
            builder.append("Your Come bet on point " + getDiceValue() + " wins! Come Bet points are 1:1. ")
                    .append("You won " + comePoints.get(getDiceValue()) + " chips!\n");
            addChipsToWallet((comePoints.get(getDiceValue()))*2);
            comePoints.remove(getDiceValue());
            System.out.println(builder.toString());
        } else if (getDiceValue() == 7) {
            int totalValueOfPoints = 0;
            for (Map.Entry<Integer, Integer> entry: comePoints.entrySet()) {
                totalValueOfPoints += entry.getValue();
            }
            builder.append("Your Come bet points lost! You lost a total of " + totalValueOfPoints + " chips.\n");
            comePoints.clear();
            System.out.println(builder.toString());
        }
    }

    public void checkDontComeBetPoint() {
        StringBuilder builder = new StringBuilder();
        if (dontComePoints.containsKey(getDiceValue())) {
            builder.append("Your Don't Come bet on point " + getDiceValue() + " lost.  ")
                    .append("You lost " + dontComePoints.get(getDiceValue()) + " chips!");
            dontComePoints.remove(crapsDice.getTotalValue());
            System.out.println(builder.toString());
        } else if (getDiceValue() == 7) {
            int totalValueOfPoints = 0;
            for (Map.Entry<Integer, Integer> entry: dontComePoints.entrySet()) {
                totalValueOfPoints += entry.getValue();
            }
            builder.append("Your Don't Come bet points won! ")
                    .append("You won a total of " + totalValueOfPoints + " chips.");
            addChipsToWallet(totalValueOfPoints*2);
            dontComePoints.clear();
            System.out.println(builder.toString());
        }

    }

    private void checkFieldBet() {
        ArrayList<Integer> fieldValues = new ArrayList<>(Arrays.asList(2,3,4,9,10,11,12));
        String answer = "";
        if (fieldValues.contains(getDiceValue())){
            if (getDiceValue() == 2 || getDiceValue() == 12) {
                answer = "Field win is doubled! You won " + fieldBet * 2 + " chips!";
                addChipsToWallet(fieldBet * 3);
            } else {
                answer = "Field wins! Payout is 1:1. You won " + fieldBet + " chips!";
                addChipsToWallet(fieldBet*2);
            }
        } else {
            answer = "Field loses. You lost " + fieldBet + " chips.";
        }
        System.out.println(answer);
        fieldBet = 0;
    }

    public void checkPassLineOdds() {
        if (getDiceValue() == point) {
            passLineOddsWin();
        } else if (getDiceValue() ==7) {
            System.out.println("Pass Line odds lose! You lost " + passOddsBet + " chips");
            passOddsBet = 0;
        }
    }

    private void passLineOddsWin() {
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
        addChipsToWallet(chips);
    }

    public void checkDontPassLineOdds() {
        if (getDiceValue() == point) {
            System.out.println("Don't Pass odds lose! You lost " + dontPassOddsBet + " chips.");
            dontPassOddsBet = 0;
        } else if (getDiceValue() ==7) {
            dontPassLineOddsWin();
        }
    }

    private void dontPassLineOddsWin() {
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
        addChipsToWallet(chips);
    }

    public void checkComeBetPointOdds() {
        if (comePointOdds.containsKey(getDiceValue())) {
            comeBetPointOddsWin();
        } else if (getDiceValue() == 7) {
            int totalValueOfPoints = 0;
            for (Map.Entry<Integer, Integer> entry: comePointOdds.entrySet()) {
                totalValueOfPoints += entry.getValue();
            }
            System.out.println("Your Come bet odds lost! You lost a total of " + totalValueOfPoints + " chips.");
            comePointOdds.clear();
        }
    }

    private void comeBetPointOddsWin() {
        int chips = 0;
        if (getDiceValue() == 4 || getDiceValue() == 10) {
            chips = comePointOdds.get(getDiceValue()) + comePointOdds.get(getDiceValue()) * 2;
            System.out.println("Your Come bet Odds on point " + getDiceValue() + " wins! Payout is 2:1. You won " + chips  + " chips!");
            comePointOdds.remove(getDiceValue());
        } else if (getDiceValue() == 5 || getDiceValue() == 9) {
            chips = (comePointOdds.get(getDiceValue()) + (int) Math.floor(comePointOdds.get(getDiceValue()) * 1.5));
            System.out.println("Your Come bet Odds on point " + getDiceValue() + " wins! Payout is 3:2. You won " + chips  + " chips!");
            comePointOdds.remove(getDiceValue());
        } else if (getDiceValue() == 6 || getDiceValue() == 8) {
            chips = (comePointOdds.get(getDiceValue()) + (int) Math.floor(comePointOdds.get(getDiceValue()) * 1.2));
            System.out.println("Your Come bet Odds on point " + getDiceValue() + " wins! Payout is 6:5. You won " + chips  + " chips!");
            comePointOdds.remove(getDiceValue());
        }
        addChipsToWallet(chips);
    }


    public void checkDontComeBetPointOdds() {
        if (dontComePointOdds.containsKey(getDiceValue())) {
            System.out.println("Your Don't Come Odds bet on point " + getDiceValue() + " lost. You lost " +
                    dontComePointOdds.get(getDiceValue()) + " chips!");
            dontComePoints.remove(getDiceValue());
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
                grandTotal += chips;
                builder.append("Your Don't Come bet Odds on point " + entry.getKey() + " wins! Payout is 1:2. You won " + chips + " chips!\n");
            } else if (entry.getKey() == 5 || entry.getKey() == 9) {
                chips = (entry.getValue() + (int) Math.floor(entry.getValue() * .66));
                grandTotal += chips;
                builder.append("Your Don't Come bet Odds on point " + entry.getKey() + " wins! Payout is 2:3. You won " + chips + " chips!\n");
            } else if (entry.getKey() == 6 || entry.getKey() == 8) {
                chips = (entry.getValue() + (int) Math.floor(entry.getValue() * .83));
                grandTotal += chips;
                builder.append("Your Don't Come bet Odds on point " + entry.getKey() + " wins! Payout is 5:6. You won " + chips + " chips!\n");
            }
        }
        addChipsToWallet(grandTotal);
        builder.append("You won a total of " + grandTotal + " chips on Don't Come Point Odds bets!");
        System.out.println(builder.toString());
    }


    private void checkPlaceWinBet() {
        if (placeWinBets.containsKey(getDiceValue())) {
            placeWinBetWin();
        } else if (getDiceValue() == 7) {
            int totalValueOfPoints = 0;
            for (Map.Entry<Integer, Integer> entry: placeWinBets.entrySet()) {
                totalValueOfPoints += entry.getValue();
            }
            System.out.println("Your Place Win bets lost! You lost a total of " + totalValueOfPoints + " chips.");
            placeWinBets.clear();
        }
    }

    private void placeWinBetWin() {
        int diceValue = getDiceValue();
        int chips = 0;
        if (diceValue == 6 || diceValue == 8) {
            chips = (int) Math.floor(placeWinBets.get(diceValue) + placeWinBets.get(diceValue) * 1.16);
            placeWinBets.remove(diceValue);
            System.out.println("Your Place Win bet won! Payout is 7:6. You won " + chips +  " chips!");
        } else if (diceValue == 5 || diceValue == 9) {
            chips = (int) Math.floor(placeWinBets.get(diceValue) + placeWinBets.get(diceValue) * 1.4);
            placeWinBets.remove(diceValue);
            System.out.println("Your Place Win bet won! Payout is 7:5. You won " + chips +  " chips!");
        } else if (diceValue == 4 || diceValue == 10) {
            chips =(int) Math.floor(placeWinBets.get(diceValue) + placeWinBets.get(diceValue) * 1.8);
            placeWinBets.remove(diceValue);
            System.out.println("Your Place Win bet won! Payout is 9:5. You won " + chips + " chips!");
        }
        addChipsToWallet(chips);
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
                grandTotal += chips;
                builder.append(entry.getKey() + " won! Payout is 4:5. You won " + chips + " chips!\n");
            } else if (entry.getKey() == 5 || entry.getKey() == 9) {
                chips = entry.getValue() + (int) Math.floor(entry.getValue() * .62);
                grandTotal += chips;
                builder.append(entry.getKey() + " won! Payout is 5:8. You won " + chips + " chips!\n");
            } else if (entry.getKey() == 4 || entry.getKey() == 10) {
                chips = entry.getValue() + (int) Math.floor(entry.getValue() * .45);
                grandTotal += chips;
                builder.append(entry.getKey() + " won! Payout is 5:11. You won " + chips + " chips!\n");
            }
        }
        addChipsToWallet(grandTotal);
        builder.append("You won a total of " + grandTotal + " chips on Place Lose bets!");
        System.out.println(builder.toString());
    }

    private void makePhaseTwoBet() {
        checkWallet();
        List<String> betTypes = Arrays.asList("come","don't come","field","odds","place win","place lose","roll dice");
        String userAnswer = "";
        if (checkChipAmount(player) < 5) {
            System.out.println("You don't have enough money for more bets!");
        } else {
            do {
                System.out.println("What type of bet would you like to place?" +
                        "\nCome\nDon't Come\nField\nOdds\nPlace Win\nPlace Lose\n");
                userAnswer = userInput.nextLine().toLowerCase();
                if(userAnswer.equals("come")){
                    comeBet = makeComeFieldPassBets(comeBet);
                } else if (userAnswer.equals("don't come")){
                    dontComeBet = makeComeFieldPassBets(dontComeBet);
                } else if (userAnswer.equals("field")){
                    fieldBet = makeComeFieldPassBets(fieldBet);
                } else if (userAnswer.equals("odds")){
                    oddsTypeHandler();
                } else if (userAnswer.equals("place win")){
                    makePlaceBet("win");
                } else if (userAnswer.equals("place lose")){
                    makePlaceBet("lose");
                } else if (userAnswer.equals("roll dice")){
                    break;
                } else {
                    System.out.println("Input not recognized. Please try again.");
                }
            } while (!betTypes.contains(userAnswer));
        }
    }

    private int makeComeFieldPassBets(int bet) {
        System.out.println("How much would you like to bet?");
            bet += getBetAmount();
            placeBet(player, bet);
        return bet;
    }

    private void makePlaceBet(String choice){
        ArrayList<Integer> winLosePlaceValues= new ArrayList<>(Arrays.asList(4,5,6,8,9,10));
        System.out.println("Which number would you like to bet on? 4, 5, 6, 8, 9, or 10");
        int userAnswer = userInput.nextInt();
        userInput.nextLine();
        if (winLosePlaceValues.contains(userAnswer)) {
            System.out.println("How much would you like to bet?");
            int userBet = getBetAmount();
            if(choice.equals("win")){
                placeWinBets.put(userAnswer, userBet);
                placeBet(player,userBet);
            } else if (choice.equals("lose")){
                placeLoseBets.put(userAnswer,userBet);
                placeBet(player,userBet);
            }
        } else {
            System.out.println("Input not recognized.");
        }
    }

    private void oddsTypeHandler() {
        ArrayList<String> oddsTypes = new ArrayList<>(Arrays.asList("none","pass","don't pass","come","don't come"));
        String userAnswer = "";
        do {
            System.out.println("What type of odds would you like to place?\nPass\nDon't Pass\nCome\nDon't Come\nNone");
            userAnswer = userInput.nextLine().toLowerCase();
            if(userAnswer.equals("pass")){
                passOddsBet= makePassDontPassOddsBet("pass line",passLineBet);
            } else if (userAnswer.equals("don't pass")){
                dontPassOddsBet = makePassDontPassOddsBet("don't pass line",dontPassLineBet);
            } else if (userAnswer.equals("come")){
                comePointOdds = makeComeDontComeOddsBet(comePoints, comePointOdds);
            } else if (userAnswer.equals("don't come")){
                dontComePointOdds = makeComeDontComeOddsBet(dontComePoints, dontComePointOdds);
            } else if (userAnswer.equals("none")){
                break;
            } else {
                System.out.println("Input is not recognized. Try again");
            }
        } while (!oddsTypes.contains(userAnswer));
    }

    private int makePassDontPassOddsBet(String betType, int bet) {
        if (bet != 0){
            bet = makeComeFieldPassBets(bet);
        } else {
            System.out.println("You don't have a "+ betType + " bet!");
        }
        return bet;
    }

    private HashMap<Integer,Integer> makeComeDontComeOddsBet(HashMap<Integer,Integer> betPoints, HashMap<Integer,Integer> betPointOdds) {
        if (!(betPoints.isEmpty())) {
            System.out.println("Which point would you like to put odds on?" + betPoints.keySet().toString());
            int userAnswer = userInput.nextInt();
            userInput.nextLine();
            if (betPoints.containsKey(userAnswer)) {
                System.out.println("How much would you like to bet?");
                int bet = getBetAmount();
                betPointOdds.put(userAnswer, bet);
                placeBet(player,bet);
            } else {
                System.out.println("That option is not available.");
            }
        } else {
            System.out.println("Sorry, you do not have any points to bet on.");
        }
        return betPointOdds;
    }

    private int getBetAmount() {
        int betAmount = 0;
        do {
            if(userInput.hasNextInt()){
                betAmount = userInput.nextInt();
                userInput.nextLine();
                if (betAmount < 5) {
                    System.out.println("The minimum bet is 5 chips. Try again.");
                }
            } else {
                System.out.println("That is not a number.  Setting bet to default of 5 chips.");
                betAmount = 5;
            }
        } while (betAmount < 5);
        return betAmount;
    }

    public void end(){
        System.out.println("Thank you for playing.");
    }

    public boolean endGame() {
        boolean keepPlaying = false;
        System.out.println("Would you like to keep playing? Yes or no.");
        String userAnswer = userInput.nextLine().toLowerCase();
        if (userAnswer.equals("yes")) {
            System.out.println("New round starting!");
            keepPlaying = true;
        } else if (userAnswer.equals("no")) {
            keepPlaying = false;
        }
        if(checkChipAmount(player)<5)
            keepPlaying = false;
        return keepPlaying;
    }

    public void rollDice() {
        crapsDice.rollAllDice();
    }

    public void bootPlayerFromGame(Person personToBoot) {
        System.out.println("You don't have enough money to continue playing.  Returning to Main Menu.");
    }

    public int getDiceValue(){
        return crapsDice.getTotalValue();
    }

    public void checkWallet(){
        System.out.println("If you wish check your current available chip amount, type \'check wallet\'. If not press enter. ");
        String check = userInput.nextLine().toLowerCase();
        if(check.equals("check wallet"))
            System.out.println(checkChipAmount(player));
    }

    public int checkChipAmount(Person personToCheck) {
        return personToCheck.getWallet().checkChipAmount();
    }

    public void addChipsToWallet(int chips){
        player.getWallet().addChips(chips);
    }

    public void placeBet(Person player, int betAmount) {
        player.getWallet().removeChips(betAmount);
    }

    public Person getPlayer() {
        return player;
    }

    public DiceManager getDiceManager() {
        return crapsDice;
    }

    //For Unit Tests
    public void setPassLineBet(int passLineBet) { this.passLineBet = passLineBet; }
    public void setDontPassLineBet(int dontPassLineBet) { this.dontPassLineBet = dontPassLineBet; }
    public void setPoint(int point) { this.point = point; }
    public void setComeBet(int comeBet) { this.comeBet = comeBet; }
    public void setDontComeBet(int dontComeBet) { this.dontComeBet = dontComeBet; }
    public void setFieldBet( int fieldBet) { this.fieldBet = fieldBet; }
    public void setPassOddsBet(int passOddsBet) { this.passOddsBet = passOddsBet; }
    public void setDontPassOddsBet(int dontPassOddsBet) { this.dontPassOddsBet = dontPassOddsBet; }
    public void setComeBetPoints(int point, int bet){ comePoints.put(point,bet); }
    public void setDontComeBetPoints(int point, int bet) { dontComePoints.put(point, bet); }
    public void setComeBetPointOdds(int point, int bet) { comePointOdds.put(point, bet); }
    public void setDontComeBetPointOdds(int point, int bet) { dontComePointOdds.put(point,bet); }
    public void setPlaceWinBets(int placeValue, int bet) { placeWinBets.put(placeValue, bet); }
    public void setPlaceLoseBets(int placeValue, int bet) { placeLoseBets.put(placeValue, bet); }

    public static void main(String[] args){
        Person player = new Person("Carolynn");
        player.setWallet(50);
        Craps craps = new Craps(player);
        craps.start();
    }
}
