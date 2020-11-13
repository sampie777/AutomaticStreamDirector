export class FormComponent {
    name: string;
    labelText: string;
    type: FormComponentType;
    required: boolean;
    defaultValue: any;

    constructor(
        name: string,
        labelText: string,
        type: FormComponentType,
        required: boolean = true,
        defaultValue: any = null
    ) {
        this.name = name;
        this.labelText = labelText;
        this.type = type;
        this.required = required;
        this.defaultValue = defaultValue;
    }
}

export enum FormComponentType {
    Text = "Text",
    Number = "Number",
    Date = "Date",
    Time = "Time",
    Checkbox = "Checkbox",
    URL = "URL",
    Password = "Password",
}