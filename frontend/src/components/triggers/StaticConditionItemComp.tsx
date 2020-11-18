import React, {Component} from 'react';
import {StaticCondition} from "./objects";
import './trigger.sass';
import ComponentListItemComp from "../../common/componentList/ComponentListItemComp";

interface ComponentProps {
    condition: StaticCondition,
}

interface ComponentState {
}

export default class StaticConditionItemComp extends Component<ComponentProps, ComponentState> {
    private condition: StaticCondition;

    constructor(props: ComponentProps) {
        super(props);
        this.condition = props.condition;
    }

    render() {
        return <ComponentListItemComp>
            <div className={"StaticConditionItemComp-name"}>{this.condition.name}</div>
            <div className={"StaticConditionItemComp-previewText"}>{this.condition.previewText}</div>
        </ComponentListItemComp>;
    }
}