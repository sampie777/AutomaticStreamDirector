const apiBaseUrl = (window.location.hostname === "localhost") ? "http://localhost:8080/api/v1" : window.location.origin + "/api/v1";
console.log("API base URL: " + apiBaseUrl);

const get = (url) => fetch(url, {
    method: "GET"
});

const post = (url, data = "") => {
    let headers = {
        'Content-Type': 'application/json'
    };

    return fetch(url, {
        method: "POST",
        body: JSON.stringify(data),
        headers: headers
    });
}

export const api = {
    triggers: {
        list: () => get(`${apiBaseUrl}/triggers/list`),
        get: (name) => get(`${apiBaseUrl}/triggers/${name}`),
        save: (trigger) => post(`${apiBaseUrl}/triggers/save`, trigger),
    },
    actionSets: {
        list: () => get(`${apiBaseUrl}/actionsets/list`),
        get: (name) => get(`${apiBaseUrl}/actionsets/${name}`),
    },
    modules: {
        list: () => get(`${apiBaseUrl}/modules/list`),
        get: (name) => get(`${apiBaseUrl}/modules/${name}`),
    },
    director: {
        start: () => get(`${apiBaseUrl}/director/start`),
        stop: () => get(`${apiBaseUrl}/director/stop`),
        status: () => get(`${apiBaseUrl}/director/status`),
        lastTrigger: () => get(`${apiBaseUrl}/director/lasttrigger`),
    },
    config: {
        list: () => get(`${apiBaseUrl}/config/list`),
        get: (key) => get(`${apiBaseUrl}/config/${key}`),
    },
};