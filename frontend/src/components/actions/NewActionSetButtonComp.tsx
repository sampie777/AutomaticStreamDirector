import React, {Component} from 'react';
import App from "../../App";

interface ComponentProps {
}

interface ComponentState {
}

export default class NewActionSetButtonComp extends Component<ComponentProps, ComponentState> {

    constructor(props: ComponentProps) {
        super(props);
    }

    render() {
        return <button className={"NewActionSetButtonComp"} onClick={this.onClick}>
            New
        </button>;
    }

    private onClick() {
        App.editActionSet(null);
    }
}