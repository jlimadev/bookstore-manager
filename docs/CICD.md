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