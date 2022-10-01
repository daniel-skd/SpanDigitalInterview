package com.span;

import org.junit.jupiter.api.Test;

import java.util.Arrays;
import java.util.List;
import java.util.stream.Collectors;

import static com.span.SpanScoreboard.splitMatches;
import static org.junit.jupiter.api.Assertions.assertEquals;
import static org.junit.jupiter.api.Assertions.assertArrayEquals;

public class SpanScoreboardTest {

    @Test
    public void test_whenSplit_thenCorrect() {
        String s = "Lions 3, Snakes 3";
        String[] expected = new String[] { "Lions 3", " Snakes 3" };

        assertArrayEquals(expected, s.split(","));
    }

    @Test
    public void test_splitToList() {
        String s = " Lions 3, Snakes 3";
        String[] array = new String[] { "Lions 3", " Snakes 3" };
        List<String> expected = Arrays.asList(array).stream()
                .map(String::trim)
                .collect(Collectors.toList());
        List<String> actual = Arrays.asList(s.split(",")).stream()
                .map(String::trim)
                .collect(Collectors.toList());

        assertEquals(expected,actual);
    }

    @Test
    public void test_shouldReturnParsedTextFromInputFileStream() throws Exception {
        String text = "Lions 3, Snakes 3";

        assertEquals(Arrays.asList("Lions 3", "Snakes 3"), splitMatches(text));
    }
}
