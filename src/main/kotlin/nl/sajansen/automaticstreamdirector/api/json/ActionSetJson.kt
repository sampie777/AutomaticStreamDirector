package nl.sajansen.automaticstreamdirector.api.json

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
    }
}