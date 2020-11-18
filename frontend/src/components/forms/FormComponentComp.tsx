import React, {Component} from 'react';
import {FormComponent} from "./objects";

interface ComponentProps {
    component: FormComponent,
    inputRef: React.RefObject<any> | null,
}

interface ComponentState {
}

export default class FormComponentComp extends Component<ComponentProps, ComponentState> {
    public static defaultProps = {
        inputRef: null,
    }

    constructor(props: ComponentProps) {
        super(props);
    }

    private getInput() {
        switch (this.props.component.type) {
            case FormComponent.Type.Text:
                return <input type='text'
                              ref={this.props.inputRef}
                              name={this.props.component.name}
                              defaultValue={this.props.component.defaultValue}
                              required={this.props.component.required}
                              className='formControl'/>
            case FormComponent.Type.Number:
                return <input type='number'
                              ref={this.props.inputRef}
                              name={this.props.component.name}
                              defaultValue={this.props.component.defaultValue}
                              required={this.props.component.required}
                              className='formControl'/>
            case FormComponent.Type.Date:
                return <input type='date'
                              ref={this.props.inputRef}
                              name={this.props.component.name}
                              defaultValue={this.props.component.defaultValue}
                              required={this.props.component.required}
                              className='formControl'/>
            case FormComponent.Type.Time:
                return <input type='time'
                              ref={this.props.inputRef}
                              name={this.props.component.name}
                              defaultValue={this.props.component.defaultValue}
                              required={this.props.component.required}
                              className='formControl'/>
            case FormComponent.Type.Checkbox:
                return <input type='checkbox'
                              ref={this.props.inputRef}
                              name={this.props.component.name}
                              defaultChecked={this.props.component.defaultValue}
                              required={this.props.component.required}
                              className='formControl'/>
            case FormComponent.Type.URL:
                return <input type='url'
                              ref={this.props.inputRef}
                              name={this.props.component.name}
                              defaultValue={this.props.component.defaultValue}
                              required={this.props.component.required}
                              className='formControl'/>
            case FormComponent.Type.Password:
                return <input type='password'
                              ref={this.props.inputRef}
                              name={this.props.component.name}
                              defaultValue={this.props.component.defaultValue}
                              required={this.props.component.required}
                              className='formControl'/>
            case FormComponent.Type.Select:
                return <select name={this.props.component.name}
                               ref={this.props.inputRef}
                               required={this.props.component.required}
                               className='formControl'>
                    {this.props.component.selectValues.map((it, i) =>
                        <option value={it.value}
                                ref={this.props.inputRef}
                                selected={it == this.props.component.defaultValue}
                                key={i + it.value}>{it.text}</option>
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