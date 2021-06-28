import { Component, Vue, Inject } from 'vue-property-decorator';

import { IKlist } from '@/shared/model/klist.model';
import KlistService from './klist.service';

@Component
export default class KlistDetails extends Vue {
  @Inject('klistService') private klistService: () => KlistService;
  public klist: IKlist = {};

  beforeRouteEnter(to, from, next) {
    next(vm => {
      if (to.params.klistId) {
        vm.retrieveKlist(to.params.klistId);
      }
    });
  }

  public retrieveKlist(klistId) {
    this.klistService()
      .find(klistId)
      .then(res => {
        this.klist = res;
      });
  }

  public previousState() {
    this.$router.go(-1);
  }
}
