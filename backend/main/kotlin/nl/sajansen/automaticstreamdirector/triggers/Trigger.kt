package nl.sajansen.automaticstreamdirector.triggers

import nl.sajansen.automaticstreamdirector.actions.ActionSet
import nl.sajansen.automaticstreamdirector.db.entities.ConditionEntity
import nl.sajansen.automaticstreamdirector.db.managers.CommonDbManager
import org.hibernate.annotations.LazyCollection
import org.hibernate.annotations.LazyCollectionOption
import javax.persistence.*


@Entity
data class Trigger(
    var name: String = "",
    var importance: Int = 0,
    @Transient var conditions: ArrayList<Condition> = arrayListOf(),
    @Transient var actionSets: ArrayList<ActionSet> = arrayListOf(),

    @Id
    @GeneratedValue(strategy = GenerationType.IDENTITY)
    var id: Long? = null,
) {

    @OneToMany(mappedBy = "trigger", cascade = [CascadeType.ALL], fetch = FetchType.EAGER)
    private var conditionEntities: List<ConditionEntity> = emptyList()

    @ManyToMany(cascade = [CascadeType.DETACH])
    @JoinTable(
        name = "Trigger_ActionSet",
        joinColumns = [JoinColumn(name = "trigger_id")],
        inverseJoinColumns = [JoinColumn(name = "actionset_id")],
    )
    @LazyCollection(LazyCollectionOption.FALSE)
    private var actionSetEntities: List<ActionSet> = emptyList()

    companion object : CommonDbManager<Trigger>(Trigger::class.java) {
        override fun saveOrUpdate(obj: Trigger): Boolean {
            obj.conditionEntities = obj.conditions.map(ConditionEntity::fromCondition)
            obj.conditionEntities.forEach { it.trigger = obj }

            obj.actionSets.forEach { it.assignTrigger(obj) }
            obj.actionSetEntities = obj.actionSets

            return super.saveOrUpdate(obj)
        }

        override fun list(): MutableList<Trigger?>? {
            val list = super.list() ?: return null
            return list.map(::fromDbEntity).toMutableList()
        }

        override fun get(id: Long): Trigger? {
            return super.get(id)?.run(::fromDbEntity)
        }

        fun fromDbEntity(trigger: Trigger?): Trigger? {
            if (trigger == null) {
                return null
            }

            trigger.conditions = ArrayList(trigger.conditionEntities.mapNotNull(ConditionEntity::toCondition))
            trigger.actionSets = ArrayList(trigger.actionSetEntities.mapNotNull(ActionSet::fromDbEntity))
            return trigger
        }
    }
}