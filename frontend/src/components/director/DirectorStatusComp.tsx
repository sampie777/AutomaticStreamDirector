import React, {Component} from 'react';
import {api} from "../../api";
import {addNotification, Notification} from "../notification/notifications";
import {Config} from "../config/objects";

interface ComponentProps {
}

interface ComponentState {
    isRunning: boolean,
    keepPolling: boolean,
    updateInterval: number,
}

export default class DirectorStatusComp extends Component<ComponentProps, ComponentState> {

    constructor(props: ComponentProps) {
        super(props);

        this.state = {
            isRunning: false,
            keepPolling: true,
            updateInterval: 20,
        };

        this.update = this.update.bind(this);
        this.getConfigInterval = this.getConfigInterval.bind(this);
    }

    componentDidMount() {
        this.getConfigInterval()
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
                    window.setTimeout(this.update, this.state.updateInterval);
                }
            });
    }

    render() {
        return <div>State: {this.state.isRunning ? "Running" : "Not running"}</div>;
    }

    private getConfigInterval() {
        Config.get("directorStatusUpdateInterval", (value => {
            if (value == null) {
                return console.error("Didn't get a valid value from config API");
            }

            this.setState({
                updateInterval: value * 1000
            })
        }))
    }
}