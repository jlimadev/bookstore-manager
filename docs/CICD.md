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