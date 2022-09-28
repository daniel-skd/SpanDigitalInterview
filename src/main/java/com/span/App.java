package com.span;

import java.io.BufferedReader;
import java.io.FileReader;
import java.io.IOException;
import java.util.*;
import java.util.stream.Collectors;
import java.util.stream.Stream;

public class App {

    private static Map<String, Integer> leagueMap = new LinkedHashMap<>();

    public static void main(String[] args) {
        /*if (args.length == 0) {
            System.out.println("File name not specified.");
            System.exit(1);
        }
        System.out.println(args[0]);
        */
        //readFile(args[0]);
        readFile("C:/Users/h/Desktop/nuevo.txt");
    }

    public static void readFile(String filePath) {
        BufferedReader reader = null;
        String currentLine;
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
                    reader.close();
            } catch (IOException ex) {
                ex.printStackTrace();
            }
        }
        sortMap();
    }

    public static void sortMap() {
        LinkedHashMap<String, Integer> reverseSortedMap = new LinkedHashMap<>();

        //Use Comparator.reverseOrder() for reverse ordering
        leagueMap.entrySet()
                .stream()
                .sorted(Map.Entry.comparingByValue(Comparator.reverseOrder()))
                .forEachOrdered(x -> reverseSortedMap.put(x.getKey(), x.getValue()));

        reverseSortedMap.forEach((k, v) -> System.out.println((k + ", " + v + " points")));
    }

    public static void fillMap(String currentLine) {
        List<String> splitList = Arrays.asList(Arrays.stream(currentLine.split(","))
                .map(String::trim)
                .toArray(String[]::new));

        List<String[]> splitList2 = new LinkedList<>();
        splitList2.addAll(splitList.stream()
                .map(s -> s.split("(?<=\\D)(?=\\d)"))
                .collect(Collectors.toList()));

        for (int i = 0; i < 2; i++) {
            int points = validateScore(Integer.valueOf(splitList2.get(0)[1]), Integer.valueOf(splitList2.get(1)[1]));
            if (leagueMap.get(splitList2.get(i)[0].trim()) != null) {
                points += leagueMap.get(splitList2.get(i)[0].trim());
            }
            leagueMap.put(splitList2.get(i)[0].trim(), points);
        }
    }

    public static Integer validateScore(Integer a, Integer b) {
        if (a > b) {
            return 3;
        }
        if (a == b) {
            return 1;
        }
        return 0;
    }
}
