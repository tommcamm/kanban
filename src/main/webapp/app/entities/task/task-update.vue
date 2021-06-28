<template>
  <div class="row justify-content-center">
    <div class="col-8">
      <form name="editForm" role="form" novalidate v-on:submit.prevent="save()">
        <h2 id="kanbanApp.task.home.createOrEditLabel" data-cy="TaskCreateUpdateHeading">Create or edit a Task</h2>
        <div>
          <div class="form-group" v-if="task.id">
            <label for="id">ID</label>
            <input type="text" class="form-control" id="id" name="id" v-model="task.id" readonly />
          </div>
          <div class="form-group">
            <label class="form-control-label" for="task-title">Title</label>
            <input
              type="text"
              class="form-control"
              name="title"
              id="task-title"
              data-cy="title"
              :class="{ valid: !$v.task.title.$invalid, invalid: $v.task.title.$invalid }"
              v-model="$v.task.title.$model"
              required
            />
            <div v-if="$v.task.title.$anyDirty && $v.task.title.$invalid">
              <small class="form-text text-danger" v-if="!$v.task.title.required"> This field is required. </small>
              <small class="form-text text-danger" v-if="!$v.task.title.minLength">
                This field is required to be at least 1 characters.
              </small>
              <small class="form-text text-danger" v-if="!$v.task.title.maxLength"> This field cannot be longer than 40 characters. </small>
            </div>
          </div>
          <div class="form-group">
            <label class="form-control-label" for="task-order">Order</label>
            <input
              type="number"
              class="form-control"
              name="order"
              id="task-order"
              data-cy="order"
              :class="{ valid: !$v.task.order.$invalid, invalid: $v.task.order.$invalid }"
              v-model.number="$v.task.order.$model"
            />
          </div>
          <div class="form-group">
            <label class="form-control-label" for="task-klisttask">Klisttask</label>
            <select class="form-control" id="task-klisttask" data-cy="klisttask" name="klisttask" v-model="task.klisttask" required>
              <option v-if="!task.klisttask" v-bind:value="null" selected></option>
              <option
                v-bind:value="task.klisttask && klistOption.id === task.klisttask.id ? task.klisttask : klistOption"
                v-for="klistOption in klists"
                :key="klistOption.id"
              >
                {{ klistOption.id }}
              </option>
            </select>
          </div>
          <div v-if="$v.task.klisttask.$anyDirty && $v.task.klisttask.$invalid">
            <small class="form-text text-danger" v-if="!$v.task.klisttask.required"> This field is required. </small>
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
            :disabled="$v.task.$invalid || isSaving"
            class="btn btn-primary"
          >
            <font-awesome-icon icon="save"></font-awesome-icon>&nbsp;<span>Save</span>
          </button>
        </div>
      </form>
    </div>
  </div>
</template>
<script lang="ts" src="./task-update.component.ts"></script>
