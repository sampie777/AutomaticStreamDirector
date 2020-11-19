import React, {Component} from 'react';
import {api} from "../../api";
import {addNotification, Notification} from "../notification/notifications";
import {Action, StaticAction} from "./objects";
import StaticActionItemComp from "./StaticActionItemComp";

interface ComponentProps {
    onItemClick: (action: StaticAction) => void,
    showFormForStaticAction: StaticAction | null,
    onActionSaved: (action: Action) => void,
    onActionSaveCancelled: () => void,
}

interface ComponentState {
    staticActions: Array<StaticAction>,
}

export default class StaticActionListComp extends Component<ComponentProps, ComponentState> {
    public static defaultProps = {
        onItemClick: (action: StaticAction) => null,
        showFormForStaticAction: null,
        onActionSaved: () => null,
        onActionSaveCancelled: () => null,
    };

    constructor(props: ComponentProps) {
        super(props);

        this.state = {
            staticActions: [],
        };

        this.loadList = this.loadList.bind(this);
    }

    componentDidMount() {
        this.loadList()
    }

    loadList() {
        api.actions.list()
            .then(response => response.json())
            .then(data => {
                const staticActions = data.data;

                console.log("Loaded static actions list:", staticActions);

                this.setState({
                    staticActions: staticActions
                });
            })
            .catch(error => {
                console.error('Error updating StaticAction list', error);
                addNotification(new Notification("Error retrieving available actions", error.message, Notification.ERROR));
            });
    }

    render() {
        return <div className={"component-list"}>
            <h3>Available actions</h3>
            {this.state.staticActions.length > 0 ?
                this.state.staticActions
                    .map(staticActions => <StaticActionItemComp staticAction={staticActions}
                                                                showForm={staticActions == this.props.showFormForStaticAction}
                                                                onClick={this.props.onItemClick}
                                                                onActionSaved={this.props.onActionSaved}
                                                                onActionSaveCancelled={this.props.onActionSaveCancelled}
                                                                key={staticActions.className}/>)
                : <i>Much empty</i>}
        </div>;
    }
}