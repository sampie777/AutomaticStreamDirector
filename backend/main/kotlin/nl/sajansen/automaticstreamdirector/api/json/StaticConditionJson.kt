package nl.sajansen.automaticstreamdirector.api.json

import nl.sajansen.automaticstreamdirector.triggers.StaticCondition


data class StaticConditionJson(
    val className: String,
    val name: String,
    val previewText: String,
) {

    companion object {
        fun from(it: StaticCondition): StaticConditionJson {
            return StaticConditionJson(
                className = it::class.java.name,
                name = it.name,
                previewText = it.previewText,
            )
        }
    }
}