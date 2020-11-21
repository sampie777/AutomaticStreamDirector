import React, {Component} from 'react';
import {Condition, StaticCondition, Trigger} from "./objects";
import StaticConditionListComp from "./StaticConditionListComp";
import ConditionItemComp from "./ConditionItemComp";
import FormComponentComp from "../../common/forms/FormComponentComp";
import {FormComponent} from "../../common/forms/objects";
import {api} from "../../api";
import {addNotification, Notification} from "../notification/notifications";
import {Button, Menu, MenuItemProps, Modal, Segment} from "semantic-ui-react";
import './trigger.sass'
import {ActionSet} from "../actions/objects";
import ActionSetComp from "../actions/ActionSetComp";
import ActionSetListComp from "../actions/ActionSetListComp";

interface ComponentProps {
    isOpen: boolean,
    trigger: Trigger | null,
    onSuccess: (trigger: Trigger) => void,
    onCancel: () => void,
}

interface ComponentState {
    selectedConditions: Array<Condition>,
    selectedActionSets: Array<ActionSet>,
    newCondition: StaticCondition | null,
    trigger: Trigger,
    activeMenuItem: string | undefined,
}

export default class TriggerFormComp extends Component<ComponentProps, ComponentState> {
    public static defaultProps = {
        onSuccess: (trigger: Trigger) => null,
        onCancel: () => null,
    };

    private readonly nameInputRef: React.RefObject<HTMLInputElement>;
    private readonly importanceInputRef: React.RefObject<HTMLInputElement>;

    private readonly MenuItem = {
        Conditions: "conditions",
        ActionSets: "actionsets",
    }

    constructor(props: ComponentProps) {
        super(props);

        this.state = {
            selectedConditions: [],
            selectedActionSets: [],
            trigger: new Trigger(null, "", 0, [], []),
            newCondition: null,
            activeMenuItem: this.MenuItem.Conditions,
        }
        this.updateStateWithTrigger(this.props.trigger);

        this.nameInputRef = React.createRef();
        this.importanceInputRef = React.createRef();

        this.updateStateWithTrigger = this.updateStateWithTrigger.bind(this);
        this.onConditionItemClickAdd = this.onConditionItemClickAdd.bind(this);
        this.onConditionItemClickRemove = this.onConditionItemClickRemove.bind(this);
        this.onConditionSaved = this.onConditionSaved.bind(this);
        this.onConditionSaveCancelled = this.onConditionSaveCancelled.bind(this);
        this.onActionSetClickRemove = this.onActionSetClickRemove.bind(this);
        this.onActionSetClickAdd = this.onActionSetClickAdd.bind(this);
        this.onSave = this.onSave.bind(this);
        this.onMenuItemClick = this.onMenuItemClick.bind(this);
    }

    componentDidUpdate(prevProps: ComponentProps) {
        if (Trigger.equals(prevProps.trigger, this.props.trigger))
            return;

        this.updateStateWithTrigger(this.props.trigger);
    }

    private updateStateWithTrigger(trigger: Trigger | null) {
        let newTrigger = trigger || new Trigger(null, "", 0, [], []);
        this.setState({
            selectedConditions: newTrigger.conditions,
            selectedActionSets: newTrigger.actionSets,
            trigger: newTrigger,
            newCondition: null,
            activeMenuItem: this.MenuItem.Conditions,
        });
    }

    render() {
        return <Modal centered={false}
                      open={this.props.isOpen}
                      onClose={this.props.onCancel}
                      size={"large"}
                      className={"TriggerFormComp"}>
            <Modal.Header>
                {this.state.trigger.id == null ? "New trigger" : `Edit '${this.state.trigger.name}'`}
            </Modal.Header>
            <Modal.Content scrolling>
                <Modal.Description>
                    <FormComponentComp
                        className={"name-input-form-group"}
                        inputRef={this.nameInputRef}
                        component={new FormComponent(
                            "name",
                            "Name",
                            FormComponent.Type.Text,
                            true,
                            this.state.trigger.name)}/>

                    <FormComponentComp
                        inputRef={this.importanceInputRef}
                        component={new FormComponent(
                            "importance",
                            "Importance",
                            FormComponent.Type.Number,
                            true,
                            this.state.trigger.importance)}/>


                    <Menu tabular
                          attached={'top'}>
                        <Menu.Item
                            name={this.MenuItem.Conditions}
                            active={this.state.activeMenuItem === this.MenuItem.Conditions}
                            onClick={this.onMenuItemClick}
                        />
                        <Menu.Item
                            name={this.MenuItem.ActionSets}
                            active={this.state.activeMenuItem === this.MenuItem.ActionSets}
                            onClick={this.onMenuItemClick}
                        />
                    </Menu>

                    <Segment attached='bottom'>
                        <div className={this.state.activeMenuItem === this.MenuItem.Conditions ? "" : "hidden"}>
                            <div className={"conditions-lists"}>
                                <div className={"component-list"}>
                                    <h3>Selected conditions</h3>
                                    {this.state.selectedConditions
                                        .map((condition, i) => <ConditionItemComp condition={condition}
                                                                                  onDeleteClick={this.onConditionItemClickRemove}
                                                                                  key={i + condition.name}/>)
                                    }
                                </div>

                                <StaticConditionListComp onItemClick={this.onConditionItemClickAdd}
                                                         showFormForStaticCondition={this.state.newCondition}
                                                         onConditionSaved={this.onConditionSaved}
                                                         onConditionSaveCancelled={this.onConditionSaveCancelled}/>
                            </div>
                        </div>

                        <div className={this.state.activeMenuItem === this.MenuItem.ActionSets ? "" : "hidden"}>
                            <div className={"actionsets-lists"}>
                                <div className={"component-list"}>
                                    <h3>Selected action sets</h3>
                                    {this.state.selectedActionSets
                                        .map((actionSet, i) => <ActionSetComp actionSet={actionSet}
                                                                              onDeleteClick={() => this.onActionSetClickRemove(actionSet)}
                                                                              key={i + actionSet.name}/>)
                                    }
                                </div>

                                <ActionSetListComp title={"Available action sets"}
                                                   onItemClick={this.onActionSetClickAdd}/>
                            </div>
                        </div>
                    </Segment>
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

    private onConditionItemClickAdd(condition: StaticCondition) {
        if (condition == this.state.newCondition) {
            return;
        }

        this.setState({
                newCondition: null,
            },
            () => {
                this.setState({
                    newCondition: condition
                })
            })
    }

    private onConditionItemClickRemove(condition: Condition) {
        this.setState({
            selectedConditions: this.state.selectedConditions.filter(it => it !== condition)
        })
    }

    private onConditionSaved(condition: Condition) {
        this.setState({
            newCondition: null,
            selectedConditions: this.state.selectedConditions.concat([condition])
        })
    }

    private onConditionSaveCancelled() {
        this.setState({
            newCondition: null,
        })
    }

    private onSave() {
        let inputElement = this.nameInputRef.current;
        if (inputElement == null) {
            console.error("No name input field (ref) found in Trigger form");
            return;
        }

        const name = inputElement.value.trim();
        const id = this.state.trigger.id;
        const importance = +this.importanceInputRef.current!.value.trim();
        const data = new Trigger(id, name, importance, this.state.selectedConditions, this.state.selectedActionSets);

        api.triggers.save(data)
            .then(response => response.json())
            .then(data => {
                const response = data.data;
                console.log("Save trigger response: ", response);

                if (response instanceof Array) {
                    return response.forEach(it =>
                        addNotification(new Notification(`Could not save trigger`, it, Notification.ERROR))
                    );
                }

                if (response instanceof String) {
                    return addNotification(new Notification(`Error saving trigger`,
                        response, Notification.ERROR))
                }

                if (response ! instanceof Trigger) {
                    return addNotification(new Notification(`Error saving trigger`,
                        "Unexpected response", Notification.ERROR));
                }

                addNotification(new Notification(`Saved trigger`, response.name, Notification.SUCCESS));
                this.setState({
                    trigger: response,
                });
                this.props.onSuccess(response);
            })
            .catch(error => {
                console.error('Error saving trigger', error);
                addNotification(new Notification(`Error saving trigger`, error.message, Notification.ERROR));
            });
    }

    private onActionSetClickRemove(actionSet: ActionSet) {
        this.setState({
            selectedActionSets: this.state.selectedActionSets.filter(it => it !== actionSet)
        })
    }

    private onActionSetClickAdd(actionSet: ActionSet) {
        this.setState({
            selectedActionSets: this.state.selectedActionSets.concat([actionSet])
        })
    }

    private onMenuItemClick(event: React.MouseEvent<HTMLAnchorElement, MouseEvent>, data: MenuItemProps) {
        this.setState({
            activeMenuItem: data.name,
        })
    }
}