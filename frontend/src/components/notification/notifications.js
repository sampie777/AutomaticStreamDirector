import React, {Component} from 'react';
import './notifications.css';

export class Notification {
    constructor(title, message, type) {
        this.title = title;
        this.message = message;
        this.type = type;
    }

    static INFO = "info";
    static SUCCESS = "success";
    static ERROR = "error";
}

/* NOTIFICATIONS */
const notificationDuration = 10_000;

export function addNotification(notification) {
    window.addNotification(notification);
}

export class NotificationComponent extends Component {
    constructor(props) {
        super(props);

        this.state = {
            notifications: []
        }

        window.addNotification = (notification) => this.addNotification(notification)

        this.addNotification = this.addNotification.bind(this)
        this.removeNotification = this.removeNotification.bind(this)
    }

    addNotification(notification) {
        if (!(notification instanceof Notification)) {
            console.error("Cannot add notification, not a valid instance: ", notification);
            return
        }

        console.info("Adding notification: ", notification);

        this.setState({
            notifications: this.state.notifications.concat([notification])
        });
    }

    removeNotification(notification) {
        const newList = this.state.notifications.filter(n => n !== notification);
        this.setState({
            notifications: newList
        });
    }

    render() {
        return <div id="notifications">
            {this.state.notifications.map(notification =>
                <NotificationMessage notification={notification}
                                     key={notification.title}
                                     remove={() => this.removeNotification(notification)}/>)}
        </div>;
    }
}

class NotificationMessage extends Component {
    constructor(props) {
        super(props);
        this.notification = props.notification;

        this.element = React.createRef();

        this.getTransitionDurationForElement = this.getTransitionDurationForElement.bind(this);
        this.removeNotification = this.removeNotification.bind(this);
    }

    componentDidMount() {
        window.setTimeout(this.removeNotification, notificationDuration);
    }

    removeNotification() {
        let element = this.element.current;
        console.debug("Removing notification:", this);

        if (element === null){
            console.debug("Notification already (being) removed");
            return
        }

        element.style.opacity = "0";

        const transitionDuration = this.getTransitionDurationForElement(element);
        window.setTimeout(this.props.remove, transitionDuration);
    }

    getTransitionDurationForElement(element) {
        const transitionDurationString = window.getComputedStyle(element).transitionDuration;
        return transitionDurationString.substring(0, transitionDurationString.length - 1) * 1000;
    }

    render() {
        return <div className={`notification notification-${this.notification.type}`}
                    ref={this.element}>
            <a className="notification-close"
               onClick={this.removeNotification}>close</a>
            <div className="notification-title">{this.notification.title}</div>
            <div className="notification-message">{this.notification.message}</div>
        </div>;
    }
}