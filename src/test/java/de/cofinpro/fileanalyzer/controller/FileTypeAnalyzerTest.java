package de.cofinpro.fileanalyzer.controller;

import de.cofinpro.fileanalyzer.config.MessageResourceBundle;
import de.cofinpro.fileanalyzer.io.ConsolePrinter;
import org.junit.jupiter.api.BeforeEach;
import org.junit.jupiter.api.Test;
import org.junit.jupiter.api.extension.ExtendWith;
import org.junit.jupiter.params.ParameterizedTest;
import org.junit.jupiter.params.provider.CsvSource;
import org.mockito.Mock;
import org.mockito.junit.jupiter.MockitoExtension;

import static org.mockito.ArgumentMatchers.anyString;
import static org.mockito.Mockito.never;
import static org.mockito.Mockito.verify;

@ExtendWith(MockitoExtension.class)
class FileTypeAnalyzerTest {

    String FOUND_MSG = "FOUND";

    @Mock
    ConsolePrinter printer;

    FileTypeAnalyzer fileTypeAnalyzer;

    @BeforeEach
    void setup() {
        fileTypeAnalyzer = new FileTypeAnalyzer(printer);
    }

    @ParameterizedTest
    @CsvSource({
            "src/test/resources/jetbrains-academy-certificate-12-java-backend-developer.pdf, %PDF-",
            "src/test/resources/setup-instructions.pdf, %PDF-",
            "src/test/resources/setup-instructions.pdf, Gradle"
    })
    void whenFileWithGivenPatternAnalyze_FoundMessagePrinted(String filepath, String toSearch) {
        String[] args = new String[] {"--naive", filepath, toSearch, FOUND_MSG};
        fileTypeAnalyzer.analyze(args);
        verify(printer).printInfo(FOUND_MSG);
        verify(printer, never()).printError(anyString());
    }

    @ParameterizedTest
    @CsvSource({
            "src/test/resources/jetbrains-academy-certificate-12-java-backend-developer.pdf, blahblah",
            "src/test/resources/setup-instructions.pdf, xyxyxy",
            "src/test/resources/setup-instructions.pdf, its_not_in"
    })
    void whenFileWithoutGivenPatternAnalyze_UnknownPrinted(String filepath, String toSearch) {
        String[] args = new String[] {"--naive", filepath, toSearch, FOUND_MSG};
        fileTypeAnalyzer.analyze(args);
        verify(printer).printInfo(MessageResourceBundle.UNKNOWN_FILE_TYPE_MSG);
        verify(printer, never()).printError(anyString());
    }

    @Test
    void whenFileNotExist_IOError() {
        String[] args = new String[] {"--naive", "src/test/resources/notthere", "toSearch", FOUND_MSG};
        fileTypeAnalyzer.analyze(args);
        verify(printer).printError(anyString());
    }
}