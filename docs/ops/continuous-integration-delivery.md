# Continuous Integration and Continuous Deployment
- Continuous Integration: TravisCI
- Continuous Delivery/Deployment: Heroku

## TravisCI
- Create an Account in TravisCI, link it to your GitHub and to the project containing the .travis.yaml file.

Using Travis for Continuous Integration (Integrate branches into a main branch, automatic tests, automatic builds)

To do it, we need to add this `.travis.yml` file with the following content:
This is the Maven config:
```yaml
dist: xenial
language: java
sudo: false
install: true
jdk:
  - openjdk11
cache:
  directories:
    - $HOME/.m2
before_install:
  - chmod +x mvnw
```

This is the gradle config:
```yaml
dist: xenial
language: java
sudo: false
install: true
jdk:
  - openjdk11
before_install:
  - chmod +x gradlew
before_cache:
  - rm -f  $HOME/.gradle/caches/modules-2/modules-2.lock
  - rm -fr $HOME/.gradle/caches/*/plugin-resolution/
cache:
  directories:
    - $HOME/.m2
    - $HOME/.gradle/caches/
    - $HOME/.gradle/wrapper/
```

If we want to add an [environment variable](https://docs.travis-ci.com/user/environment-variables/) we just need to add on .travis.yml file, such as:

```yaml
env:
  - SPRING_PROFILES_ACTIVE=ci
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

We can also set the default profile to run on Heroku, by adding the following property on system.properties file:

```properties
spring.profiles.active=prod
```

Using maven these configs should be enough. If you are using gradle, you have to add a Procfile with the following content

```properties
java -jar -Dspring.profiles.active=prod -Dserver.port=$PORT $JAVA_OPTS build/libs/bookstore-manager.jar
```
[How to](https://devcenter.heroku.com/articles/deploying-gradle-apps-on-heroku) Deploy gradle app in Heroku.

---
### Bonus

To change the default app name, on build.gradle, you can add:
```groovy
tasks {
    bootJar {
        archiveFileName.set("bookstore-manager.jar")
    }
}
```

### Files

The final .travis.yaml file, with all configurations:

```yaml
dist: xenial
language: java
sudo: false
install: true
jdk:
  - openjdk11
before_install:
  - chmod +x gradlew
  - ./gradlew assemble
before_cache:
  - rm -f  $HOME/.gradle/caches/modules-2/modules-2.lock
  - rm -fr $HOME/.gradle/caches/*/plugin-resolution/
cache:
  directories:
    - $HOME/.m2
    - $HOME/.gradle/caches/
    - $HOME/.gradle/wrapper/
    - $HOME/.sonar/cache/
addons:
  sonarcloud:
    organization: ${SONAR_ORGANIZATION}
    token: ${SONAR_TOKEN}
script:
  - ./gradlew jacocoTestReport
  - ./gradlew sonarqube -Dsonar.login=$SONAR_TOKEN -Dsonar.projectKey=$SONAR_PROJECT_KEY
env:
  - SPRING_PROFILES_ACTIVE=ci
```

The final Procfile with all configurations:

```shell
web: java -jar -Dspring.profiles.active=prod -Dserver.port=$PORT $JAVA_OPTS build/libs/bookstore-manager.jar
```