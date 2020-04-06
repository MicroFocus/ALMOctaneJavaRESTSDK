# ALM Octane REST API Java SDK

#### Maven
```xml
<dependency>
    <groupId>com.microfocus.adm.almoctane.sdk</groupId>
    <artifactId>sdk-src</artifactId>
    <version>15.0.60-SNAPSHOT</version>
</dependency>
```
#### Gradle
```groovy
compile group: 'com.microfocus.adm.almoctane.sdk', name: 'sdk-src', version: '15.0.60-SNAPSHOT'
```

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

To enable this add the following to your project's POM file (assuming 15.0.60-SNAPSHOT being the SDK version):

```xml
 <build>
        <plugins>
            <plugin>
                <groupId>com.microfocus.adm.almoctane.sdk</groupId>
                <artifactId>sdk-generate-entity-models-maven-plugin</artifactId>
                <version>15.0.60-SNAPSHOT</version>
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

The generator will create three directories under the `com.hpe.adm.nga.sdk` package:

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

Please note that due to the way that the Octane REST API works only a limited number of fields will be automatically retrieved from the server.  Due to this the `addFields` method
should be used to explicitly state which fields should be retrieved.  You can see more information [here](https://admhelp.microfocus.com/octane/en/latest/Online/Content/API/fields_clause.htm)

## Logging

The SDK uses [SLF4J](https://www.slf4j.org/) internally for all logging. This means that the users of the library can control the logging framework used for the implementation. 
The easiest way is to add a maven dependency to such an implementation (slf4j-simple, log4j, logback etc.)

### Example
```xml
    <dependencies>
        <dependency>
            <groupId>com.microfocus.adm.almoctane.sdk</groupId>
            <artifactId>sdk-src</artifactId>
            <version>15.0.60-SNAPSHOT</version>
        </dependency>
        <dependency>
            <groupId>org.slf4j</groupId>
            <artifactId>slf4j-log4j12</artifactId>
            <version>1.7.25</version>
        </dependency>
    </dependencies>
```
This will make the sdk use log4j as an slf4j implementation, configuring a log4j.xml in your project will also take effect on the sdk.

## Space and Workspace admin
By various combinations of not setting the space id, or setting the space id and not the workspace id, the admin of those
spaces can be accessed.  See the `TestSharedSpaceAdmin` and `TestWorkSpaceAdmin` tests for examples of how these can be used.

Currently the admin sections are not available using generated entities - but the CRUD functions are available

## What's New
* 15.0.60
  * Manipulate test scripts using the SDK.  See `TestExample` in the `sdk-usage-examples` module for more information
  * Able to get the context for space and workspace admins
* 15.0.40.1
  * FIX for [Bug 79](https://github.com/MicroFocus/ALMOctaneJavaRESTSDK/issues/79).  User defined lists are now created with a `_` in front of the
  package name when using the generator to ensure Java convention is followed
* 15.0.40
  * Get the server version using the `SiteAdmin` API.  This matches the `serverurl/admin/server/version` REST call
  * The way that the API mode can be set has changed.  It is now easier to set the technical preview by using the *APIMode* interface and 
  default classes.  Other modes can be set as well.  See Javadoc for more details
* 15.0.20
  * Lists are now created in their own classes.  The package name is based on the list's logical name.  This was necessary due to non-unique list names.   In order
  to try to preserve backward compatibility the actual class name should be the same but are now in separate packages.  That means that in the best case only
  imports need to be changed.
    * In addition to this list names need to conform to Java standards. If a list name starts with an illegal character such as a number then the name will start with 
    a '$'.   
  * Due to a bug on Octane - the *run_history* entity's ID is marked as an integer as opposed to a string.  This causes an issue in the entity generation.
  Therefore the *run_history* entity will not be generated until this bug is fixed in Octane
* 12.60.41
  * Float fields now supported via FloatFieldModel if they are enabled on the Octane server.
  * Change to maven group id: com.microfocus.adm.almoctane.sdk
* 12.60.21
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
  * [SDK extension](https://github.com/Microfocus/sdk-extension) moved, now part of the sdk repository, 
  * SDK now uses SLF4J internally for all logging
  
  See the ALM Octane documentation for more information
  
## Disclaimer
  
Certain versions of software accessible here may contain branding from Hewlett-Packard Company (now HP Inc.) and Hewlett Packard Enterprise Company.  As of September 1, 2017, the software is now offered by Micro Focus, a separately owned and operated company.  Any reference to the HP and Hewlett Packard Enterprise/HPE marks is historical in nature, and the HP and Hewlett Packard Enterprise/HPE marks are the property of their respective owners. 
