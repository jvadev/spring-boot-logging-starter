plugins {
    id("com.jvadev.gradle-kotlin-common-plugin")
}

dependencies {
    api(project(":loggers"))
    implementation("org.springframework.boot:spring-boot")
    implementation("org.springframework.boot:spring-boot-starter-web")
    implementation("com.playtika.reactivefeign:feign-reactor-webclient")

    testImplementation("io.kotest:kotest-assertions-core-jvm")
    testImplementation("org.springframework.boot:spring-boot-starter-test")
}
