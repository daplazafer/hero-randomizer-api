package com.dpf.hero.controller

import com.dpf.hero.domain.model.Hero
import com.dpf.hero.domain.service.HeroService
import org.springframework.web.bind.annotation.GetMapping
import org.springframework.web.bind.annotation.RequestMapping
import org.springframework.web.bind.annotation.RequestParam
import org.springframework.web.bind.annotation.RestController

@RestController
@RequestMapping("/hero")
class HeroController(private val heroService: HeroService) {

    @GetMapping("/new")
    fun getRandomHero(): Hero {
        return heroService.generateRandomHero()
    }

    @GetMapping
    fun getHeroByBlueprint(@RequestParam(required = true) blueprint: String?): Hero {
        return heroService.buildHeroFromBlueprint(blueprint)
    }
}
