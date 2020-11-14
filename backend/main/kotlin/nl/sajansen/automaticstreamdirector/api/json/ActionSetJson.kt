package nl.sajansen.automaticstreamdirector.api.json

import nl.sajansen.automaticstreamdirector.actions.Action
import nl.sajansen.automaticstreamdirector.actions.ActionSet


data class ActionSetJson(
    val name: String,
    val actions: List<ActionJson> = emptyList(),
) {

    companion object {
        fun from(it: ActionSet): ActionSetJson {
            return ActionSetJson(
                name = it.name,
                actions = it.actions.map(ActionJson::from),
            )
        }

        fun toActionSet(it: ActionSetJson): ActionSet? {
            return ActionSet(
                name = it.name,
                actions = it.actions.map(ActionJson::toAction) as ArrayList<Action>
            )
        }
    }
}