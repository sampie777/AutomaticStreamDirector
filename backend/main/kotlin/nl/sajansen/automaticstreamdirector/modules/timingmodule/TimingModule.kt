package nl.sajansen.automaticstreamdirector.modules.timingmodule


import nl.sajansen.automaticstreamdirector.actions.StaticAction
import nl.sajansen.automaticstreamdirector.modules.Module
import nl.sajansen.automaticstreamdirector.modules.httpmodule.HttpModule
import nl.sajansen.automaticstreamdirector.modules.timingmodule.actions.DelayAction
import nl.sajansen.automaticstreamdirector.modules.timingmodule.conditions.ClockCondition
import nl.sajansen.automaticstreamdirector.modules.timingmodule.conditions.DateCondition
import nl.sajansen.automaticstreamdirector.modules.timingmodule.conditions.TimerCondition
import nl.sajansen.automaticstreamdirector.triggers.StaticCondition
import java.util.logging.Logger

class TimingModule : Module {
    private val logger = Logger.getLogger(HttpModule::class.java.name)

    override val name: String = HttpModule::class.java.name

    override fun actions(): List<StaticAction> {
        return listOf<StaticAction>(
            DelayAction,
        )
    }

    override fun conditions(): List<StaticCondition> {
        return listOf<StaticCondition>(
            TimerCondition,
            ClockCondition,
            DateCondition,
        )
    }
}