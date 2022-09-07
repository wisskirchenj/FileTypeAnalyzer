package de.cofinpro.fileanalyzer.algorithms;

public class KnuthMorrisPrattAnalyzer implements SearchStrategy {

    @Override
    public boolean bufferContainsSearchText(byte[] buffer, String searchText) {
        return false;
    }
}
