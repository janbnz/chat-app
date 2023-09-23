<script lang="ts">
	import TextMessage from '../../components/TextMessage.svelte';
	import loginStore from '../../loginStore.ts';
	import store from '../../socketStore.ts';
	import { writable } from 'svelte/store';

	let messagesDiv;

	loginStore.loadToken();
	store.sendMessage(JSON.stringify({
		"action": "load_public",
		"token": loginStore.getToken()
	}));

	let messages = writable([]);
	function handleEnterPress(event) {
		if (event.key === 'Enter' && !event.shiftKey && event.target.value.trim().length !== 0) {
			event.preventDefault();
			var data = {
				token: loginStore.getToken(),
				message: event.target.value
			};

			var json = JSON.stringify(data);
			store.sendMessage(json);
			event.target.value = '';
		}
	}

	function addNewMessage(username, date, imageSource, message) {
		const currentDate = new Date();
		const messageDate = new Date(date);

		const dayDifference = Math.floor((currentDate - messageDate) / (24 * 60 * 60 * 1000));

		let formattedDate;
		if (dayDifference === 0) {
			formattedDate = `heute um ${messageDate.toLocaleTimeString([], {
				hour: '2-digit',
				minute: '2-digit'
			})}`;
		} else if (dayDifference === 1) {
			formattedDate = `gestern um ${messageDate.toLocaleTimeString([], {
				hour: '2-digit',
				minute: '2-digit'
			})}`;
		} else {
			formattedDate = `${messageDate.toLocaleDateString()}, ${messageDate.toLocaleTimeString([], {
				hour: '2-digit',
				minute: '2-digit'
			})}`;
		}

		let imageUrl = "http://localhost:7070/profile_picture/" + loginStore.getUserId();
		const newMessage = {
			username,
			date: formattedDate + ' Uhr',
			imageSource: imageUrl,
			message
		};
		$messages = [...$messages, newMessage];

		// Scroll to bottom
		const threshold = 10;
		if (messagesDiv.scrollTop + messagesDiv.clientHeight + threshold >= messagesDiv.scrollHeight) {
			setTimeout(() => {
				messagesDiv.scrollTop = messagesDiv.scrollHeight;
			}, 20);
		}
	}

	store.subscribe((receivedMessage) => {
		try {
			const messageData = JSON.parse(receivedMessage);
			const { name, sentAt, imageSource, message } = messageData;
			addNewMessage(name, sentAt, imageSource, message);
		} catch (error) {
			console.error('Fehler beim Verarbeiten der empfangenen Nachricht:', error);
		}
	});
</script>

<div class="content">
	<h1>Global</h1>

	<div class="chat">
		<div class="messages" bind:this={messagesDiv}>
			{#each $messages as message}
				<TextMessage
					username={message.username}
					date={message.date}
					imageSource={message.imageSource}
					message={message.message}
				/>
			{/each}
		</div>
		<textarea class="input" type="text" on:keydown={handleEnterPress} />
	</div>
</div>

<style>
	h1 {
		color: white;
	}

	.chat {
		width: 1500px;
		height: 780px;
		margin-top: 40px;
		background-color: #24262b;
		box-shadow: 0 4px 6px rgba(0, 0, 0, 0.6);
		border-radius: 10px;
		padding-top: 15px;
		padding-left: 15px;
		display: flex;
		flex-direction: column;
	}

	.input {
		position: relative;
		bottom: 25px;
		width: 1300px;
		height: 60px;
		background-color: #1e2024;
		color: white;
		font-size: 19px;
		border-radius: 8px;
	}

	.messages {
		margin-top: 5px;
		display: flex;
		flex-direction: column;
		gap: 5px;
		height: 80px;
		flex-grow: 1;
		overflow-y: auto;
		margin-bottom: 50px;
	}
</style>
