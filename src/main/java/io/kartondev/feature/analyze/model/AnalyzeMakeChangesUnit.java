package io.kartondev.feature.analyze.model;

import lombok.Data;

@Data
public class AnalyzeMakeChangesUnit {
    private final String where;
    private final String operation;
    private final String uniquePath;
    private final String sourceName;
}
