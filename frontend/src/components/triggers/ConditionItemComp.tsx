import React, {Component} from 'react';
import {Condition, StaticCondition} from "./objects";
import './trigger.sass';
import ComponentListItemComp from "../../common/componentList/ComponentListItemComp";
import {api} from "../../api";
import {addNotification, Notification} from "../notification/notifications";
import ConditionFormComp from "./ConditionFormComp";

interface ComponentProps {
    condition: Condition,
    onClick: (condition: Condition) => void,
    onEditClick: (() => void) | null,
    onDeleteClick: (condition: Condition) => void,
    showForm: boolean,
    onSaved: (condition: Condition) => void,
    onSaveCancelled: () => void,
}

interface ComponentState {
    editStaticCondition: StaticCondition | null,
}

export default class ConditionItemComp extends Component<ComponentProps, ComponentState> {
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
            editStaticCondition: null,
        }

        this.loadStaticCondition = this.loadStaticCondition.bind(this);
    }

    componentDidUpdate(prevProps: Readonly<ComponentProps>, prevState: Readonly<ComponentState>, snapshot?: any) {
        if (prevProps.showForm === this.props.showForm) {
            return;
        }

        if (!this.props.showForm) {
            return;
        }

        this.loadStaticCondition();
    }

    render() {
        return <ComponentListItemComp className={"ConditionItemComp"}
                                      onEditClick={this.props.onEditClick}
                                      onClick={() => this.props.onClick(this.props.condition)}
                                      onDeleteClick={() => this.props.onDeleteClick(this.props.condition)}>
            <div className={"name"}>{this.props.condition.name}</div>
            {this.props.children}

            {!this.props.showForm || this.state.editStaticCondition === null ? "" :
                <ConditionFormComp staticCondition={this.state.editStaticCondition!}
                                   condition={this.props.condition}
                                   onSuccess={this.props.onSaved}
                                   onCancel={this.props.onSaveCancelled}/>}
        </ComponentListItemComp>;
    }

    private loadStaticCondition() {
        if (this.state.editStaticCondition !== null) {
            // StaticCondition is already loaded, so why load again?
            return;
        }

        api.conditions.edit(this.props.condition.id)
            .then(response => response.json())
            .then(data => {
                const staticCondition = data.data;

                if (staticCondition ! instanceof StaticCondition) {
                    return addNotification(new Notification(`Failed to load form for condition: ${this.props.condition.name}`, "", Notification.ERROR));
                }

                this.setState({
                    editStaticCondition: staticCondition,
                });
            })
            .catch(error => {
                console.error(`Error during loading form for condition: ${this.props.condition.name}`, error);
                addNotification(new Notification(`Error during loading form`, error.message, Notification.ERROR));
            });
    }
}