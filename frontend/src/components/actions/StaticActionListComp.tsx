import React, {Component} from 'react';
import {api} from "../../api";
import {addNotification, Notification} from "../notification/notifications";
import {StaticAction} from "./objects";
import StaticActionItemComp from "./StaticActionItemComp";

interface ComponentProps {
}

interface ComponentState {
    actions: Array<StaticAction>,
}

export default class StaticActionListComp extends Component<ComponentProps, ComponentState> {
    constructor(props: ComponentProps) {
        super(props);

        this.state = {
            actions: [],
        };

        this.loadList = this.loadList.bind(this);
    }

    componentDidMount() {
        this.loadList()
    }

    loadList() {
        api.modules.actions()
            .then(response => response.json())
            .then(data => {
                const actions = data.data;

                console.log(actions);

                this.setState({
                    actions: actions
                });
            })
            .catch(error => {
                console.error('Error updating StaticAction list', error);
                addNotification(new Notification("Error retrieving available actions", error.message, Notification.ERROR));
            });
    }

    render() {
        return <div className={"component-list"}>
            <h3>Actions</h3>
            {this.state.actions.length > 0 ?
                this.state.actions
                    .map(action => <StaticActionItemComp action={action} key={action.className}/>)
                : <i>Much empty</i>}
        </div>;
    }
}