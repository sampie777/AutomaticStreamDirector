import React, {Component} from 'react';
import {Action, ActionSet, StaticAction} from "./objects";
import StaticActionListComp from "./StaticActionListComp";
import ActionItemComp from "./ActionItemComp";
import FormComponentComp from "../../common/forms/FormComponentComp";
import {FormComponent} from "../../common/forms/objects";
import {api} from "../../api";
import {addNotification, Notification} from "../notification/notifications";
import {Button, Modal} from "semantic-ui-react";
import './actions.sass'

interface ComponentProps {
    isOpen: boolean,
    actionSet: ActionSet | null,
    onSuccess: (actionSet: ActionSet) => void,
    onCancel: () => void,
}

interface ComponentState {
    selectedActions: Array<Action>,
    newAction: StaticAction | null,
    actionSet: ActionSet,
    editAction: Action | null,
}

export default class ActionSetFormComp extends Component<ComponentProps, ComponentState> {
    public static defaultProps = {
        onSuccess: (actionSet: ActionSet) => null,
        onCancel: () => null,
    };

    private readonly nameInputRef: React.RefObject<HTMLInputElement>;

    constructor(props: ComponentProps) {
        super(props);

        this.state = {
            selectedActions: [],
            actionSet: new ActionSet(null, "", []),
            newAction: null,
            editAction: null,
        }
        this.updateStateWithActionSet(this.props.actionSet);

        this.nameInputRef = React.createRef();

        this.updateStateWithActionSet = this.updateStateWithActionSet.bind(this);
        this.onActionItemClickAdd = this.onActionItemClickAdd.bind(this);
        this.onActionItemClickRemove = this.onActionItemClickRemove.bind(this);
        this.onActionSaved = this.onActionSaved.bind(this);
        this.onActionSaveCancelled = this.onActionSaveCancelled.bind(this);
        this.onSave = this.onSave.bind(this);
        this.onActionEditClick = this.onActionEditClick.bind(this);
        this.onEditActionSaved = this.onEditActionSaved.bind(this);
        this.onEditActionSaveCancelled = this.onEditActionSaveCancelled.bind(this);
    }

    componentDidUpdate(prevProps: ComponentProps) {
        if (ActionSet.equals(prevProps.actionSet, this.props.actionSet))
            return;

        this.updateStateWithActionSet(this.props.actionSet);
    }

    private updateStateWithActionSet(actionSet: ActionSet | null) {
        let newActionSet = actionSet || new ActionSet(null, "", []);
        this.setState({
            selectedActions: newActionSet.actions,
            actionSet: newActionSet,
            newAction: null,
        });
    }

    render() {
        return <Modal centered={false}
                      open={this.props.isOpen}
                      onClose={this.props.onCancel}
                      size={"large"}
                      className={"ActionSetFormComp"}>
            <Modal.Header>
                {this.state.actionSet.id == null ? "New action set" : `Edit '${this.state.actionSet.name}'`}
            </Modal.Header>
            <Modal.Content scrolling>
                <Modal.Description>
                    <FormComponentComp
                        className={"name-input-form-group"}
                        inputRef={this.nameInputRef}
                        component={
                            new FormComponent(
                                "name",
                                "Name",
                                FormComponent.Type.Text,
                                true,
                                this.state.actionSet.name)}/>


                    <div className={"actions-lists"}>
                        <div className={"component-list"}>
                            <h3>Selected actions</h3>
                            {this.state.selectedActions
                                .map((action, i) => <ActionItemComp action={action}
                                                                    onDeleteClick={this.onActionItemClickRemove}
                                                                    onEditClick={() => this.onActionEditClick(action)}
                                                                    showForm={this.state.editAction === action}
                                                                    onSaved={this.onEditActionSaved}
                                                                    onSaveCancelled={this.onEditActionSaveCancelled}
                                                                    key={i + action.name}/>)
                            }
                        </div>

                        <StaticActionListComp onItemClick={this.onActionItemClickAdd}
                                              showFormForStaticAction={this.state.newAction}
                                              onActionSaved={this.onActionSaved}
                                              onActionSaveCancelled={this.onActionSaveCancelled}/>
                    </div>
                </Modal.Description>
            </Modal.Content>
            <Modal.Actions>
                <Button.Group attached='bottom'>
                    <Button positive type={'submit'} onClick={this.onSave}>Save</Button>
                    <Button onClick={this.props.onCancel}>Cancel</Button>
                </Button.Group>
            </Modal.Actions>
        </Modal>;
    }

    private onActionItemClickAdd(action: StaticAction) {
        if (action == this.state.newAction) {
            return;
        }

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
        const id = this.state.actionSet.id;
        const data = new ActionSet(id, name, this.state.selectedActions)

        api.actionSets.save(data)
            .then(response => response.json())
            .then(data => {
                const response = data.data;
                console.log("Save action set response: ", response);

                if (response instanceof Array) {
                    return response.forEach(it =>
                        addNotification(new Notification(`Could not save action set`, it, Notification.ERROR))
                    );
                }

                if (response instanceof String) {
                    return addNotification(new Notification(`Error saving action set`,
                        response, Notification.ERROR))
                }

                if (response ! instanceof ActionSet) {
                    return addNotification(new Notification(`Error saving action set`,
                        "Unexpected response", Notification.ERROR));
                }

                addNotification(new Notification(`Saved action set`, response.name, Notification.SUCCESS));
                this.setState({
                    actionSet: response,
                });
                this.props.onSuccess(response);
            })
            .catch(error => {
                console.error('Error saving action set', error);
                addNotification(new Notification(`Error saving action set`, error.message, Notification.ERROR));
            });
    }

    private onActionEditClick(action: Action) {
        this.setState({
            editAction: action,
        });
    }

    private onEditActionSaved(action: Action) {
        this.setState({
            editAction: null,
            selectedActions: this.state.selectedActions.map(it => it.id === action.id ? action : it),
        });
    }

    private onEditActionSaveCancelled() {
        this.setState({
            editAction: null,
        });
    }
}