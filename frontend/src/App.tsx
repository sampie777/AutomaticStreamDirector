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
import ConfigFormComp from "./components/config/ConfigFormComp";
import OpenConfigButtonComp from "./components/config/OpenConfigButtonComp";
import {Config} from "./components/config/objects";
import DirectorControlButtonComp from "./components/director/DirectorControlButtonComp";


interface ComponentProps {
}

interface ComponentState {
    editActionSet: ActionSet | null | undefined,    // actionSet: edit, null: new, undefined: hide form
    editTrigger: Trigger | null | undefined,    // trigger: edit, null: new, undefined: hide form
    editConfig: boolean,
}


class App extends Component<ComponentProps, ComponentState> {
    private readonly actionSetListComp: React.RefObject<ActionSetListComp>;
    private readonly triggerListComp: React.RefObject<TriggerListComp>;

    constructor(props: ComponentProps) {
        super(props);

        this.state = {
            editActionSet: undefined,
            editTrigger: undefined,
            editConfig: false,
        }

        this.actionSetListComp = React.createRef();
        this.triggerListComp = React.createRef();

        this._editActionSet = this._editActionSet.bind(this);
        App.editActionSet = this._editActionSet;

        this._editTrigger = this._editTrigger.bind(this);
        App.editTrigger = this._editTrigger;

        this._editConfig = this._editConfig.bind(this);
        App.editConfig = this._editConfig;
    }

    componentDidMount() {
        Config.load();
    }

    render() {
        return <div className="App">
            <NotificationComponent/>

            {/* Modals */}
            <ActionSetFormComp isOpen={this.state.editActionSet !== undefined}
                               actionSet={this.state.editActionSet || null}
                               onSuccess={(actionSet) => {
                                   // this.actionSetListComp.current?.loadList();
                               }}
                               onCancel={() => this.setState({editActionSet: undefined})}/>

            <TriggerFormComp isOpen={this.state.editTrigger !== undefined}
                             trigger={this.state.editTrigger || null}
                             onSuccess={this.triggerListComp.current?.loadList}
                             onCancel={() => this.setState({editTrigger: undefined})}/>

            <ConfigFormComp isOpen={this.state.editConfig}
                            onCancel={() => this.setState({editConfig: false})}/>

            {/* End Modals */}

            <OpenConfigButtonComp/>

            <DirectorStatusComp/>
            <DirectorControlButtonComp/>

            <div className={"trigger-list-action-list-wrapper"}>
                <TriggerListComp ref={this.triggerListComp}/>
                {/*<ActionSetListComp ref={this.actionSetListComp}/>*/}
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

    static editConfig = () => console.error("Function not defined yet");

    private _editConfig() {
        console.log("On editConfig click")
        this.setState({
            editConfig: true
        })
    }
}

export default App;
