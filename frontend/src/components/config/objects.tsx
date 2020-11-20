import {FormComponent} from "../../common/forms/objects";
import {api} from "../../api";
import {addNotification, Notification} from "../notification/notifications";


export class ConfigItem {
    key: string;
    value: any | null;
    formComponent: FormComponent | null;

    constructor(key: string,
                value: any | null,
                formComponent: FormComponent | null = null) {
        this.key = key;
        this.value = value;
        this.formComponent = formComponent;
    }
}

export class ConfigItemsWrapper {
    frontend: Array<ConfigItem>;
    backend: Array<ConfigItem>;

    constructor(frontend: Array<ConfigItem>,
                backend: Array<ConfigItem>) {
        this.frontend = frontend;
        this.backend = backend;
    }
}

export class Config {
    static directorStatusUpdateInterval = 5

    static updateValuesWith(configItemsWrapper: ConfigItemsWrapper) {
        Config.directorStatusUpdateInterval = configItemsWrapper.frontend.find(it =>
            it.key == "directorStatusUpdateInterval")?.value
    }

    static load(callback: (config: ConfigItemsWrapper | null) => void = () => null) {
        console.log("(Re)loading config");

        api.config.list()
            .then(response => response.json())
            .then(data => {
                const config = data.data;

                console.log("Loaded config list:", config);

                if (config ! instanceof ConfigItemsWrapper) {
                    return callback(null);
                }

                Config.updateValuesWith(config);
                callback(config);
            })
            .catch(error => {
                console.error('Error loading Config', error);
                addNotification(new Notification("Error loading Config", error.message, Notification.ERROR));
                callback(null);
            });
    }

    static get(key: string, callback: (value: any | null) => void): any | null {
        console.debug("Loading config property: " + key);

        api.config.get(key)
            .then(response => response.json())
            .then(data => {
                const item = data.data;

                console.log("Loaded config item:", item);

                if (item ! instanceof ConfigItem) {
                    addNotification(new Notification("Error getting config property", "Unexpected response: " + item, Notification.ERROR));
                    callback(null);
                }

                callback(item.value);
            })
            .catch(error => {
                console.error('Error getting config property: ' + key, error);
                addNotification(new Notification("Error getting config property", error.message, Notification.ERROR));
                callback(null);
            });
    }
}