package de.cofinpro.fileanalyzer.algorithms;

public class NaiveSearchStrategy implements SearchStrategy {

    @Override
    public boolean bufferContainsSearchText(byte[] buffer, String searchText) {
        return new String(buffer).contains(searchText);
    }
}
