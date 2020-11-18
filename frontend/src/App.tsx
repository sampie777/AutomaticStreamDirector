import React, {Component} from 'react';
import './App.css';
import {NotificationComponent} from "./components/notification/notifications";
import TriggerListComp from "./components/triggers/TriggerListComp";
import ActionSetListComp from "./components/actions/ActionSetListComp";
import DirectorStatusComp from "./components/director/DirectorStatusComp";
import StaticActionListComp from "./components/actions/StaticActionListComp";
import StaticConditionListComp from "./components/triggers/StaticConditionListComp";
import ActionSetFormComp from "./components/actions/ActionSetFormComp";
import {ActionSet} from "./components/actions/objects";


interface ComponentProps {
}

interface ComponentState {
    editingActionSet: ActionSet | null,
}

class App extends Component<ComponentProps, ComponentState> {
    private readonly actionSetListComp: React.RefObject<ActionSetListComp>;

    constructor(props: ComponentProps) {
        super(props);

        this.state = {
            editingActionSet: null,
        }

        this.actionSetListComp = React.createRef();
    }

    render() {
        return <div className="App">
            <NotificationComponent/>

            <ActionSetFormComp actionSet={this.state.editingActionSet}
                               onSuccess={(actionSet) => {
                                   this.actionSetListComp.current?.loadList();
                               }}/>

            <DirectorStatusComp/>
            <TriggerListComp/>
            <ActionSetListComp ref={this.actionSetListComp}/>

            <hr/>

            <StaticConditionListComp/>
            <StaticActionListComp/>
        </div>
    }
}

export default App;
