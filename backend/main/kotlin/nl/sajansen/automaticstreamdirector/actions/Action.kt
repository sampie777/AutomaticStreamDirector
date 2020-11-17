package nl.sajansen.automaticstreamdirector.actions

import nl.sajansen.automaticstreamdirector.db.entities.ActionEntity

abstract class Action {
    companion object {
        fun saveOrUpdate(obj: Action) {
            val actionEntity = ActionEntity.fromAction(obj)

            ActionEntity.saveOrUpdate(actionEntity)

            obj.id = actionEntity.id
        }

        fun list(): List<Action?>? {
            val actionEntities = ActionEntity.list() ?: return null
            return actionEntities
                .filterNotNull()
                .map(ActionEntity::toAction)
        }

        fun get(id: Long): Action? {
            val actionEntity = ActionEntity.get(id) ?: return null
            return actionEntity.run(ActionEntity::toAction)
        }
    }

    open var id: Long? = null
    open var dbData: HashMap<String, Any?> = hashMapOf()

    abstract fun execute()
    abstract fun displayName(): String
    abstract fun getDbDataSet(): String?
}