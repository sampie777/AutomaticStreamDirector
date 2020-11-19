export class FormComponent {
    name: string;
    labelText: string;
    type: string;
    required: boolean;
    defaultValue: any;

    selectValues: Array<SelectOption>;

    constructor(
        name: string,
        labelText: string,
        type: string,
        required: boolean = true,
        defaultValue: any = null,
        selectValues: Array<SelectOption> = [],
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
        TextArea: "TextArea",
        Number: "Number",
        Date: "Date",
        Time: "Time",
        Checkbox: "Checkbox",
        URL: "URL",
        Password: "Password",
        Select: "Select",
    }

    static SelectOption = (value: any | null, text: string) => new SelectOption(value, text)
}

class SelectOption {
    value: any | null;
    text: string;

    constructor(name: any | null, text: string) {
        this.value = name;
        this.text = text;
    }
}