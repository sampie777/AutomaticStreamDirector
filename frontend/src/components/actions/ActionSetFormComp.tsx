import React, {Component} from 'react';
import {Action, ActionSet, StaticAction} from "./objects";
import StaticActionListComp from "./StaticActionListComp";
import ActionItemComp from "./ActionItemComp";
import FormComponentComp from "../forms/FormComponentComp";
import {FormComponent} from "../forms/objects";
import ActionFormComp from "./ActionFormComp";
import {api} from "../../api";
import {addNotification, Notification} from "../notification/notifications";

interface ComponentProps {
    actionSet: ActionSet | null,
    onSuccess: (actionSet: ActionSet) => void,
    onCancel: () => void,
}

interface ComponentState {
    selectedActions: Array<Action>,
    newAction: StaticAction | null,
}

export default class ActionSetFormComp extends Component<ComponentProps, ComponentState> {
    public static defaultProps = {
        onSuccess: (actionSet: ActionSet) => null,
        onCancel: () => null,
    };

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
        this.onSave = this.onSave.bind(this);
    }

    render() {
        return <div>
            <div>{this.actionSet == null ? "New action set" : `Edit '${this.actionSet.name}'`}</div>

            <FormComponentComp
                inputRef={this.nameInputRef}
                component={
                    new FormComponent(
                        "name",
                        "Name",
                        FormComponent.Type.Text,
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

            <button type={'submit'} onClick={this.onSave}>Save</button>
            <button onClick={this.props.onCancel}>Cancel</button>
        </div>;
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

    private onSave() {
        let inputElement = this.nameInputRef.current;
        if (inputElement == null) {
            console.error("No name input field (ref) found in ActionSet form");
            return;
        }

        const name = inputElement.value.trim();
        const data = new ActionSet(name, this.state.selectedActions)

        api.actionSets.save(data)
            .then(response => response.json())
            .then(data => {
                const response = data.data;
                console.log("Save action set response: ", response);

                if (response instanceof Array) {
                    response.forEach(it =>
                        addNotification(new Notification(`Error saving action set`, it, Notification.ERROR))
                    );
                    return
                }

                if (response ! instanceof ActionSet) {
                    addNotification(new Notification(`Error saving action set`, "Unexpected response", Notification.ERROR));
                    return
                }

                addNotification(new Notification(`Saved action set`, response.name, Notification.SUCCESS));
                this.props.onSuccess(response);
            })
            .catch(error => {
                console.error('Error saving action set', error);
                addNotification(new Notification(`Error saving action set`, error.message, Notification.ERROR));
            });
    }
}