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

        fun toAction(it: ActionJson): Action {
            return object : Action{
                override fun execute() {

                }

                override fun displayName(): String {
                    return "NOT IMPLEMENTED"
                }
            }
        }
    }
}