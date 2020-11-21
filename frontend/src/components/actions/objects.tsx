import {FormComponent} from "../../common/forms/objects";

export class Action {
    id: number | null;
    name: string;

    constructor(id: number | null, name: string) {
        this.id = id;
        this.name = name;
    }

    static equals(a: Action | null, b: Action | null) {
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

export class ActionSet {
    id: number | null;
    name: string;
    actions: Array<Action>;

    constructor(id: number | null,
                name: string,
                actions: Array<Action>) {
        this.id = id;
        this.name = name;
        this.actions = actions;
    }

    static equals(a: ActionSet | null, b: ActionSet | null) {
        if (a == null && b == null)
            return true;

        if (a == null || b == null)
            return false;

        if (a.id !== b.id)
            return false;

        if (a.name !== b.name)
            return false;

        if (a.actions.length !== b.actions.length)
            return false;

        for (let i = 0; i < a.actions.length; i++) {
            if (!Action.equals(a.actions[i], b.actions[i]))
                return false;
        }

        return true;
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

    static equals(a: StaticAction | null, b: StaticAction | null) {
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