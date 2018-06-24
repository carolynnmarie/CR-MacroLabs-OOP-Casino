package io.zipcoder.casino.games;


import io.zipcoder.casino.diceAndCoins.DiceManager;
import io.zipcoder.casino.money.GamblingInterface;
import io.zipcoder.casino.people.Person;

import java.util.*;

import static io.zipcoder.casino.games.CrapsPayouts.dontPassLineDontComePayout;
import static io.zipcoder.casino.games.CrapsPayouts.placeLoseBetPayout;
import static io.zipcoder.casino.games.CrapsPayouts.placeWinBetPayout;

public class Craps extends Game implements DiceGameInterface, GamblingInterface {

    private DiceManager crapsDice = new DiceManager(2);
    private Person player;
    private int diceValue;
    private int point;
    private HashMap<String, Integer> bets;
    private ArrayList<Integer> fieldValues;
    private ArrayList<Integer> winLosePlaceValues;
    private HashMap<Integer, Integer> comeBetPoints;
    private HashMap<Integer, Integer> dontComeBetPoints;
    private HashMap<Integer, Integer> comeBetPointOdds;
    private HashMap<Integer, Integer> dontComeBetPointOdds;
    private HashMap<Integer, Integer> placeWinBets;
    private HashMap<Integer, Integer> placeLoseBets;
    private Scanner userInput = new Scanner(System.in);

    public Craps(Person player) {
        this.player = player;
        this.fieldValues = new ArrayList<>(Arrays.asList(2, 3, 4, 9, 10, 11, 12));
        this.winLosePlaceValues = new ArrayList<>(Arrays.asList(4, 5, 6, 8, 9, 10));
        this.point = 0;
        this.diceValue = 0;
        this.comeBetPoints = new HashMap<>();
        this.dontComeBetPoints = new HashMap<>();
        this.comeBetPointOdds = new HashMap<>();
        this.dontComeBetPointOdds = new HashMap<>();
        this.placeWinBets = new HashMap<>();
        this.placeLoseBets = new HashMap<>();
        this.bets = new HashMap<>();
        bets.put("pass", 0);
        bets.put("don't pass", 0);
        bets.put("pass odds", 0);
        bets.put("don't pass odds", 0);
        bets.put("come", 0);
        bets.put("don't come", 0);
        bets.put("field", 0);
    }

    public HashMap<String, Integer> getBets(){
        return this.bets;
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
            setDiceValue(value);
            comeOutRoll();
            if (getPoint() != 0) {
                value = rollDice();
                setDiceValue(value);
                placeBetPhaseTwoHandler();
                do {
                    phaseTwoRolls(getPoint(), value);
                } while (getPoint() != value && value != 7);
            }
            keep = quitProgram();
        } while (keep);
        end();
    }

    public void makeFirstBet() {
        do {
            System.out.println("Please place initial bet\nIf you would like to place a pass bet, type pass.  " +
                    "If you would like to place a don't pass bet, type don't pass.\n");
            if (getBetTypeInput().equalsIgnoreCase("pass")) {
                System.out.println("How much would you like to bet on the Pass Line?");
                int passLineBet = makeBet();
                getBets().replace("pass", passLineBet);
                placeBet(player, getPassLineBet());
            } else if (getBetTypeInput().equalsIgnoreCase("don't pass")) {
                System.out.println("How much would you like to bet on the Don't Pass Line?");
                int dontPassLineBet = makeBet();
                getBets().replace("don't pass", dontPassLineBet);
                placeBet(player, dontPassLineBet);
            }
        } while (getPassLineBet() == 0 && getDontPassLineBet() == 0);
    }

    public void comeOutRoll() {
        int value = getDiceValue();
        int passBet = getBets().get("pass");
        int dontPassBet = getBets().get("don't pass");
        String x = "Time to make your first roll!\nYou rolled a " + value + ".";
        if (value == 2 || value == 3) {
            x += "You crapped out. Pass line bets loose and Don't Pass bets win. "
                    + passLineBetLose(passBet) + dontPassLineWin(dontPassBet);
        } else if (value == 7 || value == 11) {
            x += "You rolled a natural! Pass Line bets win and Don't Pass loses. "
                    + passLineBetWin(passBet) + dontPassLineLose(dontPassBet);
        } else if (value == 12) {
            x += "Pass Line looses and Don't Pass bets are pushed to next round. "
                    + passLineBetLose(passBet);
        } else {
            x += "The point is now " + value;
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
        } else {
            checkBetHandler(value);
        }
        return out;
    }

    public String passLineBetWin(int passLineBet) {
        String bet = "";
        if (passLineBet != 0) {
            bet = "Pass line bets pay 1:1. You won " + passLineBet + " chips!";
            player.addChips(passLineBet);
            getBets().replace("pass", 0);
        }
        return bet;
    }

    public String passLineBetLose(int passLineBet) {
        String bet = "";
        if (passLineBet != 0) {
            bet = "Pass Line lost. You lost " + passLineBet + " chips.";
            getBets().replace("pass", 0);
        }
        return bet;
    }

    public String dontPassLineWin(int dontPassLineBet) {
        String bet = "";
        if (dontPassLineBet != 0) {
            bet = "Don't Pass bets pay 1:1. You won " + dontPassLineBet + " chips!";
            player.getWallet().addChips(dontPassLineBet);
            getBets().replace("don't pass", 0);
        }
        return bet;
    }

    public String dontPassLineLose(int dontPassLineBet) {
        String bet = "";
        if (dontPassLineBet != 0) {
            bet = "Don't Pass Line lost. You lost " + dontPassLineBet + " chips.";
            getBets().replace("don't pass", 0);
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
        } while (correct == false);
    }

    public void placeBetSelection(String userAnswer) {
        if (userAnswer.equals("come")) {
            placeComeBet();
        } else if (userAnswer.equals("don't come")) {
            placeDontComeBet();
        } else if (userAnswer.equals("field")) {
            placeFieldBet();
        } else if (userAnswer.equals("odds")) {
            System.out.println("What type of odds would you like to place?\nPass\nDon't Pass\nCome\nDon't Come");
            oddsTypeSelector(getBetTypeInput());
        } else if (userAnswer.equals("place win")) {
            placeWinBet();
        } else if (userAnswer.equals("place lose")) {
            placeLoseBet();
        }
    }

    public void checkBetHandler(int value) {
        StringBuilder out = new StringBuilder();
        if(getComeBet() != 0) {
            out.append(comeBetResult(value)) ;
        }
        if(!getComeBetPoints().isEmpty()) {
            out.append(comeBetPointResult(value));
        }
        if(getDontComeBet()!=0) {
            out.append(dontComeBetResult(value));
        }
        if(getFieldBet()!=0) {
            out.append(fieldBetResult(value));
        }
        if(!getDontComeBetPoints().isEmpty()) {
            out.append(dontComeBetPointResult(value));
        }
        if(getPassOddsBet()!=0) {
            out.append(passLineOddsResult(value));
        }
        if(getDontPassOddsBet()!=0) {
            out.append(dontPassLineOddsResult(value, getDontPassOddsBet()));
        }
        if(!getComeBetPointOdds().isEmpty()) {
            out.append(comeBetPointOdds(value, getComeBetPointOdds()));
        }
        if(!getDontComeBetPoints().isEmpty()) {
            out.append(dontComeBetPointOdds(value, getDontComeBetPointOdds()));
        }
        if(!getPlaceWinBets().isEmpty()) {
            out.append(placeWinBetResult(value, getPlaceWinBets()));
        }
        if(!getPlaceLoseBets().isEmpty()) {
            out.append(placeLoseBetResult(value, getPlaceLoseBets()));
        }
        System.out.println(out.toString());
    }

    public String comeBetResult(int diceValue) {
        String x = "";
        getBets().replace("come", getComeBet());
        if ((diceValue == 7 || diceValue == 11)) {
            x = "Come bet wins! You won " + getComeBet() + " chips!";
            player.addChips(getComeBet());
        } else if ((diceValue == 2 || diceValue == 3 || diceValue == 12)) {
            x = "Come Bet loses. You lost " + getComeBet() + " chips.";
        } else {
            x = "Your Come bet is now a point.";
            getComeBetPoints().put(diceValue, getComeBet());
        }
        return x;
    }


    public String comeBetPointResult(int dieValue) {
        int totalValueOfPoints = 0;
        String bet = "";
        if (getComeBetPoints().containsKey(dieValue)) {
            bet = "Your Come bet on point " + dieValue + " wins! You won " + getComeBetPoints().get(dieValue) + " chips!";
            player.addChips(getComeBetPoints().get(dieValue));
            getComeBetPoints().remove(dieValue);
        } else if (dieValue == 7) {
            for (Map.Entry<Integer, Integer> entry : getComeBetPoints().entrySet()) {
                totalValueOfPoints += entry.getValue();
            }
            bet = "Your Come bet points lost! You lost a total of " + totalValueOfPoints + " chips.";
            getComeBetPoints().clear();
        }
        return bet;
    }

    public String dontComeBetResult(int diceValue) {
        String bet = "";
        if ((diceValue == 7 || diceValue == 11)) {
            bet = "Don't Come bet loses. You lost " + getDontComeBet() + " chips.";
        } else if (diceValue == 2 || diceValue == 3) {
            bet = "Don't Come bet wins! You won " + getDontComeBet() + " chips!";
            player.addChips(getDontComeBet());
        } else if (!(diceValue == 12)) {
            bet = "You Don't Come bet is now a point.";
            getDontComeBetPoints().put(diceValue, getDontComeBet());
        }
        return bet;
    }

    public String dontComeBetPointResult(int diceValue) {
        String bet = "";
        if (diceValue ==7) {
            int totalValueOfPoints = 0;
            for (Map.Entry<Integer, Integer> entry : getDontComeBetPoints().entrySet()) {
                totalValueOfPoints += entry.getValue();
            }
            bet = "Your Don't Come bet points won! You won a total of " + totalValueOfPoints + " chips.";
            player.addChips(totalValueOfPoints);
            getDontComeBetPoints().clear();
        } else if (getDontComeBetPoints().containsKey(diceValue)) {
            bet = "Your Don't Come bet on point " + diceValue + " lost." +
                    " You lost " + getDontComeBetPoints().get(diceValue) + " chips!";
            getDontComeBetPoints().remove(diceValue);
        }
        return bet;
    }

    public String fieldBetResult(int diceValue) {
        String bet = "";
        int fieldBet = getFieldBet();
        if (getFieldValues().contains(diceValue)) {
            if (diceValue == 2 || diceValue == 12) {
                bet = "Field win is doubled! You wn " + fieldBet * 2 + " chips!";
                player.addChips(fieldBet * 2);
            } else {
                bet = "Field wins! Payout is 1:1. You won " + fieldBet + " chips!";
                player.addChips(fieldBet);
            }
        } else{
            bet = "Field loses. You lost " + fieldBet + " chips.";
        }
        return bet;
    }

    public String passLineOddsResult(int diceValue) {
        HashMap<Integer, Double> payout = CrapsPayouts.passLineComeBetPayout();
        String bet = "";
        if (diceValue == 7) {
            bet = "Pass Line odds lose! You lost " + getPassOddsBet() + " chips";
        } else if (diceValue == getPoint()) {
            for (Map.Entry<Integer, Double> entry : payout.entrySet()) {
                if (entry.getValue().equals(diceValue)) {
                    bet = "Pass Line odds wins on " + entry.getKey() + "! You won "
                            + (getPassOddsBet() + getPassOddsBet() * entry.getValue()) + " chips!";
                    player.addChips((int) (getPassOddsBet() + getPassOddsBet() * entry.getValue()));
                }
            }
        }
        if (!bet.equals("")) {
            getBets().replace("pass", 0);
        }
        return bet;
    }


    public String dontPassLineOddsResult(int diceValue, int dontPassOddsBet) {
        HashMap<Integer, Double> payout = dontPassLineDontComePayout();
        String bet = "";
        int point = getPoint();
        if (diceValue == point) {
            bet = "Don't Pass odds lose! You lost " + dontPassOddsBet + " chips.";
        } else if (diceValue == 7) {
            for (Map.Entry<Integer, Double> entry : payout.entrySet()) {
                if (entry.getKey().equals(point)) {
                    bet = "Don't Pass odds on " + entry.getKey() + " wins! You won " + (dontPassOddsBet + (int) Math.floor(dontPassOddsBet * entry.getValue())) + " chips!";
                    player.addChips(dontPassOddsBet + (int) Math.floor(dontPassOddsBet * entry.getValue()));
                }
            }
        }
        if (!bet.equals("")) {
            getBets().replace("don't pass", 0);
        }
        return bet;
    }


    public String comeBetPointOdds(int diceValue, HashMap<Integer, Integer> comeBetPointOdds) {
        int totalValueOfPoints = 0;
        String bet = "";
        if (comeBetPointOdds.containsKey(diceValue)) {
            bet = comeBetPointOddsWin(diceValue, comeBetPointOdds);
        } else if (diceValue == 7 && !(comeBetPointOdds.isEmpty())) {
            for (Map.Entry<Integer, Integer> entry : comeBetPointOdds.entrySet()) {
                totalValueOfPoints += entry.getValue();
            }
            bet = "Your Come bet odds lost! You lost a total of " + totalValueOfPoints + " chips.";
            getComeBetPointOdds().clear();
        }
        return bet;
    }

    private String comeBetPointOddsWin(int diceValue, HashMap<Integer, Integer> comeBetPointOdds) {
        HashMap<Integer, Double> payout = CrapsPayouts.passLineComeBetPayout();
        int winnings = comeBetPointOdds.get(diceValue);
        String bet = "";
        for (Map.Entry<Integer, Double> entry : payout.entrySet()) {
            if (entry.getKey().equals(diceValue)){
                bet = "Your Come bet Odds on point " + diceValue + " wins! You won "
                + (int)Math.round((winnings + winnings * entry.getValue())) + " chips!";
                player.addChips((int)Math.round((winnings + winnings * entry.getValue())));
            }
        }
        if(!bet.equals("")){
            getComeBetPointOdds().remove(diceValue);
        }
        return bet;
    }

    public String dontComeBetPointOdds(int diceValue, HashMap<Integer, Integer> dontComeBetPointOdds) {
        HashMap<Integer, Double> payout = dontPassLineDontComePayout();
        String bet = "";
        if (dontComeBetPointOdds.containsKey(diceValue)) {
            bet = "Your Don't Come Odds bet on point " + diceValue + " lost. You lost " + dontComeBetPointOdds.get(diceValue) + " chips!";
            getDontComeBetPoints().remove(diceValue);
        } else if (diceValue == 7 && !dontComeBetPointOdds.isEmpty()) {
            int grandTotal = 0;
            for(Map.Entry<Integer, Double> payoutEntry: payout.entrySet()){
                for (Map.Entry<Integer, Integer> entry : dontComeBetPointOdds.entrySet()) {
                    Integer key = entry.getKey();
                    if(key.equals(payoutEntry.getKey())){
                        grandTotal = entry.getValue() + (int) Math.floor(entry.getValue() * payoutEntry.getValue());
                        bet = "Your Don't Come bet Odds on point " + key + " wins! You won " + grandTotal + " chips!";
                        player.addChips(grandTotal);
                    }
                }
            }
            getDontComeBetPointOdds().clear();
            bet += "You won a total of " + grandTotal + " chips on Don't Come Point Odds bets!";
        }
        return bet;
    }

    public String placeWinBetResult(int diceValue, HashMap<Integer, Integer> placeWinBets) {
        String bet = "";
        int totalValueOfPoints = 0;
        if (placeWinBets.containsKey(diceValue)) {
            bet = placeWinBetWin(diceValue, placeWinBets);
        } else if (diceValue == 7 && !(placeWinBets.isEmpty())) {
            for (Map.Entry<Integer, Integer> entry: placeWinBets.entrySet()) {
                totalValueOfPoints += entry.getValue();
            }
            bet = "Your Place Win bets lost! You lost a total of " + totalValueOfPoints + " chips.";
            getPlaceWinBets().clear();
        }
        return bet;
    }

    private String placeWinBetWin(int diceValue, HashMap<Integer, Integer> placeWinBets) {
        int winnings = placeWinBets.get(diceValue);
        HashMap<Integer, Double> payout = placeWinBetPayout();
        String bet = "";
        for(Map.Entry<Integer, Double>entry: payout.entrySet()){
            if(entry.getKey().equals(diceValue)){
                bet = "Your Place Win bet on " + entry.getKey() + "won! You won "
                        + (int)Math.floor(winnings + winnings*entry.getValue()) + " chips!";
                player.addChips((int)Math.floor(winnings + winnings*entry.getValue()));
            }
        }
            getPlaceWinBets().remove(diceValue);
        return bet;
    }


    public String placeLoseBetResult(int diceValue, HashMap<Integer, Integer> placeLoseBets) {
        String bet = "";
        if (placeLoseBets.containsKey(diceValue)) {
            bet = "Your Place Lose bet on point " + diceValue + " lost. You lost " + placeLoseBets.get(diceValue) + " chips!";
            getPlaceLoseBets().remove(diceValue);
        } else if (diceValue == 7 && !(placeLoseBets.isEmpty())) {
            bet = placeLoseBetWin(placeLoseBets);
        }
        return bet;
    }

    private String placeLoseBetWin(HashMap<Integer, Integer> placeLoseBets) {
        HashMap<Integer,Double> payout = placeLoseBetPayout();
        int grandTotal = 0;
        String bet = "";
        for (Map.Entry<Integer, Integer> entry : placeLoseBets.entrySet()) {
            for(Map.Entry<Integer, Double> payoutEntry: payout.entrySet()) {
                if (entry.getKey().equals(payoutEntry.getKey())) {
                    grandTotal = entry.getValue() + (int) Math.floor(entry.getValue() * payoutEntry.getValue());
                    bet = "Your Place Lose bet on " + entry.getKey() + " won! You won " + grandTotal + " chips!";
                    player.addChips(grandTotal);
                }
            }
        }
        getPlaceLoseBets().clear();
        return bet;
    }


    private int makeBet() {
        int betAmount = 0;
        do {
            betAmount = getBetInput();
            if (betAmount < 5) { System.out.println("The minimum bet is " + 5 + ". Try again."); }
        } while (betAmount < 5);
        return betAmount;
    }

    private int placeComeBet() {
        System.out.println("How much would you like to bet for the Come bet?");
        int comeBet = makeBet();
        getBets().replace("come", comeBet);
        placeBet(player, comeBet);
        return comeBet;
    }

    private int placeDontComeBet() {
        System.out.println("How much would you like to bet for the Don't Come bet?");
        int dontComeBet = makeBet();
        getBets().replace("don't come", dontComeBet);
        placeBet(player, dontComeBet);
        return dontComeBet;
    }

    private int placeFieldBet() {
        System.out.println("How much would you like to bet on the Field?");
        int fieldBet = makeBet();
        getBets().replace("field", fieldBet);
        placeBet(player, fieldBet);
        return fieldBet;
    }

    private void placeWinBet() {
        System.out.println("Which number would you like to be on?" + winPlaceLoseValuesToString() + "\n");
        int userAnswer = getBetInput();
        if (getWinLosePlaceValues().contains(userAnswer)) {
            System.out.println("How much would you like to bet?");
            int userBet = getBetInput();
            placeWinBets.put(userAnswer, userBet);
        }
    }

    private void placeLoseBet() {
        System.out.println("Which number would you like to be on?" + winPlaceLoseValuesToString() + "\n");
        int userAnswer = getBetInput();
        if (getWinLosePlaceValues().contains(userAnswer)) {
            System.out.println("How much would you like to bet?");
            int userBet = getBetInput();
            placeLoseBets.put(userAnswer, userBet);
        }
    }

    public void oddsTypeSelector(String userAnswer) {
        do {
            if (userAnswer.equals("pass")) {
                placePassOddsBet();
            } else if (userAnswer.equals("don't pass")) {
                placeDontPassOddsBet();
            } else if (userAnswer.equals("come")) {
                placeComeOddsBet();
            } else if (userAnswer.equals("don't come")) {
                placeDontComeOddsBet();
            } else {
                System.out.println("Input is not recognized. Try again.");
            }
        }while(!userAnswer.equals("pass") || !userAnswer.equals("don't pass")
                || !userAnswer.equals("come") || !userAnswer.equals("don't come"));
    }

    private void placePassOddsBet() {
        if (getPassLineBet() != 0){
            System.out.println("How much would you like to bet on Pass Line odds?");
            getBets().replace("pass odds", makeBet());
            placeBet(player, getPassOddsBet());
        } else { System.out.println("You don't have a Pass Line bet."); }
    }

    private void placeDontPassOddsBet() {
        if (getDontPassLineBet() != 0) {
            System.out.println("How much would you like to bet on Don't Pass odds?");
            getBets().replace("don't pass odds", makeBet());
            placeBet(player, getDontPassOddsBet());
        } else {
            System.out.println("You don't have a Don't Pass bet.");
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


    public String winPlaceLoseValuesToString(){
        String x = "";
        for(Integer integer: winLosePlaceValues){
            x+= integer.toString() + " ";
        }
        return x;
    }

    public int getAnte() { return 5; }

    public int checkPot() { return 0; }

    public void setPlayer(Person player) { this.player = player; }
    public Person getPlayer() { return player; }

    public void setCrapsDiceManager(DiceManager crapsDice) { this.crapsDice = crapsDice; }
    public DiceManager getDiceManager() { return crapsDice; }

    public void setPoint(int testInput) { point = testInput; }
    public int getPoint(){ return point;}


    public int getPassLineBet() { return bets.get("pass"); }

    public int getDontPassLineBet() { return bets.get("don't pass"); }

    public int getPassOddsBet() { return bets.get("pass odds"); }

    public int getDontPassOddsBet() { return bets.get("don't pass odds"); }

    public int getComeBet() { return this.bets.get("come"); }

    public int getDontComeBet() { return this.bets.get("don't come"); }

    public int getFieldBet() { return this.bets.get("field"); }


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

    public void setFieldValues(ArrayList<Integer> fieldValues) { this.fieldValues = fieldValues; }
    public ArrayList<Integer> getFieldValues() { return fieldValues; }

    public void setWinLosePlaceValues(ArrayList<Integer> winLosePlaceValues) { this.winLosePlaceValues = winLosePlaceValues; }
    public ArrayList<Integer> getWinLosePlaceValues() { return winLosePlaceValues; }

    public void setDontComeBetPointOdds(HashMap<Integer, Integer> dontComeBetPointOdds) { this.dontComeBetPointOdds = dontComeBetPointOdds; }
    public HashMap<Integer, Integer> getDontComeBetPointOdds() { return dontComeBetPointOdds; }

    public DiceManager getCrapsDice() { return crapsDice; }

    public int getDiceValue() { return diceValue; }
    public void setDiceValue(int diceValue) { this.diceValue = diceValue; }

    @Override
    public void bootPlayerFromGame(Person personToBoot) { }
    @Override
    public void end() {
        this.point = 0;
        this.diceValue = 0;
        this.bets.clear();
        this.comeBetPoints.clear();
        this.dontComeBetPoints.clear();
        this.comeBetPointOdds.clear();
        this.dontComeBetPointOdds.clear();
        this.placeWinBets.clear();
        this.placeLoseBets.clear();
    }
}