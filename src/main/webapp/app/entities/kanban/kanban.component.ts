import { mixins } from 'vue-class-component';

import { Component, Vue, Inject } from 'vue-property-decorator';
import Vue2Filters from 'vue2-filters';
import { IKanban } from '@/shared/model/kanban.model';

import KanbanService from './kanban.service';

@Component({
  mixins: [Vue2Filters.mixin],
})
export default class Kanban extends Vue {
  @Inject('kanbanService') private kanbanService: () => KanbanService;
  private removeId: number = null;
  private removeName: string = null;
  public itemsPerPage = 20;
  public queryCount: number = null;
  public page = 1;
  public previousPage = 1;
  public propOrder = 'id';
  public reverse = false;
  public totalItems = 0;

  public kanbans: IKanban[] = [];

  public isFetching = false;

  public mounted(): void {
    this.retrieveAllKanbans();
  }

  public clear(): void {
    this.page = 1;
    this.retrieveAllKanbans();
  }

  public retrieveAllKanbans(): void {
    this.isFetching = true;
    const paginationQuery = {
      page: this.page - 1,
      size: this.itemsPerPage,
      sort: this.sort(),
    };
    this.kanbanService()
      .retrieve(paginationQuery)
      .then(
        res => {
          this.kanbans = res.data;
          this.totalItems = Number(res.headers['x-total-count']);
          this.queryCount = this.totalItems;
          this.isFetching = false;
        },
        err => {
          this.isFetching = false;
        }
      );
  }

  public handleSyncList(): void {
    this.clear();
  }

  public prepareRemove(instance: IKanban): void {
    this.removeId = instance.id;
    this.removeName = instance.name;
    if (<any>this.$refs.removeEntity) {
      (<any>this.$refs.removeEntity).show();
    }
  }

  public removeKanban(): void {
    this.kanbanService()
      .delete(this.removeId)
      .then(() => {
        const message = 'Il Kanban di nome ' + this.removeName + ' è stato eliminato';
        this.$bvToast.toast(message.toString(), {
          toaster: 'b-toaster-top-center',
          title: 'Info',
          variant: 'danger',
          solid: true,
          autoHideDelay: 5000,
        });
        this.removeId = null;
        this.retrieveAllKanbans();
        this.closeDialog();
      });
  }

  public sort(): Array<any> {
    const result = [this.propOrder + ',' + (this.reverse ? 'desc' : 'asc')];
    if (this.propOrder !== 'id') {
      result.push('id');
    }
    return result;
  }

  public loadPage(page: number): void {
    if (page !== this.previousPage) {
      this.previousPage = page;
      this.transition();
    }
  }

  public transition(): void {
    this.retrieveAllKanbans();
  }

  public changeOrder(propOrder): void {
    this.propOrder = propOrder;
    this.reverse = !this.reverse;
    this.transition();
  }

  public closeDialog(): void {
    (<any>this.$refs.removeEntity).hide();
  }
}
