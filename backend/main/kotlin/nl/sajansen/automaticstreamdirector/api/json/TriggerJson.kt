package nl.sajansen.automaticstreamdirector.api.json

import nl.sajansen.automaticstreamdirector.actions.ActionSet
import nl.sajansen.automaticstreamdirector.triggers.Condition
import nl.sajansen.automaticstreamdirector.triggers.Trigger


data class TriggerJson(
    val name: String,
    val importance: Int = 0,
    val conditions: List<ConditionJson> = emptyList(),
    val actionSets: List<ActionSetJson> = emptyList(),
    val id: Long? = null,
) {

    companion object {
        fun from(it: Trigger): TriggerJson {
            return TriggerJson(
                id = it.id,
                name = it.name,
                importance = it.importance,
                conditions = it.conditions.map(ConditionJson::from),
                actionSets = it.actionSets.map(ActionSetJson::from),
            )
        }

        fun toTrigger(it: TriggerJson): Trigger? {
            return Trigger(
                id = it.id,
                name = it.name,
                importance = it.importance,
                conditions = it.conditions.map(ConditionJson::toCondition) as ArrayList<Condition>,
                actionSets = it.actionSets.map(ActionSetJson::toActionSet) as ArrayList<ActionSet>,
            )
        }
    }
}