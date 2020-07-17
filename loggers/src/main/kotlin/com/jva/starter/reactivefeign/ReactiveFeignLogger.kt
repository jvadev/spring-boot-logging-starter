package com.jva.starter.reactivefeign

import com.jva.starter.properties.LoggingProperties
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

private val logger = KotlinLogging.logger { }

class ReactiveFeignLogger(
    private val jsonFormatter: JsonFormatter
) : ReactiveLoggerListener<LogContext> {


    override fun requestStarted(
        request: ReactiveHttpRequest?,
        target: Target<*>,
        methodMetadata: MethodMetadata?
    ): LogContext {
        TODO("Not yet implemented")
    }

    override fun errorReceived(throwable: Throwable?, context: LogContext) {
        logger.error("[{}]--->{} {} HTTP/1.1", context.feignMethodTag, context.request.method(), context.request.uri())
    }

    override fun logRequestBody(): Boolean {
        TODO("Not yet implemented")
    }

    override fun bodyReceived(body: Any?, context: LogContext) {

        val headers = msg { context.response?.headers()?.format(headerType = "RESPONSE") }
        val bodyType = msg { context.response?.body()?.formatBodyType("RESPONSE") }
        val message = msg {
            body?.let {
                "$bodyType ${prettifyJsonBodyIfExist(it, context)}"
            }
        }

        logger.info("{} {} {}", context.feignMethodTag, headers, message)
    }

    override fun responseReceived(response: ReactiveHttpResponse<*>?, context: LogContext?) {
        TODO("Not yet implemented")
    }

    override fun logResponseBody(): Boolean {
        TODO("Not yet implemented")
    }

    override fun bodySent(body: Any?, context: LogContext?) {
        TODO("Not yet implemented")
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

    private fun Publisher<*>.formatBodyType(bodyType: String): String =
        if (this is Flux<*>) "$bodyType BODY ELEMENT:\n" else "$bodyType BODY:\n"

}
