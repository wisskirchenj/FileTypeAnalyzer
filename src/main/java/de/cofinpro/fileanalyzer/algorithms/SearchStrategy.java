package de.cofinpro.fileanalyzer.algorithms;

public interface SearchStrategy {

    boolean bufferContainsSearchText(byte[] buffer, String searchText);
}
