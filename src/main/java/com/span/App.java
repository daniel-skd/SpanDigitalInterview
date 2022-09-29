package com.span;

import java.io.*;
import java.util.*;
import java.util.stream.Collectors;

public class App {

    final private static Map<String, Integer> leagueMap = new LinkedHashMap<>();

    public static void main(String[] args) {
        //We receive as input the filepath from the user where the Games are
        Scanner scanner = new Scanner(System.in);
        System.out.println("Write the filepath of the file with the games, please: ");

        String filePath = scanner.nextLine();

        readFile(filePath);
    }

    public static void readFile(String filePath) {
        BufferedReader reader = null;
        String currentLine;

        //Read the file line by line so that it can be formatted on the fillMap() method
        try {
            reader = new BufferedReader(new FileReader(filePath));
            while ((currentLine = reader.readLine()) != null) {
                fillMap(currentLine);
            }
            reader.close();
        } catch (IOException io) {
            System.out.println("An error occurred trying to open the file: " + io.getMessage());
            io.printStackTrace();
        } finally {
            try {
                if (reader != null)
                    reader.close(); //finally, we close the file
            } catch (IOException ex) {
                System.out.println("An error occurred trying to open the file: " + ex.getMessage());
                ex.printStackTrace();
            }
        }
        sortMap();
    }

    /*
     * This method will write the result in the desired form on a file
     */
    public static void writeResults(LinkedHashMap<String, Integer> map){
        String fileName = "Span_League_Ranking.txt";
        int i = 1;
        try {
            BufferedWriter writer = new BufferedWriter(new FileWriter(fileName));

            for (Map.Entry<String,Integer> entry : map.entrySet()) {
                writer.write(i + ". " + entry.getKey() + ", " + entry.getValue() + " points \n");
                i++;
            }

            writer.close();
        } catch (IOException io) {
            System.out.println("Error while trying to write file");
            io.printStackTrace();
        }
    }

    /*
     * This method will work for this:
     * Sort the leagueMap so that we can have a Descending Order for the Games
     */
    public static void sortMap() {
        LinkedHashMap<String, Integer> reverseSortedMap = new LinkedHashMap<>();

        //Use Comparator.reverseOrder() for reverse ordering
        leagueMap.entrySet()
                .stream()
                .sorted(Map.Entry.comparingByValue(Comparator.reverseOrder()))
                .forEachOrdered(x -> reverseSortedMap.put(x.getKey(), x.getValue()));

        writeResults(reverseSortedMap);
    }


    /*
     * This method will fill the Keys and Values on the map splitting by comma the results into a List
     * then, each record of tha list will be split again by Numbers so that the teamName and their score can be separated
     * Finally, the for cycle will work to calculate the points from each game and filling the Map with
     * the Teams Names as the Key
     */
    public static void fillMap(String currentLine) {
        List<String> splitList = Arrays.asList(Arrays.stream(currentLine.split(","))
                .map(String::trim)
                .toArray(String[]::new));

        List<String[]> splitList2 = splitList.stream()
                .map(s -> s.split("(?<=\\D)(?=\\d)"))
                .collect(Collectors.toList());

        for (int i = 0; i < 2; i++) {
            int points = validateScore(Integer.valueOf(splitList2.get(0)[1]), Integer.valueOf(splitList2.get(1)[1]));
            if (leagueMap.get(splitList2.get(i)[0].trim()) != null) {
                points += leagueMap.get(splitList2.get(i)[0].trim());
            }
            leagueMap.put(splitList2.get(i)[0].trim(), points);
        }
    }

    /*
     * Validator that will work to get the points earned on each Game following these rules:
     * Winning gives 3 points
     * Draw gives 1 point
     * Loosing gives 0 points
     */
    public static Integer validateScore(Integer a, Integer b) {
        if (a > b) {
            return 3;
        }
        if (a.equals(b)) {
            return 1;
        }
        return 0;
    }
}
