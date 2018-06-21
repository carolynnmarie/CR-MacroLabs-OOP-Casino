package io.zipcoder.casino.games;


import io.zipcoder.casino.diceAndCoins.DiceManager;
import io.zipcoder.casino.money.GamblingInterface;
import io.zipcoder.casino.people.Person;

import java.util.*;

public class Craps extends Game implements DiceGameInterface, GamblingInterface {

    private DiceManager crapsDice = new DiceManager(2);
    private Person player;
    private int point;
    private int passLineBet;
    private int dontPassLineBet;
    private int passOddsBet;
    private int dontPassOddsBet;
    private int comeBet;
    private int dontComeBet;
    private int fieldBet;
    private ArrayList<Integer> fieldValues;
    private ArrayList<Integer> winLosePlaceValues;
    private HashMap<Integer, Integer> comeBetPoints;
    private HashMap<Integer, Integer> dontComeBetPoints;
    private HashMap<Integer, Integer> comeBetPointOdds;
    private HashMap<Integer, Integer> dontComeBetPointOdds;
    private HashMap<Integer, Integer> placeWinBets;
    private HashMap<Integer, Integer> placeLoseBets;
    private ArrayList<String> betTypes;
    private ArrayList<String> oddsTypes;
    private Scanner userInput = new Scanner(System.in);

    public Craps(Person player) {
        this.player = player;
        this.fieldValues = new ArrayList<>(Arrays.asList(2,3,4,9,10,11,12));
        this.winLosePlaceValues = new ArrayList<>(Arrays.asList(4,5,6,8,9,10));
        this.point = 0;
        this.dontPassLineBet = 0;
        this.passLineBet = 0;
        this.passOddsBet = 0;
        this.dontPassOddsBet = 0;
        this.comeBetPoints = new HashMap<>();
        this.dontComeBetPoints = new HashMap<>();
        this.comeBetPointOdds = new HashMap<>();
        this.dontComeBetPointOdds = new HashMap<>();
        this.placeWinBets = new HashMap<>();
        this.placeLoseBets = new HashMap<>();
        this.betTypes = new ArrayList<>(Arrays.asList("Come", "Don't Come", "Pass Line", "Don't Pass Line", "Place win", "Place lose", "Field", "Odds"));
        this.oddsTypes = new ArrayList<>(Arrays.asList("Pass Line","Don't Pass Line", "Come", "Don't Come"));
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
            makeFirstBet();
            int value = rollDice();
            comeOutRoll(value);
            if(getPoint()!=0){
                value = rollDice();
                placeBetPhaseTwoHandler();
                do {
                    phaseTwoRolls(getPoint(), value);
                }while(getPoint()!=value && value != 7);
            }
            keep = quitProgram();
        } while (keep);
    }

    public void makeFirstBet(){
        do {
            System.out.println("Please place initial bet\nIf you would like to place a pass bet, type pass.  " +
                    "If you would like to place a don't pass bet, type don't pass.\n");
            if (getBetTypeInput().equalsIgnoreCase("pass")) {
                System.out.println("How much would you like to bet on the Pass Line?");
                int passLineBet = minimumBetChecker();
                setPassLineBet(passLineBet);
                placeBet(player, getPassLineBet());
            } else if (getBetTypeInput().equalsIgnoreCase("don't pass")) {
                System.out.println("How much would you like to bet on the Don't Pass Line?");
                int dontPassLineBet = minimumBetChecker();
                setPassLineBet(dontPassLineBet);
                placeBet(player, dontPassLineBet);
                }
        } while (getPassLineBet() == 0 && getDontPassLineBet() == 0);
    }

    public void comeOutRoll(int value) {
        int passBet = getPassLineBet();
        int dontPassBet = getDontPassLineBet();
        String x ="Time to make your first roll!\nYou rolled a " + value + ".";
        if (value == 2 || value == 3) {
            x+="You crapped out. Pass line bets loose and Don't Pass bets win. " + passLineBetLose(passBet) + dontPassLineWin(dontPassBet);
        } else if (value == 7 || value == 11) {
            x+="You rolled a natural! Pass Line bets win and Don't Pass loses. " + passLineBetWin(passBet) + dontPassLineLose(dontPassBet);
        } else if (value == 12) {
            x+="Pass Line looses and Don't Pass bets are pushed to next round. " + passLineBetLose(passBet);
        } else {
            x+="The point is now " + value;
            setPoint(value);
        }
        System.out.println(x);
    }

    private String phaseTwoRolls(int point, int value) {
        String out = "Time for phase two. Roll a " + point + " and not a 7. \nYou rolled a " + value + ".";
        if (value == point) {
            out += "You rolled the point! Pass Line wins and Don't Pass loses!"
                    + passLineBetWin(getPassLineBet()) + dontPassLineLose(getDontPassLineBet());
        } else if (value == 7) {
            out += "You rolled a 7. Don't Pass wins and Pass Line loses!"
                    + dontPassLineWin(getDontPassLineBet()) + passLineBetLose(getPassLineBet());
        } else { checkBetHandler(value);
        }
        return out;
    }

    public String passLineBetWin(int passLineBet) {
        String bet = "";
        if(passLineBet!=0) {
            bet = "Pass line bets pay 1:1. You won " + passLineBet + " chips!";
            player.addChips(passLineBet);
            setPassLineBet(0);
        }
        return bet;
    }

    public String passLineBetLose(int passLineBet) {
        String bet = "";
        if(passLineBet!=0) {
            bet = "Pass Line lost. You lost " + passLineBet + " chips.";
            setPassLineBet(0);
        }
        return bet;
    }

    public String dontPassLineWin(int dontPassLineBet) {
        String bet = "";
        if(dontPassLineBet!=0){
            bet = "Don't Pass bets pay 1:1. You won " + dontPassLineBet + " chips!";
            player.getWallet().addChips(dontPassLineBet);
            setDontPassLineBet(0);
        }
        return bet;
    }

    public String dontPassLineLose(int dontPassLineBet) {
        String bet = "";
        if(dontPassLineBet!=0) {
            bet = "Don't Pass Line lost. You lost " + dontPassLineBet + " chips.";
            setDontPassLineBet(0);
        }
        return bet;
    }


    private void placeBetPhaseTwoHandler() {
        boolean correct = false;
        String x = "";
        do {
            if (checkChipAmount(player) < 5) {
                System.out.println("You don't have enough money for more bets!");
            } else {
                System.out.println("You have " + checkChipAmount(player) + "chips. What type of bet would you like to place?" +
                        "\nCome\nDon't Come\nField\nOdds\nPlace Win\nPlace Lose");
                x = getBetTypeInput();
                if (x.equals("come") || x.equals("don't come") || x.equals("odds") || x.equals("field") ||
                        x.equals("place win") || x.equals("place lose")) {
                    placeBetSelection(x);
                    correct = true;
                }
            }
        }while (correct == false);
    }

    public void placeBetSelection(String userAnswer) {
        if(userAnswer.equals("come")){placeComeBet();
        } else if(userAnswer.equals("don't come")){placeDontComeBet();
        } else if(userAnswer.equals("field")){placeFieldBet();
        } else if(userAnswer.equals("odds")) {
            System.out.println("What type of odds would you like to place?\nPass\nDon't Pass\nCome\nDon't Come");
            oddsTypeSelector(getBetTypeInput());
        }else if(userAnswer.equals("place win")) { placeWinBet();
        }else if(userAnswer.equals("place lose")) { placeLoseBet(); }
    }


    public void checkBetHandler(int value) {
        StringBuilder out = new StringBuilder();
        out.append(comeBetResult(value))
                .append(comeBetPointResult(value))
                .append(dontComeBetResult(value))
                .append(fieldBetResult(value))
                .append(dontComeBetPointResult(value, getDontComeBetPoints()))
                .append(passLineOddsCheck(value, getPassLineBet()))
                .append(dontPassLineOddsCheck(value, getDontPassOddsBet()))
                .append(comeBetPointOdds(value, getComeBetPointOdds()))
                .append(dontComeBetPointOdds(value, getDontComeBetPointOdds()))
                .append(placeWinBetMap(value, getPlaceWinBets()))
                .append(placeLoseBetMap(value, getPlaceLoseBets()))
                .append(placeLoseBetMap(value, getPlaceLoseBets()));
        System.out.println(out.toString());
    }

    public String comeBetResult(int diceValue) {
        String x = "";
        int comeBet = placeComeBet();
        setComeBet(comeBet);
        if ((diceValue == 7 || diceValue == 11)) {
            x = "Come bet wins! You won " + comeBet + " chips!";
            player.addChips(comeBet);
        } else if ((diceValue == 2 || diceValue == 3 || diceValue == 12)) {
            x = "Come Bet loses. You lost " + comeBet + " chips.";
        } else {
            x = "Your Come bet is now a point.";
            comeBetPoints.put(diceValue, comeBet);
        }
        return x;
    }


    public String comeBetPointResult(int dieValue) {
        int totalValueOfPoints = 0;
        HashMap<Integer, Integer> comeBetPoints = getComeBetPoints();
        String bet = "";
        if (comeBetPoints.containsKey(dieValue)) {
            bet = "Your Come bet on point " + dieValue + " wins! You won " + comeBetPoints.get(dieValue) + " chips!";
            player.addChips(comeBetPoints.get(dieValue));
            comeBetPoints.remove(dieValue);
        } else if (dieValue == 7 && !comeBetPoints.isEmpty()) {
            for (Map.Entry<Integer, Integer> entry: comeBetPoints.entrySet()) {
                totalValueOfPoints += entry.getValue();
            }
            bet = "Your Come bet points lost! You lost a total of " + totalValueOfPoints + " chips.";
            comeBetPoints.clear();
        }
        return bet;
    }

    public String dontComeBetResult(int diceValue) {
        String bet = "";
        int dontComeBet = placeDontComeBet();
        if ((diceValue == 7 || diceValue == 11)) {
            bet = "Don't Come bet loses. You lost " + dontComeBet + " chips.";
        } else if (diceValue == 2 || diceValue == 3) {
            bet = "Don't Come bet wins! You won " + dontComeBet + " chips!";
            player.addChips(dontComeBet);
        } else if (!(diceValue ==12)) {
            bet = "You Don't Come bet is now a point.";
            dontComeBetPoints.put(diceValue, dontComeBet);
        }
        return bet;
    }

    public String dontComeBetPointResult(int diceValue, HashMap<Integer, Integer> dontComeBetPoints) {
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
        return bet;
    }

    private String fieldBetResult(int diceValue) {
        String bet = "";
        int fieldBet = placeFieldBet();
        if (getFieldValues().contains(diceValue) && fieldBet != 0){
            if (diceValue == 2 || diceValue == 12) {
                bet = "Field win is doubled! You wn " + fieldBet * 2 + " chips!";
                player.addChips(fieldBet * 2);
            } else {
                bet = "Field wins! Payout is 1:1. You won " + fieldBet + " chips!";
                player.addChips(fieldBet);
            }
        } else if (fieldBet != 0) {
            bet = "Field loses. You lost " + fieldBet + " chips.";
        }
        return bet;
    }

    public String passLineOddsCheck(int diceValue, int passOddsBet1) {
        String bet = "";
        int point = getPoint();
        if (passOddsBet1 != 0 && diceValue == point) {
            if (point == 4 || point == 10) {
                bet = "Pass Line odds wins! Payout is 2:1. You won " + (passOddsBet1 + passOddsBet1 * 2) + " chips!";
                player.addChips(passOddsBet1 + passOddsBet1 * 2);
            } else if (point == 5 || point == 9) {
                bet = "Pass Line odds wins! Payout is 3:2. You won " + (passOddsBet1 +(int) Math.floor(passOddsBet1 * .5)) + " chips!";
                player.addChips(passOddsBet1 + (int)Math.floor(passOddsBet1 * 1.5));
            } else if (point == 6 || point == 8) {
                bet = "Pass Line odds wins! Payout is 6:5. You won " + (passOddsBet1 + (int) Math.floor(passOddsBet1 * .2)) + " chips!";
                player.addChips(passOddsBet1 + (int)Math.floor(passOddsBet1 * 1.2));
            }
        } else if (passOddsBet1 != 0 && diceValue ==7) {
            bet = "Pass Line odds lose! You lost " + passOddsBet1 + " chips";
        }
        if(!bet.equals("")) {
            setPassOddsBet(0);
        }
        return bet;
    }


    public String dontPassLineOddsCheck(int diceValue, int dontPassOddsBet) {
        String bet = "";
        int point = getPoint();
        if (dontPassOddsBet != 0 && diceValue == point) {
            bet = "Don't Pass odds lose! You lost " + dontPassOddsBet + " chips.";
        } else if (dontPassOddsBet != 0 && diceValue == 7) {
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
            setDontPassLineBet(0);
        }
        return bet;
    }

    public String comeBetPointOdds(int diceValue, HashMap<Integer, Integer> comeBetPointOdds) {
        int totalValueOfPoints = 0;
        String bet = "";
        if (comeBetPointOdds.containsKey(diceValue)) {
            bet = comeBetPointOddsWin(diceValue, comeBetPointOdds);
        } else if (diceValue == 7 && !(comeBetPointOdds.isEmpty())) {
            for (Map.Entry<Integer, Integer> entry: comeBetPointOdds.entrySet()) {
                totalValueOfPoints += entry.getValue();
            }
            bet = "Your Come bet odds lost! You lost a total of " + totalValueOfPoints + " chips.";
            comeBetPointOdds.clear();
        }
        return bet;
    }

    private String comeBetPointOddsWin(int diceValue, HashMap<Integer, Integer> comeBetPointOdds) {
        int winnings = comeBetPointOdds.get(diceValue);
        String bet = "";
        if (diceValue == 4 || diceValue == 10) {
            bet = "Your Come bet Odds on point " + diceValue + " wins! Payout is 2:1. You won "
                    + (winnings + (winnings*2)) + " chips!";
            player.addChips(winnings + winnings * 2);
        } else if (diceValue == 5 || diceValue == 9) {
            bet = "Your Come bet Odds on point " + diceValue+ " wins! Payout is 3:2. You won "
                    + (winnings + (int) Math.floor(winnings * 1.5))  + " chips!";
            player.addChips(winnings + (int) Math.floor(winnings * 1.5));
        } else if (diceValue == 6 || diceValue == 8) {
            bet = "Your Come bet Odds on point " + diceValue + " wins! Payout is 6:5. You won "
                    + (winnings + (int) Math.floor(winnings * 1.2))  + " chips!";
            player.addChips(winnings + (int) Math.floor( winnings* 1.2));
        }
        if(!bet.equals("")){
            comeBetPointOdds.remove(diceValue);
        }
        return bet;
    }

    public String dontComeBetPointOdds(int diceValue, HashMap<Integer, Integer> dontComeBetPointOdds) {
        String bet = "";
        if (dontComeBetPointOdds.containsKey(diceValue)) {
            bet = "Your Don't Come Odds bet on point " + diceValue + " lost. You lost " + dontComeBetPointOdds.get(diceValue) + " chips!";
            dontComeBetPoints.remove(diceValue);
        } else if (diceValue == 7 && !dontComeBetPointOdds.isEmpty()) {
            int grandTotal = 0;
            for (Map.Entry<Integer, Integer> entry : dontComeBetPointOdds.entrySet()) {
                Integer key = entry.getKey();
                if (key == 4 || key == 10) {
                    grandTotal = entry.getValue() + (int) Math.floor(entry.getValue() * .5);
                    bet = "Your Don't Come bet Odds on point " + key + " wins! Payout is 1:2. You won " + grandTotal + " chips!";
                    player.addChips(grandTotal);
                } else if (key == 5 || key == 9) {
                    grandTotal = (entry.getValue() + (int) Math.floor(entry.getValue() * .66));
                    bet = "Your Don't Come bet Odds on point " + key + " wins! Payout is 2:3. You won " + grandTotal + " chips!";
                    player.addChips(grandTotal);
                } else if (key == 6 || key == 8) {
                    grandTotal = (entry.getValue() + (int) Math.floor(entry.getValue() * .83));
                    bet = "Your Don't Come bet Odds on point " + key + " wins! Payout is 5:6. You won " + grandTotal + " chips!";
                    player.addChips(grandTotal);
                }
            }
            dontComeBetPointOdds.clear();
            bet += "You won a total of " + grandTotal + " chips on Don't Come Point Odds bets!";
        }
        return bet;
    }

    public String placeWinBetMap(int diceValue, HashMap<Integer, Integer> placeWinBets) {
        String bet = "";
        int totalValueOfPoints = 0;
        if (placeWinBets.containsKey(diceValue)) {
            bet = placeWinBetWin(diceValue, placeWinBets);
        } else if (diceValue == 7 && !(placeWinBets.isEmpty())) {
            for (Map.Entry<Integer, Integer> entry: placeWinBets.entrySet()) {
                totalValueOfPoints += entry.getValue();
            }
            bet = "Your Place Win bets lost! You lost a total of " + totalValueOfPoints + " chips.";
            placeWinBets.clear();
        }
        return bet;
    }

    private String placeWinBetWin(int diceValue, HashMap<Integer, Integer> placeWinBets) {
        int winnings = placeWinBets.get(diceValue);
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
            placeWinBets.remove(diceValue);
        }
        return bet;
    }

    public String placeLoseBetMap(int diceValue, HashMap<Integer, Integer> placeLoseBets) {
        String bet = "";
        if (placeLoseBets.containsKey(diceValue)) {
            bet = "Your Place Lose bet on point " + diceValue + " lost. You lost " + placeLoseBets.get(diceValue) + " chips!";
            placeLoseBets.remove(diceValue);
        } else if (diceValue == 7 && !(placeLoseBets.isEmpty())) {
            bet = placeLoseBetWin(placeLoseBets);
        }
        return bet;
    }

    private String placeLoseBetWin(HashMap<Integer, Integer> placeLoseBets) {
        int grandTotal = 0;
        String bet = "";
        for (Map.Entry<Integer, Integer> entry : placeLoseBets.entrySet()) {
            if (entry.getKey() == 6 || entry.getKey() == 8) {
                grandTotal = entry.getValue() + (int) Math.floor(entry.getValue() * .8);
                bet = "Your Place Lose bet on " + entry.getKey() + " won! Payout is 4:5. You won " + grandTotal + " chips!";
                player.addChips(grandTotal);
            } else if (entry.getKey() == 5 || entry.getKey() == 9) {
                grandTotal = entry.getValue() + (int) Math.floor(entry.getValue() * .62);
                bet = "Your Place Lose bet on " + entry.getKey() + " won! Payout is 5:8. You won " + grandTotal + " chips!";
                player.addChips(grandTotal);
            } else if (entry.getKey() == 4 || entry.getKey() == 10) {
                grandTotal = entry.getValue()+ (int) Math.floor(entry.getValue() * .45);
                bet = "Your Place Lose bet on " + entry.getKey() + " won! Payout is 5:11. You won " + grandTotal + " chips!";
                player.addChips(grandTotal);
            }
        }
        placeLoseBets.clear();
        return bet;
    }

    private int minimumBetChecker() {
        int betAmount = 0;
        do {
            betAmount = getBetInput();
            if (betAmount < 5) { System.out.println("The minimum bet is " + 5 + ". Try again."); }
        } while (betAmount < 5);
        return betAmount;
    }

    private int placeComeBet() {
        System.out.println("How much would you like to bet for the Come bet?");
        int comeBet = minimumBetChecker();
        setComeBet(comeBet);
        placeBet(player, comeBet);
        return comeBet;
    }

    private int placeDontComeBet() {
        System.out.println("How much would you like to bet for the Don't Come bet?");
        int dontComeBet = minimumBetChecker();
        setDontComeBet(dontComeBet);
        placeBet(player, dontComeBet);
        return dontComeBet;
    }

    private int placeFieldBet() {
        System.out.println("How much would you like to bet on the Field?");
        int fieldBet = minimumBetChecker();
        setFieldBet(fieldBet);
        placeBet(player, fieldBet);
        return fieldBet;
    }

    private void placeWinBet() {
        System.out.println("Which number would you like to be on? 4, 5, 6, 8, 9, or 10\n");
        int userAnswer = getBetInput();
        if (getWinLosePlaceValues().contains(userAnswer)) {
            System.out.println("How much would you like to bet?");
            int userBet = getBetInput();
            placeWinBets.put(userAnswer, userBet);
        }
    }

    private void placeLoseBet() {
        System.out.println("Which number would you like to be on? 4, 5, 6, 8, 9, or 10\n");
        int userAnswer = getBetInput();
        if (getWinLosePlaceValues().contains(userAnswer)) {
            System.out.println("How much would you like to bet?");
            int userBet = getBetInput();
            placeLoseBets.put(userAnswer, userBet);
        }
    }

    public void oddsTypeSelector(String userAnswer) {
        if(userAnswer.equals("pass")) { placePassOddsBet();
        } else if (userAnswer.equals("don't pass")){ placeDontPassOddsBet();
        } else if (userAnswer.equals("come")){ placeComeOddsBet();
        } else if (userAnswer.equals("don't come")) { placeDontComeOddsBet();
        } else { System.out.println("Input is not recognized. Try again."); }
    }

    private void placePassOddsBet() {
        if (getPassLineBet() != 0){
            System.out.println("How much would you like to bet on Pass Line odds?");
            setPassOddsBet(minimumBetChecker());
            placeBet(player, getPassOddsBet());
        } else { System.out.println("You don't have a Pass Line bet."); }
    }

    private void placeDontPassOddsBet() {
        if (getDontPassLineBet() != 0) {
            System.out.println("How much would you like to bet on Don't Pass odds?");
            setDontPassOddsBet(minimumBetChecker());
            placeBet(player, getDontPassOddsBet());
        } else {
            System.out.println("You don't have a Don't Pass bet!");
        }
    }

    private void placeComeOddsBet() {
        HashMap<Integer, Integer> comeBetPoints = getComeBetPoints();
        if (comeBetPoints.isEmpty()) {
            System.out.println("You have no Come Bet points.");
        } else {
            System.out.println("Which point would you like to put odds on?\n" +comeBetPoints.keySet().toString() + "\n");
            int userAnswer = getBetInput();
            if (comeBetPoints.containsKey(userAnswer)) {
                System.out.println("How much would you like to bet?");
                int userBet = getBetInput();
                comeBetPointOdds.put(userAnswer, userBet);
            } else { System.out.println("That option is not available."); }
        }
    }

    private void placeDontComeOddsBet() {
        HashMap<Integer, Integer> dontComeBetPoints = getDontComeBetPoints();
        if (dontComeBetPoints.isEmpty()) {
            System.out.println("You have no Don't Come Bet points.");
        } else {
            System.out.println("Which point would you like to put odds on?" + dontComeBetPoints.keySet().toString());
            int userAnswer = getBetInput();
            if (dontComeBetPoints.containsKey(userAnswer)) {
                System.out.println("How much would you like to bet?");;
                int userBet = getBetInput();
                getDontComeBetPointOdds().put(userAnswer, userBet);
            } else { System.out.println("That option is not available."); }
        }
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
        System.out.println("Would you like to keep playing? Yes or no.");
        String userAnswer = "";
        do {
            userAnswer = getBetTypeInput();
            if (userAnswer.equals("yes")) {
                System.out.println("New round starting!");
            } else if (userAnswer.equals("no")) {
                System.out.println("Thanks for playing!");
                return false;
            } else {
                System.out.println("Your answer was not recognized. Please try again.");
            }
        }while (!(userAnswer.equals("yes")) && !(userAnswer.equals("no")));
        return true;
    }

    public int getAnte() { return 5; }

    public int checkPot() { return 0; }

    public void setPlayer(Person player) { this.player = player; }
    public Person getPlayer() { return player; }

    public void setCrapsDice(DiceManager crapsDice) { this.crapsDice = crapsDice; }
    public DiceManager getDiceManager() { return crapsDice; }

    public void setPassLineBet(int testInput) { passLineBet = testInput; }
    public int getPassLineBet() { return passLineBet; }

    public void setDontPassLineBet(int testInput) { dontPassLineBet = testInput; }
    public int getDontPassLineBet() { return dontPassLineBet; }

    public void setPoint(int testInput) { point = testInput; }
    public int getPoint(){ return point;}

    public void setPassOddsBet(int testInput) { passOddsBet = testInput; }
    public int getPassOddsBet() { return passOddsBet; }

    public void setDontPassOddsBet(int testInput) { dontPassOddsBet = testInput; }
    public int getDontPassOddsBet() { return dontPassOddsBet; }

    public void setComeBetPoints(HashMap<Integer, Integer> comeBetPoints) { this.comeBetPoints = comeBetPoints; }
    public HashMap<Integer, Integer> getComeBetPoints(){ return comeBetPoints;}

    public void setComeBetPointOdds(HashMap<Integer, Integer> comeBetPointOdds) { this.comeBetPointOdds = comeBetPointOdds; }
    public HashMap<Integer, Integer> getComeBetPointOdds() { return comeBetPointOdds; }

    public void setDontComeBetPoints(HashMap<Integer, Integer> dontComeBetPoints) { this.dontComeBetPoints = dontComeBetPoints; }
    public HashMap<Integer, Integer> getDontComeBetPoints() { return dontComeBetPoints; }

    public void setPlaceLoseBets(HashMap<Integer, Integer> placeLoseBets) { this.placeLoseBets = placeLoseBets; }
    public HashMap<Integer, Integer> getPlaceLoseBets() { return placeLoseBets; }

    public void setPlaceWinBets(HashMap<Integer, Integer> placeWinBets) { this.placeWinBets = placeWinBets; }
    public HashMap<Integer, Integer> getPlaceWinBets() { return placeWinBets; }

    public ArrayList<Integer> getFieldValues() { return fieldValues; }
    public void setFieldValues(ArrayList<Integer> fieldValues) { this.fieldValues = fieldValues; }

    public ArrayList<Integer> getWinLosePlaceValues() { return winLosePlaceValues; }
    public void setWinLosePlaceValues(ArrayList<Integer> winLosePlaceValues) { this.winLosePlaceValues = winLosePlaceValues; }

    public void setDontComeBetPointOdds(HashMap<Integer, Integer> dontComeBetPointOdds) { this.dontComeBetPointOdds = dontComeBetPointOdds; }
    public HashMap<Integer, Integer> getDontComeBetPointOdds() { return dontComeBetPointOdds; }

    public DiceManager getCrapsDice() { return crapsDice; }

    public int getComeBet() { return comeBet; }
    public void setComeBet(int comeBet) { this.comeBet = comeBet; }

    public int getDontComeBet() { return dontComeBet; }
    public void setDontComeBet(int dontComeBet) { this.dontComeBet = dontComeBet; }

    public int getFieldBet() {
        return fieldBet;
    }

    public void setFieldBet(int fieldBet) {
        this.fieldBet = fieldBet;
    }

    @Override
    public void bootPlayerFromGame(Person personToBoot) { }
    @Override
    public void end() { }
}