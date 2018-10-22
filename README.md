# ALM Octane REST API Java SDK

## Introduction

A Java SDK that can be used to connect to ALM Octane's REST API.  See the Javadoc for more information of how to use the SDK.
See also the REST API documentation for more details about Octane's API.

This has multiple sub-projects:

1. sdk-src which is the main source of the Java SDK
2. sdk-integration-tests which can be run to test the SDK against your Octane server
3. sdk-usage-examples which contain some simple examples as to how to use the SDK
4. sdk-generate-entity-models-maven-plugin which contains a maven plugin that generates POJO's for your servers Octane entities [see "Entity Generation"](#entity-generation)
5. sdk-extension which provides some tools to access more of the sdk-src's underlying implementation 

The easiest way to compile the project is to use [maven](https://maven.apache.org/) and run the command:

```
mvn clean install
```

from the root directory.

## Creating JavaDoc

In order to create javadoc run the following maven command from the `sdk-src` directory:

```
mvn javadoc:javadoc
```

This will create a javadoc site in the `sdk-src/target/site/apidocs` directory

## Entity Generation

You can generate entities based on your server's metadata using the `sdk-generate-entity-models-maven-plugin` plugin.
This plugin connects to your ALM Octane server using the given authentication credentials, shared space and work space
and generates strongly typed entities that can be used instead of the generic out of the box entity that comes
with the SDK.

To enable this add the following to your project's POM file (assuming 12.60.21 being the SDK version):

```xml
 <build>
        <plugins>
            <plugin>
                <groupId>com.microfocus.adm.octane.sdk</groupId>
                <artifactId>sdk-generate-entity-models-maven-plugin</artifactId>
                <version>12.60.21</version>
                <executions>
                    <execution>
                        <phase>generate-sources</phase>
                        <goals>
                            <goal>generate</goal>
                        </goals>
                    </execution>
                </executions>
                <configuration>
                    <clientId>client_id</clientId>
                    <clientSecret>client_secret</clientSecret>
                    <server>http[s]://server[:port]</server>
                    <sharedSpace>SSID</sharedSpace>
                    <workSpace>WSID</workSpace>
                    <!--
                        By default the plugin will generate the sources to the generated-source directory under
                        the target.  If you wish to place this in a different place then use this parameter
                    -->
                    <!-- <generatedSourcesDirectory></generatedSourcesDirectory> -->
                </configuration>
            </plugin>
            <plugin>
                <groupId>org.codehaus.mojo</groupId>
                <artifactId>build-helper-maven-plugin</artifactId>
                <version>1.9.1</version>
                <executions>
                    <execution>
                        <id>addGeneratedEntitiesSources</id>
                        <phase>generate-sources</phase>
                        <goals>
                            <goal>add-source</goal>
                        </goals>
                        <configuration>
                            <sources>
                                <!--
                                  This assumes that you leave the configuration of the plugin above
                                  to use the maven generated-sources directory as the place where the generated
                                  sources will be
                                -->
                                <source>${project.build.directory}/generated-sources</source>
                            </sources>
                        </configuration>
                    </execution>
                </executions>
            </plugin>
            <!--
                The sources need to be compiled using Java 8 at least.  Use this if this needs to 
                be explicit
            -->
            <plugin>
                <artifactId>maven-compiler-plugin</artifactId>
                <version>3.5.1</version>
                <configuration>
                    <source>1.8</source>
                    <target>1.8</target>
                </configuration>
            </plugin>
        </plugins>
    </build>
```

Alternatively you can use the maven command line command to generate.  See the maven documentation for more details as to
 how to do this.
 
### Generated Directories

The generator will create three directories under the `com.microfocus.adm.octane.sdk` package:

* entities
* enums
* model

With the generated code.  Do not edit the generated code since it will be overwritten each time you run a maven build.

### Using the generated code

See the following example for how to use the generated code:

```java
        final Octane octane = new Octane.Builder(...).build();
        final WorkItemRootEntityModel workItemRootEntityModel = octane.entityList(WorkItemRootEntityList.class).at(1001).get().execute();

        final EpicEntityModel rootEpic = octane.entityList(EpicEntityList.class).create().entities(Collections.singleton(
                new EpicEntityModel("rootepic", workItemRootEntityModel, Phases.EpicPhase.NEW)
        )).execute().iterator().next();
        final FeatureEntityModel featureEntityModel = octane.entityList(FeatureEntityList.class).create().entities(Collections.singleton(
                new FeatureEntityModel("feature1", Phases.FeaturePhase.NEW).setParent(rootEpic)
        )).execute().iterator().next();
        final DefectEntityList defectEntityList = octane.entityList(DefectEntityList.class);
        final DefectEntityModel defect = defectEntityList.create().entities(Collections.singleton(
                new DefectEntityModel("defect", featureEntityModel, Phases.DefectPhase.DEFERRED)
        )).execute().iterator().next();
        defect.setSeverity(Lists.Severity.HIGH).setDescription("this has been updated");
        defectEntityList.update().entities(Collections.singleton(defect)).execute();
```

## Logging

The SDK uses [SLF4J](https://www.slf4j.org/) internally for all logging. This means that the users of the library can control the logging framework used for the implementation. 
The easiest way is to add a maven dependency to such an implementation (slf4j-simple, log4j, logback etc.)

### Example
```xml
    <dependencies>
        <dependency>
            <groupId>com.microfocus.adm.octane.sdk</groupId>
            <artifactId>sdk-src</artifactId>
            <version>12.60.21</version>
        </dependency>
        <dependency>
            <groupId>org.slf4j</groupId>
            <artifactId>slf4j-log4j12</artifactId>
            <version>1.7.25</version>
        </dependency>
    </dependencies>
```
This will make the sdk use log4j as an slf4j implementation, configuring a log4j.xml in your project will also take effect on the sdk.

## Upgrading from 12.60.16 to 12.60.21

### Maven group id change

Maven group id changed from "com.hpe.adm.nga.sdk" to "com.microfocus.adm.octane.sdk" starting from version 12.60.21

```xml
    <dependencies>
        <dependency>
            <groupId>com.microfocus.adm.octane.sdk</groupId>
            <artifactId>sdk-src</artifactId>
            <version>12.60.21</version>
        </dependency>
    </dependencies>
```

### Updating your code to be compatible with new version of the sdk

Package names changed from "com.hpe.adm.nga.sdk" to "com.microfocus.adm.octane.sdk"

Use your IDE or any text editor to find and replace "com.hpe.adm.nga.sdk" to "com.microfocus.adm.octane.sdk".

This should replace import statements in java files and the group ids in the pom.xml / build.grade files, your code should now compile again.

## What's New
* 12.60.21
  * Re-branding from HPE to Microfocus
* 12.60.16
  * Fixed bug where etag header was not being set properly
  * Fixed null pointer if the sdk encountered un-parsable entity JSON
  * Octane.OctaneBuilder now has constructor that allows passing a specific instance of OctaneHttpClient for the Octane object
* 12.55.32
  * Octane server errors will not be properly parsed into an ErrorModel object
  * ErrorModels from OctaneExceptions will now also contain the HTTP status code
  * Added ObjectFieldModel for fields that contain JSON content but do not represent a relation to another entity model
  * Added helper methods for comparing entity models
* 12.55.8
  * Fixed various bugs related to entity generation
  * Fixed bug related to http session handling
  * HTTP proxy settings are now logged
* 12.55.5
  * Added Entity Generation
  * Added Etag support.  If the server resource has an etag then it is cached by the SDK for as long as the process is alive.
  The cache is destroyed once the process ends
  * All entity ids are now Strings.  IDs are strings according to the metadata but there was some code that assumed IDs were
   integers
  * Entity collection now returns an `OctaneCollection` which is an extension of `Collection`.  This includes important 
  metadata about the returned collection:
    * The total count of entities (not including the current limit)
    * Whether the number of requested entities exceeds the total count of entities.
  * Added support for IN and BTW for queries
  * [SDK extension](https://github.com/HPSoftware/sdk-extension) moved, now part of the sdk repository, 
  * SDK now uses SLF4J internally for all logging
  
  See the ALM Octane documentation for more information
  
## Disclaimer
  
Certain versions of software accessible here may contain branding from Hewlett-Packard Company (now HP Inc.) and Hewlett Packard Enterprise Company.  As of September 1, 2017, the software is now offered by Micro Focus, a separately owned and operated company.  Any reference to the HP and Hewlett Packard Enterprise/HPE marks is historical in nature, and the HP and Hewlett Packard Enterprise/HPE marks are the property of their respective owners. 
