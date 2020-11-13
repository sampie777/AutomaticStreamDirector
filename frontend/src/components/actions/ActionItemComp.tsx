import React, {Component} from 'react';
import {Action} from "./objects";
import './actions.sass';

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
        return <div onClick={() => this.props.onClick(this.props.action)}>
            <div className={"ActionItemComp-name"}>{this.props.action.name}</div>
        </div>;
    }
}