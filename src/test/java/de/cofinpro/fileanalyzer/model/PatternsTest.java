package de.cofinpro.fileanalyzer.model;

import org.junit.jupiter.api.Test;

import static org.junit.jupiter.api.Assertions.*;

class PatternsTest {

    final static String patternsDbPath = "src/test/resources/patterns.db";

    Patterns patterns;

    @Test
    void whenPatternsDbGiven_getPatternsPriorityDescendingHasContents() {
        patterns = new Patterns(patternsDbPath);
        var patternList = patterns.getPatternsPriorityDescending();
        assertEquals(12, patternList.size());
        assertEquals(9, patternList.get(0).priority());
        assertEquals(9, patternList.get(1).priority());
        assertEquals(8, patternList.get(2).priority());
        assertEquals("-----BEGIN\\ CERTIFICATE-----", patternList.get(2).searchText());
        assertEquals("PEM certificate", patternList.get(2).foundMessage());
        assertEquals(4, patternList.get(10).priority());
        assertEquals("PK", patternList.get(10).searchText());
        assertEquals("Zip archive", patternList.get(10).foundMessage());
        assertEquals(2, patternList.get(11).priority());
        assertEquals("pmview", patternList.get(11).searchText());
    }

    @Test
    void whenPatternsDbNonExistPathGiven_getPatternsThrows() {
        assertThrows(IllegalArgumentException.class, () -> new Patterns("src/notthere"));
    }

    @Test
    void whenInvalidPatternsDbPathGiven_getPatternsThrows() {
        assertThrows(IllegalArgumentException.class, () -> new Patterns("src/test/resources/test.txt"));
    }
}