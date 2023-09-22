<script>
    export let imageSource;
    export let username;
    export let date;
    export let message;
    export let index;
</script>

<div class="message">
    <div class="message-header">
        <div class="info">
            <img class="profile-picture" src="{imageSource}">
            <div class="username-container">
                <p class="username">{username} <span class="date">{date}</span></p>
            </div>
        </div>
        <p class="msg">{message}</p>
    </div>
</div>

<style>
  .message {
    display: flex;
    align-items: flex-start;
    margin-top: -5px;
    font-size: 17px;
}

.profile-picture {
    width: 60px;
    height: 60px;
    border-radius: 50%;
}

.info {
    margin-left: 5px;
    display: flex; /* Hinzugefügte CSS-Eigenschaft */
    align-items: center; /* Zentriert die Elemente vertikal */
}

.username-container {
    margin-left: 20px; /* Abstand zwischen Bild und Benutzername */
    padding-bottom: 30px;
}

.username {
    font-size: 20px;
}

.date {
    margin-left: 10px;
    color: rgb(143, 132, 132);
    font-size: 15px;
}

.msg {
    margin-left: 85px;
    margin-top: -45px;
    margin-right: 50px;
}
</style>