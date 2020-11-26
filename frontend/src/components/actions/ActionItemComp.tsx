import React, {Component} from 'react';
import {Action, StaticAction} from "./objects";
import './actions.sass';
import ComponentListItemComp from "../../common/componentList/ComponentListItemComp";
import ActionFormComp from "./ActionFormComp";
import {api} from "../../api";
import {addNotification, Notification} from "../notification/notifications";

interface ComponentProps {
    action: Action,
    onClick: (action: Action) => void,
    onEditClick: (() => void) | null,
    onDeleteClick: (action: Action) => void,
    showForm: boolean,
    onSaved: (action: Action) => void,
    onSaveCancelled: () => void,
}

interface ComponentState {
    editStaticAction: StaticAction | null,
}

export default class ActionItemComp extends Component<ComponentProps, ComponentState> {
    public static defaultProps = {
        onClick: () => null,
        onEditClick: null,
        onDeleteClick: null,
        showForm: false,
        onSaved: () => null,
        onSaveCancelled: () => null,
    }

    constructor(props: ComponentProps) {
        super(props);

        this.state = {
            editStaticAction: null,
        }

        this.loadStaticAction = this.loadStaticAction.bind(this);
    }

    componentDidUpdate(prevProps: Readonly<ComponentProps>, prevState: Readonly<ComponentState>, snapshot?: any) {
        if (prevProps.showForm === this.props.showForm) {
            return;
        }

        if (!this.props.showForm) {
            return;
        }

        this.loadStaticAction();
    }

    render() {
        return <ComponentListItemComp className={"ActionItemComp"}
                                      onEditClick={this.props.onEditClick}
                                      onClick={() => this.props.onClick(this.props.action)}
                                      onDeleteClick={() => this.props.onDeleteClick(this.props.action)}>
            <div className={"name"}>{this.props.action.name}</div>
            {this.props.children}

            {!this.props.showForm || this.state.editStaticAction === null ? "" :
                <ActionFormComp staticAction={this.state.editStaticAction!}
                                   action={this.props.action}
                                   onSuccess={this.props.onSaved}
                                   onCancel={this.props.onSaveCancelled}/>}
        </ComponentListItemComp>;
    }

    private loadStaticAction() {
        if (this.state.editStaticAction !== null) {
            // StaticAction is already loaded, so why load again?
            return;
        }

        api.actions.edit(this.props.action.id)
            .then(response => response.json())
            .then(data => {
                const staticAction = data.data;

                if (staticAction ! instanceof StaticAction) {
                    return addNotification(new Notification(`Failed to load form for action: ${this.props.action.name}`, "", Notification.ERROR));
                }

                this.setState({
                    editStaticAction: staticAction,
                });
            })
            .catch(error => {
                console.error(`Error during loading form for action: ${this.props.action.name}`, error);
                addNotification(new Notification(`Error during loading form`, error.message, Notification.ERROR));
            });
    }
}