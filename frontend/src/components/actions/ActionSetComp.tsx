import React, {Component} from 'react';
import {Action, ActionSet} from "./objects";

interface ComponentProps {
    actionSet: ActionSet,
}

export default class ActionSetComp extends Component<ComponentProps, any> {
    private actionSet: ActionSet;

    constructor(props: ComponentProps) {
        super(props);
        this.actionSet = props.actionSet;
    }

    render() {
        return <div>
            <div>{this.actionSet.name}</div>
            <ol>
                {this.actionSet.actions.map((it: Action) => <li key={it.name}>{it.name}</li>)}
            </ol>
        </div>;
    }
}