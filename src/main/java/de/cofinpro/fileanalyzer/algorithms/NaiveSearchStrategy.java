package de.cofinpro.fileanalyzer.algorithms;

/**
 * naive implementation of the SearchStrategy, which is assigned to the CL-option "--naive"
 */
public class NaiveSearchStrategy implements SearchStrategy {

    @Override
    public boolean bufferContainsSearchText(byte[] buffer, String searchText) {
        return new String(buffer).contains(searchText);
    }
}
