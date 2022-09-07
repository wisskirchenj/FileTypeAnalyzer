package de.cofinpro.fileanalyzer.algorithms;

import org.junit.jupiter.params.ParameterizedTest;
import org.junit.jupiter.params.provider.Arguments;
import org.junit.jupiter.params.provider.MethodSource;

import java.util.stream.Stream;

import static org.junit.jupiter.api.Assertions.*;

class PrefixFinderTest {

    PrefixFinder finder;

    static Stream<Arguments> getPrefix() {
        return Stream.of(
                Arguments.of("ACCABACCAC", new int[] {0, 0, 0, 1, 0, 1, 2, 3, 4, 2}),
                Arguments.of("ACCABACCACCACA", new int[] {0, 0, 0, 1, 0, 1, 2, 3, 4, 2, 3, 4, 2, 1}),
                Arguments.of("ACACA", new int[] {0, 0, 1, 2, 3}),
                Arguments.of("AAAAA", new int[] {0, 1, 2, 3, 4}),
                Arguments.of("ABCDEFGHIJK", new int[] {0, 0, 0, 0, 0, 0, 0, 0, 0, 0, 0}),
                Arguments.of("ABCDEFGHIJKA", new int[] {0, 0, 0, 0, 0, 0, 0, 0, 0, 0, 0, 1})
        );
    }

    @ParameterizedTest
    @MethodSource()
    void getPrefix(String text, int[] prefixElements) {
        finder = new PrefixFinder(text);
        assertEquals(text.length(), prefixElements.length);
        assertArrayEquals(prefixElements, finder.getPrefix());
    }
}