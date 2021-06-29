<template>
  <div class="info jhi-item-count">
    <!-- eslint-disable vue/no-parsing-error -->
    <span v-if="i18nEnabled">Mostrando {{ first }} - {{ second }} su {{ total }} elementi.</span>
    <span v-if="!i18nEnabled">
      Mostrando {{ (page - 1) * itemsPerPage === 0 ? 1 : (page - 1) * itemsPerPage + 1 }} -
      {{ page * itemsPerPage < total ? page * itemsPerPage : total }}
      su {{ total }} elementi.
    </span>
    <!--eslint-enable-->
  </div>
</template>

<script lang="ts" src="./jhi-item-count.component.ts"></script>
