package de.cofinpro.fileanalyzer.algorithms;

import java.util.Arrays;

public class PrefixFinder {

    private final String text;

    private final int[] p;


    public PrefixFinder(String text) {
        this.text = text;
        p = new int[text.length()];
        Arrays.fill(p, 0);
    }

    public int[] getPrefix() {
        // implementation follows https://hyperskill.org/learn/step/17545, §4
        Arrays.setAll(p, this::findPrefixElement);
        return p.clone();
    }

    private int findPrefixElement(int i) {
        if (i == 0) {
            return 0;
        }
        int j = i;
        do {
            j = p[j - 1];
            if (text.charAt(i) == text.charAt(j)) {
                return j + 1;
            }
        } while (j > 0);
        return 0;
    }
}
