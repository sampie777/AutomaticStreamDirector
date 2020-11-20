package nl.sajansen.automaticstreamdirector.api.json

import nl.sajansen.automaticstreamdirector.common.FormComponent


data class ConfigJson(
    val gui: List<ConfigJsonItem>,
    val backend: List<ConfigJsonItem>
)

data class ConfigJsonItem(
    val key: String,
    val value: Any?,
    val formComponent: FormComponent?
)