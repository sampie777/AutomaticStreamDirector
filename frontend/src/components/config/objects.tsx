import {FormComponent} from "../../common/forms/objects";


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

export class Config {
    frontend: Array<ConfigItem>;
    backend: Array<ConfigItem>;

    constructor(frontend: Array<ConfigItem>,
                backend: Array<ConfigItem>) {
        this.frontend = frontend;
        this.backend = backend;
    }
}