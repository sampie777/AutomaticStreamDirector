import React, {Component} from 'react';
import App from "../../App";
import {Button} from "semantic-ui-react";

interface ComponentProps {
}

interface ComponentState {
}

export default class OpenConfigButtonComp extends Component<ComponentProps, ComponentState> {

    constructor(props: ComponentProps) {
        super(props);
    }

    render() {
        return <Button circular
                       icon='settings'
                       className={"OpenConfigButtonComp"}
                       onClick={this.onClick}
                       content={"Settings"}/>
    }

    private onClick() {
        App.editConfig();
    }
}