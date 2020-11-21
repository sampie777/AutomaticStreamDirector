import React, {Component} from 'react';
import {Condition, StaticCondition} from "./objects";
import './trigger.sass';
import FormComponentComp from "../../common/forms/FormComponentComp";
import {Button, Form} from "semantic-ui-react";
import {FormProps} from "semantic-ui-react/dist/commonjs/collections/Form/Form";
import {api} from "../../api";
import {addNotification, Notification} from "../notification/notifications";

interface ComponentProps {
    staticCondition: StaticCondition,
    onSuccess: (condition: Condition) => void,
    onCancel: () => void,
}

interface ComponentState {
}

export default class ConditionFormComp extends Component<ComponentProps, ComponentState> {
    public static defaultProps = {
        onSuccess: (condition: Condition) => null,
        onCancel: () => null,
    };

    private readonly staticCondition: StaticCondition;
    private readonly nameInputRef: React.RefObject<HTMLInputElement>;

    constructor(props: ComponentProps) {
        super(props);
        this.staticCondition = props.staticCondition;

        this.nameInputRef = React.createRef();

        this.onSubmit = this.onSubmit.bind(this);
    }

    render() {
        return <Form onSubmit={this.onSubmit}>
            <input type={"hidden"} name={"className"} value={this.staticCondition.className}/>

            {this.staticCondition.formComponents.map((it, i) =>
                <FormComponentComp component={it} key={i + it.name}/>)}

            <Button.Group attached='bottom'>
                <Button positive type={'submit'}>Save</Button>
                <Button onClick={this.props.onCancel}>Cancel</Button>
            </Button.Group>
        </Form>;
    }

    private onSubmit(e: React.FormEvent<HTMLFormElement>, formProps: FormProps) {
        e.preventDefault();

        const htmlFormData = new FormData(e.target as HTMLFormElement);
        const data = Object.fromEntries(htmlFormData.entries());

        api.conditions.save(data)
            .then(response => response.json())
            .then(data => {
                const response = data.data;
                console.log("Save condition response: ", response);

                if (response instanceof Array) {
                    response.forEach(it =>
                        addNotification(new Notification(`Error saving condition '${this.staticCondition.name}'`, it, Notification.ERROR))
                    );
                    return
                }

                if (response ! instanceof Condition) {
                    addNotification(new Notification(`Error saving condition '${this.staticCondition.name}'`, "Unexpected response", Notification.ERROR));
                    return
                }

                addNotification(new Notification(`Saved new condition`, response.name, Notification.SUCCESS));
                this.props.onSuccess(response);
            })
            .catch(error => {
                console.error('Error saving Condition', error);
                addNotification(new Notification(`Error saving condition '${this.staticCondition.name}'`, error.message, Notification.ERROR));
            });
    }
}