import { Authority } from '@/shared/security/authority';

const Kanban = () => import('@/kanban/kanban.vue');

export default [
  {
    path: '/kanban',
    name: 'Kanban',
    component: Kanban,
    meta: { authorities: [Authority.USER] },
  },
];
