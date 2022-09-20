package de.cofinpro.fileanalyzer.algorithms;

import de.cofinpro.fileanalyzer.model.SearchPattern;

import java.util.List;

/**
 * Strategy Pattern interface, used to switch between different multi-pattern search strategies
 * in the FileTypeAnalyzer.
 */
public interface SearchStrategy {

    SearchStrategy setSearchPatterns(List<SearchPattern> priorityDescendingPatterns);

    boolean fileTypeDetected(byte[] buffer);

    String getFoundMessage();
}
