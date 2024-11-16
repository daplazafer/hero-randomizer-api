package com.dpf.hero.controller

import com.dpf.hero.domain.model.Hero
import com.dpf.hero.domain.service.HeroService
import org.springframework.web.bind.annotation.GetMapping
import org.springframework.web.bind.annotation.RequestMapping
import org.springframework.web.bind.annotation.RequestParam
import org.springframework.web.bind.annotation.RestController

@RestController
@RequestMapping("/api/hero")
class HeroController(private val heroService: HeroService) {

    @GetMapping
    fun getHero(@RequestParam(required = false) blueprint: String?): Hero {
        return if (blueprint.isNullOrEmpty()) {
            heroService.generateRandomHero()
        } else {
            heroService.buildHeroFromBlueprint(blueprint)
        }
    }
}
