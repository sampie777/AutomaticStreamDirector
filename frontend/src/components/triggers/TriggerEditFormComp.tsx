import React, {Component} from 'react';
import {Trigger} from "./objects";
import SaveResult from "../../common/SaveResult";
import {api} from "../../api";
import {addNotification, Notification} from "../notification/notifications";

interface ComponentProps {
    trigger: Trigger | null,
    onUpdated: Function
}

interface ComponentState {
    saveResult: SaveResult,
    trigger: Trigger | null,
}

export default class TriggerEditFormComp extends Component<ComponentProps, ComponentState> {
    private readonly nameInputRef: React.RefObject<HTMLInputElement>;
    private readonly importanceInputRef: React.RefObject<HTMLInputElement>;

    constructor(props: ComponentProps) {
        super(props);

        this.state = {
            saveResult: SaveResult.UNKNOWN,
            trigger: props.trigger,
        };

        this.nameInputRef = React.createRef();
        this.importanceInputRef = React.createRef();

        this.onSaveEvent = this.onSaveEvent.bind(this);
    }

    private onSaveEvent() {
        this.setState({saveResult: SaveResult.PROCESSING});

        const trigger = new Trigger(
            this.nameInputRef.current!.value.trim(),
            +this.importanceInputRef.current!.value.trim(),
            [],
            []
        );

        console.log(trigger);

        api.triggers.save(trigger)
            .then(response => response.json())
            .then(data => {
                const trigger = data.data;

                this.setState({
                    trigger: trigger,
                    saveResult: SaveResult.SUCCESS
                })

                this.props.onUpdated()
            })
            .catch(error => {
                console.error('Error saving Trigger', error);
                addNotification(new Notification("Error saving Trigger", error.message, Notification.ERROR));

                this.setState({
                    trigger: trigger,
                    saveResult: SaveResult.ERROR
                })
            });
    }

    render() {
        return <div>
            <h3>Edit trigger {this.state.trigger?.name}</h3>

            <div className='formGroup'>
                <label>name</label>
                <input type='text'
                       defaultValue={this.state.trigger?.name}
                       ref={this.nameInputRef}
                       autoFocus={true}
                       required={true}
                       className='formControl'/>
            </div>
            <div className='formGroup'>
                <label>importance</label>
                <input type='number'
                       defaultValue={this.state.trigger?.importance}
                       ref={this.importanceInputRef}
                       required={true}
                       className='formControl'/>
            </div>

            <hr/>
            <div className='formGroup'>
                <button type='submit'
                        onClick={this.onSaveEvent}
                        className='formControl'>
                    {(() => {
                        switch (this.state.saveResult) {
                            case SaveResult.PROCESSING:
                                return this.props.trigger == null ? 'Adding' : 'Saving...';
                            case SaveResult.SUCCESS:
                                return this.props.trigger == null ? 'Added' : 'Saved';
                            case SaveResult.ERROR:
                                return "Error";
                            default:
                                return this.props.trigger == null ? 'Add' : 'Save'
                        }
                    })()}
                </button>
            </div>
        </div>;
    }
}