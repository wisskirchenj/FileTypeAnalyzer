package de.cofinpro.fileanalyzer.algorithms;

import java.util.Arrays;

/**
 * class for finding the prefix function of a string, as needed by search algorithms as Knuth-Morris-Pratt.
 */
public class PrefixFinder {

    private final String text;
    private final int[] prefix;

    public PrefixFinder(String text) {
        this.text = text;
        prefix = new int[text.length()];
    }

    /**
     * main entry point of the class, that calculates and returns the prefix for the string set as text property.
     * @return the prefix array for the string given as constructor parameter.
     */
    public int[] getPrefix() {
        // implementation follows https://hyperskill.org/learn/step/17545, §4
        Arrays.setAll(prefix, this::findPrefixElement);
        return prefix;
    }

    private int findPrefixElement(int i) {
        int j = i;
        while (j > 0) {
            j = prefix[j - 1];
            if (text.charAt(i) == text.charAt(j)) {
                return j + 1;
            }
        }
        return 0;
    }
}
