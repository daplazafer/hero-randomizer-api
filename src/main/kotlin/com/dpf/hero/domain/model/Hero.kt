package com.dpf.hero.domain.model

data class Hero(
    val blueprint: String,
    val lvl: Int,
    val name: String,
    val title: String,
    val raze: String,
    val age: Int,
    val height: Double,
    val haircut: String,
    val background: String,
    val stats: Stats,
    val equipment: Equipment,
)
