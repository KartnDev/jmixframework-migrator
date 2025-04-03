package io.kartondev.outdated.processor;

import lombok.AllArgsConstructor;
import lombok.Data;
import lombok.NoArgsConstructor;
import spoon.reflect.code.CtExpression;
import spoon.reflect.declaration.*;
import spoon.reflect.factory.Factory;
import spoon.reflect.reference.CtTypeReference;

import java.lang.annotation.Annotation;
import java.util.Map;

public class AnnotationChangeParametersProcessor<A extends Annotation> {

    public void process(
            CtType<?> unit,
            String newAnnotationName,
            Map<String, MethodParams> newParameters,
            CtAnnotation<? extends A> targetAnnotation
    ) {
        Factory factory = unit.getFactory();

        @SuppressWarnings("unchecked")
        CtAnnotationType<A> annotationType = (CtAnnotationType<A>) factory.Annotation().create(newAnnotationName);

        for (Map.Entry<String, MethodParams> entry : newParameters.entrySet()) {
            CtAnnotationMethod<CtMethod<Object>> newMethod = factory.Core().createAnnotationMethod();
            newMethod.setSimpleName(entry.getValue().name);
            newMethod.setType(entry.getValue().type);

            if (entry.getValue().ctExpression != null) {
                //noinspection unchecked
                newMethod.setDefaultExpression((CtExpression<CtMethod<Object>>) entry.getValue().ctExpression);
            }

            annotationType.addMethod(newMethod);
        }

        factory.Annotation().annotate(unit, annotationType.getReference());

        if (targetAnnotation != null) {
            unit.removeAnnotation(targetAnnotation);
        }
    }

    @Data
    public static class MethodParams {
        public final String name;
        public final CtTypeReference<?> type;
        public final CtExpression<?> ctExpression;
    }
}
