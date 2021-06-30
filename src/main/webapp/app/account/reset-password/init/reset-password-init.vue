<template>
  <div>
    <div class="row justify-content-center">
      <div class="col-md-8">
        <h1>Reimposta la tua password</h1>

        <div class="alert alert-warning" v-if="!success">
          <p>Inserisci la email con cui ti sei registrato.</p>
        </div>

        <div class="alert alert-success" v-if="success">
          <p>Controlla la tua email per vedere i dettagli su come resettare la tua password.</p>
        </div>

        <form v-if="!success" name="form" role="form" v-on:submit.prevent="requestReset()">
          <div class="form-group">
            <label class="form-control-label" for="email">Email</label>
            <input
              type="email"
              class="form-control"
              id="email"
              name="email"
              :class="{ valid: !$v.resetAccount.email.$invalid, invalid: $v.resetAccount.email.$invalid }"
              v-model="$v.resetAccount.email.$model"
              minlength="5"
              maxlength="254"
              email
              required
              data-cy="emailResetPassword"
            />
            <div v-if="$v.resetAccount.email.$anyDirty && $v.resetAccount.email.$invalid">
              <small class="form-text text-danger" v-if="!$v.resetAccount.email.required"> Your email is required. </small>
              <small class="form-text text-danger" v-if="!$v.resetAccount.email.email"> Your email is invalid. </small>
              <small class="form-text text-danger" v-if="!$v.resetAccount.email.minLength"> La email deve avere almeno 5 caratteri. </small>
              <small class="form-text text-danger" v-if="!$v.resetAccount.email.maxLength">
                La email non può superare i 100 caratteri.
              </small>
            </div>
          </div>
          <button type="submit" :disabled="$v.resetAccount.$invalid" class="btn btn-primary" data-cy="submit">Reset</button>
        </form>
      </div>
    </div>
  </div>
</template>

<script lang="ts" src="./reset-password-init.component.ts"></script>
