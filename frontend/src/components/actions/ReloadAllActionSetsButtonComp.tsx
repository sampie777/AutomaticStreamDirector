import React, {Component} from 'react';
import {Button} from "semantic-ui-react";
import {api} from "../../api";
import {addNotification, Notification} from "../notification/notifications";

interface ComponentProps {
}

interface ComponentState {
    isLoading: boolean,
}

export default class ReloadAllActionSetsButtonComp extends Component<ComponentProps, ComponentState> {

    constructor(props: ComponentProps) {
        super(props);

        this.state = {
            isLoading: false,
        }

        this.onClick = this.onClick.bind(this);
        this.callReloadAll = this.callReloadAll.bind(this);
    }

    render() {
        return <Button size='small'
                       basic={!this.state.isLoading}
                       color='pink'
                       content={"Reload backend Action Sets"}
                       className={"ReloadAllActionSetsButtonComp"}
                       onClick={this.onClick} />;
    }

    private onClick() {
        this.setState({
            isLoading: true,
        }, this.callReloadAll);
    }

    private callReloadAll() {
        api.actionSets.reloadAll()
            .then(response => response.json())
            .then(data => {
                const result = data.data;

                this.setState({
                    isLoading: false,
                });

                if (result !== "ok") {
                    console.warn("Reloading all action sets returned an unexpected response");
                    return addNotification(new Notification("Failed to reload all ActionSets in backend", "", Notification.ERROR));
                }

                console.log("Backend ActionSets reloaded");
            })
            .catch(error => {
                console.error('Error updating ActionSet list', error);
                addNotification(new Notification("Failed to reload all ActionSets in backend", error.message, Notification.ERROR));
            });
    }
}