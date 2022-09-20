package de.cofinpro.fileanalyzer.algorithms;

import de.cofinpro.fileanalyzer.model.SearchPattern;

import java.nio.charset.Charset;
import java.nio.charset.StandardCharsets;
import java.util.ArrayList;
import java.util.Arrays;
import java.util.List;

/**
 * Knuth-Morris-Pratt algorithm implementation of the SearchStrategy, which is assigned to the CL-option "--KMP"
 */
public class KnuthMorrisPrattStrategy implements SearchStrategy {

    public static final Charset DEFAULT_CHARSET = StandardCharsets.UTF_8;

    private List<PatternRecord> patterns = new ArrayList<>();
    private String foundMessage = "";

    public String getFoundMessage() {
        return foundMessage;
    }

    @Override
    public SearchStrategy setSearchPatterns(List<SearchPattern> priorityDescendingPatterns) {
        priorityDescendingPatterns
                .forEach(pattern -> patterns.add(initializePrefixAndSearchBytes(pattern)));
        return this;
    }

    /**
     * the KMP algorithm is adapted immediately on the byte-array read by the FileInputStream of the analyzer
     * method to save memory and decoding time. To compare it with the search string given as String type,
     * the searchText is encoded to byte[] too. The compare in KMP is on bytes then - instead of char.
     * @param buffer the bytes buffer to search in
     * @return true if search string found, false else.
     */
    @Override
    public boolean fileTypeDetected(byte[] buffer) {
        for (PatternRecord pattern: patterns) {
            if (bufferContainsSearchPattern(buffer, pattern)) {
                return handlePatternFound(pattern);
            }
        }
        return false;
    }

    /**
     * as the pattern list is searched priority descending, a hit in the current buffer can stop the further search of
     * this buffer. The foundMessage of the pattern is stored. All patterns with lower or equal priority are removed from
     * further buffer searches.
     * @param pattern the pattern where the hit was discovered
     * @return true only, if a highest priority pattern was hit - then the search can be aborted, false else
     */
    private boolean handlePatternFound(PatternRecord pattern) {
        foundMessage = pattern.foundMessage();
        patterns = patterns.stream().filter(p -> p.priority() > pattern.priority()).toList();
        return patterns.isEmpty();
    }

    /**
     * method that checksa the current buffer for one specific pattern.
     * @return the result of the sarch for the pattern in this buffer
     */
    private boolean bufferContainsSearchPattern(byte[] buffer, PatternRecord pattern) {
        int bufferLength = buffer.length;
        int position = 0;
        while (position + pattern.searchLength <= bufferLength) {
            int matchLength = getMatchLengthAtPosition(buffer, position, pattern);
            if (matchLength == pattern.searchLength) {
                return true;
            }
            position += matchLength == 0 ? 1 : matchLength - pattern.prefix[matchLength - 1];
        }
        return false;
    }

    /**
     * as core part of KMP algorithm, get the matching length of the pattern at the specified position to apply appropriate
     * jump
     * @return the number of bytes of the pattenr that match here.
     */
    private int getMatchLengthAtPosition(byte[] buffer, int position, PatternRecord pattern) {
        int length = 0;
        while (length < pattern.searchLength && buffer[position + length] == pattern.searchBytes[length]) {
            length++;
        }
        return length;
    }

    /**
     * takes one SearchPattern as read from patterns.db and initializes the attached PatternRecord - e.g. with
     * prefix function.
     * @return the mapped PatternRecord.
     */
    private PatternRecord initializePrefixAndSearchBytes(SearchPattern pattern) {
        return new PatternRecord(new PrefixFinder(pattern.searchText()).getPrefix(),
                DEFAULT_CHARSET.encode(pattern.searchText()).array(), pattern.priority(),
                pattern.searchText().length(), pattern.searchText(), pattern.foundMessage());
    }

    /**
     * inner record class that represents all data for one of the various patterns used in the multi-pattern search.
     */
    private record PatternRecord(int[] prefix,
                                   byte[] searchBytes,
                                   int priority,
                                   int searchLength,
                                   String searchText,
                                   String foundMessage) {
        @Override
        public boolean equals(Object o) {
            if (this == o) return true;
            if (o == null || getClass() != o.getClass()) return false;

            PatternRecord that = (PatternRecord) o;

            return searchText.equals(that.searchText);
        }

        @Override
        public int hashCode() {
            return searchText.hashCode();
        }

        @Override
        public String toString() {
            return "PatternRecord{" +
                    "prefix=" + Arrays.toString(prefix) +
                    ", searchBytes=" + Arrays.toString(searchBytes) +
                    ", priority=" + priority +
                    ", searchLength=" + searchLength +
                    ", searchText='" + searchText + '\'' +
                    ", foundMessage='" + foundMessage + '\'' +
                    '}';
        }
    }
}
