import { Component, Vue, Inject } from 'vue-property-decorator';

import { required, minLength, maxLength } from 'vuelidate/lib/validators';

import UserService from '@/admin/user-management/user-management.service';

import { IKanban, Kanban } from '@/shared/model/kanban.model';
import KanbanService from './kanban.service';

const validations: any = {
  kanban: {
    name: {
      required,
      minLength: minLength(1),
      maxLength: maxLength(30),
    },
    created_at: {},
    last_edit: {},
    userkanban: {
      required,
    },
  },
};

@Component({
  validations,
})
export default class KanbanUpdate extends Vue {
  @Inject('kanbanService') private kanbanService: () => KanbanService;
  public kanban: IKanban = new Kanban();

  @Inject('userService') private userService: () => UserService;

  public users: Array<any> = [];
  public isSaving = false;
  public currentLanguage = '';

  beforeRouteEnter(to, from, next) {
    next(vm => {
      if (to.params.kanbanId) {
        vm.retrieveKanban(to.params.kanbanId);
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
    if (this.kanban.id) {
      this.kanbanService()
        .update(this.kanban)
        .then(param => {
          this.isSaving = false;
          this.$router.go(-1);
          const message = 'A Kanban is updated with identifier ' + param.id;
          return this.$root.$bvToast.toast(message.toString(), {
            toaster: 'b-toaster-top-center',
            title: 'Info',
            variant: 'info',
            solid: true,
            autoHideDelay: 5000,
          });
        });
    } else {
      this.kanbanService()
        .create(this.kanban)
        .then(param => {
          this.isSaving = false;
          this.$router.go(-1);
          const message = 'A Kanban is created with identifier ' + param.id;
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

  public retrieveKanban(kanbanId): void {
    this.kanbanService()
      .find(kanbanId)
      .then(res => {
        this.kanban = res;
      });
  }

  public previousState(): void {
    this.$router.go(-1);
  }

  public initRelationships(): void {
    this.userService()
      .retrieve()
      .then(res => {
        this.users = res.data;
      });
  }
}
