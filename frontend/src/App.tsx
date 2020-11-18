import React, {Component} from 'react';
import './App.css';
import {NotificationComponent} from "./components/notification/notifications";
import TriggerListComp from "./components/triggers/TriggerListComp";
import ActionSetListComp from "./components/actions/ActionSetListComp";
import DirectorStatusComp from "./components/director/DirectorStatusComp";
import StaticConditionListComp from "./components/triggers/StaticConditionListComp";
import ActionSetFormComp from "./components/actions/ActionSetFormComp";
import {ActionSet} from "./components/actions/objects";


interface ComponentProps {
}

interface ComponentState {
    editActionSet: ActionSet | null | undefined,    // null: new, undefined: none
}


class App extends Component<ComponentProps, ComponentState> {
    private readonly actionSetListComp: React.RefObject<ActionSetListComp>;

    constructor(props: ComponentProps) {
        super(props);

        this.state = {
            editActionSet: undefined,
        }

        this.actionSetListComp = React.createRef();

        this._editActionSet = this._editActionSet.bind(this);
        App.editActionSet = this._editActionSet;
    }

    static editActionSet = (actionSet: ActionSet | null | undefined) => console.error("Function not defined yet");

    private _editActionSet(actionSet: ActionSet | null | undefined) {
        console.log("On editActionSet click")
        this.setState({
            editActionSet: actionSet
        })
    }

    render() {
        return <div className="App">
            <NotificationComponent/>

            {this.state.editActionSet === undefined ? "" :
                <ActionSetFormComp actionSet={this.state.editActionSet}
                                   onSuccess={(actionSet) => {
                                       this.actionSetListComp.current?.loadList();
                                   }}
                                   onCancel={() => this.setState({editActionSet: undefined})}
                />}

            <DirectorStatusComp/>
            <TriggerListComp/>
            <ActionSetListComp ref={this.actionSetListComp}/>

            <hr/>

            <StaticConditionListComp/>
        </div>
    }
}

export default App;
