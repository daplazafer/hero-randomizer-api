package com.dpf.hero

import org.springframework.boot.autoconfigure.SpringBootApplication
import org.springframework.boot.runApplication

@SpringBootApplication
class HeroRandomizerApiApplication

fun main(args: Array<String>) {
	runApplication<HeroRandomizerApiApplication>(*args)
}
