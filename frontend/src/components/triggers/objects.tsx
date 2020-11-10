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