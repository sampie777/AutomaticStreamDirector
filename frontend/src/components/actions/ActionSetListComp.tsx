import React, {Component} from 'react';
import {api} from "../../api";
import {addNotification, Notification} from "../notification/notifications";
import {ActionSet} from "./objects";
import ActionSetComp from "./ActionSetComp";
import NewActionSetButtonComp from "./NewActionSetButtonComp";
import {Input, InputOnChangeData} from "semantic-ui-react";

interface ComponentProps {
    onItemClick: (actionSet: ActionSet) => void,
    title: string,
}

interface ComponentState {
    actionSets: Array<ActionSet>,
    filteredActionSets: Array<ActionSet>,
}

export default class ActionSetListComp extends Component<ComponentProps, ComponentState> {
    public static defaultProps = {
        onItemClick: () => null,
        title: "ActionSets",
    }

    constructor(props: ComponentProps) {
        super(props);

        this.state = {
            actionSets: [],
            filteredActionSets: [],
        };

        this.loadList = this.loadList.bind(this);
        this.onSearchChange = this.onSearchChange.bind(this);
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
                    actionSets: actionSets,
                    filteredActionSets: actionSets,
                });
            })
            .catch(error => {
                console.error('Error updating ActionSet list', error);
                addNotification(new Notification("Error updating ActionSet list", error.message, Notification.ERROR));
            });
    }

    render() {
        return <div className={"component-list"}>
            <h3>{this.props.title}</h3>

            <Input icon='search'
                   fluid
                   placeholder='Search...'
                   className={"search"}
                   onChange={this.onSearchChange}/>

            <NewActionSetButtonComp />

            {this.state.filteredActionSets.length > 0 ?
                this.state.filteredActionSets
                    .map((actionSet, i) => <ActionSetComp actionSet={actionSet}
                                                          onClick={() => this.props.onItemClick(actionSet)}
                                                          onDelete={this.loadList}
                                                          key={i + actionSet.name}/>)
                : <i>Much empty</i>}
        </div>;
    }

    private onSearchChange(event: React.ChangeEvent<HTMLInputElement>, data: InputOnChangeData) {
        if (data.value.length == 0) {
            return this.setState({
                filteredActionSets: this.state.actionSets
            });
        }

        const matcher = new RegExp(data.value, 'gi');
        this.setState({
            filteredActionSets: this.state.actionSets.filter(it => it.name.match(matcher))
        });
    }
}