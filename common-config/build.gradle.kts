//plugins {
//    id("org.springframework.boot")
//}


dependencies {

    implementation(project(":app-config-data"))
    implementation("org.springframework.retry:spring-retry:1.3.0")

}


tasks.bootJar {
    enabled = false
}

tasks.jar {
    enabled = true
}
