<template>
  <div class="row justify-content-center">
    <div class="col-8">
      <form name="editForm" role="form" novalidate v-on:submit.prevent="save()">
        <h2 id="kanbanApp.klist.home.createOrEditLabel" data-cy="KlistCreateUpdateHeading">Create or edit a Klist</h2>
        <div>
          <div class="form-group" v-if="klist.id">
            <label for="id">ID</label>
            <input type="text" class="form-control" id="id" name="id" v-model="klist.id" readonly />
          </div>
          <div class="form-group">
            <label class="form-control-label" for="klist-title">Title</label>
            <input
              type="text"
              class="form-control"
              name="title"
              id="klist-title"
              data-cy="title"
              :class="{ valid: !$v.klist.title.$invalid, invalid: $v.klist.title.$invalid }"
              v-model="$v.klist.title.$model"
              required
            />
            <div v-if="$v.klist.title.$anyDirty && $v.klist.title.$invalid">
              <small class="form-text text-danger" v-if="!$v.klist.title.required"> This field is required. </small>
              <small class="form-text text-danger" v-if="!$v.klist.title.minLength">
                This field is required to be at least 1 characters.
              </small>
              <small class="form-text text-danger" v-if="!$v.klist.title.maxLength">
                This field cannot be longer than 40 characters.
              </small>
            </div>
          </div>
          <div class="form-group">
            <label class="form-control-label" for="klist-order">Order</label>
            <input
              type="number"
              class="form-control"
              name="order"
              id="klist-order"
              data-cy="order"
              :class="{ valid: !$v.klist.order.$invalid, invalid: $v.klist.order.$invalid }"
              v-model.number="$v.klist.order.$model"
            />
          </div>
          <div class="form-group">
            <label class="form-control-label" for="klist-kanbanlist">Kanbanlist</label>
            <select class="form-control" id="klist-kanbanlist" data-cy="kanbanlist" name="kanbanlist" v-model="klist.kanbanlist" required>
              <option v-if="!klist.kanbanlist" v-bind:value="null" selected></option>
              <option
                v-bind:value="klist.kanbanlist && kanbanOption.id === klist.kanbanlist.id ? klist.kanbanlist : kanbanOption"
                v-for="kanbanOption in kanbans"
                :key="kanbanOption.id"
              >
                {{ kanbanOption.id }}
              </option>
            </select>
          </div>
          <div v-if="$v.klist.kanbanlist.$anyDirty && $v.klist.kanbanlist.$invalid">
            <small class="form-text text-danger" v-if="!$v.klist.kanbanlist.required"> This field is required. </small>
          </div>
        </div>
        <div>
          <button type="button" id="cancel-save" data-cy="entityCreateCancelButton" class="btn btn-secondary" v-on:click="previousState()">
            <font-awesome-icon icon="ban"></font-awesome-icon>&nbsp;<span>Cancel</span>
          </button>
          <button
            type="submit"
            id="save-entity"
            data-cy="entityCreateSaveButton"
            :disabled="$v.klist.$invalid || isSaving"
            class="btn btn-primary"
          >
            <font-awesome-icon icon="save"></font-awesome-icon>&nbsp;<span>Save</span>
          </button>
        </div>
      </form>
    </div>
  </div>
</template>
<script lang="ts" src="./klist-update.component.ts"></script>
