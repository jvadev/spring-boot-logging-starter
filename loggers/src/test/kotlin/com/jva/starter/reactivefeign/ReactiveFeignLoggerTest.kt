package com.jva.starter.reactivefeign

import ch.qos.logback.classic.Logger
import com.fasterxml.jackson.databind.JsonNode
import com.fasterxml.jackson.databind.ObjectMapper
import com.fasterxml.jackson.module.kotlin.readValue
import com.jva.starter.appenders.InMemoryAppender
import com.jva.starter.appenders.NAME
import com.jva.starter.testclient.TestReactiveFeignClient
import com.jva.starter.utils.JsonFormatter
import feign.Target
import io.kotest.matchers.string.shouldContain
import io.mockk.every
import io.mockk.mockk
import org.junit.jupiter.api.AfterEach
import org.junit.jupiter.api.Test

import org.slf4j.LoggerFactory
import org.springframework.cloud.openfeign.support.SpringMvcContract
import org.springframework.core.io.ClassPathResource
import reactivefeign.client.ReactiveHttpRequest
import reactivefeign.client.ReactiveHttpResponse
import reactor.core.publisher.Mono
import java.time.Clock

private val URI = java.net.URI("http://localhost:8989/path1?queryKey1=queryVal1&queryKey2=queryVal2")
private val REQUEST_HEADERS =
    mapOf("Content-Type" to listOf("application/json"), "Header-Type" to listOf("headerValue", "anotherOne"))
private val RESPONSE_HEADERS = mapOf("Content-Type" to listOf("application/json"))


class ReactiveFeignLoggerTest {
    private val mapper = ObjectMapper().findAndRegisterModules()
    private val jsonFormatter = JsonFormatter(ObjectMapper())
    private val root = LoggerFactory.getLogger(Logger.ROOT_LOGGER_NAME) as Logger
    private val inMemoryAppender = root.getAppender(NAME) as InMemoryAppender

    private val response: ReactiveHttpResponse<*> = mockk()

    @AfterEach
    fun cleanUp() {
        inMemoryAppender.cleanUp()
    }

    @Test
    fun `should log request and response as extended logging enabled`() {
        // given:
        val bodyRequest = createJsonRequest()
        val bodyResponse = createJsonResponse()
        val metadataList = SpringMvcContract().parseAndValidateMetadata(TestReactiveFeignClient::class.java)
        val target = Target.HardCodedTarget(TestReactiveFeignClient::class.java, URI.toString())
        val request = ReactiveHttpRequest(metadataList[0], target, URI, REQUEST_HEADERS, Mono.just(bodyRequest))
        every { response.body() } returns Mono.just(bodyResponse)
        every { response.headers() } returns RESPONSE_HEADERS
        val reactiveLogger = ReactiveFeignLogger(
            isExtendedLoggingEnabled = true,
            jsonFormatter = jsonFormatter,
            clock = Clock.systemDefaultZone()
        )
        // when:
        val context = reactiveLogger.requestStarted(request, target, metadataList[0])
        if (reactiveLogger.logRequestBody()) reactiveLogger.bodySent(bodyRequest, context)
        reactiveLogger.responseReceived(response, context)
        if (reactiveLogger.logResponseBody()) reactiveLogger.bodyReceived(bodyResponse, context)
        val capturedLogs = inMemoryAppender.capturedLogs.toString()
        // then:
        capturedLogs shouldContain createJsonRequest().toPrettyString()
        capturedLogs shouldContain createJsonResponse().toPrettyString()
    }

    private fun createJsonRequest() = mapToJson("/files/request.json")

    private fun createJsonResponse() = mapToJson("/files/response.json")

    private fun mapToJson(path: String): JsonNode = mapper.readValue(ClassPathResource(path).file)
}
