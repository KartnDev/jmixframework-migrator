package io.kartondev.feature.analyze;

import io.kartondev.feature.analyze.model.AnalyzeMakeChangesUnit;
import io.kartondev.feature.analyze.model.AnalyzeTargetUnit;

import java.util.List;

public interface AnalyzingService {
    List<AnalyzeMakeChangesUnit> deepAnalyze(AnalyzeTargetUnit analyzeTargetUnit);
}
