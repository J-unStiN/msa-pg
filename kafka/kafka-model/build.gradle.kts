plugins {
    id("com.github.davidmc24.gradle.plugin.avro-base") version "1.9.1"
}

dependencies {
    implementation("org.apache.avro:avro:1.12.0")
}

avro {
    outputCharacterEncoding = "UTF-8"
}

val generateAvro = tasks.register<com.github.davidmc24.gradle.plugin.avro.GenerateAvroJavaTask>("generateAvro") {
    source(file("src/main/resources/avro")) // *.avsc 파일의 위치
    setOutputDir(file("src/main/kotlin")) // 저장될 위치
}

tasks.named("compileKotlin") {
    dependsOn(generateAvro)
}
