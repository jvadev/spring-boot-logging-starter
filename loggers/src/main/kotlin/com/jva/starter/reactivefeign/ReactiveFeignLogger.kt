package com.jva.starter.reactivefeign

import com.jva.starter.utils.JsonFormatter
import com.jva.starter.utils.containsJsonContentTypeHeader
import com.jva.starter.utils.format
import feign.MethodMetadata
import feign.Target
import mu.KotlinLogging
import org.reactivestreams.Publisher
import reactivefeign.client.ReactiveHttpRequest
import reactivefeign.client.ReactiveHttpResponse
import reactivefeign.client.log.ReactiveLoggerListener
import reactor.core.publisher.Flux
import java.time.Clock

private const val REQUEST = "REQUEST"
private const val RESPONSE = "RESPONSE"
private val logger = KotlinLogging.logger { }

class ReactiveFeignLogger(
    private val isExtendedLoggingEnabled: Boolean,
    private val jsonFormatter: JsonFormatter,
    private val clock: Clock
) : ReactiveLoggerListener<LogContext> {


    override fun requestStarted(
        request: ReactiveHttpRequest,
        target: Target<*>,
        methodMetadata: MethodMetadata
    ): LogContext {
        val logContext = LogContext(request, target, methodMetadata, clock)
        val headers = msg { request.headers()?.format(headerType = REQUEST) }
        logger.info("[{}]--->{} {} HTTP/1.1{}", logContext.feignMethodTag, request.method(), request.uri(), headers)
        return logContext
    }

    override fun errorReceived(throwable: Throwable?, context: LogContext) {
        logger.error("[{}]--->{} {} HTTP/1.1", context.feignMethodTag, context.request.method(), context.request.uri())
    }

    override fun logRequestBody() = isExtendedLoggingEnabled

    override fun bodyReceived(body: Any?, context: LogContext?) {
        if (isExtendedLoggingEnabled) {
            val headers = msg { context?.response?.headers()?.format(headerType = RESPONSE) }
            val bodyPrefix = msg { context?.response?.body()?.formatBodyPrefix(RESPONSE) }
            val message = msg {
                body?.let { body ->
                    "$bodyPrefix ${context?.let { context -> prettifyJsonBodyIfExist(body, context) }}"
                }
            }
            logger.info("[{}] {} {}", context?.feignMethodTag, headers, message)
        }
    }

    override fun responseReceived(response: ReactiveHttpResponse<*>?, context: LogContext?) {
        context?.response = response
    }

    override fun logResponseBody() = isExtendedLoggingEnabled

    override fun bodySent(body: Any?, context: LogContext?) {
        if (isExtendedLoggingEnabled) {

        }
    }

    private fun prettifyJsonBodyIfExist(body: Any, context: LogContext): Any? =
        if (context.request.headers().containsJsonContentTypeHeader()) {
            try {
                jsonFormatter.prettifyJsonBody(body)
            } catch (e: Exception) {
                body
            }
        } else body

    private inline fun <T> msg(supplier: () -> T) = supplier()

    private fun Publisher<*>.formatBodyPrefix(bodyType: String): String =
        if (this is Flux<*>) "$bodyType BODY ELEMENT:\n" else "$bodyType BODY:\n"

}
