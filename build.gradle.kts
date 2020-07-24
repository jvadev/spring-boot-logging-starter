import io.spring.gradle.dependencymanagement.dsl.DependencyManagementExtension

plugins {
    id("com.jvadev.gradle-kotlin-common-plugin") version "1.0.9"
    id("io.spring.dependency-management") version "1.0.9.RELEASE"
}

allprojects {
    repositories {
        jcenter()
        mavenCentral()
    }

    apply(plugin = "io.spring.dependency-management")
    apply(plugin = "com.jvadev.gradle-kotlin-common-plugin")

    val reactiveFeignVersion = "2.0.8"
    configure<DependencyManagementExtension> {
        imports {
            mavenBom("org.springframework.boot:spring-boot-dependencies:2.3.0.RELEASE")
        }
        dependencies {
            dependency("com.playtika.reactivefeign:feign-reactor-webclient:$reactiveFeignVersion")
            dependency("net.logstash.logback:logstash-logback-encoder:5.2")
            dependency("ch.qos.logback:logback-core:1.1.11")
            dependency("io.github.microutils:kotlin-logging:1.7.10")
            dependency("io.kotest:kotest-assertions-core-jvm:4.0.6")
        }
    }

    tasks.test {
        useJUnitPlatform {
            systemProperties("junit.jupiter.testinstance.lifecycle.default" to "per_class")
        }
    }
}
