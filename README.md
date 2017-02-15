# ALM Octane REST API Java SDK

## Introduction

A Java SDK that can be used to connect to ALM Octane's REST API.  See the Javadoc for more information of how to use the SDK.
See also the REST API documentation for more details about Octane's API.

This project has three sub-projects:

1. sdk-src which is the main source of the Java SDK
2. sdk-integration-tests which can be run to test the SDK against your Octane server
3. sdk-usage-examples which contain some simple examples as to how to use the SDK

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