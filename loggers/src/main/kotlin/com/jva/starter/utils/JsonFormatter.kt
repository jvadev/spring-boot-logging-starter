package com.jva.starter.utils

import com.fasterxml.jackson.databind.ObjectMapper

class JsonFormatter(objectMapper: ObjectMapper) {
    private val prettifiedMapper = objectMapper.writerWithDefaultPrettyPrinter()

    fun prettifyJsonBody(body: Any): String? = prettifiedMapper.writeValueAsString(body)
}