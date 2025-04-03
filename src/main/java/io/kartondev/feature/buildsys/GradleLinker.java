package io.kartondev.feature.buildsys;

import io.kartondev.cache.CacheService;
import lombok.extern.slf4j.Slf4j;
import org.gradle.tooling.GradleConnector;
import org.gradle.tooling.model.idea.IdeaProject;
import org.gradle.tooling.model.idea.IdeaSingleEntryLibraryDependency;

import java.io.*;
import java.util.ArrayList;
import java.util.List;

@Slf4j
public class GradleLinker {
    private final File projectPath;
    private final CacheService cacheService = CacheService.getInstance();

    public GradleLinker(File file) {
        this.projectPath = file;
    }

    public List<String> getDependentRepos() {
        // todo non-cross-platform
        String projectName = projectPath.getAbsolutePath()
                .replace("\\", "/")
                .split("/")[projectPath.getAbsolutePath().split("/").length - 1];

        if(cacheService.exists(projectName)) {
            return cacheService.get(projectName);
        }

        return collectDependentRepos();
    }

    public List<String> collectDependentRepos() {
        List<String> listOfDependencies = new ArrayList<>();

        try (var connection = GradleConnector.newConnector().forProjectDirectory(projectPath).connect()) {
            IdeaProject ideaProject = connection.getModel(IdeaProject.class);

            for (var module : ideaProject.getModules()) {
                for (var dep : module.getDependencies()) {
                    if (dep instanceof IdeaSingleEntryLibraryDependency) {
                        File libFile = ((IdeaSingleEntryLibraryDependency) dep).getFile();
                        listOfDependencies.add(libFile.getPath());
                    }
                }
            }
        }

        return listOfDependencies;
    }
}
