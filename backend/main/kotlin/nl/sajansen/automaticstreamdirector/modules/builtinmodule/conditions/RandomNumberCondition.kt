package nl.sajansen.automaticstreamdirector.modules.builtinmodule.conditions


import com.google.gson.Gson
import nl.sajansen.automaticstreamdirector.api.json.FormDataJson
import nl.sajansen.automaticstreamdirector.common.FormComponent
import nl.sajansen.automaticstreamdirector.db.entities.ConditionEntity
import nl.sajansen.automaticstreamdirector.jsonBuilder
import nl.sajansen.automaticstreamdirector.triggers.Condition
import nl.sajansen.automaticstreamdirector.triggers.StaticCondition
import java.util.logging.Logger
import kotlin.math.roundToInt
import kotlin.random.Random

class RandomNumberCondition(
    val chance: Double,
    override var id: Long? = null,
) : Condition() {
    private val logger = Logger.getLogger(RandomNumberCondition::class.java.name)

    override fun check(): Boolean {
        return Random.nextDouble() <= chance
    }

    override fun displayName(): String {
        return "If I'm lucky for ${(chance * 100).roundToInt()} %"
    }

    override fun getDbDataSet(): String?  = jsonBuilder(prettyPrint = false).toJson(
        DbDataSet(chance = chance)
    )

    data class DbDataSet(
        val chance: Double,
    )

    companion object : StaticCondition {
        override val name: String = RandomNumberCondition::class.java.simpleName
        override val previewText = "If I'm lucky for [...] %"

        override fun formComponents() = listOf(
            FormComponent("chance", "Chance (0% - 100%)", FormComponent.Type.Number, required = true),
        )
        
        @JvmStatic
        override fun save(data: FormDataJson): Any {
            val chance = data["chance"]?.toDoubleOrNull()

            when {
                chance == null -> return listOf("Enter a valid chance")
                chance < 0 -> return listOf("Chance may not be less than 0")
                chance > 100 -> return listOf("Chance may not be greater than 100")
                else -> {
                }
            }

            RandomNumberCondition(chance = chance / 100.0).also {
                saveOrUpdate(it)
                return it
            }
        }

        override fun fromDbEntity(conditionEntity: ConditionEntity): Condition? {
            val data = Gson().fromJson(conditionEntity.dataString, DbDataSet::class.java)

            return RandomNumberCondition(
                chance = data.chance,
                id = conditionEntity.id,
            )
        }
    }
}