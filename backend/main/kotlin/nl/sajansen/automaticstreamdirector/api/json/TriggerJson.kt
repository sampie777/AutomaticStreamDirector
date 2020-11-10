package nl.sajansen.automaticstreamdirector.api.json

import nl.sajansen.automaticstreamdirector.triggers.Trigger


data class TriggerJson(
    val name: String,
    val importance: Int = 0,
    val conditions: List<ConditionJson> = emptyList(),
    val actionSets: List<ActionSetJson> = emptyList(),
) {

    companion object {
        fun from(it: Trigger): TriggerJson {
            return TriggerJson(
                name = it.name,
                importance = it.importance,
                conditions = it.conditions.map(ConditionJson::from),
                actionSets = it.actionSets.map(ActionSetJson::from),
            )
        }
    }
}