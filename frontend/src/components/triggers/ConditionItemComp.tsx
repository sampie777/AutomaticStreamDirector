import React, {Component} from 'react';
import {Condition} from "./objects";
import './trigger.sass';
import ComponentListItemComp from "../../common/componentList/ComponentListItemComp";

interface ComponentProps {
    condition: Condition,
    onClick: (condition: Condition) => void,
}

interface ComponentState {
}

export default class ConditionItemComp extends Component<ComponentProps, ComponentState> {
    constructor(props: ComponentProps) {
        super(props);
    }

    render() {
        return <ComponentListItemComp className={"ConditionItemComp"}
                                      onDeleteClick={() => this.props.onClick(this.props.condition)}>
            <div className={"name"}>{this.props.condition.name}</div>
        </ComponentListItemComp>;
    }
}