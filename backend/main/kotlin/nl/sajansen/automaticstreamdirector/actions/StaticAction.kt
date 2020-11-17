package nl.sajansen.automaticstreamdirector.actions

import nl.sajansen.automaticstreamdirector.api.json.FormDataJson
import nl.sajansen.automaticstreamdirector.common.FormComponent
import nl.sajansen.automaticstreamdirector.db.entities.ActionEntity

interface StaticAction {
    val name: String
    val previewText: String

    fun formComponents(): List<FormComponent>

    /**
     * Returns List<String> for validation errors. Else Action if successful.
     */
    fun save(data: FormDataJson): Any

    fun fromDbEntity(actionEntity: ActionEntity): Action?
}