import {FormComponent} from "../../common/forms/objects";

export class Action {
    id: number | null;
    name: string;

    constructor(id: number | null, name: string) {
        this.id = id
        this.name = name;
    }
}

export class ActionSet {
    id: number | null;
    name: string;
    actions: Array<Action>;

    constructor(id: number | null,
                name: string,
                actions: Array<Action>) {
        this.id = id
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