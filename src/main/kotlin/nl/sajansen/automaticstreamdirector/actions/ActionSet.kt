package nl.sajansen.automaticstreamdirector.actions

interface ActionSet {
    val name: String
    val actions: ArrayList<Action>

    fun execute() {
        actions.forEach(Action::execute)
    }
}