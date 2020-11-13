import React, {Component} from 'react';
import {StaticAction} from "./objects";
import './actions.sass';

interface ComponentProps {
    staticAction: StaticAction,
    onClick: (action: StaticAction) => void,
}

interface ComponentState {
}

export default class StaticActionItemComp extends Component<ComponentProps, ComponentState> {
    private readonly staticAction: StaticAction;

    constructor(props: ComponentProps) {
        super(props);
        this.staticAction = props.staticAction;
    }

    render() {
        return <div onClick={() => this.props.onClick(this.staticAction)}>
            <div className={"StaticActionItemComp-name"}>{this.staticAction.name}</div>
            <div className={"StaticActionItemComp-previewText"}>{this.staticAction.previewText}</div>
        </div>;
    }
}