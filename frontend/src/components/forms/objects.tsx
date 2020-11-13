enum FormComponentType {
    Text = "Text",
    Number = "Number",
    Date = "Date",
    Time = "Time",
    Checkbox = "Checkbox",
    URL = "URL",
    Password = "Password",
}

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

    static Type = {
        Text: FormComponentType.Text,
        Number: FormComponentType.Number,
        Date: FormComponentType.Date,
        Time: FormComponentType.Time,
        Checkbox: FormComponentType.Checkbox,
        URL: FormComponentType.URL,
        Password: FormComponentType.Password,
    }
}