package de.cofinpro.fileanalyzer.io;

import lombok.extern.slf4j.Slf4j;

@Slf4j
public class ConsolePrinter {

    public void printInfo(String message) {
        log.info(message);
    }

    public void printError(String message) {
        log.error(message);
    }
}
