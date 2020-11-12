package nl.sajansen.automaticstreamdirector.modules.builtinmodule.conditions


import nl.sajansen.automaticstreamdirector.triggers.Condition
import nl.sajansen.automaticstreamdirector.triggers.StaticCondition
import java.util.logging.Logger
import kotlin.math.roundToInt
import kotlin.random.Random

class RandomNumberCondition(
    val chance: Double,
) : Condition {
    private val logger = Logger.getLogger(RandomNumberCondition::class.java.name)

    override fun check(): Boolean {
        return Random.nextDouble() <= chance
    }

    override fun displayName(): String {
        return "If I'm lucky for ${(chance * 100).roundToInt()} %"
    }

    override fun toString() = displayName()

    companion object : StaticCondition {
        override fun name(): String = RandomNumberCondition::class.java.simpleName
        override fun previewText(): String = "If I'm lucky for [...] %"
    }
}