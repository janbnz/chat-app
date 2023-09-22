<script lang="ts">
	import { logout } from "../loginStore";
	import { isLoggedIn } from "../loginStore";
    import { loadToken } from '../loginStore.ts';
    
	var loginState: boolean;
	isLoggedIn.subscribe((value) => loginState = value);
</script>

<svelte:head>
	<title>chat-app</title>
</svelte:head>

<div class="sidebar">
	<div class="title">
		<h1>chat-app</h1>
		<hr class="rounded" />
	</div>

	<ul>
		<li><img alt="Logo" src="/assets/home.png" /> <a href="/">Home</a></li>
		<li><img alt="Logo" src="/assets/global.png" /> <a href="/global">Global chat</a></li>
		<li><img alt="Logo" src="/assets/chats.png" /> <a href="/chats">Chats</a></li>
		<li><img alt="Logo" src="/assets/profile.png" /> <a href="/profile">Profile</a></li>
	</ul>

	<div class="logout">
		{#if loginState}
			<img alt="Logo" src="/assets/logout.png" /> <a on:click={logout}>Logout</a>
		{:else}
			<img alt="Logo" src="/assets/login.png" /> <a href="/login">Login</a>
		{/if}
	</div>
</div>

<slot />

<style>
	@import url('https://fonts.googleapis.com/css2?family=Open+Sans:wght@500&display=swap');

	:global(body) {
		background-color: #1e2024;
		margin: 0;
		font-family: 'Open Sans', sans-serif;
	}

	.sidebar {
		width: 230px;
		background-color: #2c3038;
		color: white;
		position: fixed;
		top: 0;
		bottom: 0;
		padding: 20px;
	}

	ul {
		list-style-type: none;
		padding: 0;
		margin-top: 60px;
	}

	li {
		margin-bottom: 10px;
		margin-top: 40px;
		font-size: 24px;
		display: flex;
		align-items: center;
	}

	li img,
	.logout img {
		filter: invert(100%);
		width: 40px;
		margin-right: 10px;
	}

	.title h1 {
		font-size: 38px;
		margin-bottom: -5px;
	}

	.title hr {
		border-top: 3px solid #ffffff;
		width: 90px;
		margin-left: 0px;
	}

	.logout {
		position: absolute;
		bottom: 20px;
		left: 20px;
		font-size: 24px;
		display: flex;
		align-items: center;
	}

	:global(a, p, h1) {
		text-decoration: none;
		color: white;
	}

	:global(.content) {
		padding-left: 300px;
		padding-top: 30px;
	}
</style>
