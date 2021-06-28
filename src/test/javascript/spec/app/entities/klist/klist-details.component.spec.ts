/* tslint:disable max-line-length */
import { shallowMount, createLocalVue, Wrapper } from '@vue/test-utils';
import sinon, { SinonStubbedInstance } from 'sinon';
import VueRouter from 'vue-router';

import * as config from '@/shared/config/config';
import KlistDetailComponent from '@/entities/klist/klist-details.vue';
import KlistClass from '@/entities/klist/klist-details.component';
import KlistService from '@/entities/klist/klist.service';
import router from '@/router';

const localVue = createLocalVue();
localVue.use(VueRouter);

config.initVueApp(localVue);
const store = config.initVueXStore(localVue);
localVue.component('font-awesome-icon', {});
localVue.component('router-link', {});

describe('Component Tests', () => {
  describe('Klist Management Detail Component', () => {
    let wrapper: Wrapper<KlistClass>;
    let comp: KlistClass;
    let klistServiceStub: SinonStubbedInstance<KlistService>;

    beforeEach(() => {
      klistServiceStub = sinon.createStubInstance<KlistService>(KlistService);

      wrapper = shallowMount<KlistClass>(KlistDetailComponent, {
        store,
        localVue,
        router,
        provide: { klistService: () => klistServiceStub },
      });
      comp = wrapper.vm;
    });

    describe('OnInit', () => {
      it('Should call load all on init', async () => {
        // GIVEN
        const foundKlist = { id: 123 };
        klistServiceStub.find.resolves(foundKlist);

        // WHEN
        comp.retrieveKlist(123);
        await comp.$nextTick();

        // THEN
        expect(comp.klist).toBe(foundKlist);
      });
    });

    describe('Before route enter', () => {
      it('Should retrieve data', async () => {
        // GIVEN
        const foundKlist = { id: 123 };
        klistServiceStub.find.resolves(foundKlist);

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
