package de.cofinpro.fileanalyzer.algorithms;


import de.cofinpro.fileanalyzer.model.SearchPattern;

import java.util.ArrayList;
import java.util.Arrays;
import java.util.List;

/**
 * implementation of a multi-pattern search Rabin-Karp algorithm.
 */
public class RabinKarpStrategy implements SearchStrategy{

    private List<PatternRecord> patterns = new ArrayList<>();
    private String foundMessage = "";
    private int foundPriority = 0;

    public String getFoundMessage() {
        return foundMessage;
    }

    /**
     * "builder-like" method to set the search pattern after creating the search strategy
     */
    @Override
    public SearchStrategy setSearchPatterns(List<SearchPattern> priorityDescendingPatterns) {
        priorityDescendingPatterns
                .forEach(pattern -> patterns.add(initializeHashAndSearchBytes(pattern)));
        return this;
    }

    /**
     * the RabinKarp algorithm is adapted immediately on the byte-array read by the FileInputStream of the analyzer
     * method to save memory and decoding time. To compare it with the search string given as String type,
     * the searchText is encoded to byte[] too.
     * @param buffer the bytes buffer to search in
     * @return true only if one of the highest priority search strings is found, otherwise after reading all bytes, the
     *         getFoundMessage can be used to get the best result if any was found.
     */
    @Override
    public boolean fileTypeDetected(byte[] buffer) {
        int bufferLength = buffer.length;
        int position = 0;
        initPatternHashes(buffer);
        while (position < bufferLength) {
            for (PatternRecord pattern: patterns) {
                if (searchPatternAtPosition(buffer, position, pattern)
                        && fileTypeDeterminedWithFoundPattern(pattern)) {
                        return true;
                    }
                }
            position++;
        }
        return false;
    }

    private void initPatternHashes(byte[] buffer) {
        patterns.forEach(pattern -> pattern.hashCalculator().resetPattern(buffer));
    }

    /**
     * check one pattern at the given buffer psoition - strings are only compared, of hashes match
     */
    private boolean searchPatternAtPosition(byte[] buffer, int position, PatternRecord pattern) {
        if (position + pattern.searchLength() <= buffer.length
                && pattern.hash() == pattern.hashCalculator().getRollingHash(buffer, position)) {

            return Arrays.equals(pattern.searchBytes(), 0, pattern.searchLength(),
                    buffer, position, position + pattern.searchLength());
        }
        return false;
    }

    /**
     * called when a match to a search pattern was found. If the match has hoghest priority iit is stored and
     * all searches with same or lower priority are aborted.
     * @param pattern the pattern structrue, that had the hit.
     * @return true, if the match was for highest priority, i.e. the search can be stopped.
     */
    private boolean fileTypeDeterminedWithFoundPattern(PatternRecord pattern) {
        if (pattern.priority() > foundPriority) {
            foundMessage = pattern.foundMessage();
            foundPriority = pattern.priority();
        }
        patterns = patterns.stream().filter(p -> p.priority() > pattern.priority()).toList();
        return patterns.isEmpty();
    }

    /** takes one SearchPattern as read from patterns.db and initializes the attached PatternRecord - e.g. with
     * a RollingHashCalculator and the hash value of the pattern.
     */
    private PatternRecord initializeHashAndSearchBytes(SearchPattern pattern) {
        byte[] searchBytes = pattern.searchText().getBytes();
        var hashCalculator = new RollingHashCalculator(searchBytes);
        return new PatternRecord(hashCalculator, hashCalculator.getCurrentHash(),
                searchBytes, pattern.priority(),
                pattern.searchText().length(), pattern.searchText(), pattern.foundMessage());
    }

    /**
     * inner record structure to pass between methods during stream processing.
     * Instances are created and initialized once, when the patterns are set after construction of the strategy.
     */
    private record PatternRecord(RollingHashCalculator hashCalculator,
                                 int hash,
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
                    "hash=" + hash +
                    ", searchBytes=" + Arrays.toString(searchBytes) +
                    ", priority=" + priority +
                    ", searchLength=" + searchLength +
                    ", searchText='" + searchText + '\'' +
                    ", foundMessage='" + foundMessage + '\'' +
                    '}';
        }
    }
}
