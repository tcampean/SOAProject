import axios from "axios";
import {getCookie} from "./cookies";

export function getUser(username) {
    return axios.get(`http://host.docker.internal:4000/app/user/${username}`, {
        headers: {
            Authorization: `Bearer ${getCookie('JWT_TOKEN')}`
        }
    });
}

export function getUsers() {
    return axios.get(`http://host.docker.internal:4000/app/user`, {
        headers: {
            Authorization: `Bearer ${getCookie('JWT_TOKEN')}`
        }
    });
}

export function getCurrentUser() {
    return axios.get(`http://host.docker.internal:4000/app/user/me`, {
        headers: {
            Authorization: `Bearer ${getCookie("JWT_TOKEN")}`
        }
    });
}

export function getUserPosts(username, htmlBody) {
    return axios.get(`http://host.docker.internal:4000/app/posts/author/${username}?htmlBody=${htmlBody}`, {
        headers: {
            Authorization: `Bearer ${getCookie('JWT_TOKEN')}`
        }
    });
}

export function createPost(title, content) {
    return axios.post(`http://host.docker.internal:4000/app/posts`, {
        title: title, content: content
    }, {
        headers: {
            Authorization: `Bearer ${getCookie('JWT_TOKEN')}`
        }
    });
}

export function deletePost(id) {
    return axios.delete(`http://host.docker.internal:4000/app/posts/${id}`, {
        headers: {
            Authorization: `Bearer ${getCookie('JWT_TOKEN')}`
        }
    });
}

export function getUserSubscribers(username) {
    return axios.get(`http://host.docker.internal:4000/app/subscribers/${username}`, {
        headers: {
            Authorization: `Bearer ${getCookie('JWT_TOKEN')}`
        }
    });
}

export function getSubscribedUser(username) {
    return axios.get(`http://host.docker.internal:4000/app/subscribers/${username}/subscribed`, {
        headers: {
            Authorization: `Bearer ${getCookie('JWT_TOKEN')}`
        }
    });
}

export function subscribeToUser(username) {
    return axios.post(`http://host.docker.internal:4000/app/subscribers/${username}`, {}, {
        headers: {
            Authorization: `Bearer ${getCookie('JWT_TOKEN')}`
        }
    });
}

export function unsubscribeUser(username) {
    return axios.delete(`http://host.docker.internal:4000/app/subscribers/${username}`, {
        headers: {
            Authorization: `Bearer ${getCookie('JWT_TOKEN')}`
        }
    });
}