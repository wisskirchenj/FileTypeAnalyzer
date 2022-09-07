package de.cofinpro.fileanalyzer;

import de.cofinpro.fileanalyzer.controller.FileTypeAnalyzer;
import de.cofinpro.fileanalyzer.io.ConsolePrinter;
import lombok.extern.slf4j.Slf4j;

/**
 * Application main for the FiletypeAnalyzer CL-application.
 */
@Slf4j
public class Main {

    public static void main(String[] args) {
        if (args.length != 4) {
            log.error("Usage: java fileanalyzer.Main --naive|--KMP <path-to-file> <search-string> <found-message>");
            return;
        }
        new FileTypeAnalyzer(new ConsolePrinter()).analyze(args);
    }
}