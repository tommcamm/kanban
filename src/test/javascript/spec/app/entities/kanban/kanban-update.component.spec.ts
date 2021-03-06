/* tslint:disable max-line-length */
import { shallowMount, createLocalVue, Wrapper } from '@vue/test-utils';
import sinon, { SinonStubbedInstance } from 'sinon';
import Router from 'vue-router';

import * as config from '@/shared/config/config';
import KanbanUpdateComponent from '@/entities/kanban/kanban-update.vue';
import KanbanClass from '@/entities/kanban/kanban-update.component';
import KanbanService from '@/entities/kanban/kanban.service';

import UserService from '@/admin/user-management/user-management.service';

const localVue = createLocalVue();

config.initVueApp(localVue);
const store = config.initVueXStore(localVue);
const router = new Router();
localVue.use(Router);
localVue.component('font-awesome-icon', {});
localVue.component('b-input-group', {});
localVue.component('b-input-group-prepend', {});
localVue.component('b-form-datepicker', {});
localVue.component('b-form-input', {});

describe('Component Tests', () => {
  describe('Kanban Management Update Component', () => {
    let wrapper: Wrapper<KanbanClass>;
    let comp: KanbanClass;
    let kanbanServiceStub: SinonStubbedInstance<KanbanService>;

    beforeEach(() => {
      kanbanServiceStub = sinon.createStubInstance<KanbanService>(KanbanService);

      wrapper = shallowMount<KanbanClass>(KanbanUpdateComponent, {
        store,
        localVue,
        router,
        provide: {
          kanbanService: () => kanbanServiceStub,

          userService: () => new UserService(),
        },
      });
      comp = wrapper.vm;
    });

    describe('save', () => {
      it('Should call update service on save for existing entity', async () => {
        // GIVEN
        const entity = { id: 123 };
        comp.kanban = entity;
        kanbanServiceStub.update.resolves(entity);

        // WHEN
        comp.save();
        await comp.$nextTick();

        // THEN
        expect(kanbanServiceStub.update.calledWith(entity)).toBeTruthy();
        expect(comp.isSaving).toEqual(false);
      });

      it('Should call create service on save for new entity', async () => {
        // GIVEN
        const entity = {};
        comp.kanban = entity;
        kanbanServiceStub.create.resolves(entity);

        // WHEN
        comp.save();
        await comp.$nextTick();

        // THEN
        expect(kanbanServiceStub.create.calledWith(entity)).toBeTruthy();
        expect(comp.isSaving).toEqual(false);
      });
    });

    describe('Before route enter', () => {
      it('Should retrieve data', async () => {
        // GIVEN
        const foundKanban = { id: 123 };
        kanbanServiceStub.find.resolves(foundKanban);
        kanbanServiceStub.retrieve.resolves([foundKanban]);

        // WHEN
        comp.beforeRouteEnter({ params: { kanbanId: 123 } }, null, cb => cb(comp));
        await comp.$nextTick();

        // THEN
        expect(comp.kanban).toBe(foundKanban);
      });
    });

    describe('Previous state', () => {
      it('Should go previous state', async () => {
        comp.previousState();
        await comp.$nextTick();

        expect(comp.$router.currentRoute.fullPath).toContain('/');
      });
    });
  });
});
