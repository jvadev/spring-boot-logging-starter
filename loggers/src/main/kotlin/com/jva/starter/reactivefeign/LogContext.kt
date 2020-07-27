package com.jva.starter.reactivefeign

import feign.MethodMetadata
import feign.Target
import reactivefeign.client.ReactiveHttpRequest
import reactivefeign.client.ReactiveHttpResponse
import reactivefeign.utils.FeignUtils
import java.time.Clock

data class LogContext(
    val request: ReactiveHttpRequest,
    val target: Target<*>,
    val methodMetadata: MethodMetadata,
    val clock: Clock,
    val startTime: Long = clock.millis(),
    val feignMethodTag: String = FeignUtils.methodTag(methodMetadata),
    var response: ReactiveHttpResponse<*>? = null
) {

    fun timeSpent(): Long = clock.millis() - startTime
}
