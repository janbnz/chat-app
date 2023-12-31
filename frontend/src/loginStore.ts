import { writable } from "svelte/store";
import { goto } from '$app/navigation';

export var isLoggedIn = writable(false);
export var token = "";
export var userId = "";

const BASE_URL = "http://localhost:7070"

export function register(username: string, password: string) {
    fetch(new Request(BASE_URL + "/register", {
        method: 'POST',
        body: JSON.stringify({
            "username": username,
            "password": password
        })
    })).then(response => {
        if (response.status == 200) {
            login(username, password);
        } else {
            // TODO: show error
            console.log("Error!");
        }
    });
}

export function login(username: string, password: string) {
    fetch(new Request(BASE_URL + "/login", {
        method: 'POST',
        body: JSON.stringify({
            "username": username,
            "password": password
        })
    })).then(response => {
        console.log(response);
        if (response.status === 200) {
            return response.json();
        } else {
            console.log("Error!");
            throw new Error("Login failed");
        }
    }).then(data => {
        token = data.response;
        userId = data.id;
        setCookie('token', token, 30);
        setCookie('userId', userId, 30);
        isLoggedIn.set(true);
        goto('/');
    }).catch(error => {
        console.error("Error:", error);
        // TODO: show error
    });
}

export function logout() {
    const headers = new Headers({
        'Authorization': `Bearer ${token}`
    });

    fetch(new Request(BASE_URL + "/logout", {
        method: 'POST',
        headers: headers
    })).then(response => {
        console.log(response);
        if (response.status === 200) {
            isLoggedIn.set(false);
            token = "";
            setCookie("token", "", -1);
            setCookie("userId", "", -1);
            goto('/');
        } else {
            console.log("Error!");
            throw new Error("Logout failed");
        }
    }).catch(error => {
        console.error("Error:", error);
        // TODO: show error
    });
}

export function uploadPicture(formData) {
    const headers = new Headers({
        'Authorization': `Bearer ${token}`
    });

      fetch(BASE_URL + "/profile_picture", {
        method: 'POST',
        body: formData,
        headers: headers
    }).then(response => {
        if (response.status === 200) {
            console.log("Bild erfolgreich hochgeladen!");
        } else {
            console.log("Fehler beim Hochladen des Bildes!");
        }
    }).catch(error => {
        console.error("Fehler:", error);
    });
}

export function loadToken() {
    token = getCookie('token');
    userId = getCookie('userId');
    if (token === '') {
        isLoggedIn.set(false);
        return;
    }
    isLoggedIn.set(true);
}

function setCookie(name: String, value: String, days: number) {
    var expires = "";
    if (days) {
        var date = new Date();
        date.setTime(date.getTime() + (days * 24 * 60 * 60 * 1000));
        expires = "; expires=" + date.toUTCString();
    }
    document.cookie = name + "=" + (value || "") + expires + "; SameSite=Lax; path=/;";
}

function getCookie(name: String) {
    var nameEQ = name + "=";
    var ca = document.cookie.split(';');
    for (var i = 0; i < ca.length; i++) {
        var c = ca[i];
        while (c.charAt(0) == ' ') c = c.substring(1, c.length);
        if (c.indexOf(nameEQ) == 0) return c.substring(nameEQ.length, c.length);
    }
    return "";
}

function getToken() {
    return token;
}

function getUserId() {
    return userId;
}

export default {
	login,
    logout,
    register,
    getCookie,
    setCookie,
    loadToken,
    getToken,
    uploadPicture,
    getUserId
}