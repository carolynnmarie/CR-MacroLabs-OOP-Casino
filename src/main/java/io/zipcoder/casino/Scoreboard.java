package io.zipcoder.casino;

import io.zipcoder.casino.People.Person;

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

    public Scoreboard(LinkedHashMap<Person, ArrayList<Integer>> scoreboard) {
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


    public Integer getCurrentScore(Person person) {
        Integer x = 0;
        if(scoreboard.get(person).size()>0){
            x = scoreboard.get(person).get(scoreboard.values().size()-1);
        }
        return x;
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
        StringBuilder builder = new StringBuilder("SCOREBOARD\n----------\n")
                .append(String.format("%-12s | %-21s | Total Won\n", "Name", "Games"))
                .append("-------------------------------------------------\n");
        for(Map.Entry<Person, ArrayList<Integer>> entry: scoreboard.entrySet()) {
            builder.append(String.format("%-12s |",entry.getKey().getName()));
            int total = 0;
            StringBuilder scores = new StringBuilder();
            for(Integer val: entry.getValue()){
                scores.append(val.toString())
                        .append(", ");
                total += val;
            }
            builder.append(String.format(" %-21s | ",scores.toString()))
                    .append(total)
                    .append("\n");
        }
        return builder.toString();
    }

}
