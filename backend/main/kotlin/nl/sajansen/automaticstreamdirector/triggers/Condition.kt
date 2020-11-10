package nl.sajansen.automaticstreamdirector.triggers

interface Condition {
    fun check(): Boolean
}