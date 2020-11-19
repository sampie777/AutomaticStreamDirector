import React, {Component} from 'react';
import {Action} from "./objects";
import './actions.sass';
import ComponentListItemComp from "../../common/componentList/ComponentListItemComp";

interface ComponentProps {
    action: Action,
    onClick: (action: Action) => void,
}

interface ComponentState {
}

export default class ActionItemComp extends Component<ComponentProps, ComponentState> {
    constructor(props: ComponentProps) {
        super(props);
    }

    render() {
        return <ComponentListItemComp className={"ActionItemComp"}
                                      onDeleteClick={() => this.props.onClick(this.props.action)}>
            <div className={"name"}>{this.props.action.name}</div>
        </ComponentListItemComp>;
    }
}