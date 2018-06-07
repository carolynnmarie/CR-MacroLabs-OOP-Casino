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
    private ArrayList<Integer> fieldValues;
    private ArrayList<Integer> winLosePlaceValues;
    private HashMap<Integer, Integer> comeBetPoints = new HashMap<>();
    private HashMap<Integer, Integer> dontComeBetPoints = new HashMap<>();
    private HashMap<Integer, Integer> comeBetPointOdds = new HashMap<>();
    private HashMap<Integer, Integer> dontComeBetPointOdds = new HashMap<>();
    private HashMap<Integer, Integer> placeWinBets = new HashMap<>();
    private HashMap<Integer, Integer> placeLoseBets = new HashMap<>();

    private Scanner userInput = new Scanner(System.in);


    public Craps(){
        this.player = new Person("Player");
        this.fieldValues = new ArrayList<>(Arrays.asList(2,3,4,9,10,11,12));
        this.winLosePlaceValues = new ArrayList<>(Arrays.asList(4,5,6,8,9,10));
        this.point = 0;
        this.dontPassLineBet = 0;
        this.passLineBet = 0;
        this.passOddsBet = 0;
        this.dontPassOddsBet = 0;

    }

    public Craps(Person player) {
        this.player = player;
        this.fieldValues = new ArrayList<>(Arrays.asList(2,3,4,9,10,11,12));
        this.winLosePlaceValues = new ArrayList<>(Arrays.asList(4,5,6,8,9,10));
        this.point = 0;
        this.dontPassLineBet = 0;
        this.passLineBet = 0;
        this.passOddsBet = 0;
        this.dontPassOddsBet = 0;
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
        int value = rollDice();
        int passBet = passLineBet;
        int dontPassBet = dontPassLineBet;
        String x ="Time to make your first roll!\nYou rolled a" + value;
        if (value == 2 || value == 3) {
            x+="You crapped out. Pass line bets loose and Don't Pass bets win." + passLineBetLose(passBet) + dontPassLineBetWin(dontPassBet);
        } else if (value == 7 || value == 11) {
            x+="You rolled a natural! Pass Line bets win and Don't Pass loses." + passLineBetWin(passBet) + dontPassLineBetLose(dontPassBet);
        } else if (value == 12) {
            x+="Pass Line looses and Don't Pass bets are pushed to next round." + passLineBetLose(passBet);
        } else {
            x+="The point is now " + value;
            setPoint(value);
            phaseTwoRolls(value);
        }
        System.out.println(x);
    }

    private void phaseTwoRolls(int point) {
        System.out.println("Time for phase two! Roll a " + point + " and not a 7!");
        do {
            placeBetPhaseTwoHandler();
            int value = rollDice();
            System.out.println("You rolled a " + value + "!");
            if (value == point) {
                System.out.println("You rolled the point! Pass Line wins and Don't Pass loses!");
            } else if (value == 7) {
                System.out.println("You rolled a 7. Don't Pass wins and Pass Line loses!");
            }
            checkBetHandler(value);
        } while (!(crapsDice.getTotalValue() == point) && !(crapsDice.getTotalValue() == 7));
    }


    public void checkBetHandler(int value) {
        passLineChecker(value, passLineBet);
        dontPassLineChecker(value, dontPassLineBet);
        comeBetPointMap(value);
        comeBetChecker(value);
        dontComeBetPointMap(value);
        dontComeBetChecker(value);
        checkPassLineOdds(value, passLineBet);
        checkDontPassLineOdds(value, dontPassOddsBet);
        comeBetPointOddsMap(value);
        dontComeBetPointOddsMap(value);
        checkFieldBet(value);
        placeWinBetMap(value);
        placeLoseBetMap(value);
    }

    public void passLineChecker(int value, int passLineBet) {
        String bet = "";
        if (value == getPoint() && passLineBet != 0) {
            bet = passLineBetWin(passLineBet);
        } else if (value == 7 && passLineBet != 0) {
            bet = passLineBetLose(passLineBet);
        }
        if(!bet.equals("")){
            System.out.println(bet);
        }
    }

    public String passLineBetWin(int passLineBet) {
        String bet = "Pass line bets pay 1:1. You won " + passLineBet + " chips!";
        player.addChips(passLineBet);
        setPassLineBet(0);
        return bet;
    }

    public String passLineBetLose(int passLineBet) {
        String bet = "Pass Line lost. You lost " + passLineBet + " chips.";
        setPassLineBet(0);
        return bet;
    }

    public void dontPassLineChecker(int value, int dontPassLineBet) {
        String bet = "";
        if (value == getPoint() && dontPassLineBet != 0) {
            bet = dontPassLineBetLose(dontPassLineBet);
        } else if (value == 7 && dontPassLineBet != 0) {
            bet = dontPassLineBetWin(dontPassLineBet);
        }
        if(!bet.equals("")){
            System.out.println(bet);
        }
    }

    public String dontPassLineBetWin(int dontPassLineBet) {
        String bet = "Don't Pass bets pay 1:1. You won " + dontPassLineBet + " chips!";
        player.getWallet().addChips(dontPassLineBet);
        setDontPassLineBet(0);
        return bet;
    }

    public String dontPassLineBetLose(int dontPassLineBet) {
        String bet = "Don't Pass Line lost. You lost " + dontPassLineBet + " chips.";
        setDontPassLineBet(0);
        return bet;
    }

    public void comeBetChecker(int diceValue) {
        String x = "";
        int comeBet = placeComeBet();
        if (comeBet != 0) {
            if ((diceValue == 7 || diceValue == 11)) {
                x = "Come bet wins! You won " + comeBet + " chips!";
                player.addChips(comeBet);
            } else if ((diceValue == 2 || diceValue == 3 || diceValue == 12)) {
                x = "Come Bet loses. You lost " + comeBet + " chips.";
            } else {
                x = "Your Come bet is now a point.";
                comeBetPoints.put(diceValue, comeBet);
            }
            System.out.println(x);
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
            dontComeBetPoints.put(diceValue, dontComeBet);
        }
        if(!bet.equals("")) {
            System.out.println(bet);
        }
    }

    public void comeBetPointMap(int dieValue) {
        int totalValueOfPoints = 0;
        HashMap<Integer, Integer> comeBetPoints = getComeBetPoints();
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

    public void dontComeBetPointMap(int diceValue) {
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


    private void checkFieldBet(int diceValue) {
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
        if(!bet.equals("")){
            System.out.println(bet);
        }
    }


    public void checkPassLineOdds(int diceValue, int passOddsBet1) {
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
            System.out.println(bet);
        }

    }


    public void checkDontPassLineOdds(int diceValue, int dontPassOddsBet) {
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
            System.out.println(bet);
        }

    }

    public void comeBetPointOddsMap(int diceValue) {
        int totalValueOfPoints = 0;
        if (comeBetPointOdds.containsKey(diceValue)) {
            comeBetPointOddsWin(diceValue);
        } else if (diceValue == 7 && !(comeBetPointOdds.isEmpty())) {
            for (Map.Entry<Integer, Integer> entry: comeBetPointOdds.entrySet()) {
                totalValueOfPoints += entry.getValue();
            }
            System.out.println("Your Come bet odds lost! You lost a total of " + totalValueOfPoints + " chips.");
            comeBetPointOdds.clear();
        }
    }

    private void comeBetPointOddsWin(int diceValue) {
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
            System.out.println(bet);
        }
    }


    public void dontComeBetPointOddsMap(int diceValue) {
        String bet = "";
        if (dontComeBetPointOdds.containsKey(diceValue)) {
            bet = "Your Don't Come Odds bet on point " + diceValue + " lost." +
                    " You lost " + dontComeBetPointOdds.get(diceValue) + " chips!";
            dontComeBetPoints.remove(diceValue);
        } else if (diceValue == 7 && !(dontComeBetPointOdds.isEmpty())) {
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
        if (!bet.equals(""))System.out.println(bet);
    }


    private void placeWinBetMap(int diceValue) {
        int totalValueOfPoints = 0;
        if (placeWinBets.containsKey(diceValue)) {
            placeWinBetWin(diceValue);
        } else if (diceValue == 7 && !(placeWinBets.isEmpty())) {
            for (Map.Entry<Integer, Integer> entry: placeWinBets.entrySet()) {
                totalValueOfPoints += entry.getValue();
            }
            System.out.println("Your Place Win bets lost! You lost a total of " + totalValueOfPoints + " chips.");
            placeWinBets.clear();
        }
    }

    private void placeWinBetWin(int diceValue) {
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
            System.out.println(bet);
            placeWinBets.remove(diceValue);
        }
    }

    private void placeLoseBetMap(int diceValue) {
        if (placeLoseBets.containsKey(diceValue)) {
            System.out.println("Your Place Lose bet on point " + diceValue + " lost. You lost " + placeLoseBets.get(diceValue) + " chips!");
            placeLoseBets.remove(diceValue);
        } else if (diceValue == 7 && !(placeLoseBets.isEmpty())) {
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
        System.out.println(bet + "You won a total of " + grandTotal + " chips on Place Lose bets!");
    }

    private void placeBetPhaseTwoHandler() {
        boolean correct = false;
        do {
            if (checkChipAmount(player) < 5) {
                System.out.println("You don't have enough money for more bets!");
            } else {
                System.out.println("You have " + checkChipAmount(player) + "chips. What type of bet would you like to place?" +
                        "\nCome\nDon't Come\nField\nOdds\nPlace Win\nPlace Lose");
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
        if (getWinLosePlaceValues().contains(userAnswer)) {
            System.out.println("How much would you like to bet?");
            int userBet = getBetInput();
            placeWinBets.put(userAnswer, userBet);
        }
    }

    private void placeLoseChecker() {
        System.out.println("Which number would you like to be on? 4, 5, 6, 8, 9, or 10\n");
        int userAnswer = getBetInput();
        if (getWinLosePlaceValues().contains(userAnswer)) {
            System.out.println("How much would you like to bet?");
            int userBet = getBetInput();
            placeLoseBets.put(userAnswer, userBet);
        }
    }

    public void firstBetChecker(){
        do {
            System.out.println("Please place initial bet\n" +
                    "If you would like to place a pass bet, type 'p'.  If you would like to place a don't pass bet, type 'd'.\n");
            if (getBetTypeInput().equalsIgnoreCase("p")) {
                setPassLineBet(placePassLineBet());
                System.out.println("If you would also like to place a don't pass bet, type 'd', if not, type any other key");
                if(getBetTypeInput().equalsIgnoreCase("d")) {
                    if (player.checkChips() < 5) {
                        System.out.println("You don't have enough money for another bet");
                        break;
                    } else {
                        setDontPassLineBet(placeDontPassLineBet());
                    }
                }
            } else {
                if (getBetTypeInput().equalsIgnoreCase("don't pass")) {
                    setDontPassLineBet(placeDontPassLineBet());
                }
            }
        } while (passLineBet == 0 && dontPassLineBet == 0);
    }


    public int placePassLineBet(){
        System.out.println("How much would you like to bet on the Pass Line?");
        int passLineBet = minimumBetChecker();
        placeBet(player, passLineBet);
        return passLineBet;
    }

    public int placeDontPassLineBet(){
        System.out.println("How much would you like to bet on the Don't Pass Line?");
        int dontPassLineBet = minimumBetChecker();
        placeBet(player, dontPassLineBet);
        return dontPassLineBet;
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
        if (getPassLineBet() != 0){
            System.out.println("How much would you like to bet on Pass Line odds?");
            setPassOddsBet(passOddsBet + minimumBetChecker());
            placeBet(player, passOddsBet);
        } else {
            System.out.println("You don't have a Pass Line bet.");
        }
    }

    private void placeDontPassOddsBet() {

        if (getDontPassLineBet() != 0) {
            System.out.println("How much would you like to bet on Don't Pass odds?");
            setDontPassOddsBet(getDontPassOddsBet() + minimumBetChecker());
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
                getComeBetPointOdds().put(userAnswer, userBet);
            } else {
                System.out.println("That option is not available.");
            }
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
    public void setPassOddsBet(int testInput) {
        passOddsBet = testInput;
    }
    public void setDontPassOddsBet(int testInput) { dontPassOddsBet = testInput; }
    public int getPoint(){ return point;}
    public HashMap<Integer, Integer> getComeBetPoints(){ return comeBetPoints;}
    public int getPassLineBet() { return passLineBet; }
    public int getDontPassLineBet() { return dontPassLineBet; }
    public int getDontPassOddsBet() { return dontPassOddsBet; }
    public HashMap<Integer, Integer> getComeBetPointOdds() { return comeBetPointOdds; }
    public HashMap<Integer, Integer> getDontComeBetPointOdds() { return dontComeBetPointOdds; }
    public HashMap<Integer, Integer> getDontComeBetPoints() { return dontComeBetPoints; }
    public HashMap<Integer, Integer> getPlaceLoseBets() { return placeLoseBets; }
    public HashMap<Integer, Integer> getPlaceWinBets() { return placeWinBets; }
    public int getPassOddsBet() { return passOddsBet; }

    public DiceManager getCrapsDice() {
        return crapsDice;
    }

    public void setCrapsDice(DiceManager crapsDice) {
        this.crapsDice = crapsDice;
    }

    public ArrayList<Integer> getFieldValues() {
        return fieldValues;
    }

    public void setFieldValues(ArrayList<Integer> fieldValues) {
        this.fieldValues = fieldValues;
    }

    public ArrayList<Integer> getWinLosePlaceValues() {
        return winLosePlaceValues;
    }

    public void setWinLosePlaceValues(ArrayList<Integer> winLosePlaceValues) {
        this.winLosePlaceValues = winLosePlaceValues;
    }

    public void setComeBetPoints(HashMap<Integer, Integer> comeBetPoints) {
        this.comeBetPoints = comeBetPoints;
    }

    public void setDontComeBetPoints(HashMap<Integer, Integer> dontComeBetPoints) {
        this.dontComeBetPoints = dontComeBetPoints;
    }

    public void setComeBetPointOdds(HashMap<Integer, Integer> comeBetPointOdds) {
        this.comeBetPointOdds = comeBetPointOdds;
    }

    public void setDontComeBetPointOdds(HashMap<Integer, Integer> dontComeBetPointOdds) {
        this.dontComeBetPointOdds = dontComeBetPointOdds;
    }

    public void setPlaceWinBets(HashMap<Integer, Integer> placeWinBets) {
        this.placeWinBets = placeWinBets;
    }

    public void setPlaceLoseBets(HashMap<Integer, Integer> placeLoseBets) {
        this.placeLoseBets = placeLoseBets;
    }

    public void setPlayer(Person player) {
        this.player = player;
    }

    @Override
    public void bootPlayerFromGame(Person personToBoot) { }
    @Override
    public void end() {

    }
}
