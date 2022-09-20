package de.cofinpro.fileanalyzer.algorithms;

import de.cofinpro.fileanalyzer.model.Patterns;
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
        Patterns patterns = new Patterns("src/test/resources/patterns.db");
        kmp.setSearchPatterns(patterns.getPatternsPriorityDescending());
    }

    @ParameterizedTest
    @CsvSource({
            "src/test/resources/patterns.db,ISO Media JPEG 2000",
            "src/test/resources/setup-instructions.pdf,PDF document",
            "src/test/resources/test.txt,''"
    })
    void whenFileWithGivenString_KmpContainsIsTrue(String filepath, String foundMessage) throws IOException {
        byte[] fileContent = Files.readAllBytes(Path.of(filepath));
        kmp.fileTypeDetected(fileContent);
        assertEquals(foundMessage, kmp.getFoundMessage());
    }
}