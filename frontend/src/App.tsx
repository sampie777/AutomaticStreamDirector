import React from 'react';
import './App.css';
import {NotificationComponent} from "./components/notification/notifications";
import TriggerListComp from "./components/triggers/TriggerListComp";
import ActionSetListComp from "./components/actions/ActionSetListComp";
import DirectorStatusComp from "./components/director/DirectorStatusComp";
import StaticActionListComp from "./components/actions/StaticActionListComp";
import StaticConditionListComp from "./components/triggers/StaticConditionListComp";
import ActionSetFormComp from "./components/actions/ActionSetFormComp";

function App() {
  return (
    <div className="App">
      <NotificationComponent/>

      <ActionSetFormComp  actionSet={null}/>

      <DirectorStatusComp/>
      <TriggerListComp/>
      <ActionSetListComp/>

      <hr/>

      <StaticConditionListComp/>
      <StaticActionListComp/>
    </div>
  );
}

export default App;
