package nl.sajansen.automaticstreamdirector.actions

import nl.sajansen.automaticstreamdirector.api.json.FormDataJson
import nl.sajansen.automaticstreamdirector.common.FormComponent

interface StaticAction {
    val name: String
    val previewText: String
    val formComponents: List<FormComponent>

    /**
     * Returns List<String> for validation errors. Else Action if successful.
     */
    fun save(data: FormDataJson): Any
}