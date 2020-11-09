const triggersDiv = document.getElementById("triggers");
const actionSetsDiv = document.getElementById("actionSets");
const directorStatusDiv = document.getElementById("director-status");

function updateTriggersList() {
    api.triggers.list()
        .then(response => response.json())
        .then(data => {
            const triggers = data.data;
            triggersDiv.innerHTML = "";

            console.log(triggers);

            if (triggers.length === 0) {
                triggersDiv.innerHTML = `<i>Much empty</i>`;
                return;
            }

            triggers.forEach(trigger => {
                const element = document.createElement("div");
                element.innerText = trigger.name;
                triggersDiv.appendChild(element);
            });
        })
        .catch(error => {
            console.error('Error updating Trigger list', error);
            addNotification(new Notification("Error updating Trigger list", error.message, Notification.ERROR));
        });
}

function updateActionSetsList() {
    api.actionSets.list()
        .then(response => response.json())
        .then(data => {
            const actionSets = data.data;
            actionSetsDiv.innerHTML = "";

            console.log(actionSets);

            if (actionSets.length === 0) {
                actionSetsDiv.innerHTML = `<i>Much empty</i>`;
                return;
            }

            actionSets.forEach(actionSet => {
                const element = document.createElement("div");
                element.innerText = actionSet.name;
                actionSetsDiv.appendChild(element);
            });
        })
        .catch(error => {
            console.error('Error updating ActionSets list', error);
            addNotification(new Notification("Error updating ActionSet list", error.message, Notification.ERROR));
        });
}

function updateDirectorStatus(keepPolling) {
    api.director.status()
        .then(response => response.json())
        .then(data => {
            const isRunning = data.data;
            directorStatusDiv.innerHTML = isRunning ? "Running" : "Not running";
        })
        .catch(error => {
            console.error('Error updating Director status', error);
            addNotification(new Notification("Error updating Director status", error.message, Notification.ERROR));
        })
        .finally(() => {
            if (keepPolling) {
                window.setTimeout(updateDirectorStatus, 2000, keepPolling);
            }
        });
}

updateTriggersList();
updateActionSetsList();
updateDirectorStatus(true);