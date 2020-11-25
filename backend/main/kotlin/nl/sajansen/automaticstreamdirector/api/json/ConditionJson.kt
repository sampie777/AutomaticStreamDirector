package nl.sajansen.automaticstreamdirector.api.json

import nl.sajansen.automaticstreamdirector.triggers.Condition


data class ConditionJson(
    val id: Long?,
    val name: String,
    val data: Any = {},
) {

    companion object {
        fun from(it: Condition): ConditionJson {
            return ConditionJson(
                id = it.id,
                name = it.displayName(),
                data = it.getDataSet() ?: {},
            )
        }

        fun toCondition(it: ConditionJson): Condition {
            if (it.id == null) {
                throw IllegalArgumentException("Cannot convert Condition which hasn't been saved first (id = null): $it")
            }
            return Condition.get(it.id) ?: throw IllegalArgumentException("Could not find condition: $it")
        }
    }
}