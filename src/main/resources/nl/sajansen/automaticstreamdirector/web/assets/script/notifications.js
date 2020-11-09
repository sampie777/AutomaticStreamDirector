class Notification {
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
const notificationsParent = document.querySelector("#notifications");
const notificationTemplate = document.querySelector("#notification-template");
const notificationDuration = 10_000;

const getTransitionDurationForElement = (notificationElement) => {
    const transitionDurationString = window.getComputedStyle(notificationElement).transitionDuration;
    return transitionDurationString.substring(0, transitionDurationString.length - 1) * 1000;
}

const removeNotification = (notificationElement) => {
    console.debug("Removing notification:", notificationElement);
    notificationElement.style.opacity = "0";

    const transitionDuration = getTransitionDurationForElement(notificationElement);
    window.setTimeout(() => notificationElement.remove(), transitionDuration);
}

const addNotification = (notification) => {
    console.info("Adding notification: ", notification);
    const template = notificationTemplate.cloneNode(true);
    template.removeAttribute("id");
    template.classList.remove("template");

    if (notification instanceof Notification) {
        template.querySelector(".notification-title").innerHTML = notification.title;
        template.querySelector(".notification-message").innerHTML = notification.message;
        template.classList.add("notification-" + notification.type);
    }

    template.querySelector(".notification-close")
        .addEventListener("click", () => removeNotification(template));

    notificationsParent.appendChild(template);

    window.setTimeout(removeNotification, notificationDuration, template);
}