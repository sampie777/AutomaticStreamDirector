package nl.sajansen.automaticstreamdirector.actions

interface Action {
    fun execute()
    fun displayName(): String
}