package nl.sajansen.automaticstreamdirector.triggers

import nl.sajansen.automaticstreamdirector.api.json.FormDataJson
import nl.sajansen.automaticstreamdirector.common.FormComponent
import nl.sajansen.automaticstreamdirector.db.entities.ConditionEntity

interface StaticCondition {
    val name: String
    val previewText: String

    fun formComponents(): List<FormComponent>

    /**
     * Returns List<String> for validation errors. Else Condition if successful.
     */
    fun save(data: FormDataJson): Any

    fun fromDbEntity(conditionEntity: ConditionEntity): Condition?
}