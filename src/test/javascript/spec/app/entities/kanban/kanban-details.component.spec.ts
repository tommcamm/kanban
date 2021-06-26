/* tslint:disable max-line-length */
import { shallowMount, createLocalVue, Wrapper } from '@vue/test-utils';
import sinon, { SinonStubbedInstance } from 'sinon';
import VueRouter from 'vue-router';

import * as config from '@/shared/config/config';
import KanbanDetailComponent from '@/entities/kanban/kanban-details.vue';
import KanbanClass from '@/entities/kanban/kanban-details.component';
import KanbanService from '@/entities/kanban/kanban.service';
import router from '@/router';

const localVue = createLocalVue();
localVue.use(VueRouter);

config.initVueApp(localVue);
const store = config.initVueXStore(localVue);
localVue.component('font-awesome-icon', {});
localVue.component('router-link', {});

describe('Component Tests', () => {
  describe('Kanban Management Detail Component', () => {
    let wrapper: Wrapper<KanbanClass>;
    let comp: KanbanClass;
    let kanbanServiceStub: SinonStubbedInstance<KanbanService>;

    beforeEach(() => {
      kanbanServiceStub = sinon.createStubInstance<KanbanService>(KanbanService);

      wrapper = shallowMount<KanbanClass>(KanbanDetailComponent, {
        store,
        localVue,
        router,
        provide: { kanbanService: () => kanbanServiceStub },
      });
      comp = wrapper.vm;
    });

    describe('OnInit', () => {
      it('Should call load all on init', async () => {
        // GIVEN
        const foundKanban = { id: 123 };
        kanbanServiceStub.find.resolves(foundKanban);

        // WHEN
        comp.retrieveKanban(123);
        await comp.$nextTick();

        // THEN
        expect(comp.kanban).toBe(foundKanban);
      });
    });

    describe('Before route enter', () => {
      it('Should retrieve data', async () => {
        // GIVEN
        const foundKanban = { id: 123 };
        kanbanServiceStub.find.resolves(foundKanban);

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
