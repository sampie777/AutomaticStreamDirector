package nl.sajansen.automaticstreamdirector.mocks


import com.google.gson.Gson
import nl.sajansen.automaticstreamdirector.api.json.FormDataJson
import nl.sajansen.automaticstreamdirector.common.FormComponent
import nl.sajansen.automaticstreamdirector.db.entities.ConditionEntity
import nl.sajansen.automaticstreamdirector.triggers.Condition
import nl.sajansen.automaticstreamdirector.triggers.StaticCondition
import java.util.logging.Logger

class ConditionMock(
    var checkReturnValue: Boolean = false, 
    override var id: Long? = null
) : Condition() {
    private val logger = Logger.getLogger(ConditionMock::class.java.name)

    override fun check(): Boolean {
        return checkReturnValue
    }

    override fun displayName(): String {
        return "ConditionMock"
    }

    override fun getDataSet() = DbDataSet(checkReturnValue = checkReturnValue)

    data class DbDataSet(
        val checkReturnValue: Boolean,
    )

    companion object : StaticCondition {
        override val name: String = ConditionMock::class.java.simpleName
        override val previewText = "If ..."

        override fun formComponents() = listOf(
            FormComponent("checkReturnValue", "CheckReturnValue", FormComponent.Type.Checkbox, required = true),
        )

        @JvmStatic
        override fun save(data: FormDataJson): Any {
            val id = data["id"]?.toLongOrNull()
            val checkReturnValue = data["checkReturnValue"]?.toBoolean() ?: false

            ConditionMock(
                id = id,
                checkReturnValue = checkReturnValue
            ).also {
//                saveOrUpdate(it)
                return it
            }
        }

        override fun fromDbEntity(conditionEntity: ConditionEntity): Condition? {
            val data = Gson().fromJson(conditionEntity.dataString, DbDataSet::class.java)

            return ConditionMock(
                checkReturnValue = data.checkReturnValue,
                id = conditionEntity.id,
            )
        }
    }
}