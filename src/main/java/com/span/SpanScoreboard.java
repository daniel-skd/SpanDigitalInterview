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
        // Method to get an ordered list of teams by Score
        List<Team> orderedTeam = orderScoreboard();

        if (writeScoreboard(orderedTeam)){
            System.out.println("The file: \"Span_League_Ranking.txt\" has been written with the according results.");
        } else {
            System.out.println("There was an error while trying to write the file: \"Span_League_Ranking.txt\".");
        }
    }

    /*
     * Method that will read the file, line by line, calling the splitMatches and the buildScoreboard
     * so that SCOREBOARD List can be filled with all the necessary values.
     */
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

    /*
     * With the help of a Map, this method will group the results by Team Name and add the scores
     * of each team with the same name, then with that same map, a Final Ordered List of Teams
     * will be created and will work as the final source to write the results on a file.
     */
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

    /*
     * This is the final method which will write the result on a new file called "Span_League_Ranking.txt".
     * The validation on the variable "c" is to know when a team has a draw with another on the final scores
     * then both will have the same final place.
     */
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

    /*
     * Simple method to split and trim the line being read by the fileReader which will return a list of strings
     * with the following format each record: "name score".
     */
    public static List<String> splitMatches(String currentLine) {

        return Arrays.asList(Arrays.stream(currentLine.split(","))
                .map(String::trim)
                .toArray(String[]::new));
    }

    /*
     * This method will get the split values that we obtain from the previous method to get
     * the attributes of both Teams (name and score) and then will call the validator to know
     * how many points are going to be assigned to each one.
     */
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

    /*
     * Validator that will work to get the points earned on each Game following these rules:
     * Winning gives 3 points and since there's a winner we also have a looser who receives 0 points
     * Draw gives 1 point to both teams.
     */
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
