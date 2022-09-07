package de.cofinpro.fileanalyzer.controller;

import de.cofinpro.fileanalyzer.algorithms.KnuthMorrisPrattAnalyzer;
import de.cofinpro.fileanalyzer.algorithms.NaiveSearchStrategy;
import de.cofinpro.fileanalyzer.algorithms.SearchStrategy;
import de.cofinpro.fileanalyzer.io.ConsolePrinter;

import java.io.*;

import static de.cofinpro.fileanalyzer.config.MessageResourceBundle.*;

/**
 * Controller class that does the file analyzing.
 */
public class FileTypeAnalyzer {

    private static final int BUFFER_SIZE = 1;
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
     * a little more complicated to implement, but with a large buffer size (here 1.3 GB) equivalently
     * efficient and fast as method below (tested) and also works for arbitrarily large files
     * -> E.g. tested with a 65 GB Container-image that "surprisingly" was "a pdf-file"..
     * Not so surprising on 2nd thought, that a image containing a full OS and more has some pdf in it somewhere :-)
     */
    public void analyze(String[] args) {
        scanCLArguments(args);
        try (InputStream stream = new FileInputStream(filePath)) {
            byte[] buffer = new byte[searchText.length() + BUFFER_SIZE];

            while (stream.read(buffer, searchText.length(), BUFFER_SIZE) != END_OF_STREAM) {
                if (searchStrategy.bufferContainsSearchText(buffer, searchText)) {
                    printer.printInfo(foundMessage);
                    return;
                }
                // copy the last searchText length many characters from the end to the beginning to enable clipping search
                System.arraycopy(buffer, buffer.length - searchText.length(), buffer, 0, searchText.length());
            }
            printer.printInfo(UNKNOWN_FILE_TYPE_MSG);

        } catch (IOException exception) {
            printer.printError("Could not open the given file '%s'!%n%s".formatted(filePath, exception.toString()));
        }
    }

    /**
     * scan command line (CL) arguments into class fields used in analyze logic.
     * @param args CL arguments given
     */
    private void scanCLArguments(String[] args) {
        searchStrategy = "--KMP".equals(args[0]) ? new KnuthMorrisPrattAnalyzer() : new NaiveSearchStrategy();
        filePath = args[1];
        searchText = args[2];
        foundMessage = args[3];
    }
}
