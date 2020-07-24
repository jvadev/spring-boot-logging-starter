package com.jva.starter.configuration

import io.kotest.matchers.shouldBe
import org.junit.jupiter.api.Test
import org.springframework.boot.autoconfigure.jackson.JacksonAutoConfiguration
import org.springframework.boot.test.util.TestPropertyValues
import org.springframework.context.annotation.AnnotationConfigApplicationContext

class ReactiveFeignConfigurationTest {
    private val context = AnnotationConfigApplicationContext()

    @Test
    fun `reactive feign logging configuration should be disabled by property`() {
        // when:
        TestPropertyValues.of("logging.reactive-feign.enabled: false").applyTo(context)

        // then:
        context.containsBean("reactiveFeignLogger") shouldBe false
    }

    @Test
    fun `reactive feign logging configuration should be enabled by property`() {
        // when:
        TestPropertyValues.of("logging.reactive-feign.enabled: true").applyTo(context)

        // then:
        context.containsBean("reactiveFeignLogger") shouldBe true
    }

    private fun registerConfigurationBeans() {
        context.register(ReactiveFeignConfiguration::class.java, JacksonAutoConfiguration::class.java)
    }
}