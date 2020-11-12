package nl.sajansen.automaticstreamdirector.api.json

import nl.sajansen.automaticstreamdirector.actions.StaticAction


data class StaticActionJson(
    val className: String,
    val name: String,
    val previewText: String,
) {

    companion object {
        fun from(it: StaticAction): StaticActionJson {
            return StaticActionJson(
                className = it::class.java.name,
                name = it.name(),
                previewText = it.previewText(),
            )
        }
    }
}