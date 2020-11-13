import React, {Component} from 'react';
import {Action, ActionSet, StaticAction} from "./objects";
import StaticActionListComp from "./StaticActionListComp";
import ActionItemComp from "./ActionItemComp";
import FormComponentComp from "../forms/FormComponentComp";
import {FormComponent, FormComponentType} from "../forms/objects";
import ActionFormComp from "./ActionFormComp";

interface ComponentProps {
    actionSet: ActionSet | null,
}

interface ComponentState {
    selectedActions: Array<Action>,
    newAction: StaticAction | null,
}

export default class ActionSetFormComp extends Component<ComponentProps, ComponentState> {
    private readonly actionSet: ActionSet | null;

    private readonly nameInputRef: React.RefObject<HTMLInputElement>;

    constructor(props: ComponentProps) {
        super(props);
        this.actionSet = props.actionSet;

        this.state = {
            selectedActions: [],
            newAction: null,
        }

        this.nameInputRef = React.createRef();

        this.onActionItemClickAdd = this.onActionItemClickAdd.bind(this);
        this.onActionItemClickRemove = this.onActionItemClickRemove.bind(this);
        this.onActionSaved = this.onActionSaved.bind(this);
        this.onActionSaveCancelled = this.onActionSaveCancelled.bind(this);
    }

    private onActionItemClickAdd(action: StaticAction) {
        console.log("Opening form for action: " + action.name)

        this.setState({
                newAction: null,
            },
            () => {
                this.setState({
                    newAction: action
                })
            })
    }

    private onActionItemClickRemove(action: Action) {
        this.setState({
            selectedActions: this.state.selectedActions.filter(it => it !== action)
        })
    }

    private onActionSaved(action: Action) {
        this.setState({
            newAction: null,
            selectedActions: this.state.selectedActions.concat([action])
        })
    }

    private onActionSaveCancelled() {
        this.setState({
            newAction: null,
        })
    }

    render() {
        return <div>
            <div>{this.actionSet == null ? "New action set" : `Edit '${this.actionSet.name}'`}</div>

            <FormComponentComp component={
                new FormComponent(
                    "name",
                    "Name",
                    FormComponentType.Text,
                    true,
                    this.actionSet?.name)}/>

            <div className={"component-list"}>
                <h3>Selected actions</h3>
                {this.state.selectedActions
                    .map((action, i) => <ActionItemComp action={action}
                                                        onClick={this.onActionItemClickRemove}
                                                        key={i}/>)
                }
            </div>

            <StaticActionListComp onItemClick={this.onActionItemClickAdd}/>

            {this.state.newAction == null ? "" :
                <ActionFormComp staticAction={this.state.newAction} onSuccess={this.onActionSaved}
                                onCancel={this.onActionSaveCancelled}/>}
        </div>;
    }
}