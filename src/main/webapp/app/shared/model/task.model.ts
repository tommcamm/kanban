import { IKanban } from '@/shared/model/kanban.model';

export interface ITask {
  id?: number;
  title?: string;
  order?: number | null;
  kanban?: IKanban | null;
}

export class Task implements ITask {
  constructor(public id?: number, public title?: string, public order?: number | null, public kanban?: IKanban | null) {}
}
