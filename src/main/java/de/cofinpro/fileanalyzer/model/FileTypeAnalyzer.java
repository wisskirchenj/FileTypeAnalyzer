package de.cofinpro.fileanalyzer.model;

import de.cofinpro.fileanalyzer.algorithms.KnuthMorrisPrattStrategy;
import de.cofinpro.fileanalyzer.algorithms.SearchStrategy;
import de.cofinpro.fileanalyzer.io.ConsolePrinter;

import java.io.FileInputStream;
import java.io.IOException;
import java.io.InputStream;
import java.nio.file.Path;
import java.util.List;

import static de.cofinpro.fileanalyzer.config.MessageResourceBundle.*;

/**
 * Domain logic class that does the actual file type analyzing.
 */
public class FileTypeAnalyzer {

    private static final int BUFFER_SIZE = 67108864;
    private static final int END_OF_STREAM = -1;

    private final ConsolePrinter printer;

    private final SearchStrategy searchStrategy = new KnuthMorrisPrattStrategy();
    private final List<SearchPattern> searchPatterns;

    public FileTypeAnalyzer(Patterns patterns, ConsolePrinter consolePrinter) {
        this.printer = consolePrinter;
        this.searchPatterns = patterns.getPatternsPriorityDescending();
    }

    /**
     * entry point of the analyzer class to analyze a file given with a filePath string argument.
     * The method loops over all search patterns retrieved from Patterns during construction and calls
     * the buffer search. If a pattern matches, due to priority sorted patterns, the file type is found.
     * If none matches, an unknown file type message is printed.
     */
    public void analyze(Path filePath) {
        for (SearchPattern pattern: searchPatterns) {
            if (fileContainsPattern(filePath, pattern)) {
                return;
            }
        }
        printer.printInfo(filePath.getFileName().toString() + ": " + UNKNOWN_FILE_TYPE_MSG);
    }

    /**
     * search the file given as path string for the pattern given. The method
     * does a buffered stream read which is analyzed - buffer by buffer - with the SearchStrategy chosen (KMP).
     * By using streams, the method can handle arbitrarily large files (tested with 65GB).
     * To enable clipping search, i.e. string reaches across the buffer border, the last search string length many
     * characters are copied before the newly read buffer and are searched together with the new buffer.
     * @return true if pattern is contained, false if not contained OR file cannot be read (IOException)
     */
    private boolean fileContainsPattern(Path filePath, SearchPattern pattern) {
        String searchText = pattern.searchText();
        String foundMessage = pattern.foundMessage();
        try (InputStream stream = new FileInputStream(filePath.toString())) {
            byte[] buffer = new byte[searchText.length() + BUFFER_SIZE];

            while (stream.read(buffer, searchText.length(), BUFFER_SIZE) != END_OF_STREAM) {
                if (searchStrategy.bufferContainsSearchText(buffer, searchText)) {
                    printer.printInfo(filePath.getFileName().toString() + ": " + foundMessage);
                    return true;
                }
                // copy the last searchText length many characters from the end to the beginning to enable clipping search
                System.arraycopy(buffer, buffer.length - searchText.length(), buffer, 0, searchText.length());
            }
        } catch (IOException exception) {
            printer.printError("Could not open the given file '%s'!%n%s".formatted(filePath, exception.toString()));
            return true; // do not try with other patterns...
        }
        return false;
    }
}
