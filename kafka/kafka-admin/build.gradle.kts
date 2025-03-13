plugins {
    id("org.springframework.boot") version "3.4.3"
}

dependencies {
    implementation(project(":app-config-data"))
    implementation(project(":common-config"))


    implementation("org.springframework.kafka:spring-kafka")
    implementation("org.springframework.boot:spring-boot-starter-webflux")
//    testImplementation("io.projectreactor:reactor-test")
//    testImplementation("org.springframework.kafka:spring-kafka-test")
}

tasks.bootJar {
    enabled = false
}

tasks.jar {
    enabled = true
}
