package io.kartondev.outdated.migration.java;

import io.kartondev.Main;
import io.kartondev.outdated.processor.AnnotationChangeParametersProcessor;
import io.kartondev.outdated.util.MigrationUtils;
import spoon.reflect.declaration.CtAnnotation;
import spoon.reflect.declaration.CtType;
import spoon.reflect.factory.Factory;
import spoon.reflect.reference.CtTypeReference;

import java.lang.annotation.Annotation;
import java.util.Arrays;
import java.util.Map;

public class ClassicRoute2FlowRouteRefactor implements JavaRefactor {

    @Override
    public boolean canRefactor(CtType<?> type) {
        return type.getSuperclass() != null &&
                Arrays.asList("StandardLookup", "StandardEditor", "StandardDetailView", "StandardScreen", "Screen")
                        .contains(type.getSuperclass().getSimpleName());
    }

    @Override
    public void makeRefactorOnTarget(CtType<?> type) {
        CtAnnotation<?> annotation = type.getAnnotations().stream()
                .filter(a -> "Route".equals(a.getName()))
                .findFirst()
                .orElse(null);

        if (annotation != null && MigrationUtils.isMainRoute(annotation)) {
            refactorMainRoute(type, annotation);
        } else {
            refactorNonMainRoute(type, annotation);
        }
    }

    private <T extends Annotation> void refactorNonMainRoute(CtType<?> type, CtAnnotation<T> annotation) {
        Factory factory = type.getFactory();
        String newPath = buildPathForScreenClass(type, annotation);
        CtTypeReference<Class> classRef = factory.Class().createReference(Class.class);

        new AnnotationChangeParametersProcessor<T>().process(
                type,
                "com.vaadin.flow.router.Route",
                Map.of(
                        "value", new AnnotationChangeParametersProcessor.MethodParams("value", factory.Type().STRING, factory.createCodeSnippetExpression("")),
                        "layout", new AnnotationChangeParametersProcessor.MethodParams("layout", classRef, factory.createCodeSnippetExpression("UI.class"))
                ),
                annotation
        );

        CtType<?> mainLayoutClass = Main.classicASTContextGlobal.rootScreen();
        CtAnnotation<?> newAnnotation = type.getAnnotations().stream()
                .filter(a -> "Route".equals(a.getName()))
                .findFirst()
                .orElse(null);

        if (newAnnotation != null) {
            newAnnotation.addValue("value", factory.Code().createLiteral(newPath));
            newAnnotation.addValue("layout", factory.Code().createClassAccess(mainLayoutClass.getReference()));
        }
    }

    private <T extends Annotation> String buildPathForScreenClass(CtType<?> type, CtAnnotation<T> annotation) {
        boolean isEditorScreen = "StandardEditor".equals(type.getSuperclass().getSimpleName());
        Object pathFromAnnotation = annotation != null ? annotation.getValues().get("value") : null;
        return MigrationUtils.buildPathForScreen(type.getSimpleName(), isEditorScreen, pathFromAnnotation != null ? pathFromAnnotation.toString() : null);
    }

    private <T extends Annotation> void refactorMainRoute(CtType<?> type, CtAnnotation<T> annotation) {
        Factory factory = annotation.getFactory();

        new AnnotationChangeParametersProcessor<T>().process(
                type,
                "com.vaadin.flow.router.Route",
                Map.of("value", new AnnotationChangeParametersProcessor.MethodParams("value", factory.Type().STRING, factory.createCodeSnippetExpression("\"\""))),
                annotation
        );

        type.getAnnotations().stream()
                .filter(a -> "Route".equals(a.getName()))
                .findFirst()
                .ifPresent(newAnnotation ->
                        newAnnotation.addValue("value", factory.Code().createLiteral("")));

    }
}

