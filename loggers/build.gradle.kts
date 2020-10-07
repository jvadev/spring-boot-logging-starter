plugins {
    id("com.jvadev.gradle-kotlin-common-plugin")
    groovy
}

dependencies {
    implementation("com.playtika.reactivefeign:feign-reactor-webclient")
    implementation("io.github.microutils:kotlin-logging")
    implementation("org.springframework.boot:spring-boot")
    implementation("net.logstash.logback:logstash-logback-encoder")
    implementation("ch.qos.logback:logback-core")
    implementation("ch.qos.logback:logback-classic")
    implementation("org.spockframework:spock-core")

    testImplementation("io.kotest:kotest-assertions-core-jvm")
    testImplementation("org.springframework.boot:spring-boot-starter-test")
    testImplementation("net.logstash.logback:logstash-logback-encoder")
    testImplementation("com.playtika.reactivefeign:feign-reactor-spring-configuration")
}
