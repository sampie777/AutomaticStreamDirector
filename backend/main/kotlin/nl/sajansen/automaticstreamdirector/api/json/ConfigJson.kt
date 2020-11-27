package nl.sajansen.automaticstreamdirector.api.json

import nl.sajansen.automaticstreamdirector.common.FormComponent


data class ConfigJson(
    val frontend: List<ConfigJsonItem> = emptyList(),
    val backend: List<ConfigJsonItem> = emptyList()
)

data class ConfigJsonItem(
    val key: String,
    val value: Any?,
    val formComponent: FormComponent?
)