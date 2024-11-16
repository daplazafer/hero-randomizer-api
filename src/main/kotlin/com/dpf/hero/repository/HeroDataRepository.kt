package com.dpf.hero.repository

import com.dpf.hero.domain.repository.HeroDataSource
import com.fasterxml.jackson.databind.ObjectMapper
import com.fasterxml.jackson.module.kotlin.jacksonObjectMapper
import com.fasterxml.jackson.module.kotlin.readValue
import org.springframework.stereotype.Component
import java.nio.file.Files
import java.nio.file.Paths
import java.util.Collections

@Component
class HeroDataRepository : HeroDataSource {

    companion object {
        private const val BASE_PATH = "data/"
    }

    private val mapper: ObjectMapper = jacksonObjectMapper()

    override val name1: List<String> = loadJsonList("${BASE_PATH}character/name_1.json")
    override val name2: List<String> = loadJsonList("${BASE_PATH}character/name_2.json")
    override val alignment: List<String> = loadJsonList("${BASE_PATH}character/alignment.json")
    override val title1: List<String> = loadJsonList("${BASE_PATH}character/title_1.json")
    override val title2: List<String> = loadJsonList("${BASE_PATH}character/title_2.json")
    override val faith: List<String> = loadJsonList("${BASE_PATH}character/faith.json")
    override val raze1: List<String> = loadJsonList("${BASE_PATH}character/raze_1.json")
    override val raze2: List<String> = loadJsonList("${BASE_PATH}character/raze_2.json")
    override val raze3: List<String> = loadJsonList("${BASE_PATH}character/raze_3.json")
    override val orientation: List<String> = loadJsonList("${BASE_PATH}character/orientation.json")
    override val haircut: List<String> = loadJsonList("${BASE_PATH}character/haircut.json")
    override val background1: List<String> = loadJsonList("${BASE_PATH}character/background_1.json")
    override val background2: List<String> = loadJsonList("${BASE_PATH}character/background_2.json")
    override val background3: List<String> = loadJsonList("${BASE_PATH}character/background_3.json")
    override val helmet: List<String> = loadJsonList("${BASE_PATH}equipment/helmet.json")
    override val chest: List<String> = loadJsonList("${BASE_PATH}equipment/chest.json")
    override val gloves: List<String> = loadJsonList("${BASE_PATH}equipment/gloves.json")
    override val pants: List<String> = loadJsonList("${BASE_PATH}equipment/pants.json")
    override val boots: List<String> = loadJsonList("${BASE_PATH}equipment/boots.json")
    override val weapon: List<String> = loadJsonList("${BASE_PATH}equipment/weapon.json")
    override val accessory: List<String> = loadJsonList("${BASE_PATH}equipment/accessory.json")
    override val uncommon: List<String> = loadJsonList("${BASE_PATH}equipment/rarity/uncommon.json")
    override val rare: List<String> = loadJsonList("${BASE_PATH}equipment/rarity/rare.json")
    override val epic: List<String> = loadJsonList("${BASE_PATH}equipment/rarity/epic.json")
    override val legendary: List<String> = loadJsonList("${BASE_PATH}equipment/rarity/legendary.json")

    private fun loadJsonList(filePath: String): List<String> {
        val path = Paths.get(filePath)
        val jsonData = Files.readString(path)
        val list = mapper.readValue<List<String>>(jsonData)
        return Collections.unmodifiableList(list)
    }
}