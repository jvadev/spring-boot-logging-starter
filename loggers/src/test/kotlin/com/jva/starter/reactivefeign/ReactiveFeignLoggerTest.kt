package com.jva.starter.reactivefeign

import ch.qos.logback.classic.Logger
import com.fasterxml.jackson.databind.ObjectMapper
import com.jva.starter.testappender.InMemoryAppender
import com.jva.starter.testappender.NAME
import com.jva.starter.testclient.TestReactiveFeignClient
import com.jva.starter.utils.JsonFormatter
import feign.Target
import io.mockk.every
import io.mockk.mockk
import org.junit.jupiter.api.AfterEach
import org.junit.jupiter.params.ParameterizedTest
import org.junit.jupiter.params.provider.Arguments
import org.junit.jupiter.params.provider.MethodSource
import org.reactivestreams.Publisher

import org.slf4j.LoggerFactory
import org.springframework.cloud.openfeign.support.SpringMvcContract
import reactivefeign.client.ReactiveHttpRequest
import reactivefeign.client.ReactiveHttpResponse
import java.time.Clock

private const val CONFIG_KEY = "someMethod()"
private val URI = java.net.URI("http://alfahost:8555/path1?queryKey1=queryVal1&queryKey2=queryVal2")
private val REQUEST_HEADERS = mapOf("Content-Type" to listOf("text/plain"))
private val RESPONSE_HEADERS = mapOf("Content-Type" to listOf("text/plainResponse"))


class ReactiveFeignLoggerTest {
    private val jsonFormatter = JsonFormatter(ObjectMapper())
    private val root = LoggerFactory.getLogger(Logger.ROOT_LOGGER_NAME) as Logger
    private val logger = LoggerFactory.getLogger(ReactiveFeignLoggerTest::class.java)
    private val inMemoryAppender = root.getAppender(NAME) as InMemoryAppender

    private val response: ReactiveHttpResponse<*> = mockk()

    @AfterEach
    fun cleanUp() {
        inMemoryAppender.cleanUp()
    }

    @ParameterizedTest
    @MethodSource(value = ["arguments"])
    fun `should log request body according to property isExtendedLoggingEnabled`(
        isExtendedLoggingEnabled: Boolean,
        isRequestLogged: Boolean,
        isHeadersLogged: Boolean,
        isBodyLogged: Boolean,
        requestBody: Publisher<Any>,
        responseBody: Publisher<Any>,
        isMono: Boolean) {
        // given:
        val metadataList = SpringMvcContract().parseAndValidateMetadata(TestReactiveFeignClient::class.java)
        val target = Target.HardCodedTarget(TestReactiveFeignClient::class.java, URI.toString())
        val request = ReactiveHttpRequest(metadataList[0], target, URI, REQUEST_HEADERS, requestBody)
        every { response.body() } returns responseBody
        every { response.headers() } returns RESPONSE_HEADERS
        val reactiveLogger = ReactiveFeignLogger(isExtendedLoggingEnabled, jsonFormatter, Clock.systemDefaultZone())
        // when:

    }

    private fun arguments() =
        listOf(Arguments.of())
}
