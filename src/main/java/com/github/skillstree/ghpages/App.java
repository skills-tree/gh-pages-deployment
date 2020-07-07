package com.github.skillstree.ghpages;

import java.io.File;
import java.io.FileWriter;
import java.io.IOException;
import java.util.Collections;
import java.util.HashMap;
import java.util.List;
import java.util.Objects;

import com.github.skillstree.core.model.SkillsTree;
import com.github.skillstree.core.transformer.SkillsToJsonTreeTransformer;
import com.github.skillstree.core.transformer.YamlToSkillsTransformer;
import org.slf4j.Logger;
import org.slf4j.LoggerFactory;

public class App {

    private static final Logger logger = LoggerFactory.getLogger(App.class);

    private static final List<String> REPO_FILES_TO_IGNORE =
            Collections.unmodifiableList(List.of(".git", ".gitignore"));

    public static void main(String[] args) throws IOException {
        File skillsDir = new File("java");

        File[] mainSkillDirs = skillsDir.listFiles(f ->
                f.isDirectory() && !REPO_FILES_TO_IGNORE.contains(f.getName()));
        Objects.requireNonNull(mainSkillDirs, "No root skills are found in the skills directory");

        for (File baseDir : mainSkillDirs) {
            logger.info("Parsing base directory for a root skill: " + baseDir.getName());

            YamlToSkillsTransformer tr = new YamlToSkillsTransformer(new HashMap<>());
            SkillsTree skillsTree = tr.transform(baseDir);

            SkillsToJsonTreeTransformer skillsToJsonTransformer = new SkillsToJsonTreeTransformer();
            String skillsTreeJson = skillsToJsonTransformer.transform(skillsTree);

            File stJson = new File("frontend/js/skillstree.json");
            FileWriter writer = new FileWriter(stJson, false);
            writer.write(skillsTreeJson);
            writer.close();
        }
    }
}
