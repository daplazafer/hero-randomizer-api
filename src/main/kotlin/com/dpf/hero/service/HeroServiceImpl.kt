package com.dpf.hero.service

import com.dpf.hero.domain.crypto.EncryptionEngine
import com.dpf.hero.domain.model.Equipment
import com.dpf.hero.domain.model.Hero
import com.dpf.hero.domain.model.Item
import com.dpf.hero.domain.model.Rarity
import com.dpf.hero.domain.model.Stats
import com.dpf.hero.domain.repository.HeroDataSource
import com.dpf.hero.domain.service.HeroService
import org.springframework.beans.factory.annotation.Value
import org.springframework.stereotype.Service
import kotlin.random.Random



@Service
class HeroServiceImpl (
    private val data: HeroDataSource,
    private val crypto: EncryptionEngine
) : HeroService {

    companion object {
        private const val BLUEPRINT_SEPARATOR = ","
        private const val INVALID_BLUEPRINT_ERROR_MESSAGE = "Invalid blueprint"
        private const val BLUEPRINT_SIZE = 51
    }

    @Value("\${hero.generation.max-lvl}")
    private val maxLvl: Int = 0

    @Value("\${hero.generation.max-augment}")
    private val maxAugment: Int = 0

    @Value("\${hero.generation.empty-equipment-factor}")
    private val emptyEquipmentFactor: Double = 0.0

    @Value("\${hero.generation.rarity-factor}")
    private val rarityFactor: Double = 0.0

    override fun generateRandomHero(): Hero {
        var blueprint = "${Random.nextInt(1, maxLvl + 1)}" // lvl
        blueprint += "$BLUEPRINT_SEPARATOR${Random.nextInt(0, data.name1.size)}" // name1
        blueprint += "$BLUEPRINT_SEPARATOR${Random.nextInt(0, data.name2.size)}" // name2
        blueprint += "$BLUEPRINT_SEPARATOR${Random.nextInt(0, data.title1.size)}" // title1
        blueprint += "$BLUEPRINT_SEPARATOR${Random.nextInt(0, data.title2.size)}" // title2

        val raze = Random.nextInt(0, data.raze1.size)
        val maxAge = data.raze2[raze].toInt();
        blueprint += "$BLUEPRINT_SEPARATOR$raze" // raze
        blueprint += "$BLUEPRINT_SEPARATOR${Random.nextInt(1, maxAge)}" // age

        blueprint += "$BLUEPRINT_SEPARATOR${Random.nextInt(0, data.background1.size)}" // background1
        blueprint += "$BLUEPRINT_SEPARATOR${Random.nextInt(0, data.background2.size)}" // background2
        blueprint += "$BLUEPRINT_SEPARATOR${Random.nextInt(0, data.background3.size)}" // background3

        blueprint += "$BLUEPRINT_SEPARATOR${Random.nextInt(1, 99)}" // constitution
        blueprint += "$BLUEPRINT_SEPARATOR${Random.nextInt(1, 99)}" // wisdom
        blueprint += "$BLUEPRINT_SEPARATOR${Random.nextInt(1, 99)}" // endurance
        blueprint += "$BLUEPRINT_SEPARATOR${Random.nextInt(1, 99)}" // strength
        blueprint += "$BLUEPRINT_SEPARATOR${Random.nextInt(1, 99)}" // intelligence
        blueprint += "$BLUEPRINT_SEPARATOR${Random.nextInt(1, 99)}" // dexterity
        blueprint += "$BLUEPRINT_SEPARATOR${Random.nextInt(1, 99)}" // perception
        blueprint += "$BLUEPRINT_SEPARATOR${Random.nextInt(1, 99)}" // luck
        blueprint += "$BLUEPRINT_SEPARATOR${Random.nextInt(1, 99)}" // charisma

        blueprint += generateItemBlueprint(randomRarity(), data.helmet.size) // helmet
        blueprint += generateItemBlueprint(randomRarity(), data.chest.size) // chest
        blueprint += generateItemBlueprint(randomRarity(), data.gloves.size) // gloves
        blueprint += generateItemBlueprint(randomRarity(), data.pants.size) // pants
        blueprint += generateItemBlueprint(randomRarity(), data.boots.size) // boots
        val mainWeaponRarity = randomRarity()
        blueprint += generateItemBlueprint(mainWeaponRarity, data.weapon.size) // main weapon
        blueprint += if (mainWeaponRarity > 0) {
            generateItemBlueprint(randomRarity(),data.weapon.size) // secondary weapon
        } else {
            "${BLUEPRINT_SEPARATOR}0${BLUEPRINT_SEPARATOR}0${BLUEPRINT_SEPARATOR}0${BLUEPRINT_SEPARATOR}0"
        }
        blueprint += generateItemBlueprint(randomRarity(),data.accessory.size) // accessory

        val encryptedBlueprint = crypto.encrypt(blueprint)

        return buildHeroFromBlueprint(encryptedBlueprint)
    }

    override fun buildHeroFromBlueprint(blueprint: String?): Hero {
        var blueprint = blueprint ?: ""

        val decryptedBlueprint = crypto.decrypt(blueprint)

        val regex = Regex("^\\d+(?:${BLUEPRINT_SEPARATOR}\\d+){${BLUEPRINT_SIZE-1}}$")
        if (!regex.matches(decryptedBlueprint)) {
            throw IllegalArgumentException(INVALID_BLUEPRINT_ERROR_MESSAGE)
        }

        try {
            val values = decryptedBlueprint.split(BLUEPRINT_SEPARATOR)
                .map { it.toInt() }
                .toIntArray()

            val lvl = values[0] // lvl
            val name = "${data.name1[values[1]]} ${data.name2[values[2]]}" // name
            val title = "${data.title1[values[3]]} ${data.title2[values[4]]}" // title
            val raze = data.raze1[values[5]] // raze
            val age = values[6] // age
            val background =
                "${data.background1[values[7]]}, ${data.background2[values[8]]}, ${data.background3[values[9]]}" // background

            val stats = Stats(
                constitution = values[10], // constitution
                wisdom = values[11], // wisdom
                endurance = values[12], // endurance
                strength = values[13], // strength
                intelligence = values[14], // intelligence
                dexterity = values[15], // dexterity
                perception = values[16], // perception
                luck = values[17], // luck
                charisma = values[18], // charisma
            )

            val equipment = Equipment(
                helmet = buildItem(data.helmet, values[19], values[20], values[21],values[22]), // helmet
                chest = buildItem(data.chest, values[23], values[24], values[25],values[26]), // chest
                gloves = buildItem(data.gloves, values[27], values[28], values[29],values[30]), // gloves
                pants = buildItem(data.pants, values[31], values[32], values[33],values[34]), // pants
                boots = buildItem(data.boots, values[35], values[36], values[37],values[38]), // boots
                mainWeapon = buildItem(data.weapon, values[39], values[40], values[41],values[42]), // main weapon
                secondaryWeapon = buildItem(data.weapon, values[43], values[44], values[45],values[46]), // secondary weapon
                accessory = buildItem(data.accessory, values[47], values[48], values[49],values[50]), // accessory
            )

            return Hero(
                blueprint = blueprint,
                lvl = lvl,
                name = name,
                title = title,
                raze = raze,
                age = age,
                background = background,
                stats = stats,
                equipment = equipment
            )

        } catch (e: Exception) {
            throw IllegalArgumentException(INVALID_BLUEPRINT_ERROR_MESSAGE, e)
        }
    }

    private fun buildItem(itemList: List<String>, part1: Int, part2: Int, augment: Int, rarityId: Int): Item {

        if(rarityId < 1){
            return Item(null, null)
        }

        var itemName = itemList[part1]
        val rarity = Rarity.entries.find { it.id == rarityId }

        val part2Value = when (rarity) {
            Rarity.UNCOMMON -> data.uncommon[part2]
            Rarity.RARE -> data.rare[part2]
            Rarity.EPIC -> data.epic[part2]
            Rarity.LEGENDARY -> data.legendary[part2]
            else -> ""
        }
        itemName += " $part2Value"
        itemName = itemName.trim()
        if(augment > 1) {
            itemName += " +${augment}"
        }

        return Item(rarity, itemName)
    }

    private fun generateItemBlueprint(rarity: Int, itemListSize: Int): String {
        val part1 = Random.nextInt(0, itemListSize)
        val part2 = when (rarity) {
            2 -> Random.nextInt(0, data.uncommon.size)
            3 -> Random.nextInt(0, data.rare.size)
            4 -> Random.nextInt(0, data.epic.size)
            5 -> Random.nextInt(0, data.legendary.size)
            else -> 0
        }
        val augment = if (rarity > 1 ) Random.nextInt(0, maxAugment + 1) else 0
        return "$BLUEPRINT_SEPARATOR$part1$BLUEPRINT_SEPARATOR$part2$BLUEPRINT_SEPARATOR$augment$BLUEPRINT_SEPARATOR$rarity"
    }

    private fun randomRarity(): Int {
        if (Random.nextDouble() < emptyEquipmentFactor) {
            return 0
        }
        val random = Random.nextDouble()
        return when {
            random < rarityFactor * 0.15 -> Rarity.LEGENDARY.id
            random < rarityFactor * 0.35 -> Rarity.EPIC.id
            random < rarityFactor * 0.65 -> Rarity.RARE.id
            random < rarityFactor * 1.00 -> Rarity.UNCOMMON.id
            else -> Rarity.COMMON.id
        }
    }
}