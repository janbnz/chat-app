var wsocket;

function connect() {
    wsocket = new WebSocket("ws://localhost:8887");
    wsocket.onopen = onopen;
    wsocket.onmessage = onmessage;
    wsocket.onclose = onclose;
}

function onopen() {
    console.log("Connected!");
}

function onmessage(event) {
    console.log("Data received: " + event.data);

    var messageData = JSON.parse(event.data);
    var name = messageData.name;
    var message = messageData.message;

    var messageContainer = document.querySelector(".message-container");
    var messageDiv = document.createElement("div");
    messageDiv.className = "message";

    var nameParagraph = document.createElement("p");
    nameParagraph.className = "name";
    nameParagraph.textContent = name + ":";

    var messageParagraph = document.createElement("p");
    messageParagraph.className = "message-text";
    messageParagraph.textContent = message;

    messageDiv.appendChild(nameParagraph);
    messageDiv.appendChild(messageParagraph);

    messageContainer.appendChild(messageDiv);

    // Auto scroll to the bottom
    messageContainer.scrollTop = messageContainer.scrollHeight;
}

function onclose(e) {
    console.log("Connection closed.");
}

// Is called when a message is sent
function onSubmit() {
    var nameInput = document.querySelector(".name-input");
    var messageInput = document.querySelector(".message-input");

    var name = nameInput.value;
    var message = messageInput.value;

    if (name === '' || message === '') return;

    var data = {
        name: name,
        message: message
    };

    var json = JSON.stringify(data);
    wsocket.send(json);

    messageInput.value = "";
}

window.addEventListener("load", connect, false);