package nl.sajansen.automaticstreamdirector.triggers

interface StaticCondition {
    fun name(): String
    fun previewText(): String
}