package de.cofinpro.fileanalyzer.algorithms;

import java.nio.charset.Charset;
import java.nio.charset.StandardCharsets;

/**
 * Knuth-Morris-Pratt algorithm implementation of the SearchStrategy, which is assigned to the CL-option "--KMP"
 */
public class KnuthMorrisPrattStrategy implements SearchStrategy {

    public static final Charset DEFAULT_CHARSET = StandardCharsets.UTF_8;

    private int[] prefix;
    private byte[] searchBytes;
    private int searchLength;

    /**
     * the KMP algorithm is adapted immediately on the byte-array read by the FileInputStream of the analyzer
     * method to save memory and decoding time. To compare it with the serahc string given as String type,
     * the pattern is encoded to byte[] too. The compare in KMP is on bytes then - instead of char.
     * @param buffer the bytes buffer to search in
     * @param searchText the text to search
     * @return true if serach string found, false else.
     */
    @Override
    public boolean bufferContainsSearchText(byte[] buffer, String searchText) {
        initializePrefixAndSearchBytes(searchText);
        int bufferLength = buffer.length;
        int position = 0;
        while (position + searchLength <= bufferLength) {
            int matchLength = getMatchLengthAtPosition(buffer, position);
            if (matchLength == searchLength) {
                return true;
            }
            position += matchLength == 0 ? 1 : matchLength - prefix[matchLength - 1];
        }
        return false;
    }

    private int getMatchLengthAtPosition(byte[] buffer, int position) {
        int length = 0;
        while (length < searchLength && buffer[position + length] == searchBytes[length]) {
            length++;
        }
        return length;
    }

    private void initializePrefixAndSearchBytes(String searchText) {
        prefix = new PrefixFinder(searchText).getPrefix();
        searchLength = searchText.length();
        searchBytes = DEFAULT_CHARSET.encode(searchText).array();
    }
}
