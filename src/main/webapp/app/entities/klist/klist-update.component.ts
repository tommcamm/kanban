import { Component, Vue, Inject } from 'vue-property-decorator';

import { required, minLength, maxLength } from 'vuelidate/lib/validators';

import KanbanService from '@/entities/kanban/kanban.service';
import { IKanban } from '@/shared/model/kanban.model';

import { IKlist, Klist } from '@/shared/model/klist.model';
import KlistService from './klist.service';

const validations: any = {
  klist: {
    title: {
      required,
      minLength: minLength(1),
      maxLength: maxLength(40),
    },
    order: {},
    kanbanlist: {
      required,
    },
  },
};

@Component({
  validations,
})
export default class KlistUpdate extends Vue {
  @Inject('klistService') private klistService: () => KlistService;
  public klist: IKlist = new Klist();

  @Inject('kanbanService') private kanbanService: () => KanbanService;

  public kanbans: IKanban[] = [];
  public isSaving = false;
  public currentLanguage = '';

  beforeRouteEnter(to, from, next) {
    next(vm => {
      if (to.params.klistId) {
        vm.retrieveKlist(to.params.klistId);
      }
      vm.initRelationships();
    });
  }

  created(): void {
    this.currentLanguage = this.$store.getters.currentLanguage;
    this.$store.watch(
      () => this.$store.getters.currentLanguage,
      () => {
        this.currentLanguage = this.$store.getters.currentLanguage;
      }
    );
  }

  public save(): void {
    this.isSaving = true;
    if (this.klist.id) {
      this.klistService()
        .update(this.klist)
        .then(param => {
          this.isSaving = false;
          this.$router.go(-1);
          const message = 'A Klist is updated with identifier ' + param.id;
          return this.$root.$bvToast.toast(message.toString(), {
            toaster: 'b-toaster-top-center',
            title: 'Info',
            variant: 'info',
            solid: true,
            autoHideDelay: 5000,
          });
        });
    } else {
      this.klistService()
        .create(this.klist)
        .then(param => {
          this.isSaving = false;
          this.$router.go(-1);
          const message = 'A Klist is created with identifier ' + param.id;
          this.$root.$bvToast.toast(message.toString(), {
            toaster: 'b-toaster-top-center',
            title: 'Success',
            variant: 'success',
            solid: true,
            autoHideDelay: 5000,
          });
        });
    }
  }

  public retrieveKlist(klistId): void {
    this.klistService()
      .find(klistId)
      .then(res => {
        this.klist = res;
      });
  }

  public previousState(): void {
    this.$router.go(-1);
  }

  public initRelationships(): void {
    this.kanbanService()
      .retrieve()
      .then(res => {
        this.kanbans = res.data;
      });
  }
}
