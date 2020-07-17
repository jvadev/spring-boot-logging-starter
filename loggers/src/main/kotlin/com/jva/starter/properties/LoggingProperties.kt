package com.jva.starter.properties

import org.springframework.boot.context.properties.ConfigurationProperties

@ConfigurationProperties(prefix = "logging")
class LoggingProperties {
    var reactiveFeign = ReactiveFeignLoggingProperties(
        isEnabled = false,
        isExtendedLoggingEnabled = false
    )


    open class LoggingConfiguration(
        var isEnabled: Boolean,
        var isExtendedLoggingEnabled: Boolean
    )

    class ReactiveFeignLoggingProperties(
        isEnabled: Boolean,
        isExtendedLoggingEnabled: Boolean
    ) : LoggingConfiguration(isEnabled, isExtendedLoggingEnabled)
}