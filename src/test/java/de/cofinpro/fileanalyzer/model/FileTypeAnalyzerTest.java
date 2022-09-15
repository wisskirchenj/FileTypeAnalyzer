package de.cofinpro.fileanalyzer.model;

import de.cofinpro.fileanalyzer.config.MessageResourceBundle;
import de.cofinpro.fileanalyzer.io.ConsolePrinter;
import org.junit.jupiter.api.Test;
import org.junit.jupiter.api.extension.ExtendWith;
import org.junit.jupiter.params.ParameterizedTest;
import org.junit.jupiter.params.provider.CsvSource;
import org.mockito.Mock;
import org.mockito.junit.jupiter.MockitoExtension;

import java.nio.file.Path;
import java.util.List;

import static org.mockito.ArgumentMatchers.anyString;
import static org.mockito.Mockito.*;

@ExtendWith(MockitoExtension.class)
class FileTypeAnalyzerTest {

    String FOUND_MSG = "FOUND";

    @Mock
    ConsolePrinter printer;

    @Mock
    Patterns patterns;

    @ParameterizedTest
    @CsvSource({
            "src/test/resources/jetbrains-academy-certificate-12-java-backend-developer.pdf, %PDF-",
            "src/test/resources/setup-instructions.pdf, %PDF-",
            "src/test/resources/setup-instructions.pdf, Gradle"
    })
    void whenFileWithGivenPatternAnalyze_FoundMessagePrinted(String pathname, String toSearch) {
        Path filepath = Path.of(pathname);
        when(patterns.getPatternsPriorityDescending())
                .thenReturn(List.of(new SearchPattern(1, toSearch, FOUND_MSG)));
        new FileTypeAnalyzer(patterns, printer).analyze(filepath);
        verify(printer).printInfo(filepath.getFileName().toString() + ": " + FOUND_MSG);
        verify(printer, never()).printError(anyString());
    }

    @ParameterizedTest
    @CsvSource({
            "src/test/resources/jetbrains-academy-certificate-12-java-backend-developer.pdf, blahblah",
            "src/test/resources/setup-instructions.pdf, xyxyxy",
            "src/test/resources/setup-instructions.pdf, its_not_in"
    })
    void whenFileWithoutGivenPatternAnalyze_UnknownPrinted(String pathname, String toSearch) {
        Path filepath = Path.of(pathname);
        when(patterns.getPatternsPriorityDescending())
                .thenReturn(List.of(new SearchPattern(1, toSearch, FOUND_MSG)));
        new FileTypeAnalyzer(patterns, printer).analyze(filepath);
        verify(printer).printInfo(filepath.getFileName().toString() + ": " + MessageResourceBundle.UNKNOWN_FILE_TYPE_MSG);
        verify(printer, never()).printError(anyString());
    }

    @Test
    void whenFileNotExist_IOError() {
        when(patterns.getPatternsPriorityDescending())
                .thenReturn(List.of(new SearchPattern(1, "toSearch", FOUND_MSG)));
        new FileTypeAnalyzer(patterns, printer).analyze(Path.of("src/test/resources/notthere"));
        verify(printer).printError(anyString());
    }
}