import {ActionSet} from "../actions/objects";
import {FormComponent} from "../../common/forms/objects";

export class Condition {
    id: number | null;
    name: string;

    constructor(id: number | null, name: string) {
        this.id = id;
        this.name = name;
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
}