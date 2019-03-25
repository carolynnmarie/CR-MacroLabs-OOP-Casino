package io.zipcoder.casino.games.diceGames;


import io.zipcoder.casino.diceAndCoins.DiceManager;
import io.zipcoder.casino.games.Game;
import io.zipcoder.casino.money.GamblingInterface;
import io.zipcoder.casino.people.Person;
import io.zipcoder.casino.Pair;

import java.util.*;

import static io.zipcoder.casino.games.diceGames.CrapsPayouts.dontPassLineDontComePayout;
import static io.zipcoder.casino.games.diceGames.CrapsPayouts.placeLoseBetPayout;
import static io.zipcoder.casino.games.diceGames.CrapsPayouts.placeWinBetPayout;

public class Craps extends Game implements DiceGameInterface, GamblingInterface {

    private DiceManager crapsDice = new DiceManager(2);
    private Person player;
    private int diceValue;
    private int point;
    private HashMap<String, Integer> bets;
    private HashMap<Integer, Integer> comeBetPoints;
    private HashMap<Integer, Integer> dontComeBetPoints;
    private HashMap<Integer, Integer> comeBetPointOdds;
    private HashMap<Integer, Integer> dontComeBetPointOdds;
    private HashMap<Integer, Integer> placeWinBets;
    private HashMap<Integer, Integer> placeLoseBets;
    private Scanner userInput = new Scanner(System.in);

    public Craps(Person player) {
        this.player = player;
        this.point = 0;
        this.diceValue = 0;
        this.comeBetPoints = new HashMap<>();
        this.dontComeBetPoints = new HashMap<>();
        this.comeBetPointOdds = new HashMap<>();
        this.dontComeBetPointOdds = new HashMap<>();
        this.placeWinBets = new HashMap<>();
        this.placeLoseBets = new HashMap<>();
        this.bets = new HashMap<>();
        String[] array = {"pass","don't pass","pass odds","don't pass odds","come","don't come","field"};
        Arrays.stream(array).forEach(e->bets.put(e,0));
    }

    public int rollDice() {
        crapsDice.rollAllDice();
        setDiceValue(crapsDice.getTotalValue());
        return crapsDice.getTotalValue();
    }
    public int checkChipAmount(Person person) {
        return person.getWallet().checkChips();
    }

    public void placeBet(Person person, int betAmount) {
        person.getWallet().removeChips(betAmount);
    }

    public void start() {
        System.out.println("Welcome to Craps!");
        do {
            if (player.getWallet().checkChips() < 5) {
                System.out.println("You don't have enough money.  You must have more than 5 chips to play. Goodbye.");
                break;
            }
            makeFirstBet();
            rollDice();
            comeOutRoll();
            if (getPoint() != 0) {
                int value = rollDice();
                placeBetPhaseTwoHandler();
                do {
                    System.out.println(phaseTwoRolls(getPoint(), value));
                } while (getPoint() != value && value != 7);
            }
            quitProgram();
        } while (quitProgram());
        end();
    }

    public void makeFirstBet(){
        do{
            System.out.println("Please place initial bet\nIf you would like to place a pass bet, type pass.  " +
                    "If you would like to place a don't pass bet, type don't pass.\n");
            String input = userInput.nextLine().toLowerCase();
            placePassComeFieldPassOddsBets(input);
        } while(getPassLineBet() == 0 && getDontPassLineBet() == 0);
    }

    public void comeOutRoll() {
        int value = getDiceValue();
        int passBet = getBets().get("pass");
        int dontPassBet = getBets().get("don't pass");
        StringBuilder builder = new StringBuilder("Time to make your first roll!\nYou rolled a " + value + ".");
        if (value == 2 || value == 3) {
            builder.append("You crapped out. Pass Line bets loose and Don't Pass Line bets win.")
                    .append(passLineBetLose(passBet))
                    .append(dontPassLineWin(dontPassBet));
        } else if (value == 7 || value == 11) {
            builder.append("You rolled a natural! Pass Line bets win and Don't Pass Line bets loose.")
                    .append(passLineBetWin(passBet))
                    .append(dontPassLineLose(dontPassBet));
        } else if (value == 12) {
            builder.append("Pass Line bets loose and Don't Pass Line bets are pushed to next round.")
                    .append(passLineBetLose(passBet));
        } else {
            builder.append("The point is now " )
                    .append(value);
            point = value;
        }
        System.out.println(builder.toString());
    }

    private String phaseTwoRolls(int point, int value) {
        StringBuilder out = new StringBuilder("Time for phase two. Roll a " + point + " and not a 7. \nYou rolled a " + value + ".");
        if (value == point) {
            out.append("You rolled the point! Pass Line wins and Don't Pass loses!")
                    .append(passLineBetWin(getPassLineBet()))
                    .append(dontPassLineLose(getDontPassLineBet()));
        } else if (value == 7) {
            out.append("You rolled a 7. Don't Pass wins and Pass Line loses!")
                    .append(dontPassLineWin(getDontPassLineBet()))
                    .append(passLineBetLose(getPassLineBet()));
        } else {
            out.append(checkBetHandler(value));
        }
        return out.toString();
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
            bet = "You lost " + dontPassLineBet + " chips.";
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
                x = userInput.nextLine().toLowerCase();
                if (x.equals("come") || x.equals("don't come") || x.equals("odds") || x.equals("field") ||
                        x.equals("place win") || x.equals("place lose")) {
                    placeBetSelection(x);
                    correct = true;
                }
            }
        } while (!correct);
    }

    public void placeBetSelection(String userAnswer) {
        if (userAnswer.equals("come") ||userAnswer.equals("don't come") || userAnswer.equals("field")) {
            placePassComeFieldPassOddsBets(userAnswer);
        } else if (userAnswer.equals("odds")) {
            System.out.println("What type of odds would you like to place?\nPass\nDon't Pass\nCome\nDon't Come");
            oddsTypeSelector(userInput.nextLine().toLowerCase());
        } else if (userAnswer.equals("place win")||userAnswer.equals("place lose")) {
            placeWinLoseBet(userAnswer);
        }
    }

    public String checkBetHandler(int value) {
        StringBuilder out = new StringBuilder();
        List<Integer> intBets =
                Arrays.asList(getComeBet(),
                        getDontComeBet(),
                        getFieldBet(),
                        getPassOddsBet(),
                        getDontPassOddsBet());
        List<String> intBetsResults =
                Arrays.asList(comeBetResult(value),
                        dontComeBetResult(value),
                        fieldBetResult(value),
                        passOddsBetResult(value),
                        dontPassOddsBetResult(value));
        for(int i = 0; i<intBets.size(); i++){
            if(intBets.get(i)!=0){
                out.append(intBetsResults.get(i));
            }
        }
        List<HashMap<Integer, Integer>> mapBets =
                Arrays.asList(getComeBetPoints(),
                        getComeBetPointOdds(),
                        getDontComeBetPoints(),
                        getDontComeBetPointOdds(),
                        getPlaceWinBets(),
                        getPlaceLoseBets());
        List<String> mapBetResults =
                Arrays.asList(comeBetPointResult(value),
                        comeBetPointOddsResult(value),
                        dontComeBetPointResult(value),
                        dontComeBetPointOddsResult(value),
                        placeWinBetResult(value),
                        placeLoseBetResult(value));
        for(int i = 0; i<mapBets.size(); i++){
            if(!mapBets.get(i).isEmpty()){
                out.append(mapBetResults.get(i));
            }
        }
        return out.toString();
    }

    public String comeBetResult(int diceValue) {
        StringBuilder builder = new StringBuilder();
        getBets().replace("come", getComeBet());
        if ((diceValue == 7 || diceValue == 11)) {
            builder.append("Come bet wins! You won ")
                    .append(getComeBet())
                    .append(" chips!");
            player.addChips(getComeBet());
        } else if ((diceValue == 2 || diceValue == 3 || diceValue == 12)) {
            builder.append("Come Bet loses. You lost ")
                    .append(getComeBet())
                    .append(" chips.");
        } else {
            builder.append("Your Come bet is now a point.");
            comeBetPoints.put(diceValue, getComeBet());
        }
        return builder.toString();
    }

    public String comeBetPointResult(int dieValue) {
        int totalValueOfPoints = 0;
        StringBuilder builder = new StringBuilder();
        if(dieValue == 7){
            for (Map.Entry<Integer, Integer> entry : getComeBetPoints().entrySet()) {
                totalValueOfPoints += entry.getValue();
            }
            builder.append("Your Come bet points lost! You lost a total of ")
                    .append(totalValueOfPoints)
                    .append(" chips.");
            getComeBetPoints().clear();
        } else if (getComeBetPoints().containsKey(dieValue)) {
            builder.append("Your Come bet on point ")
                    .append(dieValue)
                    .append(" wins! You won ")
                    .append(getComeBetPoints().get(dieValue))
                    .append(" chips!");
            player.addChips(getComeBetPoints().get(dieValue));
            getComeBetPoints().remove(dieValue);
        }
        return builder.toString();
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
            dontComeBetPoints.put(diceValue, getDontComeBet());
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
        ArrayList<Integer> fieldValues = new ArrayList<>(Arrays.asList(2, 3, 4, 9, 10, 11, 12));
        if (fieldValues.contains(diceValue)) {
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

    public String passOddsBetResult(int diceValue) {
        String bet = "";
        if (diceValue == 7) {
            bet = "Pass Line odds lose! You lost " + getPassOddsBet() + " chips";
            getBets().replace("pass", 0);
        } else if (diceValue == getPoint()) {
            for (Map.Entry<Integer, Double> entry : CrapsPayouts.passLineComeBetPayout().entrySet()) {
                if (entry.getValue().equals(diceValue)) {
                    int winnings = (int) Math.floor(getPassOddsBet() + getPassOddsBet() * entry.getValue());
                    bet = "Pass Line odds wins on " + entry.getKey() + "! You won " + winnings + " chips!";
                    player.addChips(winnings);
                }
            }
            getBets().replace("pass", 0);
        }
        return bet;
    }


    public String dontPassOddsBetResult(int diceValue) {
        String bet = "";
         if (diceValue == 7) {
            for (Map.Entry<Integer, Double> entry : dontPassLineDontComePayout().entrySet()) {
                int chips = getDontPassOddsBet() + (int) Math.floor(getDontPassOddsBet() * entry.getValue());
                if (entry.getKey().equals(getPoint())) {
                    bet = "Don't Pass odds on " + entry.getKey() + " wins! You won " + chips + " chips!";
                    player.addChips(chips);
                }
            }
            getBets().replace("don't pass", 0);
        } else if (diceValue == getPoint()) {
             bet = "Don't Pass odds lose! You lost " + getDontPassOddsBet() + " chips.";
             getBets().replace("don't pass", 0);
         }
        return bet;
    }


    public String comeBetPointOddsResult(int diceValue) {
        int totalValueOfPoints = 0;
        String bet = "";
        if (diceValue == 7) {
            for (Map.Entry<Integer, Integer> entry : comeBetPointOdds.entrySet()) {
                totalValueOfPoints += entry.getValue();
            }
            bet = "Your Come bet odds lost! You lost a total of " + totalValueOfPoints + " chips.";
            comeBetPointOdds.clear();
        } else if (comeBetPointOdds.containsKey(diceValue)) {
            HashMap<Integer, Double> payout = CrapsPayouts.passLineComeBetPayout();
            for (Map.Entry<Integer, Double> entry : payout.entrySet()) {
                if (entry.getKey().equals(diceValue)) {
                    int winnings = (int)(Math.round(comeBetPointOdds.get(diceValue) + comeBetPointOdds.get(diceValue)*entry.getValue()));
                    bet = "Your Come bet Odds on point " + diceValue + " wins! You won " + winnings + " chips!";
                    player.addChips(winnings);
                    comeBetPointOdds.remove(diceValue);
                }
            }
        }
        return bet;
    }

    public String dontComeBetPointOddsResult(int diceValue) {
        String bet = "";
        if(diceValue == 7){
            int grandTotal = 0;
            for(Map.Entry<Integer, Double> payoutEntry: dontPassLineDontComePayout().entrySet()) {
                for (Map.Entry<Integer, Integer> entry : dontComeBetPointOdds.entrySet()) {
                    Integer key = entry.getKey();
                    if (key.equals(payoutEntry.getKey())) {
                        grandTotal = entry.getValue() + (int) Math.floor(entry.getValue() * payoutEntry.getValue());
                        bet = "Your Don't Come bet Odds on point " + key + " wins! You won " + grandTotal + " chips!";
                        player.addChips(grandTotal);
                    }
                }
            }
            dontComeBetPointOdds.clear();
            bet += "You won a total of " + grandTotal + " chips on Don't Come Point Odds bets!";
        } else if (dontComeBetPointOdds.containsKey(diceValue)) {
            bet = "Your Don't Come Odds bet on point " + diceValue + " lost. You lost " + dontComeBetPointOdds.get(diceValue) + " chips!";
            getDontComeBetPoints().remove(diceValue);
        }
        return bet;
    }

    public String placeWinBetResult(int diceValue) {
        String bet = "";
        if(diceValue == 7){
            int totalValueOfPoints = 0;
            for (Map.Entry<Integer, Integer> entry: placeWinBets.entrySet()) {
                totalValueOfPoints += entry.getValue();
            }
            bet = "Your Place Win bets lost! You lost a total of " + totalValueOfPoints + " chips.";
            getPlaceWinBets().clear();
        } else if (placeWinBets.containsKey(diceValue)) {
            HashMap<Integer, Double> payout = placeWinBetPayout();
            for(Map.Entry<Integer, Double>entry: payout.entrySet()){
                if(entry.getKey().equals(diceValue)){
                    int winnings = (int)(Math.floor(placeWinBets.get(diceValue) + placeWinBets.get(diceValue)*entry.getValue()));
                    bet = "Your Place Win bet on " + entry.getKey() + "won! You won " + winnings + " chips!";
                    player.addChips(winnings);
                }
            }
            getPlaceWinBets().remove(diceValue);
        }
        return bet;
    }


    public String placeLoseBetResult(int diceValue) {
        String bet = "";
        if(diceValue == 7){
            HashMap<Integer,Double> payout = placeLoseBetPayout();
            int grandTotal = 0;
            for (Map.Entry<Integer, Integer> entry : placeLoseBets.entrySet()) {
                for(Map.Entry<Integer, Double> payoutEntry: payout.entrySet()) {
                    if (entry.getKey().equals(payoutEntry.getKey())) {
                        grandTotal = entry.getValue() + (int) Math.floor(entry.getValue()*payoutEntry.getValue());
                        bet = "Your Place Lose bet on " + entry.getKey() + " won! You won " + grandTotal + " chips!";
                        player.addChips(grandTotal);
                    }
                }
            }
            placeLoseBets.clear();
        } else if (placeLoseBets.containsKey(diceValue)) {
            bet = "Your Place Lose bet on point " + diceValue + " lost. You lost " + placeLoseBets.get(diceValue) + " chips!";
            placeLoseBets.remove(diceValue);
        }
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

    private int placePassComeFieldPassOddsBets(String input) {
        System.out.println("How much would you like to bet?");
        int bet = makeBet();
        for(Map.Entry<String, Integer> entry: bets.entrySet()) {
            if (entry.getKey().equals(input)) {
                bets.replace(entry.getKey(), bet);
                placeBet(player, bet);
            }
        }
        return bet;
    }

    private Pair<Integer, Integer> winLosePlace(){
        ArrayList<Integer> winLosePlaceValues = new ArrayList<>(Arrays.asList(4, 5, 6, 8, 9, 10));
        System.out.println("Which number would you like to be on? 4 5 6 8 9 10");
        int userAnswer = getBetInput();
        int userBet = 0;
        if (winLosePlaceValues.contains(userAnswer)) {
            System.out.println("How much would you like to bet?");
            userBet = getBetInput();
        }
        return new Pair<>(userAnswer,userBet);
    }

    private void placeWinLoseBet(String input) {
        if(input.equals("place win")){
            placeWinBets.put(winLosePlace().getKey(),winLosePlace().getValue());
        } else {
            placeLoseBets.put(winLosePlace().getKey(),winLosePlace().getValue());
        }
    }

    public void oddsTypeSelector(String userAnswer) {
        do {
            if (userAnswer.equals("pass") || userAnswer.equals("don't pass")) {
                placePassComeFieldPassOddsBets(userAnswer);
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

    private void placeComeOddsBet() {
        if (getComeBetPoints().isEmpty()) {
            System.out.println("You have no Come Bet points.");
        } else {
            System.out.println("Which point would you like to put odds on?\n" +getComeBetPoints().keySet().toString());
            int userAnswer = getBetInput();
            if (getComeBetPoints().containsKey(userAnswer)) {
                System.out.println("How much would you like to bet?");
                comeBetPointOdds.put(userAnswer,getBetInput());
            } else { System.out.println("That option is not available."); }
        }
    }

    private void placeDontComeOddsBet() {
        if (getDontComeBetPoints().isEmpty()) {
            System.out.println("You have no Don't Come Bet points.");
        } else {
            System.out.println("Which point would you like to put odds on?" + getDontComeBetPoints().keySet().toString());
            int userAnswer = getBetInput();
            if (getDontComeBetPoints().containsKey(userAnswer)) {
                System.out.println("How much would you like to bet?");
                int userBet = getBetInput();
                dontComeBetPointOdds.put(userAnswer,userBet);
            } else { System.out.println("Option not available."); }
        }
    }

    private int getBetInput() {
        int betInput = 0;
        try {
            do {
                betInput = userInput.nextInt();
                userInput.nextLine();
                if (betInput < 5) {
                    System.out.println("The minimum bet is " + 5 + ". Try again.");
                }
            } while(betInput < 5);
            return betInput;
        } catch (InputMismatchException e) {
            System.out.println("That is not a number. Setting bet to 5.");
            userInput.nextLine();
            return 5;
        }
    }

    private boolean quitProgram() {
        System.out.println("Would you like to keep playing? Yes or no.");
        String userAnswer = "";
        do {
            userAnswer = userInput.nextLine().toLowerCase();
            if (userAnswer.equals("yes")) {
                System.out.println("New round starting!");
            } else if (userAnswer.equals("no")) {
                System.out.println("Thanks for playing!");
                return false;
            }
        }while (!(userAnswer.equals("yes")) && !(userAnswer.equals("no")));
        return true;
    }

    @Override
    public boolean bootPlayerFromGame(Person personToBoot) { return false; }
    @Override
    public void end() {
        setPoint(0);
        setDiceValue(0);
        bets.clear();
        comeBetPoints.clear();
        dontComeBetPoints.clear();
        comeBetPointOdds.clear();
        dontComeBetPointOdds.clear();
        placeWinBets.clear();
        placeLoseBets.clear();
    }

    public int getAnte() { return 5; }

    public int checkPot() { return 0; }

    public void setPlayer(Person player) { this.player = player; }
    public Person getPlayer() { return player; }

    public int getDiceValue() { return diceValue; }
    public void setDiceValue(int diceValue) { this.diceValue = diceValue; }

    public void setPoint(int testInput) { point = testInput; }
    public int getPoint(){ return point;}

    public HashMap<String, Integer> getBets(){
        return this.bets;
    }

    public int getPassLineBet() { return bets.get("pass"); }

    public int getDontPassLineBet() { return bets.get("don't pass"); }

    public int getPassOddsBet() { return bets.get("pass odds"); }

    public int getDontPassOddsBet() { return bets.get("don't pass odds"); }

    public int getComeBet() { return this.bets.get("come"); }

    public int getDontComeBet() { return this.bets.get("don't come"); }

    public int getFieldBet() { return this.bets.get("field"); }

    public HashMap<Integer, Integer> getComeBetPoints(){ return comeBetPoints;}

    public HashMap<Integer, Integer> getComeBetPointOdds() { return comeBetPointOdds; }

    public HashMap<Integer, Integer> getDontComeBetPoints() { return dontComeBetPoints; }

    public HashMap<Integer, Integer> getDontComeBetPointOdds() { return dontComeBetPointOdds; }

    public void setPlaceLoseBets(HashMap<Integer, Integer> placeLoseBets) { this.placeLoseBets = placeLoseBets; }

    public HashMap<Integer, Integer> getPlaceLoseBets() { return placeLoseBets; }

    public void setPlaceWinBets(HashMap<Integer, Integer> placeWinBets) { this.placeWinBets = placeWinBets; }

    public HashMap<Integer, Integer> getPlaceWinBets() { return placeWinBets; }




}