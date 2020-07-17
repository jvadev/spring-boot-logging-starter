package com.jva.starter.utils

import org.springframework.http.HttpHeaders.CONTENT_TYPE
import org.springframework.http.MediaType.APPLICATION_JSON
import org.springframework.http.MediaType.APPLICATION_PROBLEM_JSON

val JSON_CONTENT_TYPES = listOf(
    APPLICATION_JSON,
    APPLICATION_PROBLEM_JSON
)

fun Map<String, Collection<String>>.containsJsonContentTypeHeader(): Boolean? =
    get(CONTENT_TYPE)?.any { it.isJsonContentTypeHeader() } ?: false

fun String.isJsonContentTypeHeader(): Boolean = JSON_CONTENT_TYPES.any { it.toString() == this }