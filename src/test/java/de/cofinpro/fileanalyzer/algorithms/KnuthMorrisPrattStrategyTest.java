package de.cofinpro.fileanalyzer.algorithms;

import org.junit.jupiter.api.BeforeEach;
import org.junit.jupiter.params.ParameterizedTest;
import org.junit.jupiter.params.provider.CsvSource;

import java.io.IOException;
import java.nio.file.Files;
import java.nio.file.Path;

import static org.junit.jupiter.api.Assertions.*;


class KnuthMorrisPrattStrategyTest {

    KnuthMorrisPrattStrategy kmp;

    @BeforeEach
    void setUp() {
        kmp = new KnuthMorrisPrattStrategy();
    }

    @ParameterizedTest
    @CsvSource({
            "src/test/resources/jetbrains-academy-certificate-12-java-backend-developer.pdf, %PDF-",
            "src/test/resources/setup-instructions.pdf, %PDF-",
            "src/test/resources/setup-instructions.pdf, Gradle",
            "src/test/resources/test.txt, SEARCH",
            "src/test/resources/test.txt, 789"
    })
    void whenFileWithGivenString_KmpContainsIsTrue(String filepath, String toSearch) throws IOException {
        byte[] fileContent = Files.readAllBytes(Path.of(filepath));
        assertTrue(kmp.bufferContainsSearchText(fileContent, toSearch));
    }

    @ParameterizedTest
    @CsvSource({
            "src/test/resources/jetbrains-academy-certificate-12-java-backend-developer.pdf, %PDXXF-",
            "src/test/resources/setup-instructions.pdf, %PXSF-",
            "src/test/resources/setup-instructions.pdf, Graaaadle",
            "src/test/resources/test.txt, SEACH",
            "src/test/resources/test.txt, 7890"
    })
    void whenFileWithNotContainedString_KmpContainsIsFalse(String filepath, String toSearch) throws IOException {
        byte[] fileContent = Files.readAllBytes(Path.of(filepath));
        assertFalse(kmp.bufferContainsSearchText(fileContent, toSearch));
    }
}