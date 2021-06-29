<template>
  <div>
    <h2 id="page-heading" data-cy="KanbanHeading">
      <span id="kanban-heading">I tuoi Kanban</span>
      <div class="d-flex justify-content-end">
        <button class="btn btn-info mr-2" v-on:click="handleSyncList" :disabled="isFetching">
          <font-awesome-icon icon="sync" :spin="isFetching"></font-awesome-icon> <span>Aggiorna</span>
        </button>
        <router-link :to="{ name: 'KanbanCreate' }" custom v-slot="{ navigate }">
          <button
            @click="navigate"
            id="jh-create-entity"
            data-cy="entityCreateButton"
            class="btn btn-primary jh-create-entity create-kanban"
          >
            <font-awesome-icon icon="plus"></font-awesome-icon>
            <span> Crea un nuovo Kanban </span>
          </button>
        </router-link>
      </div>
    </h2>
    <br />
    <div class="alert alert-warning" v-if="!isFetching && kanbans && kanbans.length === 0">
      <router-link :to="{ name: 'KanbanCreate' }" custom v-slot="{ navigate }">
        <span>Nessun Kanban trovato. Vuoi crearne uno nuovo? <a class="alert-link" @click="navigate">Crea</a>! </span>
      </router-link>
    </div>
    <div class="table-responsive" v-if="kanbans && kanbans.length > 0">
      <table class="table table-striped" aria-describedby="kanbans">
        <thead>
          <tr>
            <th scope="row" v-on:click="changeOrder('name')">
              <span>Nome</span> <jhi-sort-indicator :current-order="propOrder" :reverse="reverse" :field-name="'name'"></jhi-sort-indicator>
            </th>
            <th scope="row" v-on:click="changeOrder('created_at')">
              <span>Creato il</span>
              <jhi-sort-indicator :current-order="propOrder" :reverse="reverse" :field-name="'created_at'"></jhi-sort-indicator>
            </th>
            <th scope="row" v-on:click="changeOrder('last_edit')">
              <span>Ultima Modifica</span>
              <jhi-sort-indicator :current-order="propOrder" :reverse="reverse" :field-name="'last_edit'"></jhi-sort-indicator>
            </th>
            <th scope="row"></th>
          </tr>
        </thead>
        <tbody>
          <tr v-for="kanban in kanbans" :key="kanban.id" data-cy="entityTable">
            <td>
              <router-link :to="{ name: 'KanbanView', params: { kanbanId: kanban.id } }">{{ kanban.name }}</router-link>
            </td>
            <td>{{ kanban.created_at }}</td>
            <td>{{ kanban.last_edit }}</td>
            <td class="text-right">
              <div class="btn-group">
                <router-link :to="{ name: 'KanbanView', params: { kanbanId: kanban.id } }" custom v-slot="{ navigate }">
                  <button @click="navigate" class="btn btn-info btn-sm details" data-cy="entityDetailsButton">
                    <font-awesome-icon icon="eye"></font-awesome-icon>
                    <span class="d-none d-md-inline">Visualizza</span>
                  </button>
                </router-link>
                <router-link :to="{ name: 'KanbanEdit', params: { kanbanId: kanban.id } }" custom v-slot="{ navigate }">
                  <button @click="navigate" class="btn btn-primary btn-sm edit" data-cy="entityEditButton">
                    <font-awesome-icon icon="pencil-alt"></font-awesome-icon>
                    <span class="d-none d-md-inline">Modifica</span>
                  </button>
                </router-link>
                <b-button
                  v-on:click="prepareRemove(kanban)"
                  variant="danger"
                  class="btn btn-sm"
                  data-cy="entityDeleteButton"
                  v-b-modal.removeEntity
                >
                  <font-awesome-icon icon="times"></font-awesome-icon>
                  <span class="d-none d-md-inline">Elimina</span>
                </b-button>
              </div>
            </td>
          </tr>
        </tbody>
      </table>
    </div>
    <b-modal ref="removeEntity" id="removeEntity">
      <span slot="modal-title"
        ><span id="kanbanApp.kanban.delete.question" data-cy="kanbanDeleteDialogHeading">Conferma Elminazione Kanban</span></span
      >
      <div class="modal-body">
        <p id="jhi-delete-kanban-heading">Sei sicuro di voler cancellare questo Kanban?</p>
      </div>
      <div slot="modal-footer">
        <button type="button" class="btn btn-secondary" v-on:click="closeDialog()">Annulla</button>
        <button
          type="button"
          class="btn btn-primary"
          id="jhi-confirm-delete-kanban"
          data-cy="entityConfirmDeleteButton"
          v-on:click="removeKanban()"
        >
          Elimina
        </button>
      </div>
    </b-modal>
    <div v-show="kanbans && kanbans.length > 0">
      <div class="row justify-content-center">
        <jhi-item-count :page="page" :total="queryCount" :itemsPerPage="itemsPerPage"></jhi-item-count>
      </div>
      <div class="row justify-content-center">
        <b-pagination size="md" :total-rows="totalItems" v-model="page" :per-page="itemsPerPage" :change="loadPage(page)"></b-pagination>
      </div>
    </div>
  </div>
</template>

<script lang="ts" src="./kanban.component.ts"></script>
