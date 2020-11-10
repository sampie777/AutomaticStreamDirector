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