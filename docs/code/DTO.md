# Data Transfer Object

We can create our own DTOs or use MapStruct.

## MapStruct

- [Mapstruct Official website](https://mapstruct.org/)
- [Mapstruct Docs](https://mapstruct.org/documentation/reference-guide/)
- [Mapstruct Examples](https://github.com/mapstruct/mapstruct-examples)
- [Mapstruct Kotlin Maven](https://github.com/mapstruct/mapstruct-examples/tree/master/mapstruct-kotlin)
- [Mapstruct Kotlin Gradle](https://github.com/mapstruct/mapstruct-examples/blob/master/mapstruct-kotlin-gradle/)
- [Kotlin Annotation Processing Tool (KAPT)](https://kotlinlang.org/docs/kapt.html)
- [Baeldung Examples](https://www.baeldung.com/mapstruct)
Dependencies:

```groovy
plugins {
    id "org.jetbrains.kotlin.kapt" version "1.5.20"
}

apply plugin: 'kotlin-kapt'

dependencies {
    implementation(kotlin("stdlib-jdk8"))
    implementation("org.mapstruct:mapstruct:1.4.2.Final")
    kapt("org.mapstruct:mapstruct-processor:1.4.2.Final")

    testImplementation("org.junit.jupiter:junit-jupiter-api:5.3.1")
    testImplementation("org.junit.jupiter:junit-jupiter-engine:5.3.1")
    testImplementation("org.assertj:assertj-core:3.11.1")
}

kapt {
    arguments {
        // Set Mapstruct Configuration options here
        // https://kotlinlang.org/docs/reference/kapt.html#annotation-processor-arguments
        // https://mapstruct.org/documentation/stable/reference/html/#configuration-options
        // arg("mapstruct.defaultComponentModel", "spring")
    }
}
```