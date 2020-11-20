import React, {Component} from 'react';
import {Button} from "semantic-ui-react";

interface ComponentProps {
    onClick: () => void,
}

interface ComponentState {
}

export default class NewComponentButtonComp extends Component<ComponentProps, ComponentState> {

    constructor(props: ComponentProps) {
        super(props);
    }

    render() {
        return <Button size='huge'
                       basic color='blue'
                       content={"New"}
                       className={"NewComponentButtonComp"}
                       onClick={this.props.onClick} />;
    }
}