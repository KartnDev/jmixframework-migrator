package io.kartondev.outdated.migration;

import io.kartondev.outdated.migration.java.ClassicRoute2FlowRouteRefactor;
import io.kartondev.outdated.migration.java.JavaRefactor;
import io.kartondev.outdated.migration.java.UiController2ViewControllerRefactor;
import io.kartondev.outdated.migration.java.UiDescriptor2ViewDescriptorRefactor;
import spoon.reflect.declaration.CtType;

import java.util.Arrays;
import java.util.List;

public class Screen2ViewMigrationStrategy implements MigrationStrategy {

    private final List<JavaRefactor> controllerRefactors = Arrays.asList(
            new UiController2ViewControllerRefactor(),
            new UiDescriptor2ViewDescriptorRefactor(),
            new ClassicRoute2FlowRouteRefactor()
    );

    @Override
    public boolean isSupport(CtType<?> type) {
        return type.getSuperclass() != null &&
                Arrays.asList("StandardLookup", "StandardEditor", "StandardDetailView", "StandardScreen", "Screen")
                        .contains(type.getSuperclass().getSimpleName());
    }

    @Override
    public void migrate(CtType<?> type) {
        controllerRefactors.stream()
                .filter(refactor -> refactor.canRefactor(type))
                .forEach(refactor -> refactor.makeRefactorOnTarget(type));
    }
}

