import React, {Component} from 'react';
import TriggerComp from "./TriggerComp";
import {api} from "../../api";
import {addNotification, Notification} from "../notification/notifications";
import {Trigger} from "./objects";
import './trigger.sass';
import NewTriggerButtonComp from "./NewTriggerButtonComp";
import {Input, InputOnChangeData} from "semantic-ui-react";

interface ComponentProps {
}

interface ComponentState {
    triggers: Array<Trigger>,
    filteredTriggers: Array<Trigger>,
}

export default class TriggerListComp extends Component<ComponentProps, ComponentState> {
    constructor(props: ComponentProps) {
        super(props);

        this.state = {
            triggers: [],
            filteredTriggers: [],
        };

        this.loadList = this.loadList.bind(this);
        this.onSearchChange = this.onSearchChange.bind(this);
    }

    componentDidMount() {
        this.loadList()
    }

    loadList() {
        console.log("(Re)loading triggers list");

        api.triggers.list()
            .then(response => response.json())
            .then(data => {
                const triggers = data.data;

                console.log("Loaded triggers list:", triggers);

                this.setState({
                    triggers: triggers,
                    filteredTriggers: triggers,
                });
            })
            .catch(error => {
                console.error('Error updating Trigger list', error);
                addNotification(new Notification("Error updating Trigger list", error.message, Notification.ERROR));
            });
    }

    render() {
        return <div className={"component-list"}>
            <h3>Triggers</h3>

            <Input icon='search'
                   fluid
                   placeholder='Search...'
                   className={"search"}
                   onChange={this.onSearchChange}/>

            <NewTriggerButtonComp />

            {this.state.filteredTriggers.length > 0 ?
                this.state.filteredTriggers
                    .sort(((a, b) => b.importance - a.importance))
                    .map((trigger, i) => <TriggerComp trigger={trigger}
                                                 onDelete={this.loadList}
                                                 key={i + trigger.name}/>)
                : <i>Much empty</i>}
        </div>;
    }

    private onSearchChange(event: React.ChangeEvent<HTMLInputElement>, data: InputOnChangeData) {
        if (data.value.length == 0) {
            return this.setState({
                filteredTriggers: this.state.triggers
            });
        }

        const matcher = new RegExp(data.value, 'gi');
        this.setState({
            filteredTriggers: this.state.triggers.filter(it => it.name.match(matcher))
        });
    }
}