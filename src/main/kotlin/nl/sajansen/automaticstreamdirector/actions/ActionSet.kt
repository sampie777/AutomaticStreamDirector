package nl.sajansen.automaticstreamdirector.actions

data class ActionSet (
    val name: String,
    val actions: ArrayList<Action> = arrayListOf(),
) {

    fun execute() {
        actions.forEach(Action::execute)
    }
}