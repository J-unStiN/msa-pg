import com.github.davidmc24.gradle.plugin.avro.GenerateAvroJavaTask

plugins {
//    id("org.springframework.boot") version "3.4.3"
    id("com.github.davidmc24.gradle.plugin.avro-base") version "1.9.1"
//    id("io.spring.dependency-management") version "1.1.7"
}


configurations.all {
    resolutionStrategy.eachDependency {
//        if (requested.group == "com.github.davidmc24.gradle.plugin.avro-base") {
        if (requested.group == "org.apache.avro") {
            useVersion("1.11.4")
            because("Avro 버전 충돌 해결")
        }
    }
}

//tasks.withType<org.jetbrains.kotlin.gradle.tasks.KotlinCompile> {
//    kotlinOptions {
//        freeCompilerArgs = listOf("-Xextended-compiler-checks")
//        jvmTarget = "21" // 또는 적절한 JVM 버전
//    }
//}


//avro {
//    isCreateSetters.set(true)
//    isCreateOptionalGetters.set(false)
//    isGettersReturnOptional.set(false)
//    isOptionalGettersForNullableFieldsOnly.set(false)
////    fieldVisibility.set("PUBLIC_DEPRECATED")
//    fieldVisibility.set("PUBLIC")
//    outputCharacterEncoding.set("UTF-8")
//    stringType.set("String")
//    templateDirectory.set(null as String?)
//    isEnableDecimalLogicalType.set(true)
//}


tasks.bootJar {
    enabled = false
}

tasks.jar {
    enabled = true
}


dependencies {

//    apply(plugin= "java")

    // 기존 의존성 유지
//    implementation("org.apache.avro:avro:1.11.4") // 명시적 버전 지정
//    implementation("org.apache.avro:avro-compiler:1.11.4")
//    implementation("org.apache.avro:avro-tools:1.11.4")

    configurations.all {
        exclude(group = "org.apache.avro", module = "trevni-avro")
        exclude(group = "org.apache.avro", module = "trevni-core")
    }
}



val generateAvro = tasks.register<GenerateAvroJavaTask>("generateAvro") {
    source(file("src/main/resources/avro/")) // *.avsc 파일의 위치
    setOutputDir(file("src/main/java/")) // 저장될 위치
}


tasks.named("compileJava") {
    dependsOn(generateAvro)
}


