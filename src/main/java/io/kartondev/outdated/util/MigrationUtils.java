package io.kartondev.outdated.util;

import spoon.reflect.declaration.CtAnnotation;
import spoon.reflect.declaration.CtClass;
import spoon.reflect.declaration.CtType;
import spoon.reflect.factory.Factory;
import spoon.support.reflect.code.CtLiteralImpl;

import java.util.List;

public class MigrationUtils {
    public static boolean isRootRouteAnnotation(CtAnnotation<?> routeAnnotation) {
        CtClass<?> parent = (CtClass<?>) routeAnnotation.getParent();

        Object pathFromRouteAnnotationExpression = routeAnnotation.getValues().getOrDefault("value", routeAnnotation.getValues().get("path"));

        String path = buildPathForScreen(
                parent.getSimpleName(),
                parent.getSuperclass() != null && "StandardEditor".equals(parent.getSuperclass().getSimpleName()),
                pathFromRouteAnnotationExpression instanceof CtLiteralImpl ? ((CtLiteralImpl<?>) pathFromRouteAnnotationExpression).getValue().toString() : null
        );

        return !"login".equals(path) && routeAnnotation.getValues().containsKey("root") &&
                routeAnnotation.getValues().get("root") instanceof CtLiteralImpl &&
                (Boolean) ((CtLiteralImpl<?>) routeAnnotation.getValues().get("root")).getValue();
    }

    public static boolean isMainRoute(CtAnnotation<?> annotation) {
        return isRootRouteAnnotation(annotation);
    }

    public static CtType<?> searchMainViewClass(Factory factory) {
        return factory.Class().getAll().stream()
                .filter(ctType -> ctType.getSuperclass() != null &&
                        List.of("StandardLookup", "StandardEditor", "StandardDetailView", "StandardScreen", "Screen")
                                .contains(ctType.getSuperclass().getSimpleName()))
                .filter(ctType -> ctType.getAnnotations().stream().anyMatch(MigrationUtils::isRootRouteAnnotation))
                .findFirst()
                .orElse(null);
    }


    public static String buildPathForScreen(String screenClassName, boolean isEditorScreen, String pathForScreen) {
        String domainNameFromPath = (pathForScreen != null) ? pathForScreen.replaceAll("^\"|\"$", "").trim().split("/")[0] : "";

        String rawDomain = (domainNameFromPath != null && !domainNameFromPath.isBlank()) ? domainNameFromPath :
                screenClassName.replaceAll("(Browse|Browser|Edit|Editor)$", "");

        String domain = rawDomain.substring(0, 1).toLowerCase() + rawDomain.substring(1);

        return isEditorScreen ? domain + "/:id" : domain;
    }
}
