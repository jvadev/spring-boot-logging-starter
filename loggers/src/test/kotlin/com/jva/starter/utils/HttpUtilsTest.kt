package com.jva.starter.utils

import io.kotest.matchers.shouldBe
import org.junit.jupiter.api.Test

class HttpUtilsTest {

    @Test
    fun `should return true if map contains json content type header`() {
        // given:
        val headersMap = mapOf("Content-Type" to listOf("application/json"))
        // when:
        val result = headersMap.containsJsonContentTypeHeader()
        // then:
        result shouldBe true
    }

    @Test
    fun `should return false if there is no json content type header`() {
        // given:
        val  headersMap = emptyMap<String, Collection<String>>()
        // when:
        val result = headersMap.containsJsonContentTypeHeader()
        // then:
        result shouldBe false
    }
}