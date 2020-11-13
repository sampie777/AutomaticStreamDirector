import React, {Component} from 'react';
import {ActionSet} from "./objects";

interface ComponentProps {
    actionSet: ActionSet,
}

interface ComponentState {
}

export default class ActionSetComp extends Component<ComponentProps, ComponentState> {
    private actionSet: ActionSet;

    constructor(props: ComponentProps) {
        super(props);
        this.actionSet = props.actionSet;
    }

    render() {
        return <div>
            <div>{this.actionSet.name}</div>
            <ol>
                {this.actionSet.actions.map((it, i) => <li key={i + it.name}>{it.name}</li>)}
            </ol>
        </div>;
    }
}