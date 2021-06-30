<template>
  <div>
    <div class="row justify-content-center">
      <div class="col-md-8">
        <h1>Reimposta la tua password</h1>

        <div class="alert alert-danger" v-if="keyMissing">
          <strong>La chiave di reset password è mancante.</strong>
        </div>

        <div class="alert alert-danger" v-if="error">
          <p>La tua password non può essere resettata. Ricorda una richiesta di reset è valida solo per 24 ore.</p>
        </div>

        <div class="alert alert-success" v-if="success">
          <span><strong>La password è stata cambiata.</strong> Perfavore effettua il </span>
          <a class="alert-link" v-on:click="openLogin">login</a>
        </div>
        <div class="alert alert-danger" v-if="doNotMatch">
          <p>La password e la sua conferma non coincidono!</p>
        </div>

        <div class="alert alert-warning" v-if="!success && !keyMissing">
          <p>Scegli una nuova password.</p>
        </div>

        <div v-if="!keyMissing">
          <form v-if="!success" name="form" role="form" v-on:submit.prevent="finishReset()">
            <div class="form-group">
              <label class="form-control-label" for="newPassword">Nuova password</label>
              <input
                type="password"
                class="form-control"
                id="newPassword"
                name="newPassword"
                :class="{ valid: !$v.resetAccount.newPassword.$invalid, invalid: $v.resetAccount.newPassword.$invalid }"
                v-model="$v.resetAccount.newPassword.$model"
                minlength="4"
                maxlength="50"
                required
                data-cy="resetPassword"
              />
              <div v-if="$v.resetAccount.newPassword.$anyDirty && $v.resetAccount.newPassword.$invalid">
                <small class="form-text text-danger" v-if="!$v.resetAccount.newPassword.required"> Your password is required. </small>
                <small class="form-text text-danger" v-if="!$v.resetAccount.newPassword.minLength">
                  La password deve avere almeno 4 caratteri.
                </small>
                <small class="form-text text-danger" v-if="!$v.resetAccount.newPassword.maxLength">
                  La password non può superare i 50 caratteri.
                </small>
              </div>
            </div>
            <div class="form-group">
              <label class="form-control-label" for="confirmPassword">Conferma nuova password</label>
              <input
                type="password"
                class="form-control"
                id="confirmPassword"
                name="confirmPassword"
                :class="{ valid: !$v.resetAccount.confirmPassword.$invalid, invalid: $v.resetAccount.confirmPassword.$invalid }"
                v-model="$v.resetAccount.confirmPassword.$model"
                minlength="4"
                maxlength="50"
                required
                data-cy="confirmResetPassword"
              />
              <div v-if="$v.resetAccount.confirmPassword.$anyDirty && $v.resetAccount.confirmPassword.$invalid">
                <small class="form-text text-danger" v-if="!$v.resetAccount.confirmPassword.sameAsPassword">
                  La password e la sua conferma non coincidono!
                </small>
              </div>
            </div>
            <button type="submit" :disabled="$v.resetAccount.$invalid" class="btn btn-primary" data-cy="submit">Salva</button>
          </form>
        </div>
      </div>
    </div>
  </div>
</template>

<script lang="ts" src="./reset-password-finish.component.ts"></script>
