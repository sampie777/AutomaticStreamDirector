import React, {Component} from 'react';
import App from "../../App";

interface ComponentProps {
}

interface ComponentState {
}

export default class NewTriggerButtonComp extends Component<ComponentProps, ComponentState> {

    constructor(props: ComponentProps) {
        super(props);
    }

    render() {
        return <button className={"NewTriggerButtonComp"} onClick={this.onClick}>
            New
        </button>;
    }

    private onClick() {
        App.editTrigger(null);
    }
}