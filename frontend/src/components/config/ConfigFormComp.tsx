import React, {Component} from 'react';
import {Form, Modal} from "semantic-ui-react";
import {api} from "../../api";
import {addNotification, Notification} from "../notification/notifications";
import {Config} from "./objects";
import FormComponentComp from "../../common/forms/FormComponentComp";
import {FormProps} from "semantic-ui-react/dist/commonjs/collections/Form/Form";

interface ComponentProps {
    onSuccess: () => void,
    onCancel: () => void,
}

interface ComponentState {
    config: Config
}

export default class ConfigFormComp extends Component<ComponentProps, ComponentState> {
    public static defaultProps = {
        onSuccess: () => null,
        onCancel: () => null,
    };

    private readonly formRef: React.RefObject<HTMLFormElement>

    constructor(props: ComponentProps) {
        super(props);

        this.formRef = React.createRef();

        this.state = {
            config: new Config([], [])
        }

        this.loadList = this.loadList.bind(this);
        this.onSubmit = this.onSubmit.bind(this);
    }

    componentDidMount() {
        this.loadList()
    }

    loadList() {
        console.log("(Re)loading config list");

        api.config.list()
            .then(response => response.json())
            .then(data => {
                const config = data.data;

                console.log("Loaded config list:", config);

                this.setState({
                    config: config
                });
            })
            .catch(error => {
                console.error('Error updating Config list', error);
                addNotification(new Notification("Error updating Config list", error.message, Notification.ERROR));
            });
    }

    render() {
        return <Modal centered={false}
                      open={true}
                      onClose={this.props.onCancel}
                      size={"small"}
                      className={"ConfigFormComp"}>
            <Modal.Header>
                Configuration
            </Modal.Header>
            <Modal.Content scrolling>
                <Modal.Description>
                    <Form ref={this.formRef}
                          id={"ConfigFormComp-form"}
                          onSubmit={this.onSubmit}>
                        <h3>Frontend</h3>
                        {this.state.config.frontend.map((item, i) =>
                            item.formComponent == null ? "" :
                                <FormComponentComp component={item.formComponent} key={i + item.key}/>)}

                        <h3>Backend</h3>
                        {this.state.config.backend.map((item, i) =>
                            item.formComponent == null ? "" :
                                <FormComponentComp component={item.formComponent} key={i + item.key}/>)}
                    </Form>
                </Modal.Description>
            </Modal.Content>
            <Modal.Actions>
                <button type={'submit'} form={"ConfigFormComp-form"}>Save</button>
                <button onClick={this.props.onCancel}>Cancel</button>
            </Modal.Actions>
        </Modal>;
    }

    private onSubmit(e: React.FormEvent<HTMLFormElement>, formProps: FormProps) {
        e.preventDefault();

        const htmlFormData = new FormData(e.target as HTMLFormElement);
        const data = Object.fromEntries(htmlFormData.entries());

        api.config.saveList(data)
            .then(response => response.json())
            .then(data => {
                const response = data.data;
                console.log("Save config response: ", response);

                if (response instanceof Array) {
                    response.forEach(it =>
                        addNotification(new Notification(`Error saving config`, it, Notification.ERROR))
                    );
                    return
                }

                if (response !== "ok") {
                    addNotification(new Notification(`Error saving config`, "Unexpected response: " + response, Notification.ERROR));
                    return
                }

                addNotification(new Notification(`Saved config`, "", Notification.SUCCESS));
                this.props.onSuccess();
            })
            .catch(error => {
                console.error('Error saving Config', error);
                addNotification(new Notification(`Error saving config`, error.message, Notification.ERROR));
            });
    }
}