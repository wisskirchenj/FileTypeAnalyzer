package de.cofinpro.fileanalyzer.algorithms;

/**
 * Strategy Pattern interface, used to switch between different search strategies in the FileTypeAnalyzer,
 * who acts as the "context" for this pattern.
 */
public interface SearchStrategy {

    boolean bufferContainsSearchText(byte[] buffer, String searchText);
}
