package nl.sajansen.automaticstreamdirector

import nl.sajansen.automaticstreamdirector.mocks.ActionMock
import nl.sajansen.automaticstreamdirector.mocks.ActionSetMock
import nl.sajansen.automaticstreamdirector.mocks.ConditionMock
import nl.sajansen.automaticstreamdirector.mocks.TriggerMock
import nl.sajansen.automaticstreamdirector.project.Project
import kotlin.test.*

class DirectorTest {

    @BeforeTest
    fun before() {
        Project.triggers.clear()
        Project.actionSets.clear()
    }

    @Test
    fun testTriggerExecutesWhenConditionsAllAreTrue() {
        val action = ActionMock()
        val actionSet = ActionSetMock()
        actionSet.actions.add(action)
        Project.actionSets.add(actionSet)

        val trigger = TriggerMock()
        trigger.conditions.add(ConditionMock(checkReturnValue = true))
        trigger.conditions.add(ConditionMock(checkReturnValue = true))
        trigger.actionSets.add(actionSet)
        Project.triggers.add(trigger)
        Project.triggers.add(TriggerMock())

        Director.update()

        assertTrue(action.isExecuted)
        assertEquals(trigger, Director.getLastTrigger())
    }

    @Test
    fun testTriggerNotExecutesWhenConditionsNotAllAreTrue() {
        val action = ActionMock()
        val actionSet = ActionSetMock()
        actionSet.actions.add(action)
        Project.actionSets.add(actionSet)

        val trigger = TriggerMock()
        trigger.conditions.add(ConditionMock(checkReturnValue = true))
        trigger.conditions.add(ConditionMock(checkReturnValue = false))
        trigger.actionSets.add(actionSet)
        Project.triggers.add(trigger)
        Project.triggers.add(TriggerMock())

        Director.update()

        assertFalse(action.isExecuted)
        assertNull(Director.getLastTrigger())
    }

    @Test
    fun testTriggerNotExecutesWhenConditionsAllAreNotTrue() {
        val action = ActionMock()
        val actionSet = ActionSetMock()
        actionSet.actions.add(action)
        Project.actionSets.add(actionSet)

        val trigger = TriggerMock()
        trigger.conditions.add(ConditionMock(checkReturnValue = false))
        trigger.conditions.add(ConditionMock(checkReturnValue = false))
        trigger.actionSets.add(actionSet)
        Project.triggers.add(trigger)
        Project.triggers.add(TriggerMock())

        Director.update()

        assertFalse(action.isExecuted)
        assertNull(Director.getLastTrigger())
    }
}