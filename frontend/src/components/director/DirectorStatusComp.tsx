import React, {Component} from 'react';
import {api} from "../../api";
import {addNotification, Notification} from "../notification/notifications";

interface ComponentProps {
}

interface ComponentState {
    isRunning: boolean
}

export default class DirectorStatusComp extends Component<ComponentProps, ComponentState> {
    private keepPolling: boolean = true;

    constructor(props: ComponentProps) {
        super(props);

        this.state = {
            isRunning: false,
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
                if (this.keepPolling) {
                    window.setTimeout(this.update, 2000, this.keepPolling);
                }
            });
    }

    render() {
        return <div>State: {this.state.isRunning ? "Running" : "Not running"}</div>;
    }
}