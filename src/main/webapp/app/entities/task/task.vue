<template>
  <div>
    <h2 id="page-heading" data-cy="TaskHeading">
      <span id="task-heading">Tasks</span>
      <div class="d-flex justify-content-end">
        <button class="btn btn-info mr-2" v-on:click="handleSyncList" :disabled="isFetching">
          <font-awesome-icon icon="sync" :spin="isFetching"></font-awesome-icon> <span>Refresh List</span>
        </button>
        <router-link :to="{ name: 'TaskCreate' }" custom v-slot="{ navigate }">
          <button @click="navigate" id="jh-create-entity" data-cy="entityCreateButton" class="btn btn-primary jh-create-entity create-task">
            <font-awesome-icon icon="plus"></font-awesome-icon>
            <span> Create a new Task </span>
          </button>
        </router-link>
      </div>
    </h2>
    <br />
    <div class="alert alert-warning" v-if="!isFetching && tasks && tasks.length === 0">
      <span>No tasks found</span>
    </div>
    <div class="table-responsive" v-if="tasks && tasks.length > 0">
      <table class="table table-striped" aria-describedby="tasks">
        <thead>
          <tr>
            <th scope="row" v-on:click="changeOrder('id')">
              <span>ID</span> <jhi-sort-indicator :current-order="propOrder" :reverse="reverse" :field-name="'id'"></jhi-sort-indicator>
            </th>
            <th scope="row" v-on:click="changeOrder('title')">
              <span>Title</span>
              <jhi-sort-indicator :current-order="propOrder" :reverse="reverse" :field-name="'title'"></jhi-sort-indicator>
            </th>
            <th scope="row" v-on:click="changeOrder('order')">
              <span>Order</span>
              <jhi-sort-indicator :current-order="propOrder" :reverse="reverse" :field-name="'order'"></jhi-sort-indicator>
            </th>
            <th scope="row" v-on:click="changeOrder('klisttask.id')">
              <span>Klisttask</span>
              <jhi-sort-indicator :current-order="propOrder" :reverse="reverse" :field-name="'klisttask.id'"></jhi-sort-indicator>
            </th>
            <th scope="row"></th>
          </tr>
        </thead>
        <tbody>
          <tr v-for="task in tasks" :key="task.id" data-cy="entityTable">
            <td>
              <router-link :to="{ name: 'TaskView', params: { taskId: task.id } }">{{ task.id }}</router-link>
            </td>
            <td>{{ task.title }}</td>
            <td>{{ task.order }}</td>
            <td>
              <div v-if="task.klisttask">
                <router-link :to="{ name: 'KlistView', params: { klistId: task.klisttask.id } }">{{ task.klisttask.id }}</router-link>
              </div>
            </td>
            <td class="text-right">
              <div class="btn-group">
                <router-link :to="{ name: 'TaskView', params: { taskId: task.id } }" custom v-slot="{ navigate }">
                  <button @click="navigate" class="btn btn-info btn-sm details" data-cy="entityDetailsButton">
                    <font-awesome-icon icon="eye"></font-awesome-icon>
                    <span class="d-none d-md-inline">View</span>
                  </button>
                </router-link>
                <router-link :to="{ name: 'TaskEdit', params: { taskId: task.id } }" custom v-slot="{ navigate }">
                  <button @click="navigate" class="btn btn-primary btn-sm edit" data-cy="entityEditButton">
                    <font-awesome-icon icon="pencil-alt"></font-awesome-icon>
                    <span class="d-none d-md-inline">Edit</span>
                  </button>
                </router-link>
                <b-button
                  v-on:click="prepareRemove(task)"
                  variant="danger"
                  class="btn btn-sm"
                  data-cy="entityDeleteButton"
                  v-b-modal.removeEntity
                >
                  <font-awesome-icon icon="times"></font-awesome-icon>
                  <span class="d-none d-md-inline">Delete</span>
                </b-button>
              </div>
            </td>
          </tr>
        </tbody>
      </table>
    </div>
    <b-modal ref="removeEntity" id="removeEntity">
      <span slot="modal-title"
        ><span id="kanbanApp.task.delete.question" data-cy="taskDeleteDialogHeading">Confirm delete operation</span></span
      >
      <div class="modal-body">
        <p id="jhi-delete-task-heading">Are you sure you want to delete this Task?</p>
      </div>
      <div slot="modal-footer">
        <button type="button" class="btn btn-secondary" v-on:click="closeDialog()">Cancel</button>
        <button
          type="button"
          class="btn btn-primary"
          id="jhi-confirm-delete-task"
          data-cy="entityConfirmDeleteButton"
          v-on:click="removeTask()"
        >
          Delete
        </button>
      </div>
    </b-modal>
    <div v-show="tasks && tasks.length > 0">
      <div class="row justify-content-center">
        <jhi-item-count :page="page" :total="queryCount" :itemsPerPage="itemsPerPage"></jhi-item-count>
      </div>
      <div class="row justify-content-center">
        <b-pagination size="md" :total-rows="totalItems" v-model="page" :per-page="itemsPerPage" :change="loadPage(page)"></b-pagination>
      </div>
    </div>
  </div>
</template>

<script lang="ts" src="./task.component.ts"></script>
