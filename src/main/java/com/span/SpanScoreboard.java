package com.span;

import com.span.model.Team;
import org.slf4j.Logger;
import org.slf4j.LoggerFactory;

import java.io.*;
import java.util.*;
import java.util.stream.Collectors;

import static java.util.stream.Collectors.*;

public class SpanScoreboard {

    private static Logger LOGGER = LoggerFactory.getLogger(SpanScoreboard.class);
    final private static List<Team> SCOREBOARD = new ArrayList<>();

    public static void main(String[] args) {
        //We receive as input the filepath from the user where the Games are
        Scanner scanner = new Scanner(System.in);
        System.out.println("Write the filepath of the file with the games, please: ");

        String filePath = scanner.nextLine();

        readFile(filePath);
        List<Team> orderedTeam = orderScoreboard();

        if (writeScoreboard(orderedTeam)){
            System.out.println("The file: \"Span_League_Ranking.txt\" has been written with the according results.");
        } else {
            System.out.println("There was an error while trying to write the file: \"Span_League_Ranking.txt\".");
        }
    }

    public static void readFile(String filePath) {
        BufferedReader reader = null;
        String currentLine;

        //Read the file line by line so that it can be formatted on the fillMap() method
        try {
            reader = new BufferedReader(new FileReader(filePath));
            while ((currentLine = reader.readLine()) != null) {
                List<String> matchList = splitMatches(currentLine);
                buildScoreboard(matchList);
            }
            reader.close();
        } catch (IOException io) {
            LOGGER.error("An error occurred trying to open the file: " + io.getMessage());
            io.printStackTrace();
        } finally {
            try {
                //finally, we close the file
                if (reader != null)
                    reader.close();
            } catch (IOException ex) {
                LOGGER.error("An error occurred trying to open the file: " + ex.getMessage());
                ex.printStackTrace();
            }
        }
    }

    public static List<Team> orderScoreboard() {

        List<Team> finalList = new ArrayList<>();
        Map<String, Integer> map = SCOREBOARD.stream()
                .collect(groupingBy(Team::getName, summingInt(Team::getScore)));

        for (Map.Entry<String, Integer> entry : map.entrySet()) {
            Team team = new Team();
            team.setName(entry.getKey());
            team.setScore(entry.getValue());

            finalList.add(team);
        }

        return finalList.stream()
                .sorted(Comparator.comparing(Team::getScore).reversed().thenComparing(Team::getName))
                .collect(Collectors.toList());
    }

    public static boolean writeScoreboard(List<Team> orderedTeams) {
        String fileName = "Span_League_Ranking.txt";
        int c = 1;
        try {
            BufferedWriter writer = new BufferedWriter(new FileWriter(fileName));

            for (int i=0; i<orderedTeams.size(); i++) {
                if((i != 0) && (orderedTeams.get(i-1).getScore() != orderedTeams.get(i).getScore()))
                    c++;

                writer.write(c + ". " + orderedTeams.get(i).getName() + ", " + orderedTeams.get(i).getScore() + " points \n");
            }
            writer.close();
            return true;
        } catch (IOException io) {
            LOGGER.error("Error while trying to write file");
            io.printStackTrace();
            return false;
        }
    }

    public static List<String> splitMatches(String currentLine) {

        return Arrays.asList(Arrays.stream(currentLine.split(","))
                .map(String::trim)
                .toArray(String[]::new));
    }

    public static void buildScoreboard(List<String> splitList) {

        Team team1 = new Team();
        Team team2 = new Team();
        String name1 = splitList.get(0).substring(0, splitList.get(0).length() - 2);
        String name2 = splitList.get(1).substring(0, splitList.get(1).length() - 2);
        int score1 = Integer.valueOf(splitList.get(0).substring(splitList.get(0).length() - 1));
        int score2 = Integer.valueOf(splitList.get(1).substring(splitList.get(1).length() - 1));

        team1.setName(name1);
        team1.setScore(score1);
        team2.setName(name2);
        team2.setScore(score2);

        validateScore(team1, team2);
    }


    public static void validateScore(Team team1, Team team2) {

        if (team1.getScore() > team2.getScore()) {
            team1.setScore(3);
            team2.setScore(Integer.valueOf(0));
        }
        if (team1.getScore() == team2.getScore()) {
            team1.setScore(1);
            team2.setScore(1);
        }
        SCOREBOARD.add(team1);
        SCOREBOARD.add(team2);
    }
}
