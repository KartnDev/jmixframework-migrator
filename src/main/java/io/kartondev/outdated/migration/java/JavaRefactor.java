package io.kartondev.outdated.migration.java;

import spoon.reflect.declaration.CtType;

public interface JavaRefactor {
    boolean canRefactor(CtType<?> type);
    void makeRefactorOnTarget(CtType<?> type);
}
