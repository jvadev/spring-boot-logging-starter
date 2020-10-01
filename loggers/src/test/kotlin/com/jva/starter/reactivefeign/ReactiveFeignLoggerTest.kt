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
import kotlinx.coroutines.reactive.awaitFirst
import org.junit.jupiter.api.AfterEach
import org.junit.jupiter.api.Test
import org.junit.jupiter.params.ParameterizedTest
import org.junit.jupiter.params.provider.Arguments
import org.junit.jupiter.params.provider.MethodSource
import org.reactivestreams.Publisher

import org.slf4j.LoggerFactory
import org.springframework.cloud.openfeign.support.SpringMvcContract
import org.springframework.core.io.ClassPathResource
import reactivefeign.client.ReactiveHttpRequest
import reactivefeign.client.ReactiveHttpResponse
import reactor.core.publisher.Mono
import java.time.Clock

private const val CONFIG_KEY = "someMethod()"
private val URI = java.net.URI("http://localhost:8989/path1?queryKey1=queryVal1&queryKey2=queryVal2")
private val REQUEST_HEADERS = mapOf("Content-Type" to listOf("application/json"))
private val RESPONSE_HEADERS = mapOf("Content-Type" to listOf("application/json"))


class ReactiveFeignLoggerTest {
    private val mapper = ObjectMapper().findAndRegisterModules()
    private val jsonFormatter = JsonFormatter(ObjectMapper())
    private val root = LoggerFactory.getLogger(Logger.ROOT_LOGGER_NAME) as Logger
    private val log = LoggerFactory.getLogger(ReactiveFeignLoggerTest::class.java)
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
        isMono: Boolean
    ) {
        // given:
        val metadataList = SpringMvcContract().parseAndValidateMetadata(TestReactiveFeignClient::class.java)
        val target = Target.HardCodedTarget(TestReactiveFeignClient::class.java, URI.toString())
        val request = ReactiveHttpRequest(metadataList[0], target, URI, REQUEST_HEADERS, requestBody)
        every { response.body() } returns responseBody
        every { response.headers() } returns RESPONSE_HEADERS
        val reactiveLogger = ReactiveFeignLogger(isExtendedLoggingEnabled, jsonFormatter, Clock.systemDefaultZone())
        // when:
        val context = reactiveLogger.requestStarted(request, target, metadataList[0])
        log.info("=======request started=======")
        if (reactiveLogger.logRequestBody()) reactiveLogger.bodySent(request.body(), context)
        reactiveLogger.responseReceived(response, context)
        log.info("=======response received======")
        if (reactiveLogger.logResponseBody()) reactiveLogger.bodyReceived(response.body(), context)
        val capturedLogs = inMemoryAppender.capturedLogs.toString()
        // then:
        capturedLogs.shouldContain("[TestReactiveFeignClient#getId]")
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
        log.info("=======request started=======")
        if (reactiveLogger.logRequestBody()) reactiveLogger.bodySent(bodyRequest, context)
        reactiveLogger.responseReceived(response, context)
        log.info("=======response received======")
        if (reactiveLogger.logResponseBody()) reactiveLogger.bodyReceived(bodyResponse, context)
        val capturedLogs = inMemoryAppender.capturedLogs.toString()
        // then:
        capturedLogs.shouldContain(createJsonRequest())
//        capturedLogs.shouldContain("{\"responseCode\":0,\"errorText\":\"\"}")
    }

    private fun arguments() =
        listOf(
            Arguments.of(
                true,
                true,
                true,
                true,
                Mono.just(createJsonRequest()),
                Mono.just(createJsonResponse()),
                true
            )
        )

    private fun createJsonRequest() = mapToJson("/files/request.json")

    private fun createJsonResponse() = mapToJson("/files/response.json")

    private fun mapToJson(path: String): String = mapper.readValue<JsonNode>(ClassPathResource(path).file).toString()
}
