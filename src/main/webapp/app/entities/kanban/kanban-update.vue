<template>
  <div class="row justify-content-center">
    <div class="col-8">
      <form name="editForm" role="form" novalidate v-on:submit.prevent="save()">
        <h2 id="kanbanApp.kanban.home.createOrEditLabel" data-cy="KanbanCreateUpdateHeading">Crea o Modifica un Kanban</h2>
        <div>
          <div class="form-group" v-if="kanban.id">
            <label for="id">ID</label>
            <input type="text" class="form-control" id="id" name="id" v-model="kanban.id" readonly />
          </div>
          <div class="form-group">
            <label class="form-control-label" for="kanban-name">Nome</label>
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
              <small class="form-text text-danger" v-if="!$v.kanban.name.required"> Questo campo è richiesto. </small>
              <small class="form-text text-danger" v-if="!$v.kanban.name.minLength">
                Questo campo deve essere di almeno 1 carattere.
              </small>
              <small class="form-text text-danger" v-if="!$v.kanban.name.maxLength"> Questo campo non può superare i 40 caratteri. </small>
            </div>
          </div>
        </div>
        <div>
          <button type="button" id="cancel-save" data-cy="entityCreateCancelButton" class="btn btn-secondary" v-on:click="previousState()">
            <font-awesome-icon icon="ban"></font-awesome-icon>&nbsp;<span>Annulla</span>
          </button>
          <button
            type="submit"
            id="save-entity"
            data-cy="entityCreateSaveButton"
            :disabled="$v.kanban.$invalid || isSaving"
            class="btn btn-primary"
          >
            <font-awesome-icon icon="save"></font-awesome-icon>&nbsp;<span>Salva</span>
          </button>
        </div>
      </form>
    </div>
  </div>
</template>
<script lang="ts" src="./kanban-update.component.ts"></script>
