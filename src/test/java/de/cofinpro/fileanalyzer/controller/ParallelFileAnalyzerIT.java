package de.cofinpro.fileanalyzer.controller;

import de.cofinpro.fileanalyzer.config.MessageResourceBundle;
import de.cofinpro.fileanalyzer.io.ConsolePrinter;
import org.junit.jupiter.api.BeforeEach;
import org.junit.jupiter.api.Test;
import org.junit.jupiter.api.extension.ExtendWith;
import org.mockito.Spy;
import org.mockito.junit.jupiter.MockitoExtension;

import static org.mockito.ArgumentMatchers.anyString;
import static org.mockito.Mockito.never;
import static org.mockito.Mockito.verify;

@ExtendWith(MockitoExtension.class)
class ParallelFileAnalyzerIT {

    static final String DIR_PATH = "src/test/resources";
    static final String PATTERNS_DB_PATH = "src/test/resources/patterns.db";

    @Spy
    ConsolePrinter printer;

    ParallelFileAnalyzer parallelFileAnalyzer;

    @BeforeEach
    void setUp() {
        parallelFileAnalyzer = new ParallelFileAnalyzer(printer);
    }

    @Test
    void whenTestResourcesDirAndPatternsDb_ThenTypesCorrect() {
        parallelFileAnalyzer.run(new String[]{DIR_PATH, PATTERNS_DB_PATH});
        verify(printer).printInfo("jetbrains-academy-certificate-12-java-backend-developer.pdf: PDF document");
        verify(printer).printInfo("setup-instructions.pdf: PDF document");
        verify(printer).printInfo("patterns.db: ISO Media JPEG 2000");
        verify(printer).printInfo("test.txt: " + MessageResourceBundle.UNKNOWN_FILE_TYPE_MSG);
        verify(printer, never()).printError(anyString());
    }
}