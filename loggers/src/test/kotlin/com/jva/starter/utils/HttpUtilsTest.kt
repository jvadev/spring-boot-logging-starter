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
        val headersMap = emptyMap<String, Collection<String>>()
        // when:
        val result = headersMap.containsJsonContentTypeHeader()
        // then:
        result shouldBe false
    }

    @Test
    fun `should format headers as string with new line`() {
        // given:
        val headers = mapOf(
            "Content-Type" to listOf("application/json", "application/problem+json"),
            "Accept-Language" to listOf("en-US")
        )
        // when:
        val result = headers.formatHeaders(headerType = "REQUEST")
        // then:
        result shouldBe """
            |REQUEST HEADERS:
            |Content-Type: application/json; application/problem+json
            |Accept-Language: en-US
        """.trimMargin()
    }
}
