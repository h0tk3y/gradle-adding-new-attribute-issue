plugins {
    `java-library`
}

val fooAttribute = Attribute.of("com.example.foo", String::class.java)
configurations.matching { it.name in setOf("compileClasspath", "runtimeClasspath") }.all {
    attributes.attribute(fooAttribute, "foo1")
}

repositories {
    maven(layout.projectDirectory.dir("../producer/build/repo"))
}

val v2 = providers.gradleProperty("v2").map(String::toBoolean).getOrElse(false)
dependencies {
    implementation("com.example:producer:${if (v2) "2.0" else "1.0"}")
}

fun insight(configurationName: String) = tasks.registering(org.gradle.api.tasks.diagnostics.DependencyInsightReportTask::class) { 
    setDependencySpec("com.example:producer")
    setConfiguration(configurationName)
}

val resolveCompileClasspath by insight("compileClasspath")
val resolveRuntimeClasspath by insight("runtimeClasspath")

val resolve by tasks.registering {
    dependsOn(resolveCompileClasspath)
    dependsOn(resolveRuntimeClasspath)
}