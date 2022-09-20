package de.cofinpro.fileanalyzer.model;

import de.cofinpro.fileanalyzer.algorithms.RabinKarpStrategy;
import de.cofinpro.fileanalyzer.algorithms.SearchStrategy;
import de.cofinpro.fileanalyzer.io.ConsolePrinter;

import java.io.FileInputStream;
import java.io.IOException;
import java.io.InputStream;
import java.nio.file.Path;

import static de.cofinpro.fileanalyzer.config.MessageResourceBundle.*;

/**
 * Domain logic class that does the actual file type analyzing.
 */
public class FileTypeAnalyzer {

    private static final int BUFFER_SIZE = 4096;
    private static final int END_OF_STREAM = -1;

    private final ConsolePrinter printer;

    private final SearchStrategy searchStrategy;
    private final int maxSearchLength;

    public FileTypeAnalyzer(Patterns patterns, ConsolePrinter consolePrinter) {
        this.printer = consolePrinter;
        this.searchStrategy = new RabinKarpStrategy().setSearchPatterns(patterns.getPatternsPriorityDescending());
        this.maxSearchLength = (int) patterns.getMaxSearchLength();
    }

    /**
     * entry point of the analyzer class to analyze a file given with a filePath string argument. The method
     * does a buffered stream read which is analyzed - buffer by buffer - with the chosen multiple-pattern
     * SearchStrategy (Rabin-Karp).
     * By using streams, the method can handle arbitrarily large files.
     * To enable clipping search, i.e. string reaches across the buffer border, the last search string length many
     * characters are copied before the newly read buffer and are searched together with the new buffer.
     */
    public void analyze(Path filePath) {
        try (InputStream stream = new FileInputStream(filePath.toString())) {
            byte[] buffer = new byte[maxSearchLength + BUFFER_SIZE];
            while (stream.read(buffer, maxSearchLength, BUFFER_SIZE) != END_OF_STREAM) {
                if (searchStrategy.fileTypeDetected(buffer)) {
                    break;
                }
                // copy the last searchText length many characters from the end to the beginning to enable clipping search
                System.arraycopy(buffer, buffer.length - maxSearchLength, buffer, 0, maxSearchLength);
            }
        } catch (IOException exception) {
            printer.printError("Could not open the given file '%s'!%n%s".formatted(filePath, exception.toString()));
            return;
        }
        printer.printInfo(filePath.getFileName().toString() + ": "
                + (searchStrategy.getFoundMessage().isEmpty() ? UNKNOWN_FILE_TYPE_MSG : searchStrategy.getFoundMessage()));
    }
}
