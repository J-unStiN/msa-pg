
plugins {
    id("org.springframework.boot") version "3.4.3"
}
dependencies {
//	implementation("org.springframework.boot:spring-boot-starter-web")


}
tasks.bootJar {
    enabled = false
}

tasks.jar {
    enabled = true
}