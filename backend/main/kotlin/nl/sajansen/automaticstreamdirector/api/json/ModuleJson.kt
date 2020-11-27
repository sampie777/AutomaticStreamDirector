package nl.sajansen.automaticstreamdirector.api.json

import nl.sajansen.automaticstreamdirector.modules.Module


data class ModuleJson(
    val name: String,
) {

    companion object {
        fun from(it: Module): ModuleJson {
            return ModuleJson(
                name = it.name,
            )
        }
    }
}