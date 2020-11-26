import React, {Component} from 'react';
import {Action, StaticAction} from "./objects";
import './actions.sass';
import FormComponentComp from "../../common/forms/FormComponentComp";
import {Button, Form} from "semantic-ui-react";
import {FormProps} from "semantic-ui-react/dist/commonjs/collections/Form/Form";
import {api} from "../../api";
import {addNotification, Notification} from "../notification/notifications";

interface ComponentProps {
    staticAction: StaticAction,
    action: Action | null,
    onSuccess: (action: Action) => void,
    onCancel: () => void,
}

interface ComponentState {
}

export default class ActionFormComp extends Component<ComponentProps, ComponentState> {
    public static defaultProps = {
        action: null,
        onSuccess: (action: Action) => null,
        onCancel: () => null,
    };

    private readonly staticAction: StaticAction;
    private readonly nameInputRef: React.RefObject<HTMLInputElement>;

    constructor(props: ComponentProps) {
        super(props);
        this.staticAction = props.staticAction;

        this.nameInputRef = React.createRef();

        this.onSubmit = this.onSubmit.bind(this);
    }

    render() {
        return <Form onSubmit={this.onSubmit}>
            <input type={"hidden"} name={"className"} value={this.staticAction.className}/>
            {this.props.action === null || this.props.action.id === null ? "" : <input type={'hidden'} name={'id'} value={this.props.action!.id!}/>}

            {this.staticAction.formComponents.map((it, i) =>
                <FormComponentComp component={it}
                                   defaultValue={this.props.action?.data == null ? undefined : (this.props.action?.data as any)[it.name]}
                                   key={i + it.name}/>)}

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

        api.actions.save(data)
            .then(response => response.json())
            .then(data => {
                const response = data.data;
                console.log("Save action response: ", response);

                if (response instanceof Array) {
                    response.forEach(it =>
                        addNotification(new Notification(`Error saving action '${this.staticAction.name}'`, it, Notification.ERROR))
                    );
                    return
                }

                if (response ! instanceof Action) {
                    addNotification(new Notification(`Error saving action '${this.staticAction.name}'`, "Unexpected response", Notification.ERROR));
                    return
                }

                addNotification(new Notification(`Saved new action`, response.name, Notification.SUCCESS));
                this.props.onSuccess(response);
            })
            .catch(error => {
                console.error('Error saving Action', error);
                addNotification(new Notification(`Error saving action '${this.staticAction.name}'`, error.message, Notification.ERROR));
            });
    }
}