import React, {Component} from 'react';
import {api} from "../../api";
import {addNotification, Notification} from "../notification/notifications";
import {ActionSet} from "./objects";
import ActionSetComp from "./ActionSetComp";
import NewActionSetButtonComp from "./NewActionSetButtonComp";

interface ComponentProps {
    onItemClick: (actionSet: ActionSet) => void,
}

interface ComponentState {
    actionSets: Array<ActionSet>,
}

export default class ActionSetListComp extends Component<ComponentProps, ComponentState> {
    public static defaultProps = {
        onItemClick: () => null,
    }

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
        console.log("(Re)loading action sets list");

        api.actionSets.list()
            .then(response => response.json())
            .then(data => {
                const actionSets = data.data;

                console.log("Loaded action sets list:", actionSets);

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
        return <div className={"component-list"}>
            <h3>ActionSets</h3>

            <NewActionSetButtonComp />

            {this.state.actionSets.length > 0 ?
                this.state.actionSets
                    .map((actionSet, i) => <ActionSetComp actionSet={actionSet}
                                                          onClick={() => this.props.onItemClick(actionSet)}
                                                          onDelete={this.loadList}
                                                          key={i + actionSet.name}/>)
                : <i>Much empty</i>}
        </div>;
    }
}