package nl.sajansen.automaticstreamdirector.triggers

import nl.sajansen.automaticstreamdirector.db.entities.ConditionEntity

abstract class Condition {
    companion object {
        fun saveOrUpdate(obj: Condition, updateTriggerId: Boolean = false) {
            val conditionEntity = ConditionEntity.fromCondition(obj)

            if (!updateTriggerId && conditionEntity.id != null && conditionEntity.id!! > 0L) {
                val existingConditionEntity = ConditionEntity.get(conditionEntity.id!!)
                if (existingConditionEntity != null) {
                    conditionEntity.trigger = existingConditionEntity.trigger
                }
            }

            ConditionEntity.saveOrUpdate(conditionEntity)

            obj.id = conditionEntity.id
        }

        fun list(): List<Condition?>? {
            val conditionEntities = ConditionEntity.list() ?: return null
            return conditionEntities
                .filterNotNull()
                .map(ConditionEntity::toCondition)
        }

        fun get(id: Long): Condition? {
            val conditionEntity = ConditionEntity.get(id) ?: return null
            return conditionEntity.run(ConditionEntity::toCondition)
        }

        fun delete(id: Long): Boolean {
            return ConditionEntity.delete(id)
        }
    }

    abstract var id: Long?

    override fun toString(): String {
        return "${this::class.java.simpleName}(id=$id, displayName=${displayName()})"
    }

    abstract fun check(): Boolean
    abstract fun displayName(): String
    abstract fun getDataSet(): Any?
}