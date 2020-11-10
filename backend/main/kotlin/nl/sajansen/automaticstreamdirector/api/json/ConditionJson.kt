package nl.sajansen.automaticstreamdirector.api.json

import nl.sajansen.automaticstreamdirector.triggers.Condition


data class ConditionJson(
    val name: String,
) {

    companion object {
        fun from(it: Condition): ConditionJson {
            return ConditionJson(
                name = it.toString(),
            )
        }
    }
}