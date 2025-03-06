plugins {
    id("org.springframework.boot")
}

dependencies {
    implementation("org.springframework.boot:spring-boot-starter-web")
    implementation("org.twitter4j:twitter4j-stream:4.0.7")
    implementation("org.apache.httpcomponents:httpclient:4.5.13")
    implementation("org.json:json:20231013")
}
