import React, {Component} from 'react';
import {Condition, StaticCondition} from "./objects";
import './trigger.sass';
import ComponentListItemComp from "../../common/componentList/ComponentListItemComp";
import ConditionFormComp from "./ConditionFormComp";

interface ComponentProps {
    staticCondition: StaticCondition,
    onClick: (condition: StaticCondition) => void,
    showForm: boolean,
    onConditionSaved: (condition: Condition) => void,
    onConditionSaveCancelled: () => void,
}

interface ComponentState {
}

export default class StaticConditionItemComp extends Component<ComponentProps, ComponentState> {
    public static defaultProps = {
        showForm: false,
        onConditionSaved: () => null,
        onConditionSaveCancelled: () => null,
    }

    constructor(props: ComponentProps) {
        super(props);
    }

    render() {
        return <ComponentListItemComp className={"StaticConditionItemComp"}
                                      onClick={() => this.props.onClick(this.props.staticCondition)}>
            <div className={"name"}>{this.props.staticCondition.name}</div>
            <div className={"previewText"}>{this.props.staticCondition.previewText}</div>

            {!this.props.showForm ? "" :
                <ConditionFormComp staticCondition={this.props.staticCondition}
                                onSuccess={this.props.onConditionSaved}
                                onCancel={this.props.onConditionSaveCancelled}/>}
        </ComponentListItemComp>;
    }
}