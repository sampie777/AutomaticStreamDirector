import React, {Component} from 'react';
import {StaticAction} from "./objects";
import './actions.sass';

interface ComponentProps {
    action: StaticAction,
}

interface ComponentState {
}

export default class StaticActionItemComp extends Component<ComponentProps, ComponentState> {
    private action: StaticAction;

    constructor(props: ComponentProps) {
        super(props);
        this.action = props.action;
    }

    render() {
        return <div>
            <div className={"StaticActionItemComp-name"}>{this.action.name}</div>
            <div className={"StaticActionItemComp-previewText"}>{this.action.previewText}</div>
        </div>;
    }
}