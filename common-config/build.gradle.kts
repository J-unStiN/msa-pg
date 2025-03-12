plugins {
//    kotlin("jvm") version "1.9.22"
//    kotlin("plugin.spring") version "1.9.22"
    id("org.springframework.boot") version "3.4.3"
//    id("io.spring.dependency-management") version "1.1.4"
//    `java-library`
}

repositories {
    mavenCentral()
}


dependencies {

    implementation(project(":app-config-data"))
    implementation("org.springframework.retry:spring-retry:1.3.0")
    implementation("org.springframework.boot:spring-boot-starter-aop")
//    implementation("org.springframework.retry:spring-retry:2.0.11")

}


tasks.bootJar {
    enabled = false
}

tasks.jar {
    enabled = true
}
