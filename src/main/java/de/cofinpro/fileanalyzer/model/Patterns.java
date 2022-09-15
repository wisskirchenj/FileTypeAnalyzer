package de.cofinpro.fileanalyzer.model;

import lombok.extern.slf4j.Slf4j;

import java.io.IOException;
import java.nio.file.Files;
import java.nio.file.Path;
import java.util.ArrayList;
import java.util.Comparator;
import java.util.List;
import java.util.stream.Stream;

import static java.util.function.Predicate.not;

/**
 * class for providing the SearchPatterns for analyzing file types, which are read from a CSV-file,
 * whose path string is given as constructor argument.
 * The SearchPatterns are internally stored as a priority descending list which is accessed via a getter.
 */
@Slf4j
public class Patterns {

    private final List<SearchPattern> searchPatterns;

    public Patterns(String patternsPath) {
        searchPatterns = readPatterns(patternsPath);
    }

    public List<SearchPattern> getPatternsPriorityDescending() {
        return searchPatterns;
    }

    /**
     * reads a CSV file (e.g. patterns.db) into a priority descending sorted list of SearchPatterns
     * @param patternsPath path string to the CSV-file with patterns (patterns.db)
     * @return the sorted list read from the file
     * @throws IllegalArgumentException if the file could not be opened or the format is not valid (original exception carried)
     */
    private List<SearchPattern> readPatterns(String patternsPath) {
        List<SearchPattern> result = new ArrayList<>();

        try (Stream<String> lines = Files.lines(Path.of(patternsPath))) {
            lines.filter(not(String::isBlank)).map(this::scanPatternLine)
                    .sorted(Comparator.comparing(SearchPattern::priority).reversed())
                    .forEach(result::add);
        } catch (IOException | RuntimeException exception) {
            log.error("Cannot read from the searchText-csv-path given '{}'!\n{}", patternsPath, exception);
            throw new IllegalArgumentException(exception);
        }
        return result;
    }

    /**
     * scan one line of the CSV-file into a SearchPattern record.
     */
    private SearchPattern scanPatternLine(String line) {
        String[] tokens = line.trim().split(";");
        return new SearchPattern(Integer.parseInt(tokens[0]),
                tokens[1].replace("\"", ""),
                tokens[2].replace("\"", ""));
    }
}
