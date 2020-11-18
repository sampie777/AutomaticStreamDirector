import React, {Component} from 'react';
import {Action, StaticAction} from "./objects";
import './actions.sass';
import FormComponentComp from "../../common/forms/FormComponentComp";
import {Form} from "semantic-ui-react";
import {FormProps} from "semantic-ui-react/dist/commonjs/collections/Form/Form";
import {api} from "../../api";
import {addNotification, Notification} from "../notification/notifications";

interface ComponentProps {
    staticAction: StaticAction,
    onSuccess: (action: Action) => void,
    onCancel: () => void,
}

interface ComponentState {
}

export default class ActionFormComp extends Component<ComponentProps, ComponentState> {
    public static defaultProps = {
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

    render() {
        return <div>
            <div className={"ActionItemComp-name"}>{this.staticAction.name}</div>
            <div className={"ActionItemComp-previewText"}>{this.staticAction.previewText}</div>

            <Form onSubmit={this.onSubmit}>
                <input type={"hidden"} name={"className"} value={this.staticAction.className}/>

                {this.staticAction.formComponents.map((it, i) => <FormComponentComp component={it} key={i}/>)}

                <button type={'submit'}>Save</button>
                <button onClick={this.props.onCancel}>Cancel</button>
            </Form>
        </div>;
    }
}