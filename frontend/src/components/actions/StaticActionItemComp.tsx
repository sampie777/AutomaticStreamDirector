import React, {Component} from 'react';
import {Action, StaticAction} from "./objects";
import './actions.sass';
import ComponentListItemComp from "../../common/componentList/ComponentListItemComp";
import ActionFormComp from "./ActionFormComp";

interface ComponentProps {
    staticAction: StaticAction,
    onClick: (action: StaticAction) => void,
    showForm: boolean,
    onActionSaved: (action: Action) => void,
    onActionSaveCancelled: () => void,
}

interface ComponentState {
}

export default class StaticActionItemComp extends Component<ComponentProps, ComponentState> {
    public static defaultProps = {
        showForm: false,
        onActionSaved: () => null,
        onActionSaveCancelled: () => null,
    }

    constructor(props: ComponentProps) {
        super(props);
    }

    render() {
        return <ComponentListItemComp className={"StaticActionItemComp"}
                                      onClick={() => this.props.onClick(this.props.staticAction)}>
            <div className={"name"}>{this.props.staticAction.name}</div>
            <div className={"previewText"}>{this.props.staticAction.previewText}</div>

            {!this.props.showForm ? "" :
                <ActionFormComp staticAction={this.props.staticAction}
                                onSuccess={this.props.onActionSaved}
                                onCancel={this.props.onActionSaveCancelled}/>}
        </ComponentListItemComp>;
    }
}