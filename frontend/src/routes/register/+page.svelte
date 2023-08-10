<script lang="ts">
	import { register } from '../../loginStore.ts';

	let username = '';
	let password = '';
</script>

<div class="content">
	<div class="register-box">
		<h2>Register</h2>
		<form>
			<div class="box">
				<input type="text" bind:value={username} name="" required />
				<label>Username</label>
			</div>

			<div class="box">
				<input type="password" bind:value={password} name="" required />
				<label>Password</label>
			</div>

			<a class="register-button" on:click={() => register(username, password)} >Register</a>
			<p class="login-text">
				Already a member? <a href="login" class="login-link">Login here</a>
			</p>
		</form>
	</div>
</div>

<style>
	.register-box {
		position: absolute;
		top: 50%;
		left: 50%;
		width: 400px;
		padding: 40px;
		transform: translate(-50%, -50%);
		background: rgba(0, 0, 0, 0.5);
		box-sizing: border-box;
		box-shadow: 0 15px 25px rgba(0, 0, 0, 0.6);
		border-radius: 10px;
	}

	.register-box h2 {
		margin: 0 0 30px;
		padding: 0;
		color: #fff;
		text-align: center;
	}

	.register-box .box {
		position: relative;
	}

	.register-box .box input {
		width: 100%;
		padding: 10px 0;
		font-size: 16px;
		color: #fff;
		margin-bottom: 30px;
		border: none;
		border-bottom: 1px solid #fff;
		outline: none;
		background: transparent;
	}

	.register-box .box label {
		position: absolute;
		top: 0;
		left: 0;
		padding: 10px 0;
		font-size: 16px;
		color: #fff;
		pointer-events: none;
		transition: 0.5s;
	}

	.register-box .box input:focus ~ label,
	.register-box .box input:valid ~ label {
		top: -20px;
		left: 0;
		font-size: 12px;
	}

	.register-box form .register-button {
		position: relative;
		display: inline-block;
		padding: 10px 20px;
		font-size: 16px;
		text-decoration: none;
		text-transform: uppercase;
		overflow: hidden;
		transition: 0.5s;
		margin-top: 15px;
		letter-spacing: 4px;
		background: #1d1c1c;
	}

	.register-box .register-button:hover {
		background: #313131;
		color: #fff;
		border-radius: 5px;
		cursor: pointer;
	}

	.login-text {
		color: #7a7979;
	}

	.login-link {
		color: #ddd9d9;
	}
</style>
