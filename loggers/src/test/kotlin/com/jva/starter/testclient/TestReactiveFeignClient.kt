package com.jva.starter.testclient

import org.springframework.web.bind.annotation.GetMapping
import reactivefeign.spring.config.ReactiveFeignClient

@ReactiveFeignClient(name = "some-api")
interface TestReactiveFeignClient {

    @GetMapping("some/mono")
    fun getId()
}
