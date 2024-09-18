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
        private const val SEP = ","
        private const val INVALID_BLUEPRINT_ERROR_MESSAGE = "Invalid blueprint"
        private const val BLUEPRINT_SIZE = 53
    }

    @Value("\${hero.generation.max-lvl}")
    private val maxLvl: Int = 100

    @Value("\${hero.generation.max-augment}")
    private val maxAugment: Int = 12

    @Value("\${hero.generation.min-age}")
    private val minAge: Int = 8

    @Value("\${hero.generation.height-variation}")
    private val heightVariation: Double = 0.3

    @Value("\${hero.generation.empty-equipment-factor}")
    private val emptyEquipmentFactor: Double = 0.2

    @Value("\${hero.generation.rarity-factor}")
    private val rarityFactor: Double = 0.7

    override fun generateRandomHero(): Hero {
        var blueprint = "${Random.nextInt(1, maxLvl + 1)}" // lvl
        blueprint += "$SEP${Random.nextInt(0, data.name1.size)}" // name1
        blueprint += "$SEP${Random.nextInt(0, data.name2.size)}" // name2
        blueprint += "$SEP${Random.nextInt(0, data.title1.size)}" // title1
        blueprint += "$SEP${Random.nextInt(0, data.title2.size)}" // title2

        val raze = Random.nextInt(0, data.raze1.size)
        val maxAge = data.raze2[raze].toInt();
        val age = Random.nextInt(minAge, maxAge)
        val baseHeight = data.raze3[raze].toInt()
        blueprint += "$SEP$raze" // raze
        blueprint += "$SEP$age" // age
        blueprint += "$SEP${randomHeight(baseHeight, age, maxAge)}" // height

        blueprint += "$SEP${Random.nextInt(1, data.haircut.size)}" // haircut

        blueprint += "$SEP${Random.nextInt(0, data.background1.size)}" // background1
        blueprint += "$SEP${Random.nextInt(0, data.background2.size)}" // background2
        blueprint += "$SEP${Random.nextInt(0, data.background3.size)}" // background3

        blueprint += "$SEP${Random.nextInt(1, 99)}" // constitution
        blueprint += "$SEP${Random.nextInt(1, 99)}" // wisdom
        blueprint += "$SEP${Random.nextInt(1, 99)}" // endurance
        blueprint += "$SEP${Random.nextInt(1, 99)}" // strength
        blueprint += "$SEP${Random.nextInt(1, 99)}" // intelligence
        blueprint += "$SEP${Random.nextInt(1, 99)}" // dexterity
        blueprint += "$SEP${Random.nextInt(1, 99)}" // perception
        blueprint += "$SEP${Random.nextInt(1, 99)}" // luck
        blueprint += "$SEP${Random.nextInt(1, 99)}" // charisma

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
            "${SEP}0${SEP}0${SEP}0${SEP}0"
        }
        blueprint += generateItemBlueprint(randomRarity(),data.accessory.size) // accessory

        val encryptedBlueprint = crypto.encrypt(blueprint)

        return buildHeroFromBlueprint(encryptedBlueprint)
    }

    override fun buildHeroFromBlueprint(blueprint: String): Hero {

        val decryptedBlueprint = crypto.decrypt(blueprint)

        val regex = Regex("^\\d+(?:${SEP}\\d+){${BLUEPRINT_SIZE-1}}$")
        if (!regex.matches(decryptedBlueprint)) {
            throw IllegalArgumentException(INVALID_BLUEPRINT_ERROR_MESSAGE)
        }

        try {
            val values = decryptedBlueprint.split(SEP)
                .map { it.toInt() }
                .toIntArray()
            var i = values.iterator()

            val lvl = i.next() // lvl
            val name = "${data.name1[i.next()]} ${data.name2[i.next()]}" // name
            val title = "${data.title1[i.next()]} ${data.title2[i.next()]}" // title
            val raze = data.raze1[i.next()] // raze
            val age = i.next() // age
            val height = i.next() / 100.0 // height
            val haircut = data.haircut[i.next()] // haircut

            val background =
                "${data.background1[i.next()]} ${data.background2[i.next()]} ${data.background3[i.next()]}" // background

            val stats = Stats(
                constitution = i.next(), // constitution
                wisdom = i.next(), // wisdom
                endurance = i.next(), // endurance
                strength = i.next(), // strength
                intelligence = i.next(), // intelligence
                dexterity = i.next(), // dexterity
                perception = i.next(), // perception
                luck = i.next(), // luck
                charisma = i.next(), // charisma
            )

            val equipment = Equipment(
                helmet = buildItem(data.helmet, i.next(), i.next(), i.next(),i.next()), // helmet
                chest = buildItem(data.chest, i.next(), i.next(), i.next(),i.next()), // chest
                gloves = buildItem(data.gloves, i.next(), i.next(), i.next(),i.next()), // gloves
                pants = buildItem(data.pants, i.next(), i.next(), i.next(),i.next()), // pants
                boots = buildItem(data.boots, i.next(), i.next(), i.next(),i.next()), // boots
                mainWeapon = buildItem(data.weapon, i.next(), i.next(), i.next(),i.next()), // main weapon
                secondaryWeapon = buildItem(data.weapon, i.next(), i.next(), i.next(),i.next()), // secondary weapon
                accessory = buildItem(data.accessory, i.next(), i.next(), i.next(),i.next()), // accessory
            )

            return Hero(
                blueprint = blueprint,
                lvl = lvl,
                name = name,
                title = title,
                raze = raze,
                age = age,
                height = height,
                haircut = haircut,
                background = background,
                stats = stats,
                equipment = equipment
            )

        } catch (e: Exception) {
            throw IllegalArgumentException(INVALID_BLUEPRINT_ERROR_MESSAGE, e)
        }
    }

    private fun buildItem(itemList: List<String>, part1: Int, part2: Int, augment: Int, rarityId: Int): Item {

        if (rarityId < 1) {
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
        return "$SEP$part1$SEP$part2$SEP$augment$SEP$rarity"
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



    private fun randomHeight(baseHeight: Int, age: Int, maxAge: Int): Int {
        val fullHeightAge = maxAge * 0.2
        val ageFactor = if (age < fullHeightAge) {
            age / fullHeightAge
        } else {
            1.0
        }
        val adjustedBaseHeight = (baseHeight * ageFactor).toInt()
        val variation = adjustedBaseHeight * heightVariation
        return (adjustedBaseHeight - variation + (Math.random() * (2 * variation))).toInt()
    }
}