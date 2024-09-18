package com.dpf.hero.domain.model

data class Equipment(
    val helmet: Item,
    val chest: Item,
    val gloves: Item,
    val pants: Item,
    val boots: Item,
    val mainWeapon: Item,
    val secondaryWeapon: Item,
    val accessory: Item,
)
