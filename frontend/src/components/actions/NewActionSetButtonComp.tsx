import React, {Component} from 'react';
import App from "../../App";
import NewComponentButtonComp from "../../common/componentList/NewComponentButtonComp";

interface ComponentProps {
}

interface ComponentState {
}

export default class NewActionSetButtonComp extends Component<ComponentProps, ComponentState> {

    constructor(props: ComponentProps) {
        super(props);
    }

    render() {
        return <NewComponentButtonComp onClick={this.onClick} />;
    }

    private onClick() {
        App.editActionSet(null);
    }
}