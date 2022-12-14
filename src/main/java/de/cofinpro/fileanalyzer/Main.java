package de.cofinpro.fileanalyzer;

import de.cofinpro.fileanalyzer.controller.ParallelFileAnalyzer;
import de.cofinpro.fileanalyzer.io.ConsolePrinter;
import lombok.extern.slf4j.Slf4j;

/**
 * Application main for the FiletypeAnalyzer CL-application.
 */
@Slf4j
public class Main {

    public static void main(String[] args) {
        if (args.length != 2) {
            log.error("Usage: java fileanalyzer.Main <directory-path> <patterns-csv-path>");
            return;
        }
        new ParallelFileAnalyzer(new ConsolePrinter()).run(args);
    }
}