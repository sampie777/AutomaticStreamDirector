import {FormComponent} from "../forms/objects";

export class Action {
    name: string;

    constructor(name: string) {
        this.name = name;
    }
}

export class ActionSet {
    name: string;
    actions: Array<Action>;

    constructor(name: string,
                actions: Array<Action>) {
        this.name = name;
        this.actions = actions;
    }
}

export class StaticAction {
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