package io.kartondev.outdated.migration;

import spoon.reflect.declaration.CtType;

public interface MigrationStrategy {
    boolean isSupport(CtType<?> type);
    void migrate(CtType<?> type);
}
