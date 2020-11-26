package nl.sajansen.automaticstreamdirector.actions

import nl.sajansen.automaticstreamdirector.db.entities.ActionEntity

abstract class Action {
    companion object {
        fun saveOrUpdate(obj: Action, updateActionSet: Boolean = false) {
            val actionEntity = ActionEntity.fromAction(obj)

            if (!updateActionSet && actionEntity.id != null && actionEntity.id!! > 0L) {
                val existingActionEntity = ActionEntity.get(actionEntity.id!!)
                if (existingActionEntity != null) {
                    actionEntity.actionSet = existingActionEntity.actionSet
                }
            }

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

        fun delete(id: Long): Boolean {
            return ActionEntity.delete(id)
        }
    }

    abstract var id: Long?

    override fun toString(): String {
        return "${this::class.java.simpleName}(id=$id, displayName=${displayName()})"
    }

    abstract fun execute()
    abstract fun displayName(): String
    abstract fun getDataSet(): Any?
}