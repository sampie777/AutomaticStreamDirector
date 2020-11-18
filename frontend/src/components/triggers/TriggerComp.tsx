import React, {Component} from 'react';
import {Condition, Trigger} from "./objects";
import {ActionSet} from "../actions/objects";
import TriggerEditFormComp from "./TriggerEditFormComp";
import './trigger.sass';

interface ComponentProps {
    trigger: Trigger,
    onUpdated: Function
}

interface ComponentState {
    isEditing: boolean
}

export default class TriggerComp extends Component<ComponentProps, ComponentState> {
    private readonly trigger: Trigger;

    constructor(props: ComponentProps) {
        super(props);
        this.trigger = props.trigger;

        this.state = {
            isEditing: false
        };

        this.onEditClick = this.onEditClick.bind(this);
        this.onUpdated = this.onUpdated.bind(this);
    }

    render() {
        return <div className={"TriggerComp"}>
            <div>{this.trigger.name} <span className={"importance"}>{this.trigger.importance}</span></div>
            <ol>
                {this.trigger.conditions.map((it: Condition) => <li key={it.name}>{it.name}</li>)}
            </ol>
            <ol>
                {this.trigger.actionSets.map((it: ActionSet) => <li key={it.name}>{it.name}</li>)}
            </ol>

            <a onClick={this.onEditClick} className={"edit"}>Edit</a>

            {!this.state.isEditing ? "" : <TriggerEditFormComp trigger={this.trigger}
                                                               onUpdated={this.onUpdated} />}
        </div>;
    }

    private onEditClick() {
        this.setState({
            isEditing: !this.state.isEditing
        });
    }

    private onUpdated() {
        this.setState({
            isEditing: false
        });

        this.props.onUpdated();
    }
}