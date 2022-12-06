package de.cofinpro.fileanalyzer.controller;

import de.cofinpro.fileanalyzer.io.ConsolePrinter;
import de.cofinpro.fileanalyzer.model.FileTypeAnalyzer;
import de.cofinpro.fileanalyzer.model.Patterns;

import java.io.IOException;
import java.nio.file.DirectoryStream;
import java.nio.file.Files;
import java.nio.file.Path;
import java.util.concurrent.ExecutorService;
import java.util.concurrent.Executors;
import java.util.concurrent.TimeUnit;

/**
 * application controller class that manages the multithreaded file analyzing by use of an ExecutorService.
 */
public class ParallelFileAnalyzer {

    private static final int ANALYZER_THREAD_COUNT = 10;

    private final ConsolePrinter printer;

    private String dirPath;
    private String patternsPath;

    public ParallelFileAnalyzer(ConsolePrinter consolePrinter) {
        this.printer = consolePrinter;
    }

    /**
     * entry point for the executor, called by Main. scans the arguments given, starts an ExecutorService, an instance
     * of a FileTypeAnalyzer, (which is shared among the worker threads) and invokes workers for the files in the dirPath.
     * @param args CL arguments handed over from Main
     */
    public void run(String[] args) {
        scanCLArguments(args);
        final var patterns = new Patterns(patternsPath);
        var executor = Executors.newFixedThreadPool(ANALYZER_THREAD_COUNT);
        try (DirectoryStream<Path> directoryStream = Files.newDirectoryStream(Path.of(dirPath))) {
            directoryStream.forEach(path -> {
                if (Files.isRegularFile(path)) {
                    executor.submit(getJob(patterns, path));
                }
            });
        } catch (IOException exception) {
            printer.printError("Could not read from directory '%s'!%n%s".formatted(dirPath, exception.toString()));
        } finally {
            shutDownExecutorAndWait(executor);
        }
    }

    /**
     * returns a Runnable for execution by the thread pool. Uses try catch to bring up some possible thread exception,
     * that otherwise vanishes in Nirvana...
     * @param patterns the Patterns structure to use in the analysis.
     * @param path the path to the file which is analyzed in a dedicated thread worker
     * @return runnable job
     */
    private Runnable getJob(Patterns patterns, Path path) {
        return () -> {
            try {
                new FileTypeAnalyzer(patterns, printer).analyze(path);
            } catch (Exception exception) {
                printer.printError("Exception in thread for %s%n%s".formatted(path, exception));
                exception.printStackTrace();
            }
        };
    }

    /**
     * needed for Junit-tests to wait for all threads to finish - normal program runs can live without awaitTermination...
     * @param executor the executor service to shut down and wait for
     */
    private void shutDownExecutorAndWait(ExecutorService executor) {
        executor.shutdown();
        try {
            if (!executor.awaitTermination(100, TimeUnit.SECONDS)) {
                executor.shutdownNow();
            }
        } catch (InterruptedException e) {
            Thread.currentThread().interrupt();
        }
    }

    /**
     * scan command line (CL) arguments into class fields
     * @param args CL arguments given
     * @throws IllegalArgumentException if dirPath is not a valid directory
     */
    private void scanCLArguments(String[] args) {
        dirPath = args[0];
        if (!Files.isDirectory(Path.of(dirPath))) {
            printer.printError("The directory-path given '%s' is not valid!%n".formatted(dirPath));
            throw new IllegalArgumentException("Invalid directory path!");
        }
        patternsPath = args[1];
    }
}
