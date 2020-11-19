import React, {Component} from 'react';
import './App.sass';
import {NotificationComponent} from "./components/notification/notifications";
import TriggerListComp from "./components/triggers/TriggerListComp";
import ActionSetListComp from "./components/actions/ActionSetListComp";
import DirectorStatusComp from "./components/director/DirectorStatusComp";
import ActionSetFormComp from "./components/actions/ActionSetFormComp";
import {ActionSet} from "./components/actions/objects";
import {Trigger} from "./components/triggers/objects";
import TriggerFormComp from "./components/triggers/TriggerFormComp";


interface ComponentProps {
}

interface ComponentState {
    editActionSet: ActionSet | null | undefined,    // actionSet: edit, null: new, undefined: hide form
    editTrigger: Trigger | null | undefined,    // trigger: edit, null: new, undefined: hide form
}


class App extends Component<ComponentProps, ComponentState> {
    private readonly actionSetListComp: React.RefObject<ActionSetListComp>;
    private readonly triggerListComp: React.RefObject<TriggerListComp>;

    constructor(props: ComponentProps) {
        super(props);

        this.state = {
            editActionSet: undefined,
            editTrigger: undefined,
        }

        this.actionSetListComp = React.createRef();
        this.triggerListComp = React.createRef();

        this._editActionSet = this._editActionSet.bind(this);
        App.editActionSet = this._editActionSet;

        this._editTrigger = this._editTrigger.bind(this);
        App.editTrigger = this._editTrigger;
    }

    render() {
        return <div className="App">
            <NotificationComponent/>

            {this.state.editActionSet === undefined ? "" :
                <ActionSetFormComp actionSet={this.state.editActionSet}
                                   onSuccess={(actionSet) => {
                                       this.actionSetListComp.current?.loadList();
                                   }}
                                   onCancel={() => this.setState({editActionSet: undefined})}/>}
            {this.state.editTrigger === undefined ? "" :
                <TriggerFormComp trigger={this.state.editTrigger}
                                 onSuccess={(trigger) => {
                                     this.triggerListComp.current?.loadList();
                                 }}
                                 onCancel={() => this.setState({editTrigger: undefined})}/>}

            <DirectorStatusComp/>

            <div className={"trigger-list-action-list-wrapper"}>
                <TriggerListComp ref={this.triggerListComp}/>
                <ActionSetListComp ref={this.actionSetListComp}/>
            </div>
        </div>
    }

    static editActionSet = (actionSet: ActionSet | null | undefined) => console.error("Function not defined yet");

    private _editActionSet(actionSet: ActionSet | null | undefined) {
        console.log("On editActionSet click")
        this.setState({
            editActionSet: actionSet
        })
    }

    static editTrigger = (trigger: Trigger | null | undefined) => console.error("Function not defined yet");

    private _editTrigger(trigger: Trigger | null | undefined) {
        console.log("On editTrigger click")
        this.setState({
            editTrigger: trigger
        })
    }
}

export default App;
