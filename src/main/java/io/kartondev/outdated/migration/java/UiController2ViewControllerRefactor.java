package io.kartondev.outdated.migration.java;

import io.kartondev.outdated.processor.UiAnnotationRenameProcessor;
import spoon.reflect.declaration.CtAnnotation;
import spoon.reflect.declaration.CtType;

public class UiController2ViewControllerRefactor implements JavaRefactor {

    @Override
    public boolean canRefactor(CtType<?> type) {
        return type.getAnnotations().stream()
                .anyMatch(a -> "io.jmix.ui.screen.UiController".equals(a.getAnnotationType().getQualifiedName()));
    }

    @Override
    public void makeRefactorOnTarget(CtType<?> type) {
        CtAnnotation<?> uiController = type.getAnnotations().stream()
                .filter(a -> "UiController".equals(a.getName()))
                .findFirst()
                .orElse(null);

        if (uiController != null) {
            new UiAnnotationRenameProcessor<>().process(type, uiController, "io.jmix.flowui.view.ViewController");
        }
    }
}
