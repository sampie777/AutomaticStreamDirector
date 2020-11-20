import React, {Component} from 'react';
import App from "../../App";

interface ComponentProps {
}

interface ComponentState {
}

export default class OpenConfigButtonComp extends Component<ComponentProps, ComponentState> {

    constructor(props: ComponentProps) {
        super(props);
    }

    render() {
        return <button className={"OpenConfigButtonComp"} onClick={this.onClick}>
            Config
        </button>;
    }

    private onClick() {
        App.editConfig();
    }
}