import {ActionSet} from "../actions/objects";
import {FormComponent} from "../../common/forms/objects";

export class Condition {
    id: number | null;
    name: string;

    constructor(id: number | null, name: string) {
        this.id = id;
        this.name = name;
    }

    static equals(a: Condition | null, b: Condition | null) {
        if (a == null && b == null)
            return true;

        if (a == null || b == null)
            return false;

        if (a.id !== b.id)
            return false;

        if (a.name !== b.name)
            return false;
        return true;
    }
}

export class Trigger {
    id: number | null;
    name: string;
    importance: number;
    conditions: Array<Condition>;
    actionSets: Array<ActionSet>;

    constructor(id: number | null,
                name: string,
                importance: number,
                conditions: Array<Condition>,
                actionSets: Array<ActionSet>) {
        this.id = id;
        this.name = name;
        this.importance = importance;
        this.conditions = conditions;
        this.actionSets = actionSets;
    }

    static equals(a: Trigger | null, b: Trigger | null) {
        if (a == null && b == null)
            return true;

        if (a == null || b == null)
            return false;

        if (a.id !== b.id)
            return false;

        if (a.name !== b.name)
            return false;

        if (a.importance !== b.importance)
            return false;

        if (a.conditions.length !== b.conditions.length)
            return false;

        for (let i = 0; i < a.conditions.length; i++) {
            if (!Condition.equals(a.conditions[i], b.conditions[i]))
                return false;
        }

        if (a.actionSets.length !== b.actionSets.length)
            return false;

        for (let i = 0; i < a.actionSets.length; i++) {
            if (!ActionSet.equals(a.actionSets[i], b.actionSets[i]))
                return false;
        }

        return true;
    }
}

export class StaticCondition {
    className: string;
    name: string;
    previewText: string;
    formComponents: Array<FormComponent>;

    constructor(className: string,
                name: string,
                previewText: string,
                formComponents: Array<FormComponent> = []) {
        this.className = className;
        this.name = name;
        this.previewText = previewText;
        this.formComponents = formComponents;
    }

    static equals(a: StaticCondition | null, b: StaticCondition | null) {
        if (a == null && b == null)
            return true;

        if (a == null || b == null)
            return false;

        if (a.className !== b.className)
            return false;

        if (a.name !== b.name)
            return false;

        if (a.previewText !== b.previewText)
            return false;

        if (a.formComponents.length !== b.formComponents.length)
            return false;

        for (let i = 0; i < a.formComponents.length; i++) {
            if (!FormComponent.equals(a.formComponents[i], b.formComponents[i]))
                return false;
        }

        return true;
    }
}