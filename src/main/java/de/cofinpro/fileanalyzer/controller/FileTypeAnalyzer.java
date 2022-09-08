package de.cofinpro.fileanalyzer.controller;

import de.cofinpro.fileanalyzer.algorithms.KnuthMorrisPrattStrategy;
import de.cofinpro.fileanalyzer.algorithms.NaiveSearchStrategy;
import de.cofinpro.fileanalyzer.algorithms.SearchStrategy;
import de.cofinpro.fileanalyzer.io.ConsolePrinter;

import java.io.FileInputStream;
import java.io.IOException;
import java.io.InputStream;
import java.time.Duration;
import java.time.Instant;

import static de.cofinpro.fileanalyzer.config.MessageResourceBundle.*;

/**
 * Controller class that does the file analyzing.
 */
public class FileTypeAnalyzer {

    private static final int BUFFER_SIZE = 67108864;
    private static final int END_OF_STREAM = -1;

    private final ConsolePrinter printer;

    private SearchStrategy searchStrategy;
    private String filePath;
    private String searchText;
    private String foundMessage;

    public FileTypeAnalyzer(ConsolePrinter consolePrinter) {
        this.printer = consolePrinter;
    }

    /**
     * entry point for the controller class, called by Main. scans the arguments given and
     * does a buffered stream read which is analyzed buffer by buffer with the SearchStrategy chosen.
     * By using streams, the method can handle arbitrarily large files(tested with 65GB).
     * To enable clipping search, i.e. string reaches across the buffer border, the last search string length many
     * characters are copied before the newly read buffer and are searched together with the new buffer.
     * @param args CL arguments handed over from Main
     */
    public void analyze(String[] args) {
        scanCLArguments(args);
        Instant start = Instant.now();
        try (InputStream stream = new FileInputStream(filePath)) {
            byte[] buffer = new byte[searchText.length() + BUFFER_SIZE];

            while (stream.read(buffer, searchText.length(), BUFFER_SIZE) != END_OF_STREAM) {
                if (searchStrategy.bufferContainsSearchText(buffer, searchText)) {
                    printResultInfo(foundMessage, start);
                    return;
                }
                // copy the last searchText length many characters from the end to the beginning to enable clipping search
                System.arraycopy(buffer, buffer.length - searchText.length(), buffer, 0, searchText.length());
            }
            printResultInfo(UNKNOWN_FILE_TYPE_MSG, start);
        } catch (IOException exception) {
            printer.printError("Could not open the given file '%s'!%n%s".formatted(filePath, exception.toString()));
        }
    }

    private void printResultInfo(String message, Instant start) {
        printer.printInfo(message);
        printer.printInfo("It took %.3f seconds".formatted(Duration.between(start, Instant.now()).toMillis() / 1000d)
                .replace(",", "."));
    }

    /**
     * scan command line (CL) arguments into class fields used in analyze logic, e.g. set the requested SearchStrategy
     * @param args CL arguments given
     */
    private void scanCLArguments(String[] args) {
        searchStrategy = "--KMP".equals(args[0]) ? new KnuthMorrisPrattStrategy() : new NaiveSearchStrategy();
        filePath = args[1];
        searchText = args[2];
        foundMessage = args[3];
    }
}
