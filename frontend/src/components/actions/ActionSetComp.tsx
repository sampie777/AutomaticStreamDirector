import React, {Component} from 'react';
import {ActionSet} from "./objects";
import {api} from "../../api";
import {addNotification, Notification} from "../notification/notifications";
import App from "../../App";
import ComponentListItemComp from "../../common/componentList/ComponentListItemComp";
import {Accordion, Label} from "semantic-ui-react";

interface ComponentProps {
    actionSet: ActionSet,
    onDelete: () => void,
    onClick: () => void,
    onDeleteClick: (() => void) | null,
}

interface ComponentState {
}

export default class ActionSetComp extends Component<ComponentProps, ComponentState> {
    public static defaultProps = {
        onDelete: () => null,
        onClick: () => null,
        onDeleteClick: undefined,
    }

    constructor(props: ComponentProps) {
        super(props);

        this.onEditClick = this.onEditClick.bind(this);
        this.onDeleteClick = this.onDeleteClick.bind(this);
    }

    private childPanels = [
        {
            key: 'actions',
            title: {
                content: (<h4>
                    Actions
                    <Label circular content={this.props.actionSet.actions.length}/>
                </h4>),
            },
            content: {
                content: (<div className={"actions"}>
                    {this.props.actionSet.actions.length == 0 ?
                        <i>none</i> : this.props.actionSet.actions.map(it =>
                            <div title={it.name}
                                 key={it.id + it.name}>{it.name}</div>)}
                </div>),
            },
        },
    ]

    render() {
        return <ComponentListItemComp className={"ActionSetComp"}
                                      onEditClick={this.onEditClick}
                                      onDeleteClick={this.props.onDeleteClick !== undefined ? this.props.onDeleteClick : this.onDeleteClick}
                                      onClick={this.props.onClick}
                                      onDoubleClick={this.onEditClick}>
            <h3>{this.props.actionSet.name}</h3>

            <Accordion exclusive={false}
                       panels={this.childPanels}
                       fluid/>
        </ComponentListItemComp>;
    }

    private onEditClick() {
        App.editActionSet(this.props.actionSet);
    }

    private onDeleteClick() {
        const choice = window.confirm(`Are you sure you want to delete ${this.props.actionSet.name}?`)

        if (!choice) {
            return;
        }

        api.actionSets.delete(this.props.actionSet.id)
            .then(response => response.json())
            .then(data => {
                const result = data.data;

                if (!result) {
                    return addNotification(new Notification("Failed to delete ActionSet", "", Notification.ERROR));
                }

                addNotification(new Notification("Action set deleted", this.props.actionSet.name, Notification.SUCCESS));
                this.props.onDelete()
            })
            .catch(error => {
                console.error('Error deleting ActionSet', error);
                addNotification(new Notification("Error deleting ActionSet", error.message, Notification.ERROR));
            });
    }
}