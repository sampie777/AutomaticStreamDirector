import React, {Component} from 'react';
import './componentlist.sass';

interface ComponentProps {
    onEditClick: () => void | null,
    onDeleteClick: () => void | null,
    onClick: () => void,
    onDoubleClick: () => void,
    className: string,
}

interface ComponentState {
}

export default class ComponentListItemComp extends Component<ComponentProps, ComponentState> {
    public static defaultProps = {
        onEditClick: null,
        onDeleteClick: null,
        onClick: () => null,
        onDoubleClick: () => null,
        className: "",
    }

    private readonly editAnchorRef: React.RefObject<HTMLAnchorElement>;
    private readonly deleteAnchorRef: React.RefObject<HTMLAnchorElement>;

    constructor(props: ComponentProps) {
        super(props);

        this.editAnchorRef = React.createRef();
        this.deleteAnchorRef = React.createRef();

        this.onClick = this.onClick.bind(this);
    }

    render() {
        return <div className={"ComponentListItemComp " + this.props.className}
                    onClick={this.onClick}
                    onDoubleClick={this.props.onDoubleClick}>
            <div className={"content"}>
                {this.props.children}
            </div>
            <div className={"controls"}>
                {this.props.onEditClick == null ? "" : <a onClick={this.props.onEditClick} ref={this.editAnchorRef} className={"edit"}>Edit</a>}
                {this.props.onDeleteClick == null ? "" : <a onClick={this.props.onDeleteClick} ref={this.deleteAnchorRef} className={"delete"}>Delete</a>}
            </div>
        </div>;
    }

    private onClick(e: React.MouseEvent<HTMLDivElement, MouseEvent>) {
        if (this.isRefTheTarget(this.editAnchorRef, e) || this.isRefTheTarget(this.deleteAnchorRef, e)) {
            return;
        }

        this.props.onClick();
    }

    private isRefTheTarget(ref: React.RefObject<HTMLElement>, e: React.MouseEvent<HTMLDivElement, MouseEvent>): boolean {
        return ref != null && e.target == ref.current
    }
}