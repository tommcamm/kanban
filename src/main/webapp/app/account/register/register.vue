<template>
  <div>
    <div class="row justify-content-center">
      <div class="col-md-8 toastify-container">
        <h1 id="register-title" data-cy="registerTitle">Registrazione</h1>

        <div class="alert alert-success" role="alert" v-if="success">
          <strong>Registrazione effettuata con successo!</strong> Controlla la tua mail per confermare.
        </div>

        <div class="alert alert-danger" role="alert" v-if="error"><strong>Registrazione fallita!</strong> Riprova più tardi</div>

        <div class="alert alert-danger" role="alert" v-if="errorUserExists">
          <strong>Username già esistente,</strong> scegline un altro.
        </div>

        <div class="alert alert-danger" role="alert" v-if="errorEmailExists"><strong>Email già esistente,</strong> scegline un altro.</div>
      </div>
    </div>
    <div class="row justify-content-center">
      <div class="col-md-8">
        <form id="register-form" name="registerForm" role="form" v-on:submit.prevent="register()" v-if="!success" no-validate>
          <div class="form-group">
            <label class="form-control-label" for="username">Username</label>
            <input
              type="text"
              class="form-control"
              v-model="$v.registerAccount.login.$model"
              id="username"
              name="login"
              :class="{ valid: !$v.registerAccount.login.$invalid, invalid: $v.registerAccount.login.$invalid }"
              required
              minlength="1"
              maxlength="50"
              pattern="^[a-zA-Z0-9!#$&'*+=?^_`{|}~.-]+@?[a-zA-Z0-9-]+(?:\.[a-zA-Z0-9-]+)*$"
              data-cy="username"
            />
            <div v-if="$v.registerAccount.login.$anyDirty && $v.registerAccount.login.$invalid">
              <small class="form-text text-danger" v-if="!$v.registerAccount.login.required"> Username richiesto. </small>
              <small class="form-text text-danger" v-if="!$v.registerAccount.login.minLength">
                Il tuo Username deve contenere almeno 1 carattere.
              </small>
              <small class="form-text text-danger" v-if="!$v.registerAccount.login.maxLength">
                Il tuo Username può contenere al massimo 50 caratteri.
              </small>
              <small class="form-text text-danger" v-if="!$v.registerAccount.login.pattern">
                Il tuo Username può contenere solo lettere e numeri.
              </small>
            </div>
          </div>
          <div class="form-group">
            <label class="form-control-label" for="email">Email</label>
            <input
              type="email"
              class="form-control"
              id="email"
              name="email"
              :class="{ valid: !$v.registerAccount.email.$invalid, invalid: $v.registerAccount.email.$invalid }"
              v-model="$v.registerAccount.email.$model"
              minlength="5"
              maxlength="254"
              email
              required
              data-cy="email"
            />
            <div v-if="$v.registerAccount.email.$anyDirty && $v.registerAccount.email.$invalid">
              <small class="form-text text-danger" v-if="!$v.registerAccount.email.required"> Email richiesta. </small>
              <small class="form-text text-danger" v-if="!$v.registerAccount.email.email"> Email non valida.</small>
              <small class="form-text text-danger" v-if="!$v.registerAccount.email.minLength">
                La tua email deve contenere almeno 1 carattere.
              </small>
              <small class="form-text text-danger" v-if="!$v.registerAccount.email.maxLength">
                La tua email può contenere al massimo 100 caratteri.
              </small>
            </div>
          </div>
          <div class="form-group">
            <label class="form-control-label" for="firstPassword">Password</label>
            <input
              type="password"
              class="form-control"
              id="firstPassword"
              name="password"
              :class="{ valid: !$v.registerAccount.password.$invalid, invalid: $v.registerAccount.password.$invalid }"
              v-model="$v.registerAccount.password.$model"
              minlength="4"
              maxlength="50"
              required
              data-cy="firstPassword"
            />
            <div v-if="$v.registerAccount.password.$anyDirty && $v.registerAccount.password.$invalid">
              <small class="form-text text-danger" v-if="!$v.registerAccount.password.required"> Password richiesta. </small>
              <small class="form-text text-danger" v-if="!$v.registerAccount.password.minLength">
                La tua password deve contenere almeno 4 caratteri.
              </small>
              <small class="form-text text-danger" v-if="!$v.registerAccount.password.maxLength">
                La tua password può contenere al massimo 100 caratteri.
              </small>
            </div>
          </div>
          <div class="form-group">
            <label class="form-control-label" for="secondPassword">Conferma password</label>
            <input
              type="password"
              class="form-control"
              id="secondPassword"
              name="confirmPasswordInput"
              :class="{ valid: !$v.confirmPassword.$invalid, invalid: $v.confirmPassword.$invalid }"
              v-model="$v.confirmPassword.$model"
              minlength="4"
              maxlength="50"
              required
              data-cy="secondPassword"
            />
            <div v-if="$v.confirmPassword.$dirty && $v.confirmPassword.$invalid">
              <small class="form-text text-danger" v-if="!$v.confirmPassword.required"> Conferma password richiesta. </small>
              <small class="form-text text-danger" v-if="!$v.confirmPassword.minLength">
                La tua conferma della password deve contenere almeno 4 caratteri.
              </small>
              <small class="form-text text-danger" v-if="!$v.confirmPassword.maxLength">
                La tua conferma della password può contenere al massimo 100 caratteri.
              </small>
              <small class="form-text text-danger" v-if="!$v.confirmPassword.sameAsPassword">
                La password e la conferma della password non corrispondono.
              </small>
            </div>
          </div>

          <button type="submit" :disabled="$v.$invalid" class="btn btn-primary" data-cy="submit">Register</button>
        </form>
        <p></p>
        <div class="alert alert-warning">
          <span><strong>DEV-MODE</strong></span>
          <br />
          <span> Se vuoi </span>
          <a class="alert-link" v-on:click="openLogin()">entrare</a
          ><span
            >, puoi utilizzare gli account di default:<br />- Admin (username="admin" e password="admin") <br />- Utente (username="user" e
            password="user").</span
          >
        </div>
      </div>
    </div>
  </div>
</template>

<script lang="ts" src="./register.component.ts"></script>
