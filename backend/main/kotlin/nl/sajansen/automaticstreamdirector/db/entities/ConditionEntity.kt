package nl.sajansen.automaticstreamdirector.db.entities

import nl.sajansen.automaticstreamdirector.db.DB
import nl.sajansen.automaticstreamdirector.db.managers.CommonDbManager
import nl.sajansen.automaticstreamdirector.jsonBuilder
import nl.sajansen.automaticstreamdirector.modules.Modules
import nl.sajansen.automaticstreamdirector.triggers.Condition
import nl.sajansen.automaticstreamdirector.triggers.Trigger
import org.hibernate.HibernateException
import java.util.logging.Logger
import javax.persistence.*

@Entity
data class ConditionEntity(
    @Id
    @GeneratedValue(strategy = GenerationType.IDENTITY)
    var id: Long? = null,
    var className: String? = null,
    var dataString: String? = null,

    @ManyToOne
    var trigger: Trigger? = null
) {

    companion object : CommonDbManager<ConditionEntity>(ConditionEntity::class.java) {
        private val logger = Logger.getLogger(ConditionEntity::class.java.name)

        fun fromCondition(condition: Condition): ConditionEntity {
            return ConditionEntity(
                id = condition.id,
                className = condition::class.java.name,
                dataString = jsonBuilder(prettyPrint = false).toJson(condition.getDataSet()),
            )
        }

        fun toCondition(conditionEntity: ConditionEntity): Condition? {
            val staticCondition = Modules.conditions()
                .find { it::class.java.enclosingClass.name == conditionEntity.className }

            if (staticCondition == null) {
                logger.warning("Module/StaticCondition not found for ConditionEntity: $this")
                return null
            }

            return try {
                staticCondition.fromDbEntity(conditionEntity)
            } catch (e: NullPointerException) {
                logger.severe("Failed to convert ConditionEntity to Condition: $conditionEntity")
                e.printStackTrace()
                null
            }
        }

        fun getByTrigger(trigger: Trigger?): List<ConditionEntity>? {
            val session = DB.connection().openSession()

            return try {
                session.beginTransaction()

                val list = session.criteriaBuilder.createQuery(ConditionEntity::class.java).let { criteria ->
                    val root = criteria.from(ConditionEntity::class.java)
                    criteria.select(root).where(
                        session.criteriaBuilder.equal(root.get<Trigger>("trigger"), trigger),
                    )
                    session.createQuery(criteria).resultList
                }

                session.transaction.commit()
                list
            } catch (e: HibernateException) {
                logger.severe("Exception occurred when getting list of ${ConditionEntity::class.java.name}")
                e.printStackTrace()
                null
            } finally {
                session.close()
            }
        }
    }
}