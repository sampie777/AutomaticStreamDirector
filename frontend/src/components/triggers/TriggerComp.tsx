import React, {Component} from 'react';
import {Condition, Trigger} from "./objects";
import {ActionSet} from "../actions/objects";
import './trigger.sass';
import ComponentListItemComp from "../../common/componentList/ComponentListItemComp";
import App from "../../App";
import {addNotification, Notification} from "../notification/notifications";
import {api} from "../../api";
import {Accordion, Label} from "semantic-ui-react";
import ActionSetComp from "../actions/ActionSetComp";

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

    constructor(props: ComponentProps) {
        super(props);

        this.onEditClick = this.onEditClick.bind(this);
        this.onDeleteClick = this.onDeleteClick.bind(this);
    }

    private childPanels = [
        {
            key: 'conditions',
            title: {
                content: (<h4>
                    Conditions
                    <Label circular content={this.props.trigger.conditions.length}/>
                </h4>),
            },
            content: {
                content: (<div className={"conditions"}>
                    {this.props.trigger.conditions.length == 0 ?
                        <i>none</i> : this.props.trigger.conditions.map((it: Condition) =>
                            <div title={it.name}
                                 key={it.id + it.name}>{it.name}</div>)}
                </div>),
            },
        },
        {
            key: 'actionSets',
            title: {
                content: (<h4>
                    Action sets
                    <Label circular content={this.props.trigger.actionSets.length}/>
                </h4>),
            },
            content: {
                content: (<div className={"actionsets"}>
                    {this.props.trigger.actionSets.length == 0 ?
                        <i>none</i> : this.props.trigger.actionSets.map((it: ActionSet) =>
                            <ActionSetComp actionSet={it}
                                           key={it.id + it.name}
                                           onDeleteClick={null}/>)}
                </div>),
            },
        },
    ]

    render() {
        return <ComponentListItemComp className={"TriggerComp"}
                                      onEditClick={this.onEditClick}
                                      onDeleteClick={this.onDeleteClick}
                                      onDoubleClick={this.onEditClick}>
            <h3>
                {this.props.trigger.name}
                <div title={"Importance"} className={"importance"}>{this.props.trigger.importance}</div>
            </h3>

            <Accordion exclusive={false}
                       panels={this.childPanels}
                       fluid/>
        </ComponentListItemComp>;
    }

    private onEditClick() {
        App.editTrigger(this.props.trigger);
    }

    private onDeleteClick() {
        const choice = window.confirm(`Are you sure you want to delete ${this.props.trigger.name}?`)

        if (!choice) {
            return;
        }

        api.triggers.delete(this.props.trigger.id)
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