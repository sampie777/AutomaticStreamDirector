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

    static equals(a: FormComponent | null, b: FormComponent | null) {
        if (a == null && b == null)
            return true;

        if (a == null || b == null)
            return false;

        if (a.name !== b.name)
            return false;

        if (a.labelText !== b.labelText)
            return false;

        if (a.type !== b.type)
            return false;

        if (a.required !== b.required)
            return false;

        if (a.defaultValue !== b.defaultValue)
            return false;

        if (a.selectValues.length !== b.selectValues.length)
            return false;

        for (let i = 0; i < a.selectValues.length; i++) {
            if (!SelectOption.equals(a.selectValues[i], b.selectValues[i]))
                return false;
        }

        return true;
    }
}

class SelectOption {
    value: any | null;
    text: string;

    constructor(name: any | null, text: string) {
        this.value = name;
        this.text = text;
    }

    static equals(a: SelectOption | null, b: SelectOption | null) {
        if (a == null && b == null)
            return true;

        if (a == null || b == null)
            return false;

        if (a.value !== b.value)
            return false;

        if (a.text !== b.text)
            return false;

        return true;
    }
}