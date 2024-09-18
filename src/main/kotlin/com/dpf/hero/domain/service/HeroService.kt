package com.dpf.hero.domain.service

import com.dpf.hero.domain.model.Hero

interface HeroService {
    fun generateRandomHero(): Hero
    fun buildHeroFromBlueprint(blueprint: String?): Hero
}