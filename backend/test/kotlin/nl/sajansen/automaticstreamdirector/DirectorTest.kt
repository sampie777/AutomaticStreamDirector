package nl.sajansen.automaticstreamdirector

import nl.sajansen.automaticstreamdirector.actions.ActionSet
import nl.sajansen.automaticstreamdirector.mocks.ActionMock
import nl.sajansen.automaticstreamdirector.mocks.ConditionMock
import nl.sajansen.automaticstreamdirector.project.Project
import nl.sajansen.automaticstreamdirector.triggers.Trigger
import kotlin.test.*

class DirectorTest {

    @BeforeTest
    fun before() {
        Project.triggers.clear()
        Project.availableActionSets.clear()
    }

    @Test
    fun testTriggerExecutesWhenConditionsAllAreTrue() {
        val action = ActionMock()
        val actionSet = ActionSet("ActionSet1")
        actionSet.actions.add(action)
        Project.availableActionSets.add(actionSet)

        val trigger = Trigger("TriggerMock1")
        trigger.conditions.add(ConditionMock(checkReturnValue = true))
        trigger.conditions.add(ConditionMock(checkReturnValue = true))
        trigger.actionSets.add(actionSet)
        Project.triggers.add(trigger)
        Project.triggers.add(Trigger("TriggerMock2"))

        Director.update()
        Director.waitForActionSetCompletion()

        assertTrue(action.isExecuted)
        assertEquals(trigger, Director.getLastTrigger())
    }

    @Test
    fun testTriggerNotExecutesWhenConditionsNotAllAreTrue() {
        val action = ActionMock()
        val actionSet = ActionSet("ActionSet1")
        actionSet.actions.add(action)
        Project.availableActionSets.add(actionSet)

        val trigger = Trigger("TriggerMock1")
        trigger.conditions.add(ConditionMock(checkReturnValue = true))
        trigger.conditions.add(ConditionMock(checkReturnValue = false))
        trigger.actionSets.add(actionSet)
        Project.triggers.add(trigger)
        Project.triggers.add(Trigger("TriggerMock2"))

        Director.update()
        Director.waitForActionSetCompletion()

        assertFalse(action.isExecuted)
        assertNull(Director.getLastTrigger())
    }

    @Test
    fun testTriggerNotExecutesWhenConditionsAllAreNotTrue() {
        val action = ActionMock()
        val actionSet = ActionSet("ActionSet1")
        actionSet.actions.add(action)
        Project.availableActionSets.add(actionSet)

        val trigger = Trigger("TriggerMock1")
        trigger.conditions.add(ConditionMock(checkReturnValue = false))
        trigger.conditions.add(ConditionMock(checkReturnValue = false))
        trigger.actionSets.add(actionSet)
        Project.triggers.add(trigger)
        Project.triggers.add(Trigger("TriggerMock2"))

        Director.update()
        Director.waitForActionSetCompletion()

        assertFalse(action.isExecuted)
        assertNull(Director.getLastTrigger())
    }
}