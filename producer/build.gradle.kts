plugins {
    `java-library`
    `maven-publish`
}

group = "com.example"

val v2 = providers.gradleProperty("v2").map(String::toBoolean).getOrElse(false)

version = if (v2) "2.0" else "1.0"

java {
    withSourcesJar()
    registerFeature("foo") {
        usingSourceSet(sourceSets["main"])
        capability("$group", project.name, "$version")
    }
}

val fooAttribute = Attribute.of("com.example.foo", String::class.java)
configurations.matching { it.name in setOf("fooApiElements", "fooRuntimeElements") }.all {
    attributes.attribute(fooAttribute, "foo1")
}

if (v2) {
    val barAttribute = Attribute.of("com.example.bar", String::class.java)
    configurations.matching { it.name in setOf("apiElements", "runtimeElements", "fooApiElements", "fooRuntimeElements") }.all {
        attributes.attribute(barAttribute, "bar1")
    }
}

publishing {
    repositories {
        maven(layout.buildDirectory.dir("repo"))
    }
    publications {
        register<MavenPublication>("maven") {
            from(components["java"])
        }
    }
}