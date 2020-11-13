import React, {Component} from 'react';
import {FormComponent, FormComponentType} from "./objects";

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
        const ref = React.createRef();
        switch (this.props.component.type) {
            case FormComponentType.Text:
                return <input type='text'
                              name={this.props.component.name}
                              defaultValue={this.props.component.defaultValue}
                              required={this.props.component.required}
                              className='formControl'/>
            case FormComponentType.Number:
                return <input type='number'
                              name={this.props.component.name}
                              defaultValue={this.props.component.defaultValue}
                              required={this.props.component.required}
                              className='formControl'/>
            case FormComponentType.Date:
                return <input type='date'
                              name={this.props.component.name}
                              defaultValue={this.props.component.defaultValue}
                              required={this.props.component.required}
                              className='formControl'/>
            case FormComponentType.Time:
                return <input type='time'
                              name={this.props.component.name}
                              defaultValue={this.props.component.defaultValue}
                              required={this.props.component.required}
                              className='formControl'/>
            case FormComponentType.Checkbox:
                return <input type='checkbox'
                              name={this.props.component.name}
                              defaultChecked={this.props.component.defaultValue}
                              required={this.props.component.required}
                              className='formControl'/>
            case FormComponentType.URL:
                return <input type='url'
                              name={this.props.component.name}
                              defaultValue={this.props.component.defaultValue}
                              required={this.props.component.required}
                              className='formControl'/>
            case FormComponentType.Password:
                return <input type='password'
                              name={this.props.component.name}
                              defaultValue={this.props.component.defaultValue}
                              required={this.props.component.required}
                              className='formControl'/>
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