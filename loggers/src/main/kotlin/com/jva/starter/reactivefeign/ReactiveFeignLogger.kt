package com.jva.starter.reactivefeign

import com.jva.starter.properties.LoggingProperties
import feign.MethodMetadata
import feign.Target
import mu.KotlinLogging
import reactivefeign.client.ReactiveHttpRequest
import reactivefeign.client.ReactiveHttpResponse
import reactivefeign.client.log.ReactiveLoggerListener

private val logger = KotlinLogging.logger { }

class ReactiveFeignLogger(
    val properties: LoggingProperties
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

    override fun bodyReceived(body: Any?, context: LogContext?) {
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

    private inline fun <T> msg(supplier: () -> T) = supplier()

}
