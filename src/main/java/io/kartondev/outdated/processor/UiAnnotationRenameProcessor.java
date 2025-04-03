package io.kartondev.outdated.processor;

import java.lang.annotation.Annotation;

import io.kartondev.outdated.util.FlowUiTermsStringExtensions;
import spoon.reflect.declaration.*;
import spoon.reflect.factory.Factory;
import spoon.support.reflect.code.CtLiteralImpl;

public class UiAnnotationRenameProcessor<A extends Annotation> implements RefactorProcessor<CtAnnotation<? extends A>, String> {

    @Override
    public void process(CtType<?> unit, CtAnnotation<? extends A> targetAnnotation, String contextNewAnnotationName) {
        Factory factory = targetAnnotation.getFactory();

        @SuppressWarnings("unchecked")
        CtAnnotationType<A> annotationType = (CtAnnotationType<A>) factory.Annotation().create(contextNewAnnotationName);

        CtAnnotationMethod<CtMethod<String>> newMethod = factory.Core().createAnnotationMethod();
        newMethod.setSimpleName("value");
        newMethod.setType(factory.Type().createReference(String.class));
        newMethod.setDefaultExpression(factory.createCodeSnippetExpression("\"\""));

        annotationType.addMethod(newMethod);

        CtAnnotation<A> annotationValue = factory.Annotation().annotate(unit, annotationType.getReference());

        //noinspection unchecked
        String controllerName = ((CtLiteralImpl<String>) targetAnnotation.getValue("value")).getValue();
        String updatedControllerName = FlowUiTermsStringExtensions.toRefactoredFlowUiControllerName(controllerName);

        annotationValue.addValue("value", updatedControllerName);
        unit.removeAnnotation(targetAnnotation);
    }
}
