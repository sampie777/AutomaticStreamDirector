package nl.sajansen.automaticstreamdirector.api.json

import nl.sajansen.automaticstreamdirector.actions.Action


data class ActionJson(
    val id: Long?,
    val name: String,
) {

    companion object {
        fun from(it: Action): ActionJson {
            return ActionJson(
                id = it.id,
                name = it.displayName(),
            )
        }

        fun toAction(it: ActionJson): Action {
            if (it.id == null) {
                throw IllegalArgumentException("Cannot convert Action which hasn't been saved first (id = null): $it")
            }
            return Action.get(it.id) ?: throw IllegalArgumentException("Could not find action: $it")
        }
    }
}