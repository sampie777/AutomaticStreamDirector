package nl.sajansen.automaticstreamdirector.api.json

import nl.sajansen.automaticstreamdirector.actions.StaticAction
import nl.sajansen.automaticstreamdirector.common.FormComponent


data class StaticActionJson(
    val className: String,
    val name: String,
    val previewText: String,
    val formComponents: List<FormComponent>,
) {

    companion object {
        fun from(it: StaticAction): StaticActionJson {
            return StaticActionJson(
                className = it::class.java.name,
                name = it.name,
                previewText = it.previewText,
                formComponents = it.formComponents(),
            )
        }
    }
}