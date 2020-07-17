package com.jva.starter.utils

import com.fasterxml.jackson.databind.ObjectMapper

class JsonFormatter(private val objectMapper: ObjectMapper) {
    private val prettifiedMapper = objectMapper.writerWithDefaultPrettyPrinter()

    fun prettifyJsonBody(body: Any) = prettifiedMapper.writeValueAsString(body)
}