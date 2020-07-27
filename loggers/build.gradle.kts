plugins {
    id("com.jvadev.gradle-kotlin-common-plugin")
}

dependencies {
    implementation("com.playtika.reactivefeign:feign-reactor-webclient")
    implementation("io.github.microutils:kotlin-logging")
    implementation("org.springframework.boot:spring-boot")

    testImplementation("io.kotest:kotest-assertions-core-jvm")
    testImplementation("org.springframework.boot:spring-boot-starter-test")
    testImplementation("net.logstash.logback:logstash-logback-encoder")
    testImplementation("com.playtika.reactivefeign:feign-reactor-spring-configuration")
    testImplementation("net.logstash.logback:logstash-logback-encoder")
}