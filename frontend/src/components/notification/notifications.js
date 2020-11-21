import React, {Component} from 'react';
import './notifications.sass';

export class Notification {
    isRemoved = false;
    timestamp = Date.now()

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
    NotificationComponent.addNotification(notification);
}

export class NotificationComponent extends Component {
    constructor(props) {
        super(props);

        this.state = {
            notifications: []
        }

        window.addNotification = (notification) => this._addNotification(notification)

        this._addNotification = this._addNotification.bind(this)
        NotificationComponent.addNotification = this._addNotification;
        this.removeRemovedNotifications = this.removeRemovedNotifications.bind(this)
    }

    static addNotification = (notification) => console.error("Function not defined yet");

    _addNotification(notification) {
        if (!(notification instanceof Notification)) {
            console.error("Cannot add notification, not a valid instance: ", notification);
            return
        }

        console.info("Adding notification: ", notification);

        this.setState({
            notifications: this.state.notifications.concat([notification])
        });
    }

    removeRemovedNotifications() {
        this.setState({
            notifications: this.state.notifications.filter(it => !it.isRemoved)
        });
    }

    render() {
        return <div id="notifications">
            {this.state.notifications.map((notification, i) =>
                <NotificationMessage notification={notification}
                                     key={i + notification.timestamp.toString()}
                                     remove={this.removeRemovedNotifications}/>)}
        </div>;
    }
}

class NotificationMessage extends Component {
    constructor(props) {
        super(props);
        this.notification = props.notification;

        this.element = React.createRef();
        this.removeTimeout = null;

        this.getTransitionDurationForElement = this.getTransitionDurationForElement.bind(this);
        this.removeNotification = this.removeNotification.bind(this);
        this.removeElement = this.removeElement.bind(this);
    }

    componentDidMount() {
        this.removeTimeout = window.setTimeout(this.removeNotification.bind(this), notificationDuration);
    }

    removeNotification() {
        window.clearTimeout(this.removeTimeout);

        const element = this.element.current;
        console.debug("Removing notification:", this.notification);

        if (element == null) {
            console.debug("Notification DOM element already (being) removed");
            return this.removeElement();
        }

        element.style.opacity = "0";

        const transitionDuration = this.getTransitionDurationForElement(element);
        window.setTimeout(this.removeElement, transitionDuration);
    }

    getTransitionDurationForElement(element) {
        const transitionDurationString = window.getComputedStyle(element).transitionDuration;
        return transitionDurationString.substring(0, transitionDurationString.length - 1) * 1000;
    }

    removeElement() {
        const element = this.element.current;
        if (element != null) {
            element.style.display = "none";
        }

        this.notification.isRemoved = true;

        this.props.remove();
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