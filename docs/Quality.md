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
        'com/jlima/bookstoremanager/config/**',
        'com/jlima/bookstoremanager/**/exception/**',
        'com/jlima/bookstoremanager/**/entity/**',
        'com/jlima/bookstoremanager/**/dto/**',
        'com/jlima/bookstoremanager/**/repository/**'
]

jacocoTestReport {
    afterEvaluate {
        classDirectories.setFrom(files(classDirectories.files.collect {
            fileTree(dir: it, exclude: packagesToExcludeOnCoverage)
        }))
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