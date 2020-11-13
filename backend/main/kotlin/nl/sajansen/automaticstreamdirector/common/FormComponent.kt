package nl.sajansen.automaticstreamdirector.common

data class FormComponent (
    val name: String,
    val labelText: String,
    val type: Type,
    val required: Boolean = false,
    val defaultValue: Any? = null,
    val value: Any? = null,

    val selectValues: List<String> = emptyList()
) {
    enum class Type{
        Text,
        Number,
        Date,
        Time,
        Checkbox,
        URL,
        Password,
        Select,
    }
}
