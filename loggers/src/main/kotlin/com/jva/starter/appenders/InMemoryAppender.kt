package com.jva.starter.appenders

import ch.qos.logback.classic.Level
import ch.qos.logback.classic.PatternLayout
import ch.qos.logback.classic.spi.ILoggingEvent
import ch.qos.logback.core.AppenderBase
import ch.qos.logback.core.encoder.EncoderBase
import ch.qos.logback.core.encoder.LayoutWrappingEncoder
import java.io.StringWriter

const val NAME = "IN_MEMORY"

class InMemoryAppender(
    var encoder: EncoderBase<*>? = null,
    var level: Level? = null,
    val capturedLogs: StringWriter = StringWriter(),
    var layout: PatternLayout? = null
) : AppenderBase<ILoggingEvent>() {

    override fun append(iLoggingEvent: ILoggingEvent) {
        val logMessage = layout?.doLayout(iLoggingEvent)
        capturedLogs.write(logMessage)
        level = iLoggingEvent.level
    }

    override fun start() {
        layout = (encoder as LayoutWrappingEncoder).layout as PatternLayout
        super.start()
    }

    fun cleanUp() {
        capturedLogs.buffer.setLength(0)
        level = null
    }
}
