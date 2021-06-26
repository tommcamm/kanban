import { Component, Vue, Inject } from 'vue-property-decorator';

import { IKanban } from '@/shared/model/kanban.model';
import KanbanService from './kanban.service';

@Component
export default class KanbanDetails extends Vue {
  @Inject('kanbanService') private kanbanService: () => KanbanService;
  public kanban: IKanban = {};

  beforeRouteEnter(to, from, next) {
    next(vm => {
      if (to.params.kanbanId) {
        vm.retrieveKanban(to.params.kanbanId);
      }
    });
  }

  public retrieveKanban(kanbanId) {
    this.kanbanService()
      .find(kanbanId)
      .then(res => {
        this.kanban = res;
      });
  }

  public previousState() {
    this.$router.go(-1);
  }
}
