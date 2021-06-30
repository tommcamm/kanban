<template>
  <div>
    <div class="row justify-content-center">
      <div class="col-md-8 toastify-container">
        <h2 v-if="account" id="password-title">
          <span>
            Password per [<strong>{{ username }}</strong
            >]</span
          >
        </h2>

        <div class="alert alert-success" role="alert" v-if="success">
          <strong>Password cambiata!</strong>
        </div>
        <div class="alert alert-danger" role="alert" v-if="error"><strong>Errore!</strong> La password non può essere cambiata.</div>

        <div class="alert alert-danger" role="alert" v-if="doNotMatch">La password e la sua conferma non coincidono!</div>

        <form name="form" role="form" id="password-form" v-on:submit.prevent="changePassword()">
          <div class="form-group">
            <label class="form-control-label" for="currentPassword">Password corrente</label>
            <input
              type="password"
              class="form-control"
              id="currentPassword"
              name="currentPassword"
              :class="{ valid: !$v.resetPassword.currentPassword.$invalid, invalid: $v.resetPassword.currentPassword.$invalid }"
              v-model="$v.resetPassword.currentPassword.$model"
              required
              data-cy="currentPassword"
            />
            <div v-if="$v.resetPassword.currentPassword.$anyDirty && $v.resetPassword.currentPassword.$invalid">
              <small class="form-text text-danger" v-if="!$v.resetPassword.currentPassword.required"> La password è richiesta. </small>
            </div>
          </div>
          <div class="form-group">
            <label class="form-control-label" for="newPassword">Nuova Password</label>
            <input
              type="password"
              class="form-control"
              id="newPassword"
              name="newPassword"
              :class="{ valid: !$v.resetPassword.newPassword.$invalid, invalid: $v.resetPassword.newPassword.$invalid }"
              v-model="$v.resetPassword.newPassword.$model"
              minlength="4"
              maxlength="50"
              required
              data-cy="newPassword"
            />
            <div v-if="$v.resetPassword.newPassword.$anyDirty && $v.resetPassword.newPassword.$invalid">
              <small class="form-text text-danger" v-if="!$v.resetPassword.newPassword.required"> La password è richiesta. </small>
              <small class="form-text text-danger" v-if="!$v.resetPassword.newPassword.minLength">
                La Password deve essere almeno lunga 4 caratteri
              </small>
              <small class="form-text text-danger" v-if="!$v.resetPassword.newPassword.maxLength">
                La Password non può superare i 50 caratteri.
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
              :class="{ valid: !$v.resetPassword.confirmPassword.$invalid, invalid: $v.resetPassword.confirmPassword.$invalid }"
              v-model="$v.resetPassword.confirmPassword.$model"
              minlength="4"
              maxlength="50"
              required
              data-cy="confirmPassword"
            />
            <div v-if="$v.resetPassword.confirmPassword.$anyDirty && $v.resetPassword.confirmPassword.$invalid">
              <small class="form-text text-danger" v-if="!$v.resetAccount.confirmPassword.sameAsPassword">
                La password e la sua conferma non coincidono!
              </small>
            </div>
          </div>

          <button type="submit" :disabled="$v.resetPassword.$invalid" class="btn btn-primary" data-cy="submit">Salva</button>
        </form>
      </div>
    </div>
  </div>
</template>

<script lang="ts" src="./change-password.component.ts"></script>
