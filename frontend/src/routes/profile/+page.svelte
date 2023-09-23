<script lang="ts">
	import { token } from '../../loginStore.ts';
	import { loadToken } from '../../loginStore.ts';
	import store from '../../loginStore.ts';
	loadToken();

    var username = "Username";
    var rank = "User";
    var status = "This is my status!";
    let avatar = "http://localhost:7070/profile_picture/" + store.getUserId(), fileinput;

    const onFileSelected = (e) => {
        let image = e.target.files[0];
        let reader = new FileReader();
        reader.readAsDataURL(image);
        reader.onload = e => {
            avatar = e.target.result;
            const formData = new FormData();
            formData.append('image', image);
            store.uploadPicture(formData);
        };
    }
</script>

<div class="content">
	<h1>Your profile</h1>

	<div class="area">
        <div class="profile-content">
            <img class="avatar" src={avatar} on:click={()=>{fileinput.click();}}>
            <p class="username">{username} <img class="edit-button" src="assets/edit.png"></p>
            <p class="rank">{rank}</p>
            <p class="status">{status}</p>

            <input style="display:none" type="file" accept=".jpg, .jpeg, .png" on:change={(e)=>onFileSelected(e)} bind:this={fileinput} >
        </div>
    </div>
</div>

<style>
	h1 {
		color: white;
	}

    .profile-content {
        margin-left: 5px;
        display: flex;
        align-items: center;
    }

	.area {
        width: 1200px;
        height: 500px;
        margin-top: 50px;
        background-color: #24262b;
		box-shadow: 0 4px 6px rgba(0, 0, 0, 0.6);
		border-radius: 10px;
	}

    .avatar {
        margin-top: 30px;
        margin-left: 30px;
        width: 120px;
        height: 120px;
        border-radius: 50%;
    }

    .username {
        font-size: 30px;
        margin-left: 25px;
        margin-top: 15px;
    }

    .rank {
        font-size: 19px;
        margin-top: 10px;
        margin-left: 15px;
        color: rgb(143, 132, 132);
    }

    .edit-button {
        width: 20px;
        height: 20px;
        margin-left: 3px;
    }

    .status {
        margin-top: 80px;
        margin-left: -230px;
        font-size: 18px;
        color: rgb(184, 175, 175);
    }
</style>
