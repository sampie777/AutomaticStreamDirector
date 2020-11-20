package nl.sajansen.automaticstreamdirector.common

data class FormComponent (
    val name: String,
    val labelText: String,
    val type: Type,
    val required: Boolean = false,
    val defaultValue: Any? = null,

    val selectValues: List<SelectOption> = emptyList()
) {
    enum class Type{
        Text,
        TextArea,
        Number,
        Date,
        Time,
        Checkbox,
        URL,
        Password,
        Select,
    }

    data class SelectOption(
        val value: Any?,
        val text: String = value.toString(),
    )
}
