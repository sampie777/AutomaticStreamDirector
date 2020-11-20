import React, {Component} from 'react';
import {api} from "../../api";
import {addNotification, Notification} from "../notification/notifications";
import {Config} from "../config/objects";

interface ComponentProps {
}

interface ComponentState {
    isRunning: boolean,
    keepPolling: boolean,
}

export default class DirectorStatusComp extends Component<ComponentProps, ComponentState> {

    constructor(props: ComponentProps) {
        super(props);

        this.state = {
            isRunning: false,
            keepPolling: true,
        };

        this.update = this.update.bind(this);
    }

    componentDidMount() {
        this.update()
    }

    update() {
        api.director.status()
            .then(response => response.json())
            .then(data => {
                this.setState({
                    isRunning: data.data,
                });
            })
            .catch(error => {
                console.error('Error updating Director status', error);
                addNotification(new Notification("Error updating Director status", error.message, Notification.ERROR));

                this.setState({
                    isRunning: false,
                });
            })
            .finally(() => {
                if (this.state.keepPolling) {
                    window.setTimeout(this.update, Config.directorStatusUpdateInterval * 1000);
                }
            });
    }

    render() {
        return <div>State: {this.state.isRunning ? "Running" : "Not running"}</div>;
    }
}