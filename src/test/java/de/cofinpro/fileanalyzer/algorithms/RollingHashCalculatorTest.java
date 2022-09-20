package de.cofinpro.fileanalyzer.algorithms;

import org.junit.jupiter.api.Test;
import org.junit.jupiter.params.ParameterizedTest;
import org.junit.jupiter.params.provider.CsvSource;

import static org.junit.jupiter.api.Assertions.*;

class RollingHashCalculatorTest {

    RollingHashCalculator rollingHashCalculator;

    @Test
    void correctStaticSetupOfBasePow() {
        int[] expected = new int[] {1, 17, 50, 133, 110, 197, 3, 51, 150, 160, 91, 113, 9, 153, 211, 2, 34, 100, 27, 220,
                155, 6, 102, 61, 81, 182, 226, 18, 67, 183, 4, 68, 200, 54, 201, 71, 12, 204, 122, 162, 125, 213, 36, 134,
                127, 8, 136, 161, 108, 163, 142, 24, 169, 5, 85, 11, 187, 72, 29, 15, 16, 33, 83, 216, 87, 45, 48, 99, 10,
                170, 22, 135, 144, 58, 30, 32, 66, 166, 193, 174, 90, 96, 198, 20, 101, 44, 31, 49, 116, 60, 64, 132, 93,
                147, 109, 180, 192, 157, 40, 202, 88, 62, 98, 232, 120, 128, 25, 186, 55, 218, 121, 145, 75, 80, 165, 176,
                124, 196, 225, 1, 17, 50, 133, 110, 197, 3, 51, 150};
        assertArrayEquals(expected, RollingHashCalculator.BASE_POW);
    }

    @ParameterizedTest
    @CsvSource({
            "abr, 177",
            "bra, 4",
            "rak, 47",
            "abrbr, 114",
            "%PDF-, 225"
    })
    void whenStringGiven_GetCurrentHashWorks(String string, int expectedHash) {
        rollingHashCalculator = new RollingHashCalculator(string.getBytes());
        assertEquals(expectedHash, rollingHashCalculator.getCurrentHash());
    }

    @Test
    void whenRollingWithLetter_GetRollingHashWorks() {
        byte[] text = "abrakr".getBytes();
        rollingHashCalculator = new RollingHashCalculator("abr".getBytes());
        assertEquals(177, rollingHashCalculator.getCurrentHash());
        assertEquals(177, rollingHashCalculator.getRollingHash(text, 0));
        assertEquals(4, rollingHashCalculator.getRollingHash(text, 1));
        assertEquals(47, rollingHashCalculator.getRollingHash(text, 2));
        assertEquals(91, rollingHashCalculator.getRollingHash(text, 3));
    }

}