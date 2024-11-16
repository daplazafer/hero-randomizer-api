package com.dpf.hero.controller

import org.springframework.core.io.Resource
import org.springframework.core.io.ResourceLoader
import org.springframework.web.bind.annotation.GetMapping
import org.springframework.web.bind.annotation.RequestMapping
import org.springframework.web.bind.annotation.RestController

@RestController
@RequestMapping
class MainController(private val resourceLoader: ResourceLoader) {

    @GetMapping
    fun getIndex(): Resource {
        return resourceLoader.getResource("classpath:/static/index.html")
    }
}
