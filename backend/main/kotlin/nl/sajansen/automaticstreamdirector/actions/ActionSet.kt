package nl.sajansen.automaticstreamdirector.actions

import nl.sajansen.automaticstreamdirector.db.entities.ActionEntity
import nl.sajansen.automaticstreamdirector.db.managers.CommonDbManager
import javax.persistence.*

@Entity
data class ActionSet(
    var name: String = "",

    @Transient
    var actions: ArrayList<Action> = arrayListOf(),

    @Id
    @GeneratedValue(strategy = GenerationType.IDENTITY)
    var id: Long? = null,
) {

    @OneToMany(mappedBy = "actionSet", cascade = [CascadeType.ALL], fetch = FetchType.EAGER)
    private var actionEntities: List<ActionEntity> = emptyList()

    companion object : CommonDbManager<ActionSet>(ActionSet::class.java) {
        override fun saveOrUpdate(obj: ActionSet) {
            obj.actionEntities = obj.actions.map(ActionEntity::fromAction)
            obj.actionEntities.forEach {
                it.actionSet = obj
            }
            super.saveOrUpdate(obj)
        }

        override fun list(): MutableList<ActionSet?>? {
            val list = super.list() ?: return null
            return list.map(::fromDbEntity).toMutableList()
        }

        override fun get(id: Long): ActionSet? {
            return super.get(id)?.run(::fromDbEntity)
        }

        fun fromDbEntity(actionSet: ActionSet?): ActionSet? {
            if (actionSet == null) {
                return null
            }

            actionSet.actions = ArrayList(actionSet.actionEntities.mapNotNull(ActionEntity::toAction))
            return actionSet
        }
    }

    fun execute() {
        actions.forEach(Action::execute)
    }
}