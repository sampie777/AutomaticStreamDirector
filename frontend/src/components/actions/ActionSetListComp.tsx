import React, {Component} from 'react';
import {api} from "../../api";
import {addNotification, Notification} from "../notification/notifications";
import {ActionSet} from "./objects";
import ActionSetComp from "./ActionSetComp";

interface ComponentProps {
}

interface ComponentState {
    actionSets: Array<ActionSet>,
}

export default class ActionSetListComp extends Component<ComponentProps, ComponentState> {
    constructor(props: ComponentProps) {
        super(props);

        this.state = {
            actionSets: [],
        };

        this.loadList = this.loadList.bind(this);
    }

    componentDidMount() {
        this.loadList()
    }

    loadList() {
        api.actionSets.list()
            .then(response => response.json())
            .then(data => {
                const actionSets = data.data;

                console.log(actionSets);

                this.setState({
                    actionSets: actionSets
                });
            })
            .catch(error => {
                console.error('Error updating ActionSet list', error);
                addNotification(new Notification("Error updating ActionSet list", error.message, Notification.ERROR));
            });
    }

    render() {
        return <div>
            <h3>ActionSets</h3>
            {this.state.actionSets.length > 0 ?
                this.state.actionSets
                    .map(actionSet => <ActionSetComp actionSet={actionSet} key={actionSet.name}/>)
                : <i>Much empty</i>}
        </div>;
    }
}