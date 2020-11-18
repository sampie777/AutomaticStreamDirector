import React, {Component} from 'react';
import './componentlist.sass';

interface ComponentProps {
    onEditClick: () => void | null,
    onDeleteClick: () => void | null,
    onClick: () => void,
    onDoubleClick: () => void,
}

interface ComponentState {
}

export default class ComponentListItemComp extends Component<ComponentProps, ComponentState> {
    public static defaultProps = {
        onEditClick: null,
        onDeleteClick: null,
        onClick: () => null,
        onDoubleClick: () => null,
    }

    constructor(props: ComponentProps) {
        super(props);
    }

    render() {
        return <div className={"ComponentListItemComp"}
                    onClick={this.props.onClick}
                    onDoubleClick={this.props.onDoubleClick}>
            <div className={"content"}>
                {this.props.children}
            </div>
            <div className={"controls"}>
                {this.props.onEditClick == null ? "" : <a onClick={this.props.onEditClick} className={"edit"}>Edit</a>}
                {this.props.onDeleteClick == null ? "" : <a onClick={this.props.onDeleteClick} className={"delete"}>Delete</a>}
            </div>
        </div>;
    }
}