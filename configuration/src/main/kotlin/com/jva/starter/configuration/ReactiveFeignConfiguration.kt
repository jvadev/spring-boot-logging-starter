package com.jva.starter.configuration

import com.fasterxml.jackson.databind.ObjectMapper
import com.jva.starter.properties.LoggingProperties
import com.jva.starter.reactivefeign.LogContext
import com.jva.starter.reactivefeign.ReactiveFeignLogger
import com.jva.starter.utils.JsonFormatter
import org.springframework.boot.autoconfigure.condition.ConditionalOnClass
import org.springframework.boot.autoconfigure.condition.ConditionalOnProperty
import org.springframework.boot.context.properties.EnableConfigurationProperties
import org.springframework.context.annotation.Bean
import org.springframework.context.annotation.Configuration
import reactivefeign.client.log.ReactiveLoggerListener

@Configuration
@ConditionalOnProperty(name = ["logging.reactive-feign.enabled"], havingValue = "true")
@ConditionalOnClass(ReactiveLoggerListener::class)
@EnableConfigurationProperties(LoggingProperties::class)
class ReactiveFeignConfiguration {

    @Bean
    fun reactiveFeignLogger(
        loggingProperties: LoggingProperties,
        objectMapper: ObjectMapper
    ): ReactiveLoggerListener<LogContext> =
        ReactiveFeignLogger(
            isExtendedLoggingEnabled = loggingProperties.reactiveFeign.isExtendedLoggingEnabled,
            jsonFormatter = JsonFormatter(objectMapper)
        )
}
