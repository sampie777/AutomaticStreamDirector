import React, {Component} from 'react';
import {FormComponent} from "./objects";

interface ComponentProps {
    component: FormComponent,
}

interface ComponentState {
}

export default class FormComponentComp extends Component<ComponentProps, ComponentState> {

    constructor(props: ComponentProps) {
        super(props);

        console.log(this.props.component);
    }

    private getInput() {
        switch (this.props.component.type) {
            case FormComponent.Type.Text:
                return <input type='text'
                              name={this.props.component.name}
                              defaultValue={this.props.component.defaultValue}
                              required={this.props.component.required}
                              className='formControl'/>
            case FormComponent.Type.Number:
                return <input type='number'
                              name={this.props.component.name}
                              defaultValue={this.props.component.defaultValue}
                              required={this.props.component.required}
                              className='formControl'/>
            case FormComponent.Type.Date:
                return <input type='date'
                              name={this.props.component.name}
                              defaultValue={this.props.component.defaultValue}
                              required={this.props.component.required}
                              className='formControl'/>
            case FormComponent.Type.Time:
                return <input type='time'
                              name={this.props.component.name}
                              defaultValue={this.props.component.defaultValue}
                              required={this.props.component.required}
                              className='formControl'/>
            case FormComponent.Type.Checkbox:
                return <input type='checkbox'
                              name={this.props.component.name}
                              defaultChecked={this.props.component.defaultValue}
                              required={this.props.component.required}
                              className='formControl'/>
            case FormComponent.Type.URL:
                return <input type='url'
                              name={this.props.component.name}
                              defaultValue={this.props.component.defaultValue}
                              required={this.props.component.required}
                              className='formControl'/>
            case FormComponent.Type.Password:
                return <input type='password'
                              name={this.props.component.name}
                              defaultValue={this.props.component.defaultValue}
                              required={this.props.component.required}
                              className='formControl'/>
            case FormComponent.Type.Select:
                return <select name={this.props.component.name}
                               required={this.props.component.required}
                               className='formControl'>
                    {this.props.component.selectValues.map((it, i) =>
                        <option value={it}
                                selected={it == this.props.component.defaultValue}
                                key={i + it}>{it}</option>
                    )}
                </select>
            default:
                return <p>empty</p>;
        }
    }

    render() {
        return <div className={"FormComponentComp formGroup"}>
            <label>{this.props.component.labelText}</label>
            {this.getInput()}
        </div>;
    }
}