package com.jva.starter.utils

import org.springframework.http.HttpHeaders.CONTENT_TYPE
import org.springframework.http.MediaType.APPLICATION_JSON
import org.springframework.http.MediaType.APPLICATION_PROBLEM_JSON
import java.util.stream.Collectors.joining

val JSON_CONTENT_TYPES = listOf(
    APPLICATION_JSON,
    APPLICATION_PROBLEM_JSON
)

fun Map<String, Collection<String>>.containsJsonContentTypeHeader(): Boolean =
    get(CONTENT_TYPE)?.any { it.isJsonContentTypeHeader() } ?: false

fun String.isJsonContentTypeHeader(): Boolean = JSON_CONTENT_TYPES.any { it.toString() == this }

fun Map<String, Collection<String>>.format(headerType: String): String =
    String.format("%s HEADERS:\n%s", headerType,
        entries.stream()
            .map { entry -> "${entry.key}: ${entry.value.stream().collect(joining("; "))}" }
            .collect(joining("\n")))
