package de.cofinpro.fileanalyzer.algorithms;

/**
 * Strategy Pattern interface, used to switch between different search strategies in the FileTypeAnalyzer,
 * who acts as the "context" for this searchText.
 */
public interface SearchStrategy {

    boolean bufferContainsSearchText(byte[] buffer, String searchText);
}
