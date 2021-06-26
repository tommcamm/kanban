<template>
  <div class="modal-body">
    <div class="row justify-content-center">
      <div class="col-md-8">
        <b-alert show data-cy="loginError" variant="danger" v-if="authenticationError">
          <strong>Login fallito!</strong> Perfavore controlla le tue credenziali e riprova.
        </b-alert>
      </div>
      <div class="col-md-8">
        <b-form role="form" v-on:submit.prevent="doLogin()">
          <b-form-group label="Username" label-for="username">
            <b-form-input
              id="username"
              type="text"
              name="username"
              autofocus
              placeholder="Il tuo username"
              v-model="login"
              data-cy="username"
            >
            </b-form-input>
          </b-form-group>
          <b-form-group label="Password" label-for="password">
            <b-form-input
              id="password"
              type="password"
              name="password"
              v-model.trim="name"
              placeholder="La tua password"
              v-model="password"
              data-cy="password"
            >
            </b-form-input>
          </b-form-group>
          <b-form-checkbox id="rememberMe" name="rememberMe" v-model="rememberMe" checked>
            <span>Ricordami</span>
          </b-form-checkbox>
          <div>
            <b-button data-cy="submit" type="submit" variant="primary">Entra</b-button>
          </div>
        </b-form>
        <p></p>
        <div>
          <b-alert show variant="warning">
            <b-link :to="'/account/reset/request'" class="alert-link" data-cy="forgetYourPasswordSelector"
              >Hai dimenticato la password?</b-link
            >
          </b-alert>
        </div>
        <div>
          <b-alert show variant="warning">
            <span>Non hai ancora un account?</span>
            <b-link :to="'/register'" class="alert-link">Registrati</b-link>
          </b-alert>
        </div>
      </div>
    </div>
  </div>
</template>

<script lang="ts" src="./login-form.component.ts"></script>
