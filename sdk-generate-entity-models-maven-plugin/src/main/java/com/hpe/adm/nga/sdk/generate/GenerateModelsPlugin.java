package com.hpe.adm.nga.sdk.generate;

import org.apache.maven.plugin.AbstractMojo;
import org.apache.maven.plugin.MojoExecutionException;
import org.apache.maven.plugin.MojoFailureException;
import org.apache.maven.plugins.annotations.LifecyclePhase;
import org.apache.maven.plugins.annotations.Mojo;
import org.apache.maven.plugins.annotations.Parameter;

import java.io.File;
import java.io.IOException;

@Mojo(name = "generate", defaultPhase = LifecyclePhase.GENERATE_SOURCES, threadSafe = true)
public class GenerateModelsPlugin extends AbstractMojo {

    @Parameter(defaultValue = "${project.build.directory}/generated-sources")
    private File generatedSourcesDirectory;
    @Parameter(required = true)
    private String clientId;
    @Parameter(required = true)
    private String clientSecret;
    @Parameter(required = true)
    private String server;
    @Parameter(required = true)
    private long sharedSpace;
    @Parameter(required = true)
    private long workSpace;

    @Override
    public void execute() throws MojoExecutionException, MojoFailureException {
        getLog().info("Starting to generate entities");
        try {
            new GenerateModels(generatedSourcesDirectory).generate(clientId, clientSecret, server, sharedSpace, workSpace);
        } catch (IOException e) {
            throw new MojoExecutionException("Problem generating entities", e);
        }
        getLog().info("Finished generating entities");
    }
}
