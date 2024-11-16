package com.dpf.hero.domain.repository

interface HeroDataSource {
    val name1: List<String>
    val name2: List<String>
    val alignment: List<String>
    val title1: List<String>
    val title2: List<String>
    val faith: List<String>
    val raze1: List<String>
    val raze2: List<String>
    val raze3: List<String>
    val orientation: List<String>
    val haircut: List<String>
    val background1: List<String>
    val background2: List<String>
    val background3: List<String>
    val helmet: List<String>
    val chest: List<String>
    val gloves: List<String>
    val pants: List<String>
    val boots: List<String>
    val weapon: List<String>
    val accessory: List<String>
    val uncommon: List<String>
    val rare: List<String>
    val epic: List<String>
    val legendary: List<String>
}