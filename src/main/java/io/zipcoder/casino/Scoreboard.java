package io.zipcoder.casino;

import io.zipcoder.casino.people.Person;

import java.util.*;



public class Scoreboard {

    private LinkedHashMap<Person, ArrayList<Integer>> scoreboard;


    public Scoreboard() {
        this.scoreboard = new LinkedHashMap<>();
    }

    public Scoreboard(Person... people) {
        this.scoreboard = new LinkedHashMap<>();
        for(int i = 0; i<people.length; i++) {
            scoreboard.put(people[i],new ArrayList<>());
        }
    }

    public Scoreboard(Person person){
        this.scoreboard = new LinkedHashMap<>();
        scoreboard.put(person,new ArrayList<>());
    }

    public Scoreboard(LinkedHashMap<Person, ArrayList<Integer>> scoreboard) {
        this.scoreboard = scoreboard;
    }

    public void setScoreboard(LinkedHashMap<Person, ArrayList<Integer>> scoreboard) {
        this.scoreboard = scoreboard;
    }

    public LinkedHashMap<Person, ArrayList<Integer>> getScoreboard() {
        return scoreboard;
    }

    public Person[] getPlayers() {
        Set<Person> players = scoreboard.keySet();
        Person[] playerArray = players.toArray(new Person[players.size()]);
        return playerArray;
    }

    public void addPlayer(Person person) {
        this.scoreboard.put(person, new ArrayList<>());
    }

    public void removePlayer(Person person) {
        this.scoreboard.remove(person);
    }

    public void updateScore(Person person, Integer newScore){
        for(Map.Entry<Person, ArrayList<Integer>> entry: this.scoreboard.entrySet()) {
            if(scoreboard.containsKey(person) && person.equals(entry.getKey())) {
                if(entry.getValue().size()>0){
                    entry.getValue().remove(entry.getValue().size()-1);
                }
                entry.getValue().add(newScore);
                scoreboard.put(person, entry.getValue());
            }
        }
    }

    public void addScore(Person person, Integer score){
        for(Map.Entry<Person, ArrayList<Integer>> entry: this.scoreboard.entrySet()) {
            if(scoreboard.containsKey(person) && person.equals(entry.getKey())) {
                entry.getValue().add(score);
                scoreboard.put(person, entry.getValue());
            }
        }
    }

    public void resetScoreboardForSamePlayers() {
        for(Map.Entry<Person, ArrayList<Integer>> entry: this.scoreboard.entrySet()) {
            if(entry.getKey().equals(entry.getKey())) {
                entry.getValue().clear();
            }
        }
    }

    public void clearScoreboardOfPlayersAndScores() {
        this.scoreboard.clear();
    }

    //single player score at request of player during game
    public Integer getScore(Person person) {
        return scoreboard.get(person).get(scoreboard.values().size()-1);
    }


    public String displayScoreboardSingleGame() {
        String display = String.format("%-15s | %-10s\n", "Name", "Score");
        display += "------------------------\n";
        for(Map.Entry<Person, ArrayList<Integer>> entry: scoreboard.entrySet()) {
            String name = entry.getKey().getName();
            String score = entry.getValue().get(entry.getValue().size()-1).toString();
            display += String.format("%-15s | %-10s\n", name, score);
        }
        return display;
    }

    public String displayRunningGameTally() {
        LinkedHashMap<Person, ArrayList<Integer>> runningTally = new LinkedHashMap<Person, ArrayList<Integer>>();
        String tally = String.format("%-15s | %-10s \n", "Name", "Games");
        tally += "---------------------------------------\n";
        StringBuilder builder = new StringBuilder("SCOREBOARD\n----------\n");
        for(Map.Entry<Person, ArrayList<Integer>> entry: scoreboard.entrySet()) {
            builder.append(entry.getKey().getName())
                    .append(":    ");
            for(Integer val: entry.getValue()){
                builder.append(val.toString())
                        .append(",  ");
            }
            builder.append("\n");
//            for(Map.Entry<Person, ArrayList<Integer>> secondEntry: runningTally.entrySet()) {
//                if(!(runningTally.containsKey(entry.getKey()))) {
//                    ArrayList<Integer> tallyList = new ArrayList<Integer>(Arrays.asList(entry.getValue()));
//                    runningTally.put(entry.getKey(), tallyList);
//                }
//                if((runningTally.containsKey(entry.getKey()))) {
//                    secondEntry.getValue().add(entry.getValue());
//                }
//            }
        }
//        for(Map.Entry<Person, ArrayList<Integer>> secondEntry: runningTally.entrySet()) {
//            String key = secondEntry.getKey().getName();
//            tally += String.format("%-15s | \n", key);
//        }
        return builder.toString();
    }



}
