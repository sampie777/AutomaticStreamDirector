import React, {Component} from 'react';
import {Condition, Trigger} from "./objects";
import {ActionSet} from "../actions/objects";
import './trigger.sass';
import ComponentListItemComp from "../../common/componentList/ComponentListItemComp";
import App from "../../App";
import {addNotification, Notification} from "../notification/notifications";
import {api} from "../../api";

interface ComponentProps {
    trigger: Trigger,
    onDelete: () => void,
}

interface ComponentState {
}

export default class TriggerComp extends Component<ComponentProps, ComponentState> {
    public static defaultProps = {
        onDelete: () => null,
    }

    private readonly trigger: Trigger;

    constructor(props: ComponentProps) {
        super(props);
        this.trigger = props.trigger;

        this.onEditClick = this.onEditClick.bind(this);
        this.onDeleteClick = this.onDeleteClick.bind(this);
    }

    render() {
        return <ComponentListItemComp className={"TriggerComp"}
                                      onEditClick={this.onEditClick}
                                      onDeleteClick={this.onDeleteClick}
                                      onDoubleClick={this.onEditClick}>
            <h3>
                {this.trigger.name}
                <div title={"Importance"} className={"importance"}>{this.trigger.importance}</div>
            </h3>

            <h4>conditions</h4>
            <div className={"conditions"}>
                {this.trigger.conditions.length == 0 ? <i>none</i> : this.trigger.conditions.map((it: Condition) =>
                    <div title={it.name}
                         key={it.name}>{it.name}</div>)}
            </div>

            <h4>action sets</h4>
            <div className={"actionsets"}>
                {this.trigger.actionSets.length == 0 ? <i>none</i> : this.trigger.actionSets.map((it: ActionSet) =>
                    <div title={it.name}
                         key={it.name}>{it.name}</div>)}
            </div>
        </ComponentListItemComp>;
    }

    private onEditClick() {
        App.editTrigger(this.trigger);
    }

    private onDeleteClick() {
        const choice = window.confirm(`Are you sure you want to delete ${this.trigger.name}?`)

        if (!choice) {
            return;
        }

        api.triggers.delete(this.trigger.id)
            .then(response => response.json())
            .then(data => {
                const result = data.data;

                if (!result) {
                    return addNotification(new Notification("Failed to delete Trigger", "", Notification.ERROR));
                }

                addNotification(new Notification("Trigger deleted", this.props.trigger.name, Notification.SUCCESS));
                this.props.onDelete()
            })
            .catch(error => {
                console.error('Error deleting Trigger', error);
                addNotification(new Notification("Error deleting Trigger", error.message, Notification.ERROR));
            });
    }
}