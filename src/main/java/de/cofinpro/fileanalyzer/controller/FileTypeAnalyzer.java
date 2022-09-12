package de.cofinpro.fileanalyzer.controller;

import de.cofinpro.fileanalyzer.algorithms.KnuthMorrisPrattStrategy;
import de.cofinpro.fileanalyzer.algorithms.SearchStrategy;
import de.cofinpro.fileanalyzer.io.ConsolePrinter;

import java.io.FileInputStream;
import java.io.IOException;
import java.io.InputStream;
import java.nio.file.Path;

import static de.cofinpro.fileanalyzer.config.MessageResourceBundle.*;

/**
 * Controller class that does the file analyzing.
 */
public class FileTypeAnalyzer {

    private static final int BUFFER_SIZE = 67108864;
    private static final int END_OF_STREAM = -1;

    private final ConsolePrinter printer;

    private final SearchStrategy searchStrategy = new KnuthMorrisPrattStrategy();
    private final String searchText;
    private final String foundMessage;

    public FileTypeAnalyzer(ParallelFileAnalyzer executor, ConsolePrinter consolePrinter) {
        this.printer = consolePrinter;
        this.searchText = executor.getSearchText();
        this.foundMessage = executor.getFoundMessage();
    }

    /**
     * entry point of the analyzer class to analyze a file given with a PAth argument. It
     * does a buffered stream read which is analyzed - buffer by buffer - with the SearchStrategy chosen (KMP).
     * By using streams, the method can handle arbitrarily large files (tested with 65GB).
     * To enable clipping search, i.e. string reaches across the buffer border, the last search string length many
     * characters are copied before the newly read buffer and are searched together with the new buffer.
     * @param filePath CL arguments handed over from Main
     */
    public void analyze(Path filePath) {
        try (InputStream stream = new FileInputStream(filePath.toString())) {
            byte[] buffer = new byte[searchText.length() + BUFFER_SIZE];

            while (stream.read(buffer, searchText.length(), BUFFER_SIZE) != END_OF_STREAM) {
                if (searchStrategy.bufferContainsSearchText(buffer, searchText)) {
                    printer.printInfo(filePath.getFileName().toString() + ": " + foundMessage);
                    return;
                }
                // copy the last searchText length many characters from the end to the beginning to enable clipping search
                System.arraycopy(buffer, buffer.length - searchText.length(), buffer, 0, searchText.length());
            }
            printer.printInfo(filePath.getFileName().toString() + ": " + UNKNOWN_FILE_TYPE_MSG);
        } catch (IOException exception) {
            printer.printError("Could not open the given file '%s'!%n%s".formatted(filePath, exception.toString()));
        }
    }
}
