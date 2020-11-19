import React, {Component} from 'react';
import {Condition, StaticCondition, Trigger} from "./objects";
import StaticConditionListComp from "./StaticConditionListComp";
import ConditionItemComp from "./ConditionItemComp";
import FormComponentComp from "../../common/forms/FormComponentComp";
import {FormComponent} from "../../common/forms/objects";
import {api} from "../../api";
import {addNotification, Notification} from "../notification/notifications";
import {Modal} from "semantic-ui-react";
import './trigger.sass'

interface ComponentProps {
    trigger: Trigger | null,
    onSuccess: (trigger: Trigger) => void,
    onCancel: () => void,
}

interface ComponentState {
    selectedConditions: Array<Condition>,
    newCondition: StaticCondition | null,
    trigger: Trigger,
}

export default class TriggerFormComp extends Component<ComponentProps, ComponentState> {
    public static defaultProps = {
        onSuccess: (trigger: Trigger) => null,
        onCancel: () => null,
    };

    private readonly nameInputRef: React.RefObject<HTMLInputElement>;
    private readonly importanceInputRef: React.RefObject<HTMLInputElement>;

    constructor(props: ComponentProps) {
        super(props);

        this.state = {
            selectedConditions: props.trigger?.conditions || [],
            newCondition: null,
            trigger: props.trigger != null ? props.trigger : new Trigger(null, "", 0, [], []),
        }

        this.nameInputRef = React.createRef();
        this.importanceInputRef = React.createRef();

        this.onConditionItemClickAdd = this.onConditionItemClickAdd.bind(this);
        this.onConditionItemClickRemove = this.onConditionItemClickRemove.bind(this);
        this.onConditionSaved = this.onConditionSaved.bind(this);
        this.onConditionSaveCancelled = this.onConditionSaveCancelled.bind(this);
        this.onSave = this.onSave.bind(this);
    }

    render() {
        return <Modal centered={false}
                      open={true}
                      onClose={this.props.onCancel}
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


                    <div className={"conditions-lists"}>
                        <div className={"component-list"}>
                            <h3>Selected conditions</h3>
                            {this.state.selectedConditions
                                .map((condition, i) => <ConditionItemComp condition={condition}
                                                                          onClick={this.onConditionItemClickRemove}
                                                                          key={i}/>)
                            }
                        </div>

                        <StaticConditionListComp onItemClick={this.onConditionItemClickAdd}
                                                 showFormForStaticCondition={this.state.newCondition}
                                                 onConditionSaved={this.onConditionSaved}
                                                 onConditionSaveCancelled={this.onConditionSaveCancelled}/>
                    </div>
                </Modal.Description>
            </Modal.Content>
            <Modal.Actions>
                <button type={'submit'} onClick={this.onSave}>Save</button>
                <button onClick={this.props.onCancel}>Cancel</button>
            </Modal.Actions>
        </Modal>;
    }

    private onConditionItemClickAdd(condition: StaticCondition) {
        if (condition == this.state.newCondition) {
            return;
        }
        console.log("Opening form for condition: " + condition.name)

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
        const data = new Trigger(id, name, importance, this.state.selectedConditions, [])

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
}