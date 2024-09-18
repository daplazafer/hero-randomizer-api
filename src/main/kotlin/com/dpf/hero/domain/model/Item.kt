package com.dpf.hero.domain.model

import com.fasterxml.jackson.annotation.JsonInclude

@JsonInclude(JsonInclude.Include.NON_NULL)
data class Item(
    val rarity: Rarity?,
    val item: String?,
)
