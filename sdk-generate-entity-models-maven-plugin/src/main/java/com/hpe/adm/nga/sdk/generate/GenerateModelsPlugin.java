/*
 * Copyright 2017 EntIT Software LLC, a Micro Focus company, L.P.
 * Licensed under the Apache License, Version 2.0 (the "License");
 * you may not use this file except in compliance with the License.
 * You may obtain a copy of the License at
 *   http://www.apache.org/licenses/LICENSE-2.0
 *
 * Unless required by applicable law or agreed to in writing, software
 * distributed under the License is distributed on an "AS IS" BASIS,
 * WITHOUT WARRANTIES OR CONDITIONS OF ANY KIND, either express or implied.
 * See the License for the specific language governing permissions and
 * limitations under the License.
 */
package com.hpe.adm.nga.sdk.generate;

import java.io.File;
import java.io.IOException;

import org.apache.maven.plugin.AbstractMojo;
import org.apache.maven.plugin.MojoExecutionException;
import org.apache.maven.plugin.MojoFailureException;
import org.apache.maven.plugins.annotations.LifecyclePhase;
import org.apache.maven.plugins.annotations.Mojo;
import org.apache.maven.plugins.annotations.Parameter;

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
			new GenerateModels(generatedSourcesDirectory).generate(clientId, clientSecret, server, sharedSpace,
					workSpace);
		} catch (IOException e) {
			throw new MojoExecutionException("Problem generating entities", e);
		}
		getLog().info("Finished generating entities");
	}
}
