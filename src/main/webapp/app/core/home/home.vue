<template>
  <div class="home row">
    <div class="col-md-3">
      <img src="content/images/home.png" />
    </div>
    <div class="col-md-9">
      <h1 class="display-4">Benvenuto nel Kanban Project!</h1>
      <p class="lead">Questa è la tua homepage</p>

      <div>
        <div class="alert alert-success" v-if="authenticated">
          <span v-if="username">Sei autenticato come "{{ username }}"</span>
        </div>

        <div class="alert alert-warning" v-if="!authenticated">
          <span>If you want to </span>
          <a class="alert-link" v-on:click="openLogin()">sign in</a
          ><span
            >, you can try the default accounts:<br />- Administrator (login="admin" and password="admin") <br />- User (login="user" and
            password="user").</span
          >
        </div>
        <div class="alert alert-warning" v-if="!authenticated">
          <span>Non hai ancora un account?</span>&nbsp;
          <router-link class="alert-link" to="/register">Registrati ora!</router-link>
        </div>
      </div>
    </div>
  </div>
</template>

<style scoped>
.home {
  position: relative;
  text-align: center;
}

.alert {
  width: 60%;
  left: 20%;
}

img {
  width: 130%;
  height: auto;
}
</style>

<script lang="ts" src="./home.component.ts"></script>

width="460" height="380"
