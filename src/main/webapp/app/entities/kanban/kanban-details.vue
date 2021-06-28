<template>
  <div class="row justify-content-center">
    <div class="col-8">
      <div v-if="kanban">
        <h2 class="jh-entity-heading" data-cy="kanbanDetailsHeading"><span>Kanban</span> {{ kanban.name }}</h2>
        <p>Qui va il kanban</p>

        <button type="submit" v-on:click.prevent="previousState()" class="btn btn-info" data-cy="entityDetailsBackButton">
          <font-awesome-icon icon="arrow-left"></font-awesome-icon>&nbsp;<span> Ritorna</span>
        </button>
        <router-link v-if="kanban.id" :to="{ name: 'KanbanEdit', params: { kanbanId: kanban.id } }" custom v-slot="{ navigate }">
          <button @click="navigate" class="btn btn-primary">
            <font-awesome-icon icon="pencil-alt"></font-awesome-icon>&nbsp;<span> Modifica</span>
          </button>
        </router-link>
      </div>
    </div>
  </div>
</template>

<script lang="ts" src="./kanban-details.component.ts"></script>
