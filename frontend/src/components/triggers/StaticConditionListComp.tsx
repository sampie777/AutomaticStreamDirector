import React, {Component} from 'react';
import {api} from "../../api";
import {addNotification, Notification} from "../notification/notifications";
import {Condition, StaticCondition} from "./objects";
import StaticConditionItemComp from "./StaticConditionItemComp";
import {Input, InputOnChangeData} from "semantic-ui-react";

interface ComponentProps {
    onItemClick: (condition: StaticCondition) => void,
    showFormForStaticCondition: StaticCondition | null,
    onConditionSaved: (condition: Condition) => void,
    onConditionSaveCancelled: () => void,
}

interface ComponentState {
    staticConditions: Array<StaticCondition>,
    filteredConditions: Array<StaticCondition>,
}

export default class StaticConditionListComp extends Component<ComponentProps, ComponentState> {
    public static defaultProps = {
        onItemClick: (condition: StaticCondition) => null,
        showFormForStaticCondition: null,
        onConditionSaved: () => null,
        onConditionSaveCancelled: () => null,
    };
    
    constructor(props: ComponentProps) {
        super(props);

        this.state = {
            staticConditions: [],
            filteredConditions: [],
        };

        this.loadList = this.loadList.bind(this);
        this.onSearchChange = this.onSearchChange.bind(this);
    }

    componentDidMount() {
        this.loadList()
    }

    loadList() {
        api.conditions.list()
            .then(response => response.json())
            .then(data => {
                const staticConditions = data.data;

                console.log("Loaded static conditions list:", staticConditions);

                this.setState({
                    staticConditions: staticConditions,
                    filteredConditions: staticConditions,
                });
            })
            .catch(error => {
                console.error('Error updating StaticCondition list', error);
                addNotification(new Notification("Error retrieving available conditions", error.message, Notification.ERROR));
            });
    }

    render() {
        return <div className={"component-list"}>
            <h3>Available conditions</h3>

            <Input icon='search'
                   fluid
                   placeholder='Search...'
                   className={"search"}
                   onChange={this.onSearchChange}/>

            {this.state.filteredConditions.length > 0 ?
                this.state.filteredConditions
                    .map(staticConditions => <StaticConditionItemComp staticCondition={staticConditions}
                                                                showForm={staticConditions == this.props.showFormForStaticCondition}
                                                                onClick={this.props.onItemClick}
                                                                onConditionSaved={this.props.onConditionSaved}
                                                                onConditionSaveCancelled={this.props.onConditionSaveCancelled}
                                                                key={staticConditions.className}/>)
                : <i>Much empty</i>}
        </div>;
    }

    private onSearchChange(event: React.ChangeEvent<HTMLInputElement>, data: InputOnChangeData) {
        if (data.value.length == 0) {
            return this.setState({
                filteredConditions: this.state.staticConditions
            });
        }

        const matcher = new RegExp(data.value, 'gi');
        this.setState({
            filteredConditions: this.state.staticConditions.filter(it => it.name.match(matcher))
        });
    }
}