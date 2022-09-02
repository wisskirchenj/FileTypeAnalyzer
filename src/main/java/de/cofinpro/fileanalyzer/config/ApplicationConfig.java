package de.cofinpro.fileanalyzer.config;

public class ApplicationConfig {

    private ApplicationConfig() {
        // no instances
    }

    public static final String UNKNOWN_FILE_TYPE_MSG = "Unknown file type";
    public static final int BUFFER_SIZE = 134217728;
    public static final int END_OF_STREAM = -1;
}
