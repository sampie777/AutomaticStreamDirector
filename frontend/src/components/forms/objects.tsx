export class FormComponent {
    name: string;
    labelText: string;
    type: string;
    required: boolean;
    defaultValue: any;

    selectValues: Array<string>;

    constructor(
        name: string,
        labelText: string,
        type: string,
        required: boolean = true,
        defaultValue: any = null,
        selectValues: Array<string> = [],
    ) {
        this.name = name;
        this.labelText = labelText;
        this.type = type;
        this.required = required;
        this.defaultValue = defaultValue;
        this.selectValues = selectValues;
    }

    static Type = {
        Text: "Text",
        Number: "Number",
        Date: "Date",
        Time: "Time",
        Checkbox: "Checkbox",
        URL: "URL",
        Password: "Password",
        Select: "Select",
    }
}