import {ActionSet} from "../actions/objects";

export class Condition {
    name: string;

    constructor(name: string) {
        this.name = name;
    }
}

export class Trigger {
    name: string;
    importance: number;
    conditions: Array<Condition>;
    actionSets: Array<ActionSet>;

    constructor(name: string,
                importance: number,
                conditions: Array<Condition>,
                actionSets: Array<ActionSet>) {
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

    constructor(className: string,
                name: string,
                previewText: string) {
        this.className = className;
        this.name = name;
        this.previewText = previewText;
    }
}