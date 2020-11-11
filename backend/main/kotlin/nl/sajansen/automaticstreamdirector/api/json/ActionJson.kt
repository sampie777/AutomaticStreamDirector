package nl.sajansen.automaticstreamdirector.api.json

import nl.sajansen.automaticstreamdirector.actions.Action


data class ActionJson(
    val name: String,
) {

    companion object {
        fun from(it: Action): ActionJson {
            return ActionJson(
                name = it.displayName(),
            )
        }
    }
}