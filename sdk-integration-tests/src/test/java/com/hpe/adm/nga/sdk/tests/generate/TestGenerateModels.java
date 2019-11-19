package com.hpe.adm.nga.sdk.tests.generate;

import com.hpe.adm.nga.sdk.authentication.Authentication;
import com.hpe.adm.nga.sdk.generate.GenerateModels;
import com.hpe.adm.nga.sdk.utils.AuthenticationUtils;
import com.hpe.adm.nga.sdk.utils.ConfigurationUtils;
import org.apache.commons.io.FileUtils;
import org.junit.Assert;
import org.junit.Test;

import javax.tools.*;
import java.io.File;
import java.io.IOException;
import java.util.ArrayList;
import java.util.Collection;
import java.util.List;
import java.util.Locale;

public class TestGenerateModels {

    @Test
    public void testGenerateModels() throws IOException {
        final File generatedSourcesDirectory = new File("target/test-test-sources");
        FileUtils.deleteDirectory(generatedSourcesDirectory);
        //noinspection ResultOfMethodCallIgnored
        generatedSourcesDirectory.mkdirs();

        generateSources(generatedSourcesDirectory);
        compileClasses(generatedSourcesDirectory);
    }

    private void compileClasses(File generatedDirectory) {
        final Collection<File> files = FileUtils.listFiles(generatedDirectory, null, true);

        DiagnosticCollector<JavaFileObject> diagnostics = new DiagnosticCollector<>();
        JavaCompiler compiler = ToolProvider.getSystemJavaCompiler();
        StandardJavaFileManager fileManager = compiler.getStandardFileManager(diagnostics, null, null);
        List<String> optionList = new ArrayList<>();
        optionList.add("-classpath");
        optionList.add(System.getProperty("java.class.path"));

        Iterable<? extends JavaFileObject> compilationUnit
                = fileManager.getJavaFileObjectsFromFiles(files);
        compiler.getTask(
                null,
                fileManager,
                diagnostics,
                optionList,
                null,
                compilationUnit).call();

        boolean hasError = false;
        final StringBuilder stringBuilder = new StringBuilder();
        for (Diagnostic<? extends JavaFileObject> diagnostic : diagnostics.getDiagnostics()) {
            if (diagnostic.getKind() == Diagnostic.Kind.ERROR) {
                hasError = true;
                stringBuilder.append(String.format("Error on line %d in %s: %s%n",
                        diagnostic.getLineNumber(),
                        diagnostic.getSource().toUri(),
                        diagnostic.getMessage(Locale.getDefault())));
            }
        }

        if (hasError) {
            Assert.fail(stringBuilder.toString());
        }
    }

    private void generateSources(File generatedDirectory) {
        final ConfigurationUtils configuration = ConfigurationUtils.getInstance();
        String url = configuration.getString("sdk.url");
        Authentication authentication = AuthenticationUtils.getAuthentication(false);
        String sharedSpaceId = configuration.getString("sdk.sharedSpaceId");
        String workspaceId = configuration.getString("sdk.workspaceId");

        try {
            new GenerateModels(generatedDirectory).generate(authentication, url, Long.parseLong(sharedSpaceId), Long.parseLong(workspaceId));
        } catch (Exception e) {
            Assert.fail("Test failed whilst building test sources; "+e.getMessage());
        }
    }
}
