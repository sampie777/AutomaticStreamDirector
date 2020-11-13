import React, {Component} from 'react';
import {api} from "../../api";
import {addNotification, Notification} from "../notification/notifications";
import {StaticCondition} from "./objects";
import StaticConditionItemComp from "./StaticConditionItemComp";

interface ComponentProps {
}

interface ComponentState {
    conditions: Array<StaticCondition>,
}

export default class StaticConditionListComp extends Component<ComponentProps, ComponentState> {
    constructor(props: ComponentProps) {
        super(props);

        this.state = {
            conditions: [],
        };

        this.loadList = this.loadList.bind(this);
    }

    componentDidMount() {
        this.loadList()
    }

    loadList() {
        api.modules.conditions()
            .then(response => response.json())
            .then(data => {
                const conditions = data.data;

                console.log("Loaded conditions list:", conditions);

                this.setState({
                    conditions: conditions
                });
            })
            .catch(error => {
                console.error('Error updating StaticCondition list', error);
                addNotification(new Notification("Error retrieving available conditions", error.message, Notification.ERROR));
            });
    }

    render() {
        return <div className={"component-list"}>
            <h3>Conditions</h3>
            {this.state.conditions.length > 0 ?
                this.state.conditions
                    .map(condition => <StaticConditionItemComp condition={condition} key={condition.className}/>)
                : <i>Much empty</i>}
        </div>;
    }
}