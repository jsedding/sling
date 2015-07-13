/*
 * Licensed to the Apache Software Foundation (ASF) under one or more
 * contributor license agreements.  See the NOTICE file distributed with
 * this work for additional information regarding copyright ownership.
 * The ASF licenses this file to You under the Apache License, Version 2.0
 * (the "License"); you may not use this file except in compliance with
 * the License.  You may obtain a copy of the License at
 *
 *      http://www.apache.org/licenses/LICENSE-2.0
 *
 * Unless required by applicable law or agreed to in writing, software
 * distributed under the License is distributed on an "AS IS" BASIS,
 * WITHOUT WARRANTIES OR CONDITIONS OF ANY KIND, either express or implied.
 * See the License for the specific language governing permissions and
 * limitations under the License.
 */
package org.apache.sling.maven.slingstart;

import java.io.File;

import org.apache.maven.execution.MavenSession;
import org.apache.maven.plugin.AbstractMojo;
import org.apache.maven.plugins.annotations.Component;
import org.apache.maven.plugins.annotations.Parameter;
import org.apache.maven.project.MavenProject;
import org.apache.maven.project.MavenProjectHelper;
import org.apache.sling.provisioning.model.ModelUtility.ResolverOptions;

/**
 * Base class for all slingstart mojos.
 */
public abstract class AbstractSlingStartMojo extends AbstractMojo {

    @Parameter(defaultValue="${basedir}/src/main/provisioning")
    private File modelDirectory;

    @Parameter(property = "project", readonly = true, required = true)
    protected MavenProject project;

    @Component
    protected MavenProjectHelper projectHelper;

    @Parameter(property = "session", readonly = true, required = true)
    protected MavenSession mavenSession;

    @Parameter(defaultValue="false")
    protected boolean createWebapp;

    /**
     * If set to true, properties from the Maven POM can be used as variables in the provisioning files.
     */
    @Parameter(defaultValue="false")
    protected boolean usePomVariables;
        
    /**
     * If set to true, Artifact dependencies from provisioning file without explict version are tried 
     * to be resolved against the dependency versions from the Maven POM.
     */
    @Parameter(defaultValue="false")
    protected boolean usePomDependencies;
        
    /**
     * If set to true, the effective provisioning models with all variables replaced is attached instead of the raw model.
     */
    @Parameter(defaultValue="false")
    protected boolean attachEffectiveModel;
        
    protected File getTmpDir() {
        return new File(this.project.getBuild().getDirectory(), "slingstart-tmp");
    }
    
    /**
     * @return Variable to be used when building an effective provisioning model.
     */
    protected ResolverOptions getResolverOptions() {
        ResolverOptions options = new ResolverOptions();
        if (usePomVariables) {
            options.variableResolver(new PomVariableResolver(project));
        }
        if (usePomDependencies) {
            options.artifactVersionResolver(new PomArtifactVersionResolver(project));
        }
        return options;
    }
    
}
