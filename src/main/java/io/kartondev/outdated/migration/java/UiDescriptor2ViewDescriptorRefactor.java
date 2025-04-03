package io.kartondev.outdated.migration.java;

import io.kartondev.outdated.processor.UiAnnotationRenameProcessor;
import spoon.reflect.declaration.CtAnnotation;
import spoon.reflect.declaration.CtType;

import java.lang.annotation.Annotation;

public class UiDescriptor2ViewDescriptorRefactor implements JavaRefactor {

    @Override
    public boolean canRefactor(CtType<?> type) {
        return type.getAnnotations().stream()
                .anyMatch(a -> "io.jmix.ui.screen.UiDescriptor".equals(a.getAnnotationType().getQualifiedName()));
    }

    @Override
    public void makeRefactorOnTarget(CtType<?> type) {
        CtAnnotation<?> uiDescriptor = type.getAnnotations().stream()
                .filter(a -> "UiDescriptor".equals(a.getName()))
                .findFirst()
                .orElse(null);

        if (uiDescriptor != null) {
            new UiAnnotationRenameProcessor<Annotation>().process(type, uiDescriptor, "io.jmix.flowui.view.ViewDescriptor");
        }
    }
}

