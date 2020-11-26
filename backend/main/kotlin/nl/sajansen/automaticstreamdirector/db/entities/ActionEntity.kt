package nl.sajansen.automaticstreamdirector.db.entities

import nl.sajansen.automaticstreamdirector.actions.Action
import nl.sajansen.automaticstreamdirector.actions.ActionSet
import nl.sajansen.automaticstreamdirector.db.managers.CommonDbManager
import nl.sajansen.automaticstreamdirector.jsonBuilder
import nl.sajansen.automaticstreamdirector.modules.Modules
import java.util.logging.Logger
import javax.persistence.*

@Entity
data class ActionEntity(
    @Id
    @GeneratedValue(strategy = GenerationType.IDENTITY)
    var id: Long? = null,
    var className: String? = null,
    var dataString: String? = null,

    @ManyToOne
    var actionSet: ActionSet? = null
) {

    companion object : CommonDbManager<ActionEntity>(ActionEntity::class.java) {
        private val logger = Logger.getLogger(ActionEntity::class.java.name)

        fun fromAction(action: Action): ActionEntity {
            return ActionEntity(
                id = action.id,
                className = action::class.java.name,
                dataString = jsonBuilder(prettyPrint = false).toJson(action.getDataSet()),
            )
        }

        fun toAction(actionEntity: ActionEntity): Action? {
            val staticAction = Modules.actions()
                .find { it::class.java.enclosingClass.name == actionEntity.className }

            if (staticAction == null) {
                logger.warning("Module/StaticAction not found for ActionEntity: $this")
                return null
            }

            return try {
                staticAction.fromDbEntity(actionEntity)
            } catch (e: NullPointerException) {
                logger.severe("Failed to convert ActionEntity to Action: $actionEntity")
                e.printStackTrace()
                null
            }
        }
    }
}