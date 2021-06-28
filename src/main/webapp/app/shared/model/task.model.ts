import { IKlist } from '@/shared/model/klist.model';

export interface ITask {
  id?: number;
  title?: string;
  order?: number | null;
  klisttask?: IKlist;
}

export class Task implements ITask {
  constructor(public id?: number, public title?: string, public order?: number | null, public klisttask?: IKlist) {}
}
