package de.cofinpro.fileanalyzer;

import lombok.extern.slf4j.Slf4j;

/**
 * Application main for the FiletypeAnalyzer CL-application.
 */
@Slf4j
public class Main {

    public static void main(String[] args) {
        if (args.length != 1) {
            log.error("Usage: java files.Main <path-to-lines-file.json>");
        }
    }
}