# SpanDigitalInterview
### App Description
This app will help in the calculating the ranking table for a league, taking the input from a file specified by the user, the user will provide the filepath to it.  
 + Important Note 1: The jar file can be executed with this command: java insertPathOfTheJar\SpanDigitalInterview-1.0-SNAPSHOT.jar  
 + Important Note 2: The formatted text file will be generated on the folder where jar is executed with this name: "Span_League_Ranking.txt"
### Input/output
The input and output will be text.
The input contains results of games, one per line. See “Sample input” for details.
The output should be ordered from most to least points, following the format specified in
“Expected output”.
### The rules
In this league, a draw (tie) is worth 1 point and a win is worth 3 points. A loss is worth 0 points.
If two or more teams have the same number of points, they should have the same rank and be
printed in alphabetical order (as in the tie for 3rd place in the sample data).
### Sample input:
 + Lions 3, Snakes 3
 + Tarantulas 1, FC Awesome 0
 + Lions 1, FC Awesome 1
 + Tarantulas 3, Snakes 1
 + Lions 4, Grouches 0
### Expected output:
 1. Tarantulas, 6 pts
 2. Lions, 5 pts
 3. FC Awesome, 1 pt
 3. Snakes, 1 pt
 5. Grouches, 0 pt