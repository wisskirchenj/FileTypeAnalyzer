package de.cofinpro.fileanalyzer.controller;

import de.cofinpro.fileanalyzer.io.ConsolePrinter;

import java.io.*;
import java.nio.file.Files;
import java.nio.file.Path;
import java.util.regex.Matcher;
import java.util.regex.Pattern;

import static de.cofinpro.fileanalyzer.config.ApplicationConfig.*;

public class FileTypeAnalyzer {

    private final String filePath;
    private final Pattern searchPattern;
    private final String foundMessage;

    private final ConsolePrinter printer;

    public FileTypeAnalyzer(String[] args, ConsolePrinter consolePrinter) {
        filePath = args[0];
        searchPattern = Pattern.compile(args[1]);
        foundMessage = args[2];
        this.printer = consolePrinter;
    }

    /**
     * a little more complicated to implement, but with a large buffer size (here 1.3 GB) equivalently
     * efficient and fast as method below (tested) and also works for arbitrarily large files
     * -> E.g. tested with a 65 GB Container-image that "surprisingly" was "a pdf-file"..
     * Not so surprising on 2nd thought, that a image containing a full OS and more has some pdf in it somewhere :-)
     */
    public void analyze() {
        try (InputStream stream = new FileInputStream(filePath)) {
            byte[] buffer = new byte[BUFFER_SIZE];

            while (stream.read(buffer) != END_OF_STREAM) {
                if (new String(buffer).contains(searchPattern.toString())) {
                    printer.printInfo(foundMessage);
                    return;
                }
            }
            printer.printInfo(UNKNOWN_FILE_TYPE_MSG);

        } catch (IOException exception) {
            printer.printError("Could not open the given file '%s'!%n%s".formatted( filePath, exception.toString()));
        }
    }

    /**
     * well-suited message for files up to around 2GB or a little more, that fit into the heap memory.
     * Not suited for big files of 20GB or more -> OutOfMemoryError
     */
    public void analyzeFile() {
       try {
            Matcher matcher = searchPattern.matcher(new String(Files.readAllBytes(Path.of(filePath))));
            boolean found = matcher.find();
            printer.printInfo(found ? foundMessage : UNKNOWN_FILE_TYPE_MSG);
        } catch (IOException exception) {
           printer.printError("Could not open the given file '%s'!%n%s".formatted( filePath, exception.toString()));
        }
    }
}
