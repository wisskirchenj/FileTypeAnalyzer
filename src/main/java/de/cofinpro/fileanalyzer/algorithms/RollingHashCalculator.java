package de.cofinpro.fileanalyzer.algorithms;

/**
 * generic class to calculate polynomial hashes of degree searchText-Length - 1 and rolling hashes
 * for moving byte buffer positions.
 */
public class RollingHashCalculator {

    // prime Q for taking modulo in hashing calculations
    static final int Q = 239;
    static final int BASE_MOD_Q = 256 % Q;
    static final int MAX_PATTERN_LENGTH = 128;
    static final int[] BASE_POW = new int[MAX_PATTERN_LENGTH];

    static {
        BASE_POW[0] = 1;
        BASE_POW[1] = BASE_MOD_Q;
        for (int i = 2; i < MAX_PATTERN_LENGTH; i++) {
            BASE_POW[i] = BASE_POW[i - 1] * BASE_MOD_Q % Q;
        }
    }

    private final int patternLength;
    private int currentHash;

    public RollingHashCalculator(byte[] initialPattern) {
        this.patternLength = initialPattern.length;
        currentHash = calcHash(initialPattern);
    }

    public int getCurrentHash() {
        return currentHash;
    }

    public void resetPattern(byte[] newPattern) {
        currentHash = calcHash(newPattern);
    }

    public int getRollingHash(byte[] buffer, int position) {
        if (position == 0) {
            return currentHash;
        }
        currentHash += Q - (buffer[position - 1] & 0xff) * BASE_POW[patternLength - 1] % Q;
        currentHash *= BASE_MOD_Q;
        currentHash += buffer[position + patternLength - 1] & 0xff;
        currentHash %= Q;
        return currentHash;
    }

    private int calcHash(byte[] text) {
        int hash = 0;
        for (int i = 0; i < patternLength; i++) {
            hash += (text[i] & 0xff) * BASE_POW[patternLength - i - 1];
            hash %= Q;
        }
        return hash;
    }
}
