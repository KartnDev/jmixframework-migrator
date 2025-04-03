package io.kartondev;


import io.kartondev.feature.buildsys.GradleLinker;
import io.kartondev.outdated.migration.ClassicASTContext;
import io.kartondev.outdated.migration.Screen2ViewMigrationStrategy;
import io.kartondev.outdated.util.MigrationUtils;
import spoon.Launcher;
import spoon.reflect.declaration.CtType;
import spoon.support.compiler.FileSystemFolder;

import java.io.File;

public class Main {
    public static ClassicASTContext classicASTContextGlobal;

    public static void main(String[] args) {
        var projectDir = "C:\\Users\\dmutp\\IdeaProjects\\jmix15";

        var gradleLinker = new GradleLinker(new File(projectDir));
        var projectLibs = gradleLinker.getDependentRepos();

        var launcher = new Launcher();
        launcher.getEnvironment().setNoClasspath(false);
        launcher.addInputResource(new FileSystemFolder(new File(projectDir + "/src/main/java")));
        launcher.getEnvironment().setSourceClasspath(projectLibs.toArray(new String[0]));
        launcher.buildModel();

        var factory = launcher.getFactory();

        classicASTContextGlobal = new ClassicASTContext(MigrationUtils.searchMainViewClass(factory));

        Screen2ViewMigrationStrategy[] strategies = {new Screen2ViewMigrationStrategy()};

        for (CtType<?> ctClass : factory.Class().getAll()) {
            for (Screen2ViewMigrationStrategy strategy : strategies) {
                if (strategy.isSupport(ctClass)) {
                    strategy.migrate(ctClass);
                }
            }
        }

        launcher.getEnvironment().setSourceOutputDirectory(new File("~/IdeaProjects/jmix15/src/main/java"));
        launcher.getEnvironment().setAutoImports(true);
        launcher.prettyprint();
    }


}