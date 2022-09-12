package de.cofinpro.fileanalyzer.controller;

import de.cofinpro.fileanalyzer.io.ConsolePrinter;
import lombok.Getter;

import java.io.IOException;
import java.nio.file.DirectoryStream;
import java.nio.file.Files;
import java.nio.file.Path;
import java.util.concurrent.Executors;

/**
 * application controller class that manages the multithreaded file analyzing by use of an ExecutorService.
 */
@Getter
public class ParallelFileAnalyzer {

    private static final int ANALYZER_THREAD_COUNT = 10;

    private final ConsolePrinter printer;

    private String dirPath;
    private String searchText;
    private String foundMessage;

    public ParallelFileAnalyzer(ConsolePrinter consolePrinter) {
        this.printer = consolePrinter;
    }

    /**
     * entry point for the executor, called by Main. scans the arguments given, starts an ExecutorService
     * and invokes threads for the files in the dirPath given.
     * @param args CL arguments handed over from Main
     */
    public void run(String[] args) {
        scanCLArguments(args);
        var executor = Executors.newFixedThreadPool(ANALYZER_THREAD_COUNT);
        try (DirectoryStream<Path> directoryStream = Files.newDirectoryStream(Path.of(dirPath))) {
            directoryStream.forEach(path -> {
                if (Files.isRegularFile(path)) {
                    executor.submit(() -> new FileTypeAnalyzer(this, printer).analyze(path));
                }
            });
            executor.shutdown();
        } catch (IOException exception) {
            printer.printError("Could not read from directory '%s'!%n%s".formatted(dirPath, exception.toString()));
        }
    }

    /**
     * scan command line (CL) arguments into class fields used in analyze logic, e.g. set the requested SearchStrategy
     * @param args CL arguments given
     */
    private void scanCLArguments(String[] args) {
        dirPath = args[0];
        if (!Files.isDirectory(Path.of(dirPath))) {
            printer.printError("The directory-path given '%s' is not valid!%n".formatted(dirPath));
            System.exit(1);
        }
        searchText = args[1];
        foundMessage = args[2];
    }
}
