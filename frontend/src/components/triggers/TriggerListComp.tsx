import React, {Component} from 'react';
import TriggerComp from "./TriggerComp";
import {api} from "../../api";
import {addNotification, Notification} from "../notification/notifications";
import {Trigger} from "./objects";

interface ComponentProps {
}

interface ComponentState {
    triggers: Array<Trigger>,
}

export default class TriggerListComp extends Component<ComponentProps, ComponentState> {
    constructor(props: ComponentProps) {
        super(props);

        this.state = {
            triggers: [],
        };

        this.loadList = this.loadList.bind(this);
    }

    componentDidMount() {
        this.loadList()
    }

    loadList() {
        api.triggers.list()
            .then(response => response.json())
            .then(data => {
                const triggers = data.data;

                console.log(triggers);

                this.setState({
                    triggers: triggers
                });
            })
            .catch(error => {
                console.error('Error updating Trigger list', error);
                addNotification(new Notification("Error updating Trigger list", error.message, Notification.ERROR));
            });
    }

    render() {
        return <div>
            <h3>Triggers</h3>
            {this.state.triggers.length > 0 ?
                this.state.triggers
                    .sort(((a, b) => b.importance - a.importance))
                    .map(trigger => <TriggerComp trigger={trigger} key={trigger.name}/>)
                : <i>Much empty</i>}
        </div>;
    }
}