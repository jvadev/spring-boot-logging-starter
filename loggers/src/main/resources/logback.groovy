import ch.qos.logback.classic.PatternLayout
import com.jva.starter.appenders.InMemoryAppender

appender("IN_MEMORY", InMemoryAppender) {
    encoder(LayoutWrappingEncoder) {
        layout(PatternLayout) {
            pattern = "%m %n %mdc %marker"
        }
    }
}

appender("CONSOLE", ConsoleAppender) {
    encoder(LayoutWrappingEncoder) {
        layout(PatternLayout) {
            pattern = "%m %n %mdc %marker"
        }
    }
}

def appenderList = ["IN_MEMORY", "CONSOLE"]

logger("org.apache.cxf", INFO)
logger("com.jva", INFO)
root(WARN, appenderList)