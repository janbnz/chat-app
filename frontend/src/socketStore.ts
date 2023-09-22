import { writable } from 'svelte/store';
import {config} from 'dotenv';

const messageStore = writable('');

const BASE_SOCKET = "ws://localhost:8887"
const socket = new WebSocket(BASE_SOCKET);

// Connection opened
socket.addEventListener('open', function (event) {
    console.log("It's open");
});

// Listen for messages
socket.addEventListener('message', function (event) {
    messageStore.set(event.data);
});

const sendMessage = (message) => {
	if (socket.readyState <= 1) {
		socket.send(message);
	}
}

export default {
	subscribe: messageStore.subscribe,
	sendMessage
}