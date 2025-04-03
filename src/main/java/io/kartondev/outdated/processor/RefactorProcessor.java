package io.kartondev.outdated.processor;

import spoon.reflect.declaration.CtType;

public interface RefactorProcessor<TARGET, CONTEXT> {
    void process(CtType<?> unit, TARGET target, CONTEXT context);
}
