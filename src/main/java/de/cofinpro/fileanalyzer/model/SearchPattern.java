package de.cofinpro.fileanalyzer.model;

/**
 * immutable record representing a prioritized search pattern with found message - as read from a pattern.db line.
 */
public record SearchPattern(int priority, String searchText, String foundMessage) {
}
