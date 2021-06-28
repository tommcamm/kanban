<template>
  <div class="row justify-content-center">
    <div class="col-8">
      <div v-if="klist">
        <h2 class="jh-entity-heading" data-cy="klistDetailsHeading"><span>Klist</span> {{ klist.id }}</h2>
        <dl class="row jh-entity-details">
          <dt>
            <span>Title</span>
          </dt>
          <dd>
            <span>{{ klist.title }}</span>
          </dd>
          <dt>
            <span>Order</span>
          </dt>
          <dd>
            <span>{{ klist.order }}</span>
          </dd>
          <dt>
            <span>Kanbanlist</span>
          </dt>
          <dd>
            <div v-if="klist.kanbanlist">
              <router-link :to="{ name: 'KanbanView', params: { kanbanId: klist.kanbanlist.id } }">{{ klist.kanbanlist.id }}</router-link>
            </div>
          </dd>
        </dl>
        <button type="submit" v-on:click.prevent="previousState()" class="btn btn-info" data-cy="entityDetailsBackButton">
          <font-awesome-icon icon="arrow-left"></font-awesome-icon>&nbsp;<span> Back</span>
        </button>
        <router-link v-if="klist.id" :to="{ name: 'KlistEdit', params: { klistId: klist.id } }" custom v-slot="{ navigate }">
          <button @click="navigate" class="btn btn-primary">
            <font-awesome-icon icon="pencil-alt"></font-awesome-icon>&nbsp;<span> Edit</span>
          </button>
        </router-link>
      </div>
    </div>
  </div>
</template>

<script lang="ts" src="./klist-details.component.ts"></script>
