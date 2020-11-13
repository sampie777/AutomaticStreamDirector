package nl.sajansen.automaticstreamdirector.common

data class FormComponent (
    val name: String,
    val labelText: String,
    val type: FormComponentType,
    val required: Boolean = false,
    val defaultValue: Any? = null,
    val value: Any? = null,
)

enum class FormComponentType {
    Text,
    Number,
    Date,
    Time,
    Checkbox,
    URL,
    Password,
}
