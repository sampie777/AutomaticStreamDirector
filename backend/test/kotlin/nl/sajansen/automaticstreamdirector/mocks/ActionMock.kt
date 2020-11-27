package nl.sajansen.automaticstreamdirector.mocks


import com.google.gson.Gson
import nl.sajansen.automaticstreamdirector.actions.Action
import nl.sajansen.automaticstreamdirector.actions.StaticAction
import nl.sajansen.automaticstreamdirector.api.json.FormDataJson
import nl.sajansen.automaticstreamdirector.common.FormComponent
import nl.sajansen.automaticstreamdirector.db.entities.ActionEntity
import java.util.logging.Logger

class ActionMock(override var id: Long? = null, var age: Int? = null) : Action() {
    private val logger = Logger.getLogger(ActionMock::class.java.name)

    var isExecuted = false

    override fun execute() {
        isExecuted = true
    }

    override fun displayName(): String {
        return "ActionMock"
    }

    override fun getDataSet() = DbDataSet(
        age = age
    )

    data class DbDataSet(
        val age: Int?,
    )

    companion object : StaticAction {
        override val name: String = ActionMock::class.java.simpleName
        override val previewText: String = "ActionMock ..."

        override fun formComponents(): List<FormComponent> = listOf(
            FormComponent(
                "age",
                "Age",
                FormComponent.Type.Number,
                required = true,
            ),
        )

        @JvmStatic
        override fun save(data: FormDataJson): Any {
            val id = data["id"]?.toLongOrNull()
            val age = data["age"]?.toIntOrNull()

            if (age == null || age < 0) {
                return listOf("Invalid age")
            }

            ActionMock(
                id = id,
                age = age,
            ).also {
//                saveOrUpdate(it)
                return it
            }
        }

        override fun fromDbEntity(actionEntity: ActionEntity): Action? {
            val data = Gson().fromJson(actionEntity.dataString, DbDataSet::class.java)

            return ActionMock(
                age = data.age,
                id = actionEntity.id,
            )
        }
    }

}