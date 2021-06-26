<template>
  <div class="row justify-content-center">
    <div class="col-8">
      <form name="editForm" role="form" novalidate v-on:submit.prevent="save()">
        <h2 id="kanbanApp.kanban.home.createOrEditLabel" data-cy="KanbanCreateUpdateHeading">Create or edit a Kanban</h2>
        <div>
          <div class="form-group" v-if="kanban.id">
            <label for="id">ID</label>
            <input type="text" class="form-control" id="id" name="id" v-model="kanban.id" readonly />
          </div>
          <div class="form-group">
            <label class="form-control-label" for="kanban-name">Name</label>
            <input
              type="text"
              class="form-control"
              name="name"
              id="kanban-name"
              data-cy="name"
              :class="{ valid: !$v.kanban.name.$invalid, invalid: $v.kanban.name.$invalid }"
              v-model="$v.kanban.name.$model"
              required
            />
            <div v-if="$v.kanban.name.$anyDirty && $v.kanban.name.$invalid">
              <small class="form-text text-danger" v-if="!$v.kanban.name.required"> This field is required. </small>
              <small class="form-text text-danger" v-if="!$v.kanban.name.minLength">
                This field is required to be at least 1 characters.
              </small>
              <small class="form-text text-danger" v-if="!$v.kanban.name.maxLength">
                This field cannot be longer than 30 characters.
              </small>
            </div>
          </div>
          <div class="form-group">
            <label class="form-control-label" for="kanban-created_at">Created At</label>
            <b-input-group class="mb-3">
              <b-input-group-prepend>
                <b-form-datepicker
                  aria-controls="kanban-created_at"
                  v-model="$v.kanban.created_at.$model"
                  name="created_at"
                  class="form-control"
                  :locale="currentLanguage"
                  button-only
                  today-button
                  reset-button
                  close-button
                >
                </b-form-datepicker>
              </b-input-group-prepend>
              <b-form-input
                id="kanban-created_at"
                data-cy="created_at"
                type="text"
                class="form-control"
                name="created_at"
                :class="{ valid: !$v.kanban.created_at.$invalid, invalid: $v.kanban.created_at.$invalid }"
                v-model="$v.kanban.created_at.$model"
              />
            </b-input-group>
          </div>
          <div class="form-group">
            <label class="form-control-label" for="kanban-last_edit">Last Edit</label>
            <b-input-group class="mb-3">
              <b-input-group-prepend>
                <b-form-datepicker
                  aria-controls="kanban-last_edit"
                  v-model="$v.kanban.last_edit.$model"
                  name="last_edit"
                  class="form-control"
                  :locale="currentLanguage"
                  button-only
                  today-button
                  reset-button
                  close-button
                >
                </b-form-datepicker>
              </b-input-group-prepend>
              <b-form-input
                id="kanban-last_edit"
                data-cy="last_edit"
                type="text"
                class="form-control"
                name="last_edit"
                :class="{ valid: !$v.kanban.last_edit.$invalid, invalid: $v.kanban.last_edit.$invalid }"
                v-model="$v.kanban.last_edit.$model"
              />
            </b-input-group>
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
            :disabled="$v.kanban.$invalid || isSaving"
            class="btn btn-primary"
          >
            <font-awesome-icon icon="save"></font-awesome-icon>&nbsp;<span>Save</span>
          </button>
        </div>
      </form>
    </div>
  </div>
</template>
<script lang="ts" src="./kanban-update.component.ts"></script>
