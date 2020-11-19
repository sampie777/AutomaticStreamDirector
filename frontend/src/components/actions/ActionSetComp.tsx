import React, {Component} from 'react';
import {ActionSet} from "./objects";
import {api} from "../../api";
import {addNotification, Notification} from "../notification/notifications";
import App from "../../App";
import ComponentListItemComp from "../../common/componentList/ComponentListItemComp";

interface ComponentProps {
    actionSet: ActionSet,
    onDelete: () => void,
    onClick: () => void,
    onDeleteClick: () => void,
}

interface ComponentState {
}

export default class ActionSetComp extends Component<ComponentProps, ComponentState> {
    public static defaultProps = {
        onDelete: () => null,
        onClick: () => null,
        onDeleteClick: null,
    }

    private readonly actionSet: ActionSet;

    constructor(props: ComponentProps) {
        super(props);
        this.actionSet = props.actionSet;

        this.onEditClick = this.onEditClick.bind(this);
        this.onDeleteClick = this.onDeleteClick.bind(this);
    }

    render() {
        return <ComponentListItemComp className={"ActionSetComp"}
                                      onEditClick={this.onEditClick}
                                      onDeleteClick={this.onDeleteClick}
                                      onClick={this.props.onClick}
                                      onDoubleClick={this.onEditClick}>
            <h3>{this.actionSet.name}</h3>
            <div className={"actions"}>
                {this.actionSet.actions.map((it, i) =>
                    <div title={it.name}
                        key={i + it.name}>{it.name}</div>)}
            </div>
        </ComponentListItemComp>;
    }

    private onEditClick() {
        App.editActionSet(this.actionSet);
    }

    private onDeleteClick() {
        if (this.props.onDeleteClick != null) {
            console.debug("Overriding onDeleteClick");
            return this.props.onDeleteClick();
        }

        const choice = window.confirm(`Are you sure you want to delete ${this.actionSet.name}?`)

        if (!choice) {
            return;
        }

        api.actionSets.delete(this.actionSet.id)
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