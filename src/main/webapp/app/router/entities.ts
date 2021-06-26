import { Authority } from '@/shared/security/authority';
/* tslint:disable */
// prettier-ignore

// prettier-ignore
const Kanban = () => import('@/entities/kanban/kanban.vue');
// prettier-ignore
const KanbanUpdate = () => import('@/entities/kanban/kanban-update.vue');
// prettier-ignore
const KanbanDetails = () => import('@/entities/kanban/kanban-details.vue');
// jhipster-needle-add-entity-to-router-import - JHipster will import entities to the router here

export default [
  {
    path: '/kanban',
    name: 'Kanban',
    component: Kanban,
    meta: { authorities: [Authority.USER] },
  },
  {
    path: '/kanban/new',
    name: 'KanbanCreate',
    component: KanbanUpdate,
    meta: { authorities: [Authority.USER] },
  },
  {
    path: '/kanban/:kanbanId/edit',
    name: 'KanbanEdit',
    component: KanbanUpdate,
    meta: { authorities: [Authority.USER] },
  },
  {
    path: '/kanban/:kanbanId/view',
    name: 'KanbanView',
    component: KanbanDetails,
    meta: { authorities: [Authority.USER] },
  },
  // jhipster-needle-add-entity-to-router - JHipster will add entities to the router here
];
