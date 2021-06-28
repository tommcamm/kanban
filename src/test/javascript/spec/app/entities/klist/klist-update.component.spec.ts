/* tslint:disable max-line-length */
import { shallowMount, createLocalVue, Wrapper } from '@vue/test-utils';
import sinon, { SinonStubbedInstance } from 'sinon';
import Router from 'vue-router';

import * as config from '@/shared/config/config';
import KlistUpdateComponent from '@/entities/klist/klist-update.vue';
import KlistClass from '@/entities/klist/klist-update.component';
import KlistService from '@/entities/klist/klist.service';

import KanbanService from '@/entities/kanban/kanban.service';

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
  describe('Klist Management Update Component', () => {
    let wrapper: Wrapper<KlistClass>;
    let comp: KlistClass;
    let klistServiceStub: SinonStubbedInstance<KlistService>;

    beforeEach(() => {
      klistServiceStub = sinon.createStubInstance<KlistService>(KlistService);

      wrapper = shallowMount<KlistClass>(KlistUpdateComponent, {
        store,
        localVue,
        router,
        provide: {
          klistService: () => klistServiceStub,

          kanbanService: () => new KanbanService(),
        },
      });
      comp = wrapper.vm;
    });

    describe('save', () => {
      it('Should call update service on save for existing entity', async () => {
        // GIVEN
        const entity = { id: 123 };
        comp.klist = entity;
        klistServiceStub.update.resolves(entity);

        // WHEN
        comp.save();
        await comp.$nextTick();

        // THEN
        expect(klistServiceStub.update.calledWith(entity)).toBeTruthy();
        expect(comp.isSaving).toEqual(false);
      });

      it('Should call create service on save for new entity', async () => {
        // GIVEN
        const entity = {};
        comp.klist = entity;
        klistServiceStub.create.resolves(entity);

        // WHEN
        comp.save();
        await comp.$nextTick();

        // THEN
        expect(klistServiceStub.create.calledWith(entity)).toBeTruthy();
        expect(comp.isSaving).toEqual(false);
      });
    });

    describe('Before route enter', () => {
      it('Should retrieve data', async () => {
        // GIVEN
        const foundKlist = { id: 123 };
        klistServiceStub.find.resolves(foundKlist);
        klistServiceStub.retrieve.resolves([foundKlist]);

        // WHEN
        comp.beforeRouteEnter({ params: { klistId: 123 } }, null, cb => cb(comp));
        await comp.$nextTick();

        // THEN
        expect(comp.klist).toBe(foundKlist);
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
