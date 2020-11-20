import React, {Component} from 'react';
import {api} from "../../api";
import {addNotification, Notification} from "../notification/notifications";
import {Config} from "../config/objects";

interface ComponentProps {
}

interface ComponentState {
    isRunning: boolean,
}

export default class DirectorControlButtonComp extends Component<ComponentProps, ComponentState> {

    constructor(props: ComponentProps) {
        super(props);

        this.state = {
            isRunning: false,
        };

        this.update = this.update.bind(this);
        this.onClick = this.onClick.bind(this);
        this.toggle = this.toggle.bind(this);
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
                window.setTimeout(this.update, Config.directorStatusUpdateInterval * 1000);
            });
    }

    render() {
        return <button onClick={this.onClick}
                       className={"DirectorControlButtonComp"}>
            {this.state.isRunning ? "Stop" : "Start"}
        </button>;
    }

    private onClick() {
        this.toggle()
            .then(response => response.json())
            .then(data => {
                this.setState({
                    isRunning: !this.state.isRunning,
                })
            })
            .catch(error => {
                console.error('Error changing Director status', error);
                addNotification(new Notification("Error changing Director status", error.message, Notification.ERROR));
            });
    }

    private toggle() {
        return this.state.isRunning ? api.director.stop() : api.director.start()
    }
}