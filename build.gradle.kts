plugins {
    kotlin("jvm") version "1.9.25"
    kotlin("plugin.spring") version "1.9.25"
    id("org.springframework.boot") version "3.4.3" apply false
    id("io.spring.dependency-management") version "1.1.7"
    id("com.github.davidmc24.gradle.plugin.avro-base") version "1.9.1"
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


allprojects {
    group = "io.pg.demo"
    version = "0.0.1-SNAPSHOT"

    repositories {
        gradlePluginPortal()
        mavenCentral()
        maven {
            name = "confluent"
            url = uri("https://packages.confluent.io/maven/")
        }
    }
}

subprojects {
    apply(plugin = "org.jetbrains.kotlin.jvm")
    apply(plugin = "org.jetbrains.kotlin.plugin.spring")
    apply(plugin = "io.spring.dependency-management")
    apply(plugin = "org.springframework.boot")

    dependencies {
        implementation("org.springframework.boot:spring-boot-starter")
        implementation("org.jetbrains.kotlin:kotlin-reflect")
        implementation("org.jetbrains.kotlin:kotlin-stdlib-jdk8")

        implementation("org.apache.avro:avro:1.11.4") // 명시적 버전 지정

        testImplementation("org.springframework.boot:spring-boot-starter-test")
        testImplementation("org.jetbrains.kotlin:kotlin-test-junit5")
        testRuntimeOnly("org.junit.platform:junit-platform-launcher")
    }

    tasks {
        compileKotlin {
            kotlinOptions {
                freeCompilerArgs = listOf("-Xjsr305=strict")
                jvmTarget = "21"
            }
            dependsOn(processResources)
        }

        compileTestKotlin {
            kotlinOptions {
                freeCompilerArgs = listOf("-Xjsr305=strict")
                jvmTarget = "21"
            }
        }
    }


    tasks.withType<Test> {
        useJUnitPlatform()
    }
}

