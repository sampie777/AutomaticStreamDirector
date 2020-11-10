import React, {Component} from 'react';
import {Condition, Trigger} from "./objects";
import {ActionSet} from "../actions/objects";

interface ComponentProps {
    trigger: Trigger,
}

export default class TriggerComp extends Component<ComponentProps, any> {
    private trigger: Trigger;

    constructor(props: ComponentProps) {
        super(props);
        this.trigger = props.trigger;
    }

    render() {
        return <div>
            <div>{this.trigger.name} <small>{this.trigger.importance}</small></div>
            <ol>
                {this.trigger.conditions.map((it: Condition) => <li key={it.name}>{it.name}</li>)}
            </ol>
            <ol>
                {this.trigger.actionSets.map((it: ActionSet) => <li key={it.name}>{it.name}</li>)}
            </ol>
        </div>;
    }
}