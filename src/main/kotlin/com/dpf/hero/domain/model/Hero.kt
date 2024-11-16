package com.dpf.hero.domain.model

data class Hero(
    val blueprint: String,
    val lvl: Int,
    val name: String,
    val alignment: String,
    val title: String,
    val faith: String,
    val raze: String,
    val orientation: String,
    val age: Int,
    val height: Double,
    val haircut: String,
    val background: String,
    val stats: Stats,
    val equipment: Equipment,
)
