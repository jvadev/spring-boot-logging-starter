package com.jva.starter.testappender

import ch.qos.logback.classic.Level
import ch.qos.logback.classic.PatternLayout
import ch.qos.logback.classic.spi.ILoggingEvent
import ch.qos.logback.core.AppenderBase
import ch.qos.logback.core.encoder.LayoutWrappingEncoder
import java.io.StringWriter

const val NAME = "IN_MEMORY"

class InMemoryAppender(
    encoder: LayoutWrappingEncoder<*>,
    var level: Level?,
    private val capturedLogs: StringWriter = StringWriter(),
    private val layout: PatternLayout = encoder.layout as PatternLayout
) : AppenderBase<ILoggingEvent>() {

    override fun append(iLoggingEvent: ILoggingEvent) {
        val logMessage = layout.doLayout(iLoggingEvent)
        capturedLogs.write(logMessage)
        level = iLoggingEvent.level
    }

    fun cleanUp() {
        capturedLogs.buffer.setLength(0)
        level = null
    }
}
