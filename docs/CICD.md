# Continuous Integration and Continuous Deployment

- Continuous Integration: TravisCI
- Continuous Delivery/Deployment: Heroku

## TravisCI

- Create an Account in TravisCI, link it to your GitHub and to the project containing the .travis.yaml file.

Using Travis for Continuous Integration (Integrate branches into a main branch, automatic tests, automatic builds)

To do it, we need to add this `.travis.yml` file with the following content:

```yaml
dist: xenial
language: java
sudo: false
jdk: openjdk11
install: true
cache:
  directories:
    - @HOME/.m2
before_install:
  - chmod +x mvnw
```

## Heroku

Create a Heroku account and Connect to your repository.

- Enable Automatic Deployment and Check the CI flag, to allow pass only if CI has succeeded.

By default, Heroku uses Java 8. In this project we are using Java 11, so we need to create a configuration:

- Create a file called system.properties in root of the project
- in this file add the java.runtime.version:

```properties
java.runtime.version=11
```

Now Heroku is able to match the version.

We can also set the default springboot profile to run on Heroku, by adding the following property on system.properties
file:

```properties
spring.profiles.active=prod
```

## SonarCloud
Sonar will ensure our code quality and patterns.

Create an account into SonarCloud and an Organization. After that select the repository.

We need to integrate the sonar with our CI process, on Travis CI. To do it, we need to generate a token on SonarCloud
and add as Environment Variable on our TravisCI project (on travis website).

```properties
SONAR_ORGANIZATION = MY_SONAR_ORGANIZATION_NAME
SONAR_TOKE = MY_SONAR_GENERATED_TOKEN
```

After all configurations, inside our project, on our .travis.yml we need to add this env. variables:

```yaml
addons:
  sonarcloud: 
    organization: $SONAR_ORGANIZATION
    token:
      secure: $SONAR_TOKEN
```

We also need to create an execution profile to maven setting up jacoco.

```xml
<profiles>
        <profile>
            <id>sonar</id>
            <activation>
                <activeByDefault>true</activeByDefault>
            </activation>
            <properties>
                <sonar.host.url>https://sonarcloud.io/</sonar.host.url>
                <sonar.exclusions>
                    src/main/kotlin/**/BookstoremanagerApplication.kt,
                    src/main/kotlin/**/config/**,
                    src/main/kotlin/**/exception/**,
                    src/main/kotlin/**/dto/**,
                    src/main/kotlin/**/entity/**,
                    src/main/kotlin/**/repository/**,
                    src/test/kotlin/**
                </sonar.exclusions>
            </properties>
            <build>
                <plugins>
                    <plugin>
                        <groupId>org.jacoco</groupId>
                        <artifactId>jacoco-maven-plugin</artifactId>
                        <version>0.8.7</version>
                        <executions>
                            <execution>
                                <goals>
                                    <goal>prepare-agent</goal>
                                </goals>
                            </execution>
                            <execution>
                                <id>report</id>
                                <phase>prepare-package</phase>
                                <goals>
                                    <goal>report</goal>
                                </goals>
                            </execution>
                            <execution>
                                <id>jacoco-check</id>
                                <goals>
                                    <goal>check</goal>
                                </goals>
                                <configuration>
                                    <rules>
                                        <rule>
                                            <limits>
                                                <limit>
                                                    <minimum>0.1</minimum>
                                                </limit>
                                            </limits>
                                        </rule>
                                    </rules>
                                </configuration>
                            </execution>
                        </executions>
                    </plugin>
                </plugins>
            </build>
        </profile>
    </profiles>
```