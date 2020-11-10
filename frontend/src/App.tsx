import React from 'react';
import './App.css';
import {NotificationComponent} from "./components/notification/notifications";
import TriggerListComp from "./components/triggers/TriggerListComp";
import ActionSetListComp from "./components/actions/ActionSetListComp";
import DirectorStatusComp from "./components/director/DirectorStatusComp";

function App() {
  return (
    <div className="App">
      <NotificationComponent/>

      <DirectorStatusComp/>
      <TriggerListComp/>
      <ActionSetListComp/>
    </div>
  );
}

export default App;
