# Quality

In order the keep the code quality we are using Jacoco to check coverage and KTLint to set style guide.

## Jacoco

Dependencies:

```groovy
plugins {
    id 'jacoco'
    id "org.barfuin.gradle.jacocolog" version "1.2.4"
}

apply plugin: 'jacoco'
apply plugin: 'org.barfuin.gradle.jacocolog'

test {
    useJUnitPlatform()
    // report is always generated after tests run
    finalizedBy jacocoTestReport
}

jacoco {
    toolVersion = "0.8.7"
}

def packagesToExcludeOnCoverage = [
        'com/jlima/bookstoremanager/BookstoreManagerApplicationKt*.*',
        'com/jlima/bookstoremanager/config/**',
        'com/jlima/bookstoremanager/**/exception/**',
        'com/jlima/bookstoremanager/**/entity/**',
        'com/jlima/bookstoremanager/**/dto/**',
        'com/jlima/bookstoremanager/**/repository/**'
]

jacocoTestReport {
    reports {
        html.required = true
        xml.required = true
        csv.required = false
    }

    afterEvaluate {
        classDirectories.setFrom(files(classDirectories.files.collect {
            fileTree(dir: it, exclude: packagesToExcludeOnCoverage)
        }))
    }

    doLast {
        println("See report file:\\${project.rootDir}/build/reports/jacoco/test/html/index.html")
    }

    // tests are required to run before generating the report
    dependsOn test
}
```

## KTLint

We can apply lint our project using the commands `ktlintCheck` or `ktlintFormart`.

Bellow the needed dependencies:

```groovy
buildscript {
    repositories {
        maven {
            url "https://plugins.gradle.org/m2/"
        }
    }
    dependencies {
        classpath "org.jlleitschuh.gradle:ktlint-gradle:10.0.0"
    }
}

plugins {
    id "org.jlleitschuh.gradle.ktlint" version "10.0.0"
}

repositories {
    mavenCentral()
}

apply plugin: "org.jlleitschuh.gradle.ktlint"
apply plugin: "org.jlleitschuh.gradle.ktlint-idea"

ktlint {
    version = "0.40.0"
}
```

## SonarCloud/Sonarqube

Sonarqube is a tool to check the code quality.

By default, when we create our project in sonarcloud, We can run the Auto Analysis. This will check every new branch and
the PRs.

We can also add to our CI. Since we are using TravisCI, we can add it in this process. We just need to remember to deactivate the
Automatic Analysis in our SonarCloud configurations.

> Administration > Analysis Method > SonarCloud Automatic Analysis [OFF]

- [Configuring SonarCloud with Travis](https://community.sonarsource.com/t/how-to-configure-upload-coverage-reports-to-sonarcloud-io/24618)
- [Official Docs](https://plugins.gradle.org/plugin/org.sonarqube)
- [Sample Repository](https://github.com/SonarSource/sq-com_example_java-gradle-travis)

Add the dependencies:

```groovy
buildscript {
    repositories {
        maven {
            url "https://plugins.gradle.org/m2/"
        }
    }
    dependencies {
        classpath "org.sonarsource.scanner.gradle:sonarqube-gradle-plugin:3.3"
    }
}

plugins {
    id "org.sonarqube" version "3.3"
}

apply plugin: "org.sonarqube"

sonarqube {
    properties {
        property "sonar.projectName", "Bookstore Manager"
        property "sonar.projectKey", "com.jlima.bookstoremanager"
    }
}
```

Now we are able to run `./gradlew sonarqube`.

To run alongside with TravisCI, we must add a few configurations to it.

addons: add sonarqube/cloud as travis plugin using travis env. variables. script: will run a script from TravisCI build.

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
```

Environment Variables on TravisCI:
- ${SONAR_ORGANIZATION}
- ${SONAR_TOKEN}
- ${SONAR_PROJECT_KEY}

In case we were not using the env. variables from TravisCI, the yml would be this way:

```yaml
addons:
  sonarcloud:
    organization: MyOrgName
    token:
      secure: MyEncriptedToken
```

```shell
travis encrypt MyToken12343242359901
```