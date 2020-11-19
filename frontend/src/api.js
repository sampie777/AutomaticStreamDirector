const apiBaseUrl = (window.location.hostname === "localhost") ? "http://localhost:8080/api/v1" : window.location.origin + "/api/v1";
console.log("API base URL: " + apiBaseUrl);

const get = (url) => fetch(url, {
    method: "GET"
});

const post = (url, data = "") => {
    const headers = {
        'Content-Type': 'application/json'
    };

    return fetch(url, {
        method: "POST",
        body: JSON.stringify(data),
        headers: headers
    });
}

const remove = (url, data = "") => {
    throw Error("Cannot perform DELETE request due to Cross Origin failures on server side")

    const headers = {
        'Content-Type': 'application/json'
    };

    return fetch(url, {
        method: "DELETE",
        body: JSON.stringify(data),
        headers: headers,
    });
}

export const api = {
    triggers: {
        list: () => get(`${apiBaseUrl}/triggers/list`),
        get: (name) => get(`${apiBaseUrl}/triggers/${name}`),
        save: (data) => post(`${apiBaseUrl}/triggers/save`, data),
        delete: (id) => post(`${apiBaseUrl}/triggers/delete/${id}`),
    },
    conditions: {
        list: () => get(`${apiBaseUrl}/conditions/list`),
        save: (data) => post(`${apiBaseUrl}/conditions/save`, data),
    },
    actionSets: {
        list: () => get(`${apiBaseUrl}/actionsets/list`),
        get: (name) => get(`${apiBaseUrl}/actionsets/${name}`),
        save: (data) => post(`${apiBaseUrl}/actionsets/save`, data),
        delete: (id) => post(`${apiBaseUrl}/actionsets/delete/${id}`),
    },
    actions: {
        list: () => get(`${apiBaseUrl}/actions/list`),
        save: (data) => post(`${apiBaseUrl}/actions/save`, data),
    },
    modules: {
        list: () => get(`${apiBaseUrl}/modules/list`),
        conditions: () => get(`${apiBaseUrl}/modules/conditions`),
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